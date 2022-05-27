package com.devsuperior.dscatalog.servicies;

import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.servicies.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductServiceIT {

    @Autowired
    private ProductService service;
    @Autowired
    private ProductRepository repository;

    private Long existId;
    private Long nonExistId;
    private Long countTotalProducts;


    @BeforeEach
    void setUo() throws Exception {
        existId =1L;
        nonExistId = 2000L;
        countTotalProducts = 25L;
    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExist(){
        service.delete(existId);
        Assertions.assertEquals(countTotalProducts-1,repository.count());
    }
    @Test
    public void deleteShouldDeleteResourceNotFoundExceptionWhenIdNotExist(){
        Assertions.assertThrows(ResourceNotFoundException.class,()->{
            service.delete(nonExistId);
        });
    }
}
