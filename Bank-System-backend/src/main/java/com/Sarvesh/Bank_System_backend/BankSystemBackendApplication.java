package com.Sarvesh.Bank_System_backend;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Locale;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Bank Backend App",
				description = "Backend Rest APIs for My Bank",
				version = "v1.0",
				contact = @Contact(
						name="Sarvesh Shukla",
						email="sarveshshuklaad1011@gmail.com",
						url="https://github.com/Shukl123/Bank-Backend"
				),
				license = @License(
						name="The Bank Backend",
						url="https://github.com/Shukl123/Bank-Backend"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "The Bank Backend app Doucumentatiion",
				url="https://github.com/Shukl123/Bank-Backend"
		)
)
public class BankSystemBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankSystemBackendApplication.class, args);
	}

}
