package com.fundraisey.backend.unittest.startup;

import com.fundraisey.backend.unittest.UnitTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.security.Principal;

import static org.junit.Assert.assertEquals;

public class StartupControllerTest extends UnitTest {

    @MockBean
    Principal principal;

    String body = "{\n" +
            "    \"email\": \"startup@fundraisey.com\",\n" +
            "    \"password\": \"password\",\n" +
            "    \"grant_type\": \"password\",\n" +
            "    \"client_id\": \"client-web\",\n" +
            "    \"client_secret\": \"password\"\n" +
            "}";

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void getStartupProfile() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(this.body)
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

    @Test
    public void getStartupIndex() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(this.body)
        ).andReturn();

        String response = result.getResponse().getContentAsString();
        response = response.replace("{\"data\":{\"access_token\":\"", "");
        String token = response.replace("\",\"is_enabled\":true,\"roles\":[\"ROLE_STARTUP\"]},\"message\":\"success\",\"status\":200}", "");

        String uri = "/v1/startup/index";

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

    @Test
    public void getLoan() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(this.body)
        ).andReturn();

        String response = result.getResponse().getContentAsString();
        response = response.replace("{\"data\":{\"access_token\":\"", "");
        String token = response.replace("\",\"is_enabled\":true,\"roles\":[\"ROLE_STARTUP\"]},\"message\":\"success\",\"status\":200}", "");

        String uri = "/v1/loan/startup";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .header("Authorization", "Bearer " + token)
                .param("page", "0")
                .param("size", "20")
                .param("sort-by", "status")
                .param("sort-type", "desc")
                .param("search", "test")
                .principal(principal)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();

        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println("Response = ");
        System.out.println(content);
    }

    @Test
    public void getPaymentList() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(this.body)
        ).andReturn();

        String response = result.getResponse().getContentAsString();
        response = response.replace("{\"data\":{\"access_token\":\"", "");
        String token = response.replace("\",\"is_enabled\":true,\"roles\":[\"ROLE_STARTUP\"]},\"message\":\"success\",\"status\":200}", "");

        String uri = "/v1/startup/loan/1/payment-list";

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

    @Test
    public void getNotification() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(this.body)
        ).andReturn();

        String response = result.getResponse().getContentAsString();
        response = response.replace("{\"data\":{\"access_token\":\"", "");
        String token = response.replace("\",\"is_enabled\":true,\"roles\":[\"ROLE_STARTUP\"]},\"message\":\"success\",\"status\":200}", "");

        String uri = "/v1/startup/notification/all";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .header("Authorization", "Bearer " + token)
                .param("page", "0")
                .param("size", "20")
                .param("sort-by", "id")
                .param("sort-type", "desc")
                .principal(principal)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();

        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println("Response = ");
        System.out.println(content);
    }

    @Test
    public void getWithdrawHistory() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(this.body)
        ).andReturn();

        String response = result.getResponse().getContentAsString();
        response = response.replace("{\"data\":{\"access_token\":\"", "");
        String token = response.replace("\",\"is_enabled\":true,\"roles\":[\"ROLE_STARTUP\"]},\"message\":\"success\",\"status\":200}", "");

        String uri = "/v1/startup/loan/withdraw-history";

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

    @Test
    public void getBankListForLoan() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(this.body)
        ).andReturn();

        String response = result.getResponse().getContentAsString();
        response = response.replace("{\"data\":{\"access_token\":\"", "");
        String token = response.replace("\",\"is_enabled\":true,\"roles\":[\"ROLE_STARTUP\"]},\"message\":\"success\",\"status\":200}", "");

        String uri = "/v1/startup/loan/withdraw/bank-list";

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

    @Test
    public void getLoanDetail() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(this.body)
        ).andReturn();

        String response = result.getResponse().getContentAsString();
        response = response.replace("{\"data\":{\"access_token\":\"", "");
        String token = response.replace("\",\"is_enabled\":true,\"roles\":[\"ROLE_STARTUP\"]},\"message\":\"success\",\"status\":200}", "");

        String uri = "/v1/loan/detail/1";

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

    @Test
    public void getPaymentDetail() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(this.body)
        ).andReturn();

        String response = result.getResponse().getContentAsString();
        response = response.replace("{\"data\":{\"access_token\":\"", "");
        String token = response.replace("\",\"is_enabled\":true,\"roles\":[\"ROLE_STARTUP\"]},\"message\":\"success\",\"status\":200}", "");

        String uri = "/v1/startup/loan/1/payment";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .header("Authorization", "Bearer " + token)
                .param("period", "1")
                .principal(principal)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();

        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println("Response = ");
        System.out.println(content);
    }
}
