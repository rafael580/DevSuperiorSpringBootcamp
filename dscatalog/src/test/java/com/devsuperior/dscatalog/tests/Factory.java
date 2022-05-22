package com.devsuperior.dscatalog.tests;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {
    public static Product createProduct(){
        Product product = new Product(1L,"phone","good phone",800.0,"ifjdidfjfijdijf", Instant.parse("2020-02-02T03:00:00Z"));
        product.getCategories().add(new Category("ELETRONICS"));
        return product;
    }
    public static ProductDTO createProductDTO(){
        Product product = new Product(1L,"phone","good phone",800.0,"ifjdidfjfijdijf", Instant.parse("2020-02-02"));
        product.getCategories().add(new Category("ELETRONICS"));
        return new ProductDTO(product,product.getCategories());
    }
}
