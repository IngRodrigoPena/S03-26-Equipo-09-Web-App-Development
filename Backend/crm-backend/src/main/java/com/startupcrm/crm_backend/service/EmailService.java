package com.startupcrm.crm_backend.service;

import com.startupcrm.crm_backend.model.Contacto;
import com.startupcrm.crm_backend.model.Conversacion;
import com.startupcrm.crm_backend.repository.ContactoRepository;
import com.startupcrm.crm_backend.repository.ConversacionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.HashMap;


@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    private final ConversacionRepository conversacionRepository;
    private final ContactoRepository contactoRepository;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.brevo.com/v3/smtp/email")
            .build();

    public EmailService(ConversacionRepository conversacionRepository,
                        ContactoRepository contactoRepository) {
        this.conversacionRepository = conversacionRepository;
        this.contactoRepository = contactoRepository;
    }

    public void sendEmail(Long contactoId, String asunto, String contenido) {

        // 1. Buscar contacto
        Contacto contacto = contactoRepository.findById(contactoId)
                .orElseThrow(() -> new RuntimeException("Contacto no encontrado"));

        if (contacto.getEmail() == null || contacto.getEmail().isEmpty()) {
            throw new RuntimeException("El contacto no tiene email");
        }

        // 2. Construir request a Brevo
        Map<String, Object> body = new HashMap<>();

        body.put("sender", Map.of(
                "name", senderName,
                "email", senderEmail
        ));

        body.put("to", List.of(Map.of(
                "email", contacto.getEmail(),
                "name", contacto.getNombre()
        )));

        body.put("subject", asunto);
        body.put("htmlContent", contenido);

        // 3. Enviar email
        try{
            webClient.post()
                .header("api-key", apiKey)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        } catch (Exception e) {
            throw new RuntimeException("Error enviando email: " + e.getMessage());
        }


        // 4. Guardar en DB
        Conversacion conv = new Conversacion();
        conv.setCanal("EMAIL");
        conv.setContenido(contenido);
        conv.setAsunto(asunto);
        conv.setEsEntrante(false);
        conv.setLeido(true);
        conv.setContacto(contacto);

        conversacionRepository.save(conv);
    }
}
