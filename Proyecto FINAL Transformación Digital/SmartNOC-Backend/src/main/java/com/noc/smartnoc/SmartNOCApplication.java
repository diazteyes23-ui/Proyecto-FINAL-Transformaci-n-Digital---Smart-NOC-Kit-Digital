package com.noc.smartnoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title       = "SmartNOC API",
        version     = "1.0",
        description = "API REST para la gestión del Centro de Operaciones de Red (NOC) digitalizado. " +
                      "Permite gestionar incidencias, órdenes de trabajo, técnicos y equipos de red.",
        contact     = @Contact(name = "Equipo SmartNOC", email = "noc@smartnoc.es")
    )
)
public class SmartNOCApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartNOCApplication.class, args);
    }
}
