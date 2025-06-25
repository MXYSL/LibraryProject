package com.Team3.LibraryProject.Config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.Team3.LibraryProject.Repository")
@EntityScan(basePackages = "com.Team3.LibraryProject.Entity")
@EnableTransactionManagement
public class DatabaseConfig {
    // Configuración específica de base de datos si es necesaria
}