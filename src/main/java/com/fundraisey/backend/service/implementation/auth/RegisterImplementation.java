package com.fundraisey.backend.service.implementation.auth;

import com.fundraisey.backend.entity.auth.Role;
import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.investor.Investor;
import com.fundraisey.backend.entity.investor.InvestorVerification;
import com.fundraisey.backend.entity.investor.InvestorVerificationStatus;
import com.fundraisey.backend.entity.startup.Startup;
import com.fundraisey.backend.model.RegisterModel;
import com.fundraisey.backend.repository.investor.InvestorRepository;
import com.fundraisey.backend.repository.auth.RoleRepository;
import com.fundraisey.backend.repository.auth.UserRepository;
import com.fundraisey.backend.repository.investor.InvestorVerificationRepository;
import com.fundraisey.backend.repository.startup.StartupRepository;
import com.fundraisey.backend.service.interfaces.auth.RegisterService;
import com.fundraisey.backend.util.EmailSender;
import com.fundraisey.backend.util.EmailTemplate;
import com.fundraisey.backend.util.ResponseTemplate;
import com.fundraisey.backend.util.SimpleStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RegisterImplementation implements RegisterService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InvestorRepository investorRepository;
    @Autowired
    private StartupRepository startupRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private InvestorVerificationRepository investorVerificationRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    public EmailSender emailSender;

    @Autowired
    public EmailTemplate emailTemplate;

    @Value("${expired.token.password.minute:}")
    private int expiredToken;

    @Value("${base_url}")
    private String baseUrl;

    private ResponseTemplate responseTemplate = new ResponseTemplate();

    @Override
    public Map registerAdmin(RegisterModel registerModel) {
        Map map = new HashMap();
        try {
            String[] roleNames = {"ROLE_ADMIN"};
            String encodedPassword = encoder.encode(registerModel.getPassword().replaceAll("\\s+", ""));

            List<Role> roles = roleRepository.findByNameIn(roleNames);

            if (roles.size() == 0) {
                return responseTemplate.notFound("Role not found");
            }

            User usernameValidation = userRepository.findOneByEmail(registerModel.getEmail());
            if (usernameValidation != null) {
                return responseTemplate.alreadyExist("Username " + registerModel.getEmail() + " already exists");
            }

            User user = new User();
            user.setEmail(registerModel.getEmail());
            user.setPassword(encodedPassword);
            user.setRoles(roles);

            User newUser = userRepository.save(user);

            return responseTemplate.success(null);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map registerStartup(RegisterModel registerModel) {
        Map map = new HashMap();
        try {

            if (registerModel.getEmail() == null) {
                return responseTemplate.isRequired("Email required");
            }
            if (registerModel.getPassword() == null) {
                return responseTemplate.isRequired("Password required");
            }
            User usernameValidation = userRepository.findOneByEmail(registerModel.getEmail());
            if (usernameValidation != null) {
                return responseTemplate.alreadyExist("Email " + registerModel.getEmail() + " already used");
            }
            if (registerModel.getPassword().length() < 8) {
                return responseTemplate.isRequired("Password must be 8 characters or more");
            }

            String[] roleNames = {"ROLE_STARTUP"};
            String encodedPassword = encoder.encode(registerModel.getPassword().replaceAll("\\s+", ""));
            List<Role> roles = roleRepository.findByNameIn(roleNames);

            if (roles.size() == 0) {
                return responseTemplate.notFound("Role not found");
            }

            User user = new User();
            user.setEmail(registerModel.getEmail());
            user.setPassword(encodedPassword);
            user.setRoles(roles);

            User newUser = userRepository.save(user);

            Startup startup = new Startup();
            startup.setUser(newUser);

            startupRepository.save(startup);

            return responseTemplate.success(null);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map registerInvestor(RegisterModel registerModel) {
        Map map = new HashMap();
        try {
            String[] roleNames = {"ROLE_INVESTOR"};
            String encodedPassword = encoder.encode(registerModel.getPassword().replaceAll("\\s+", ""));
            List<Role> roles = roleRepository.findByNameIn(roleNames);

            if (roles.size() == 0) {
                return responseTemplate.notFound("Role not found");
            }

            User usernameValidation = userRepository.findOneByEmail(registerModel.getEmail());
            if (usernameValidation != null) {
                return responseTemplate.alreadyExist("Username " + registerModel.getEmail() + " already exists");
            }

            User user = new User();
            user.setEmail(registerModel.getEmail());
            user.setPassword(encodedPassword);
            user.setRoles(roles);
            user.setEnabled(false);

            User newUser = userRepository.save(user);

            Investor investor = new Investor();
            investor.setUser(newUser);
            investor.setCitizenID(registerModel.getCitizenID());
            investor.setFullName(registerModel.getFullName());
            investor.setPhoneNumber(registerModel.getPhoneNumber());
            investor.setDateOfBirth(registerModel.getDateOfBirth());
            investor.setGender(registerModel.getGender());
            investor.setBankAccountNumber(registerModel.getBankAccountNumber());

            investorRepository.save(investor);

            InvestorVerification investorVerification = new InvestorVerification();
            investorVerification.setInvestor(investor);
            investorVerification.setVerified(false);
            investorVerification.setStatus(InvestorVerificationStatus.pending);
            investorVerificationRepository.save(investorVerification);

            return responseTemplate.success(null);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    public Map sendEmailRegister(String email) {
        String message = "Thanks, please check your email";

        if (email == null) return responseTemplate.isRequired("No email provided");
        User found = userRepository.findOneByEmail(email);
        if (found == null) return responseTemplate.notFound("Email not found");
        if (found.isEnabled()) return responseTemplate.notAllowed("Already verified");

        String template = emailTemplate.getRegister();

        User search;
        String otp;
        do {
            otp = SimpleStringUtils.randomString(6, true);
            search = userRepository.findOneByOTP(otp);
        } while (search != null);
        Date dateNow = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateNow);
        calendar.add(Calendar.MINUTE, expiredToken);
        Date expirationDate = calendar.getTime();

        found.setOtp(otp);
        found.setOtpExpiredDate(expirationDate);
        template = template.replaceAll("\\{\\{VERIFY_URL}}", baseUrl + "/v1/user-register-confirm/" + otp);
        template = template.replaceAll("\\{\\{USERNAME}}", found.getEmail());
        userRepository.save(found);

        emailSender.sendAsync(found.getEmail(), "Fundraisey Registration Verification", template);

        return responseTemplate.success(null);
    }

    public Map confirmRegisterOTP(String otp) {
        User user = userRepository.findOneByOTP(otp);
        if (null != user) {
            System.out.println("user null: tidak ditemukan");
        }
        user.setEnabled(true);
        userRepository.save(user);

        return responseTemplate.success(null);
    }
}
