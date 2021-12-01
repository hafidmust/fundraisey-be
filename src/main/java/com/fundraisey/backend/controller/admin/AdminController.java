package com.fundraisey.backend.controller.admin;

import com.fundraisey.backend.entity.startup.CredentialStatus;
import com.fundraisey.backend.model.CredentialStatusModel;
import com.fundraisey.backend.model.InvestorVerificationModel;
import com.fundraisey.backend.model.LoanStatusModel;
import com.fundraisey.backend.service.interfaces.admin.AdminService;
import com.fundraisey.backend.service.interfaces.startup.LoanService;
import com.fundraisey.backend.util.ResponseTemplate;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/admin")
public class AdminController {
    @Autowired
    AdminService adminService;
    @Autowired
    ResponseTemplate responseTemplate;

    @Secured("ROLE_ADMIN")
    @GetMapping("/loan")
    ResponseEntity<Map> getAllUnacceptedLoan(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "sort-by", required = false) String sortAttribute,
            @RequestParam(value = "sort-type", required = false) String sortType
    ) {
        page = (page == null) ? 0 : page;
        size = (size == null) ? 20 : size;
        sortAttribute = (sortAttribute == null) ? "" : sortAttribute;
        sortType = (sortType == null) ? "" : sortType;

        Map response = adminService.getAllUnacceptedLoan(page, size, sortAttribute, sortType);
        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/loan/{id}")
    ResponseEntity<Map> getUnacceptedLoanById(@PathVariable("id") Long id) {
        Map response = adminService.getUnacceptedLoanById(id);
        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/loan/accept")
    ResponseEntity<Map> acceptLoan(@RequestBody LoanStatusModel loanStatusModel) {
        Map response = adminService.acceptLoan(loanStatusModel);
        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/loan/reject")
    ResponseEntity<Map> rejectLoan(@RequestBody LoanStatusModel loanStatusModel) {
        Map response = adminService.rejectLoan(loanStatusModel);
        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/credential")
    ResponseEntity<Map> getAllPendingCredential(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "sort-by", required = false) String sortAttribute,
            @RequestParam(value = "sort-type", required = false) String sortType
    ) {
        page = (page == null) ? 0 : page;
        size = (size == null) ? 20 : size;
        sortAttribute = (sortAttribute == null) ? "" : sortAttribute;
        sortType = (sortType == null) ? "" : sortType;

        Map response = adminService.getAllPendingCredential(page, size, sortAttribute, sortType);
        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/credential/{id}")
    ResponseEntity<Map> getCredentialLoanById(@PathVariable("id") Long id) {
        Map response = adminService.getCredentialById(id);
        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/credential/accept")
    ResponseEntity<Map> acceptCredential(@RequestBody CredentialStatusModel credentialStatusModel) {
        Map response = adminService.acceptCredential(credentialStatusModel);
        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/credential/reject")
    ResponseEntity<Map> rejectCredential(@RequestBody CredentialStatusModel credentialStatusModel) {
        Map response = adminService.rejectCredential(credentialStatusModel);
        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/investor-verification/all")
    ResponseEntity<Map> getAllUnacceptedInvestorVerification(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "sort-by", required = false) String sortAttribute,
            @RequestParam(value = "sort-type", required = false) String sortType
    ) {
        page = (page == null) ? 0 : page;
        size = (size == null) ? 20 : size;
        sortAttribute = (sortAttribute == null) ? "" : sortAttribute;
        sortType = (sortType == null) ? "" : sortType;

        Map response = adminService.getAllUnacceptedInvestorVerification(page, size, sortAttribute, sortType);

        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/investor-verification/{investorId}")
    ResponseEntity<Map> getInvestorVerification(@PathVariable Long investorId) {
        Map response = adminService.getInvestorVerificationByInvestorId(investorId);

        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/investor-verification/accept")
    ResponseEntity<Map> acceptInvestorVerification(@RequestBody InvestorVerificationModel investorVerificationModel) {
        Map response = adminService.acceptInvestorVerification(investorVerificationModel);

        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/investor-verification/reject")
    ResponseEntity<Map> rejectInvestorVerification(@RequestBody InvestorVerificationModel investorVerificationModel) {
        Map response = adminService.rejectInvestorVerification(investorVerificationModel);

        return responseTemplate.controllerHttpRestResponse(response);
    }
}
