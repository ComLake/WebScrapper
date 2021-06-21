package com.example.comlakecrawler;

import com.example.comlakecrawler.service.downloader.target.BoxCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.JdbcProperties;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.swing.*;

@SpringBootApplication
public class ComlakecrawlerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ComlakecrawlerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
