package com.startupcrm.crm_backend.controller;

import com.startupcrm.crm_backend.dto.EmailRequest;
import com.startupcrm.crm_backend.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest request) {

        emailService.sendEmail(
                request.getContactoId(),
                request.getAsunto(),
                request.getContenido()
        );

        return ResponseEntity.ok("Email enviado correctamente");
    }
}

