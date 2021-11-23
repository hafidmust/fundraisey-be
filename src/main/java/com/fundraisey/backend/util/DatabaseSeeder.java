package com.fundraisey.backend.util;

import com.fundraisey.backend.entity.auth.Client;
import com.fundraisey.backend.entity.auth.Role;
import com.fundraisey.backend.entity.auth.RolePath;
import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.investor.Investor;
import com.fundraisey.backend.entity.startup.Startup;
import com.fundraisey.backend.entity.transaction.PaymentAgent;
import com.fundraisey.backend.entity.transaction.TransactionMethod;
import com.fundraisey.backend.repository.auth.ClientRepository;
import com.fundraisey.backend.repository.auth.RolePathRepository;
import com.fundraisey.backend.repository.auth.RoleRepository;
import com.fundraisey.backend.repository.auth.UserRepository;
import com.fundraisey.backend.repository.investor.InvestorRepository;
import com.fundraisey.backend.repository.investor.PaymentAgentRepository;
import com.fundraisey.backend.repository.investor.TransactionMethodRepository;
import com.fundraisey.backend.repository.startup.StartupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
@Service
public class DatabaseSeeder implements ApplicationRunner {
    private static final String TAG = "DatabaseSeeder {}";

    private Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolePathRepository rolePathRepository;

    @Autowired
    private PaymentAgentRepository paymentAgentRepository;

    @Autowired
    private TransactionMethodRepository transactionMethodRepository;

    @Autowired
    private StartupRepository startupRepository;

    @Autowired
    private InvestorRepository investorRepository;

    private String defaultPassword = "password";

    private String[] users = new String[]{
            "admin@fundraisey.com:ROLE_ADMIN",
            // "user@fundraisey.com:ROLE_INVESTOR ROLE_STARTUP",
            "investor@fundraisey.com:ROLE_INVESTOR",
            "startup@fundraisey.com:ROLE_STARTUP"
    };

    private String[] clients = new String[]{
            "client-apps:ROLE_READ ROLE_WRITE",
            "client-web:ROLE_READ ROLE_WRITE"
    };

    private String[] roles = new String[] {
            // "ROLE_SUPERUSER:user_role:^/.*:GET|PUT|POST|PATCH|DELETE|OPTIONS",
            "ROLE_ADMIN:user_role:^/.*:GET|PUT|POST|PATCH|DELETE|OPTIONS",
            "ROLE_INVESTOR:user_role:^/.*:GET|PUT|POST|PATCH|DELETE|OPTIONS",
            "ROLE_STARTUP:user_role:^/.*:GET|PUT|POST|PATCH|DELETE|OPTIONS",
            "ROLE_READ:oauth_role:^/.*:GET|PUT|POST|PATCH|DELETE|OPTIONS",
            "ROLE_WRITE:oauth_role:^/.*:GET|PUT|POST|PATCH|DELETE|OPTIONS"
    };

    private String[] paymentAgents = new String[] {
            "Bank:BCA",
            "Bank:Mandiri",
            "E-Wallet:Gopay",
            "E-Wallet:OVO"
    };

    @Override
    @Transactional
    public void run(ApplicationArguments applicationArguments) {
        String password = encoder.encode(defaultPassword);

        this.insertRoles();
        this.insertClients(password);
        this.insertUser(password);
        this.insertPaymentAgents();
        this.insertInvestor();
        this.insertStartup();
    }

    @Transactional
    private void insertInvestor() {
        User user = userRepository.findOneByEmail("investor@fundraisey.com");
        Investor investor = investorRepository.findByUser(user);

        if (investor == null) {
            investor = new Investor();
            investor.setFullName("Fundraisey Investor");

            investorRepository.save(investor);
        }
    }

    @Transactional
    private void insertStartup() {
        User user = userRepository.findOneByEmail("investor@fundraisey.com");
        Startup startup = startupRepository.findByUser(user);

        if (startup == null) {
            startup = new Startup();
            startup.setName("Fundraisey");
            startup.setDescription("Fundraisey is a startup fundraiser platform.");

            startupRepository.save(startup);
        }
    }

    @Transactional
    private void insertPaymentAgents() {
        for (String paymentAgent: paymentAgents) {
            String[] str = paymentAgent.split(":");
            String transactionMethodName = str[0];
            String paymentAgentName = str[1];

            TransactionMethod transactionMethodObj = transactionMethodRepository.findOneByName(transactionMethodName);
            if (transactionMethodObj == null) {
                transactionMethodObj = new TransactionMethod();
                transactionMethodObj.setName(transactionMethodName);
                transactionMethodRepository.save(transactionMethodObj);
            }

            PaymentAgent paymentAgentObj = paymentAgentRepository.findOneByName(paymentAgentName);
            if ((paymentAgentObj == null) || !transactionMethodName.equals(paymentAgentObj.getTransactionMethod().getName())) {
                paymentAgentObj = new PaymentAgent();
                paymentAgentObj.setName(paymentAgentName);
                paymentAgentObj.setTransactionMethod(transactionMethodObj);
                paymentAgentRepository.save(paymentAgentObj);
            }
        }
    }

    @Transactional
    private void insertRoles() {
        for (String role: roles) {
            String[] str = role.split(":");
            String name = str[0];
            String type = str[1];
            String pattern = str[2];
            String[] methods = str[3].split("\\|");
            Role oldRole = roleRepository.findOneByName(name);
            if (null == oldRole) {
                oldRole = new Role();
                oldRole.setName(name);
                oldRole.setType(type);
                oldRole.setRolePaths(new ArrayList<>());
                for (String m: methods) {
                    String rolePathName = name.toLowerCase()+"_"+m.toLowerCase();
                    RolePath rolePath = rolePathRepository.findOneByName(rolePathName);
                    if (null == rolePath) {
                        rolePath = new RolePath();
                        rolePath.setName(rolePathName);
                        rolePath.setMethod(m.toUpperCase());
                        rolePath.setPattern(pattern);
                        rolePath.setRole(oldRole);
                        rolePathRepository.save(rolePath);
                        oldRole.getRolePaths().add(rolePath);
                    }
                }
            }

            roleRepository.save(oldRole);
        }
    }

    @Transactional
    private void insertClients(String password) {
        for (String c: clients) {
            String[] s = c.split(":");
            String clientName = s[0];
            String[] clientRoles = s[1].split("\\s");
            Client oldClient = clientRepository.findOneByClientId(clientName);
            if (null == oldClient) {
                oldClient = new Client();
                oldClient.setClientId(clientName);
                oldClient.setAccessTokenValiditySeconds(28800);
                oldClient.setRefreshTokenValiditySeconds(7257600);
                oldClient.setGrantTypes("password refresh_token authorization_code");
                oldClient.setClientSecret(password);
                oldClient.setApproved(true);
                oldClient.setRedirectUris("");
                oldClient.setScopes("read write");
                List<Role> rls = roleRepository.findByNameIn(clientRoles);

                if (rls.size() > 0) {
                    oldClient.getAuthorities().addAll(rls);
                }
            }
            clientRepository.save(oldClient);
        }
    }

    @Transactional
    private void insertUser(String password) {
        for (String userNames: users) {
            String[] str = userNames.split(":");
            String email = str[0];
            String[] roleNames = str[1].split("\\s");

            User oldUser = userRepository.findOneByEmail(email);
            if (null == oldUser) {
                oldUser = new User();
                oldUser.setEmail(email);
                oldUser.setPassword(password);
                List<Role> r = roleRepository.findByNameIn(roleNames);
                oldUser.setRoles(r);
            }

            userRepository.save(oldUser);
        }
    }

}
