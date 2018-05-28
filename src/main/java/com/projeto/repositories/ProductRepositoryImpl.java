package com.projeto.repositories;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

import com.projeto.entities.Product;
import com.projeto.entities.QProduct;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class ProductRepositoryImpl implements ProductRepositoryQueries {

	private static final QProduct PRODUCT = QProduct.product;

	@Autowired
	private EntityManager entityManager;

	@Override
	public List<Product> findRootFolders() {
		return new JPAQueryFactory( entityManager )
				.selectFrom( PRODUCT )
				.where( PRODUCT.parent.isNull() )
				.orderBy( PRODUCT.description.asc() )
				.fetch();
	}

}
