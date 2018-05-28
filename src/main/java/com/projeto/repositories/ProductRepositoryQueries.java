package com.projeto.repositories;

import java.util.List;

import com.projeto.entities.Product;

public interface ProductRepositoryQueries {
	List<Product> findRootFolders();
}
