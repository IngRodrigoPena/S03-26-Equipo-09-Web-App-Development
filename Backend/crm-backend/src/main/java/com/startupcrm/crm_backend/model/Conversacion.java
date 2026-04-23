package com.startupcrm.crm_backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Table(name = "conversaciones")
public class Conversacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Enumerated(EnumType.STRING)
    //private Canal canal; // WhatsApp, Email
    private String canal; // WhatsApp, Email

    @Column(columnDefinition = "TEXT")
    private String contenido;

    private LocalDateTime fechaHora = LocalDateTime.now();

    private Boolean esEntrante; // true: Cliente -> CRM | false: CRM -> Cliente
    private Boolean leido = false;

    @ManyToOne
    @JoinColumn(name = "contacto_id")
    private Contacto contacto;

    @Column(columnDefinition = "TEXT")
    private String asunto;

    private String estado; // ENVIADO, ERROR
}