package com.fundraisey.backend.unittest.startup;

import com.fundraisey.backend.unittest.UnitTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
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
    public void insertLoan() throws Exception {
        String json = "{\n" +
                "    \"title\":\"Test Loan\",\n" +
                "    \"targetValue\":10000000,\n" +
                "    \"description\":\"This is a description for test loan\",\n" +
                "    \"endDate\":\"2022-01-01\",\n" +
                "    \"interestRate\":10,\n" +
                "    \"paymentPlanId\":2\n" +
                "}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(this.body)
        ).andReturn();

        String response = result.getResponse().getContentAsString();
        response = response.replace("{\"data\":{\"access_token\":\"", "");
        String token = response.replace("\",\"is_enabled\":true,\"roles\":[\"ROLE_STARTUP\"]},\"message\":\"success\",\"status\":200}", "");

        String uri = "/v1/loan/insert";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .header("Authorization", "Bearer " + token)
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println("Response=");
        System.out.println(content);
    }

    @Test
    public void insertStartupLoanWithdraw() throws Exception {
        String json = "{\n" +
                "    \"loanId\":1,\n" +
                "    \"accountNumber\":\"132809828419\",\n" +
                "    \"bankId\":1\n" +
                "}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(this.body)
        ).andReturn();

        String response = result.getResponse().getContentAsString();
        response = response.replace("{\"data\":{\"access_token\":\"", "");
        String token = response.replace("\",\"is_enabled\":true,\"roles\":[\"ROLE_STARTUP\"]},\"message\":\"success\",\"status\":200}", "");

        String uri = "/v1/startup/loan/withdraw";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .header("Authorization", "Bearer " + token)
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println("Response=");
        System.out.println(content);
    }

    @Test
    public void insertStartupPayReturn() throws Exception {
        String json = "{\n" +
                "    \"loanId\":1,\n" +
                "    \"period\":1\n" +
                "}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(this.body)
        ).andReturn();

        String response = result.getResponse().getContentAsString();
        response = response.replace("{\"data\":{\"access_token\":\"", "");
        String token = response.replace("\",\"is_enabled\":true,\"roles\":[\"ROLE_STARTUP\"]},\"message\":\"success\",\"status\":200}", "");

        String uri = "/v1/startup/loan/pay";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .header("Authorization", "Bearer " + token)
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println("Response=");
        System.out.println(content);
    }

    @Test
    public void insertCredential() throws Exception {
        String json = "{\n" +
                "    \"name\":\"Credential test 2\",\n" +
                "    \"issuingOrganization\":\"Organization X\",\n" +
                "    \"issueDate\":\"2020-01-01\",\n" +
                "    \"expirationDate\":\"2023-01-01\",\n" +
                "    \"credentialUrl\":\"httpL//www.example.com/credential-url\",\n" +
                "    \"credentialId\":\"8f0as9d894u\",\n" +
                "    \"credentialDescription\":\"Test credential description\",\n" +
                "    \"credentialTypeId\":1\n" +
                "}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(this.body)
        ).andReturn();

        String response = result.getResponse().getContentAsString();
        response = response.replace("{\"data\":{\"access_token\":\"", "");
        String token = response.replace("\",\"is_enabled\":true,\"roles\":[\"ROLE_STARTUP\"]},\"message\":\"success\",\"status\":200}", "");

        String uri = "/v1/startup/credential/add";

        String text ="Text to be uploaded.";
        MockMultipartFile file = new MockMultipartFile("file","test.txt", "text/plain", text.getBytes());

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .header("Authorization", "Bearer " + token)
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println("Response=");
        System.out.println(content);
    }

    @Test
    public void insertProduct() throws Exception {
        String json = "{\n" +
                "    \"name\":\"Test Product\",\n" +
                "    \"description\":\"This is a test product\",\n" +
                "    \"url\":\"http://www.example.com\"\n" +
                "}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(this.body)
        ).andReturn();

        String response = result.getResponse().getContentAsString();
        response = response.replace("{\"data\":{\"access_token\":\"", "");
        String token = response.replace("\",\"is_enabled\":true,\"roles\":[\"ROLE_STARTUP\"]},\"message\":\"success\",\"status\":200}", "");

        String uri = "/v1/startup/products/add";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .header("Authorization", "Bearer " + token)
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println("Response=");
        System.out.println(content);
    }

    @Test
    public void uploadProductPhoto() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(this.body)
        ).andReturn();

        String response = result.getResponse().getContentAsString();
        response = response.replace("{\"data\":{\"access_token\":\"", "");
        String token = response.replace("\",\"is_enabled\":true,\"roles\":[\"ROLE_STARTUP\"]},\"message\":\"success\",\"status\":200}", "");

        String uri = "/v1/upload/startup/product/1";
        MockMultipartFile file = new MockMultipartFile("images","erd.png", "image/png", "erd".getBytes());

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.multipart(uri)
                .file(file)
                .header("Authorization", "Bearer " + token)
                .principal(principal)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println("Response=");
        System.out.println(content);
    }

    @Test
    public void uploadCredentialDocument() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(this.body)
        ).andReturn();

        String response = result.getResponse().getContentAsString();
        response = response.replace("{\"data\":{\"access_token\":\"", "");
        String token = response.replace("\",\"is_enabled\":true,\"roles\":[\"ROLE_STARTUP\"]},\"message\":\"success\",\"status\":200}", "");

        String uri = "/v1/upload/startup/credential/1";
        MockMultipartFile file = new MockMultipartFile("images","erd.png", "image/png", "erd".getBytes());

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.multipart(uri)
                .file(file)
                .header("Authorization", "Bearer " + token)
                .principal(principal)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println("Response=");
        System.out.println(content);
    }

    @Test
    public void uploadStartupLogo() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(this.body)
        ).andReturn();

        String response = result.getResponse().getContentAsString();
        response = response.replace("{\"data\":{\"access_token\":\"", "");
        String token = response.replace("\",\"is_enabled\":true,\"roles\":[\"ROLE_STARTUP\"]},\"message\":\"success\",\"status\":200}", "");

        String uri = "/v1/upload/startup/logo";
        MockMultipartFile file = new MockMultipartFile("images","erd.png", "image/png", "erd".getBytes());

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.multipart(uri)
                .file(file)
                .header("Authorization", "Bearer " + token)
                .principal(principal)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println("Response=");
        System.out.println(content);
    }

    @Test
    public void updateStartupDetail() throws Exception {
        String json = "{\n" +
                "    \"name\":\"Fundraisey\",\n" +
                "    \"description\":\"Fundraisey is a startup fundraiser platform\",\n" +
                "    \"phoneNumber\":\"081234567899\",\n" +
                "    \"web\":\"http://www.fundraisey.com\",\n" +
                "    \"address\":\"Infini Space, Jl. Kabupaten, Nusupan, Trihanggo, Gamping, Sleman Regency, Special Region of Yogyakarta 55291\",\n" +
                "    \"foundedDate\":\"2020-01-01\",\n" +
                "    \"email\":\"hello@fundraisey.com\",\n" +
                "    \"youtube\":\"https://www.youtube.com/fundraisey\",\n" +
                "    \"linkedin\":\"https://www.linkedin.com/fundraisey\",\n" +
                "    \"instagram\":\"https://www.instagram.com/fundraisey\"\n" +
                "}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(this.body)
        ).andReturn();

        String response = result.getResponse().getContentAsString();
        response = response.replace("{\"data\":{\"access_token\":\"", "");
        String token = response.replace("\",\"is_enabled\":true,\"roles\":[\"ROLE_STARTUP\"]},\"message\":\"success\",\"status\":200}", "");

        String uri = "/v1/user/startup/update";
        String inputJson = super.mapToJson(json);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                .header("Authorization", "Bearer " + token)
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println("Response=");
        System.out.println(content);
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
