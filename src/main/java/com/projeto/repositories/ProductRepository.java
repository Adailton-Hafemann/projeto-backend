package com.projeto.repositories;

import org.springframework.data.repository.CrudRepository;

import com.projeto.entities.Product;

public interface ProductRepository extends CrudRepository<Product, String>, ProductRepositoryQueries {
}
