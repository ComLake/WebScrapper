package com.example.comlakecrawler;

import com.example.comlakecrawler.service.downloader.target.DriveCrawler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ComlakecrawlerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ComlakecrawlerApplication.class, args);
		new DriveCrawler().setUp();
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
