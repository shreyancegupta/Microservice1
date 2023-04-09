package com.app.productservice;

import java.math.BigDecimal;

import org.junit.experimental.results.ResultMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.app.productservice.dto.ProductRequest;
import com.app.productservice.repo.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ProductRepository productRepository;
	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dymPropertyRegistery)
	{
		dymPropertyRegistery.add("spring.data.mongodb.uri",mongoDBContainer::getReplicaSetUrl);

	}

	@Test
	void shouldCreateProduct () throws Exception 
	{
		ProductRequest prdReq=	getProductRequest();
		String contentVal=objectMapper.writeValueAsString(prdReq);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
		.contentType(MediaType.APPLICATION_JSON)
		.content(contentVal))
		.andExpect(status().isCreated());
		Assertions.assertTrue(productRepository.findAll().size()==1);
	}

	@Test
	void shouldGetAllProduct() throws Exception
	{
			ProductRequest prdReq=	getProductRequest();
			mockMvc.perform(MockMvcRequestBuilders.get("/api/product"))
			.andExpect(status().isOk()).andReturn().equals(prdReq);
	}

	private ProductRequest getProductRequest()
	{
		return ProductRequest
		.builder()
		.name("iphone_1")
		.description("uiphone 1 des")
		.price(BigDecimal.valueOf(12345))
		.build();
	}


}
