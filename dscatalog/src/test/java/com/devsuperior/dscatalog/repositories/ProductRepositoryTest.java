package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;
// teste de integração
// testa os metodos do repository como findByID e/ou save;
@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository repository;

    Long existngId;
    Long nonExistngId;
    Long contTotalProducts;
    @BeforeEach
    void setUp() throws Exception{
        existngId = 1L;
        nonExistngId = 45L;
        contTotalProducts = 25L;
    }
    @Test
    public void saveShouldPersintWhichIdAutoIncrementWhenIdIsNull(){
        Product product = Factory.createProduct();
        product.setId(null);

        product = repository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(contTotalProducts+1,product.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){

        repository.deleteById(existngId);
        Optional<Product> result = repository.findById(existngId);
        Assertions.assertFalse(result.isPresent());
    }
    @Test
    public void deleteShouldThrowEmptyResultWhenDoesNotExist(){
        Assertions.assertThrows(EmptyResultDataAccessException.class,()->{
            repository.deleteById(nonExistngId);
        });
    }
    @Test
    public void findByIdShouldReturnOptionalProductWhenIdExist(){
        Optional<Product> product = repository.findById(existngId);
        Assertions.assertTrue(product.isPresent());
    } @Test
    public void findByIdShouldReturnOptionalProductWhenIdNotExist(){
        Optional<Product> product = repository.findById(nonExistngId);
        Assertions.assertFalse(product.isPresent());
    }
}
