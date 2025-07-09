package com.bipsqwake.anxios_shop_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class AnxiosShopApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnxiosShopApiApplication.class, args);
	}

}
