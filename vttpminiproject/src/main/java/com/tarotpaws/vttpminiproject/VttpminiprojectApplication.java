package com.tarotpaws.vttpminiproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tarotpaws.vttpminiproject.service.TarotService;

@SpringBootApplication
public class VttpminiprojectApplication implements CommandLineRunner{

	@Autowired
	TarotService tarotService;
	public static void main(String[] args) {
		SpringApplication.run(VttpminiprojectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
	}

}
