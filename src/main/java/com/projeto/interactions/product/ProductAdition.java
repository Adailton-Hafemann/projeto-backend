package com.projeto.interactions.product;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.projeto.entities.Product;
import com.projeto.repositories.ProductRepository;

@Service
public class ProductAdition {

	@Autowired
	private ProductRepository repository;

	public Product save(String parentId, Product product) {
		Optional<Product> parent = getProductParent( parentId );
		if (parent.isPresent())
			product.setParent( parent.get() );
		return repository.save( product );
	}

	private Product addProduct(Product parent, Product product) {
		if (parent.getProducts() == null)
			parent.setProducts( Lists.newArrayList() );
		parent.getProducts().add( product );
		return parent;
	}

	private Optional<Product> getProductParent(String parentId) {
		if (!Strings.isNullOrEmpty( parentId ))
			return Optional.ofNullable( repository.findOne( parentId ) );
		return Optional.empty();
	}

}
