package com.rithub.jpa.postgres.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "client")
@Data
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
}
