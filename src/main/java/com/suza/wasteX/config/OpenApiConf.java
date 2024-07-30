package com.suza.wasteX.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
    public class OpenApiConf {

        @Bean
        public OpenAPI defineOpenApi() {
            Server server = new Server();
            server.setUrl("http://localhost:8080");
            server.setDescription("Development");

            Contact myContact = new Contact();
            myContact.name("Juma Mohammed Juma");
            myContact.email("jmjgenero@gmail.com");


            Info information = new Info()
                    .title("Wastex Lab Management System API")
                    .version("1.0")
                    .description("This API exposes endpoints to manage Wastex Lab.")
                    .contact(myContact)
                    ;

            return new OpenAPI().info(information).servers(List.of(server));
        }
    }

