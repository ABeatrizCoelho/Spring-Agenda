package com.projetolinux.agenda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DemoAgendaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoAgendaApplication.class, args);
	}

}
