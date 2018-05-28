package com.projeto.dtos;

import com.google.common.base.Strings;
import com.projeto.entities.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
	private String id;
	private List<ProductDto> children;
	private String code;
	private String label;
	private String observation;
	private Boolean visible;
	private String tooltip;

	public ProductDto(Product product) {
		this.id = product.getId();
		this.code = product.getCode();
		this.label = product.getDescription();
		this.observation = product.getObservation();
		this.children = buildNodes( product.getProducts() );
		this.visible = true;
		this.tooltip = buildTooltip( product );
	}

	private String buildTooltip(Product product) {
		String observation = !Strings.isNullOrEmpty( product.getObservation() ) ?
				" (" + product.getObservation() + ")" :
				"";
		return product.getCode() + " - " + product.getDescription() + observation;
	}

	public Product toEntity() {
		return Product.builder()
				.id( this.id )
				.code( this.code )
				.description( this.label )
				.observation( this.observation )
				.products( buildProducts() )
				.build();
	}

	private List<Product> buildProducts() {
		if (children == null || children.size() == 0)
			return null;
		return children.stream().map( ProductDto::toEntity ).collect( Collectors.toList() );
	}

	private List<ProductDto> buildNodes(List<Product> products) {
		if (products == null || products.size() == 0)
			return null;
		return products.stream().map( ProductDto::new ).collect( Collectors.toList() );
	}

}