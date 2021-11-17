package com.fundraisey.backend.service.implementation;

import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.repository.auth.UserRepository;
import com.fundraisey.backend.service.ResetPasswordService;
import com.fundraisey.backend.util.EmailSender;
import com.fundraisey.backend.util.EmailTemplate;
import com.fundraisey.backend.util.ResponseTemplate;
import com.fundraisey.backend.util.SimpleStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Service
public class ResetPasswordImplementation implements ResetPasswordService {
    @Autowired
    public EmailSender emailSender;

    @Autowired
    public EmailTemplate emailTemplate;

    private ResponseTemplate responseTemplate = new ResponseTemplate();

    @Value("${expired.token.password.minute:}")
    private int expiredToken;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    Logger logger = LoggerFactory.getLogger(ResetPasswordImplementation.class);

    @Override
    public Map sendEmailResetPassword(String email) {
        try {
            String message = "Thanks, please check your email";

            if (email == null) return responseTemplate.isRequired("No email provided");
            User found = userRepository.findOneByEmail(email);
            if (found == null) return responseTemplate.notFound("Email not found");

            String template = emailTemplate.getResetPassword();

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
            template = template.replaceAll("\\{\\{PASS_TOKEN}}", otp);
            userRepository.save(found);

            emailSender.sendAsync(found.getEmail(), "Fundraisey Reset Password", template);

            return responseTemplate.success(null);
        } catch (Exception e) {
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map resetPassword(String otp, String newPassword) {
        logger.info(otp);
        logger.info(newPassword);
        if (otp == null) return responseTemplate.isRequired("Token required");
        if (newPassword == null) return responseTemplate.isRequired("New password required");
        User user = userRepository.findOneByOTP(otp);

        if (user == null) return responseTemplate.notFound("Token not valid");
        String success;

        user.setPassword(encoder.encode(newPassword.replaceAll("\\s+", "")));
        user.setOtpExpiredDate(null);
        user.setOtp(null);

        userRepository.save(user);

        return responseTemplate.success(null);
    }
}
