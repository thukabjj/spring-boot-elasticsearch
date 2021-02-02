package br.com.elasticsearch;

import br.com.elasticsearch.service.CustomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CustomerApplication {

	@Autowired
	private CustomeService customeService;

	public static void main(String[] args) {
		SpringApplication.run(CustomerApplication.class, args);
	}

	@Bean
	@ConditionalOnExpression("${start-up-data:true}")
	public void processData(){
		customeService.registerCustomerIndexBulkWithElasticseartRestTemplate();
	}
}
