package com.devsuperior.dscatalog.servicies;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.servicies.exceptions.DataBaseException;
import com.devsuperior.dscatalog.servicies.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

// serve para a camada service essa notação sem iniciar o spring
@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    private Long existId;
    private Long nonExistId;
    private Long dependentId;
    private Product product;
    private PageImpl<Product> page;
    private Category category;
    private ProductDTO productdto;

    @BeforeEach
    void setUo() throws Exception {

        existId =1L;
        nonExistId = 2L;
        dependentId = 3L;
        category = Factory.createCategory();
        product = Factory.createProduct();
        page = new PageImpl<>(List.of(product));
        productdto = Factory.createProductDTO();
        //findAll varios produtos
        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
        //save
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
        //FindById
        Mockito.when(repository.findById(existId)).thenReturn(Optional.of(product));
        Mockito.when(repository.findById(nonExistId)).thenReturn(Optional.empty());
        Mockito.doThrow(ResourceNotFoundException.class).when(repository).findById(nonExistId);
        //oneGet
        Mockito.when(repository.getOne(existId)).thenReturn(product);
        Mockito.when(repository.getOne(nonExistId)).thenThrow(EntityNotFoundException.class);
        Mockito.when(categoryRepository.getOne(existId)).thenReturn(category);
        Mockito.when(categoryRepository.getOne(nonExistId)).thenThrow(EntityNotFoundException.class);
        // delete
        Mockito.doNothing().when(repository).deleteById(existId);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

    }
    @Test
    public void updateShouldResourceNotFoundExceptionWhenIdNotExist(){
        Assertions.assertThrows(ResourceNotFoundException.class,()->{
           service.update(nonExistId,productdto);
        });
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExist(){
        ProductDTO productDTO = service.update(existId,productdto);
        Assertions.assertNotNull(productDTO);
    }


    @Test
    public void findByIdShouldResourceNotFoundExceptionWhenIdNotExist(){
        Assertions.assertThrows(ResourceNotFoundException.class,()->{
            service.findById(nonExistId);
        });
        Mockito.doThrow(ResourceNotFoundException.class).when(repository).findById(nonExistId);
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExist(){

        ProductDTO productDTO = service.findById(existId);
        Assertions.assertNotNull(productDTO);

        Mockito.verify(repository).findById(existId);
    }

    @Test
    public void findAllPageShouldReturnPage(){
        Pageable pageable = PageRequest.of(0,10);
        Page<ProductDTO> result = service.findAllPaged(pageable);

        Assertions.assertNotNull(result);
        Mockito.verify(repository).findAll(pageable);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExist() {
        Assertions.assertDoesNotThrow(() -> {
            service.delete(existId);
        });
        Mockito.verify(repository).deleteById(existId);
    }
    @Test
    public void deleteShoulTrhowEmptyResultDataAccessExceptionWhenIdNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class,()->{
           service.delete(nonExistId);
        });
        Mockito.doThrow(ResourceNotFoundException.class).when(repository).deleteById(nonExistId);
    }
    @Test
    public void deleteShouldThrowDataBaseExceptionWhenDependentId(){
        Assertions.assertThrows(DataBaseException.class,()->{
            service.delete(dependentId);
        });
        Mockito.doThrow(DataBaseException.class).when(repository).deleteById(dependentId);
    }
}
