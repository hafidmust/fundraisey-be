package com.fundraisey.backend.unittest.startup;

import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.repository.auth.UserRepository;
import com.fundraisey.backend.unittest.UnitTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.print.attribute.standard.Media;
import java.security.Principal;

import static org.junit.Assert.assertEquals;

public class StartupControllerTest extends UnitTest {

    @MockBean
    Principal principal;

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void getStartupProfile() throws Exception {

        String body = "{\n" +
                "    \"email\": \"startup@fundraisey.com\",\n" +
                "    \"password\": \"password\",\n" +
                "    \"grant_type\": \"password\",\n" +
                "    \"client_id\": \"client-web\",\n" +
                "    \"client_secret\": \"password\"\n" +
                "}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(body)
        ).andReturn();

        String response = result.getResponse().getContentAsString();
        response = response.replace("{\"data\":{\"access_token\":\"", "");
        String token = response.replace("\",\"is_enabled\":true,\"roles\":[\"ROLE_STARTUP\"]},\"message\":\"success\",\"status\":200}", "");

        String uri = "/v1/user/startup/detail";
//
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .header("Authorization", "Bearer " + token)
                .principal(principal)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();

        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println("Response = ");
        System.out.println(content);
    }
}
