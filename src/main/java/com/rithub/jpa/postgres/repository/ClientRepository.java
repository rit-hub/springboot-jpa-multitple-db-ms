package com.rithub.jpa.postgres.repository;

import com.rithub.jpa.postgres.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
