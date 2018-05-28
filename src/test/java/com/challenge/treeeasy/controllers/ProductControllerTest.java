package com.challenge.treeeasy.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;	
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import com.projeto.config.Application;
import com.projeto.dtos.ProductDto;
import com.projeto.dtos.ProductsDto;
import com.projeto.entities.Product;
import com.projeto.interactions.product.ProductAdition;
import com.projeto.repositories.ProductRepository;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class ProductControllerTest {

	@Autowired
	private ProductRepository repository;

	@Autowired
	private ProductAdition productAdition;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void list() throws Exception {
		createProducts();

		ProductsDto productsDto = restTemplate.getForEntity( "/products", ProductsDto.class ).getBody();

		assertThat( productsDto.getProducts().size(), equalTo( 2 ) );
	}

	@Test
	public void saveRootProduct() throws Exception {
		Product product = buildNode( "01.1" );

		ProductDto productDto = restTemplate
				.postForEntity( "/products", new ProductDto( product ), ProductDto.class )
				.getBody();

		assertNotNull( productDto.getId() );
		assertNull( productDto.getObservation() );
		assertThat( productDto.getCode(), equalTo( "01.1" ) );
		assertThat( productDto.getLabel(), equalTo( "P01.1" ) );
		assertThat( productDto.getTooltip(), equalTo( "01.1 - P01.1" ) );
		assertNull( repository.findOne( productDto.getId() ).getParent() );
	}

	@Test
	public void saveChildProduct() throws Exception {
		Product rootNode = repository.save( buildNode( "01" ) );
		Product product = buildNode( "01.1" );
		product.setObservation( "OBS 01" );

		ProductDto productDto = restTemplate
				.postForEntity( "/products/" + rootNode.getId(), new ProductDto( product ), ProductDto.class )
				.getBody();

		assertNotNull( productDto.getId() );
		assertThat( productDto.getObservation(), equalTo( "OBS 01" ) );
		assertThat( productDto.getCode(), equalTo( "01.1" ) );
		assertThat( productDto.getLabel(), equalTo( "P01.1" ) );
		assertThat( productDto.getTooltip(), equalTo( "01.1 - P01.1 (OBS 01)" ) );
		assertParentNode( repository.findOne( rootNode.getId() ), productDto );
	}

	@Test
	public void delete() throws Exception {
		Product parentA = repository.save( buildNode( "01" ) );
		Product product = productAdition.save( parentA.getId(), buildNode( "01.1" ) );
		restTemplate.delete( "/products/" + parentA.getId() );

		assertNull( repository.findOne( parentA.getId() ) );
		assertNull( repository.findOne( product.getId() ) );
	}

	private void assertParentNode(Product product, ProductDto productDto) {
		assertThat( product.getProducts().size(), equalTo( 1 ) );
		assertThat( product.getProducts().get( 0 ).getId(), equalTo( productDto.getId() ) );
		assertThat( product.getProducts().get( 0 ).getDescription(), equalTo( productDto.getLabel() ) );
		assertThat( product.getProducts().get( 0 ).getCode(), equalTo( productDto.getCode() ) );
	}

	private void createProducts() {
		Product parentA = repository.save( buildNode( "01" ) );
		Product parentB = repository.save( buildNode( "02" ) );

		productAdition.save( parentA.getId(), buildNode( "01.1" ) );
		productAdition.save( parentB.getId(), buildNode( "02.1" ) );
		productAdition.save( parentB.getId(), buildNode( "02.2" ) );
		productAdition.save( parentB.getId(), buildNode( "02.3" ) );
	}

	private Product buildNode(String code) {
		return Product.builder()
				.code( code )
				.description( "P" + code )
				.build();
	}

}