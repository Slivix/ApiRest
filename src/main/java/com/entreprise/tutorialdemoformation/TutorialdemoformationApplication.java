package com.entreprise.tutorialdemoformation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TutorialdemoformationApplication {

	public static void main(String[] args) {
		System.setProperty("socksProxyHost", "localhost");
        System.setProperty("socksProxyPort", "1234");

		SpringApplication.run(TutorialdemoformationApplication.class, args);
	}

}
