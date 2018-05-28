package com.projeto.entities;

import lombok.*;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Product parent;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, mappedBy = "parent")
	private List<Product> products;

	@NotNull
	private String code;

	@NotNull
	private String description;

	private String observation;

	private Date createdAt;

	private Date updatedAt;

	@PrePersist
	private void beforePersist() {
		createdAt = new Date();
		updatedAt = createdAt;
	}

	@PreUpdate
	private void beforeUpdate() {
		updatedAt = new Date();
	}

}