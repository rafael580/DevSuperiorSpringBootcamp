package com.devsuperior.dscatalog.servicies;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.servicies.exceptions.DataBaseException;
import com.devsuperior.dscatalog.servicies.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id){
        Optional<Product> obj = repository.findById(id);
        Product cat = obj.orElseThrow(()-> new ResourceNotFoundException("Entity not found"));
        return new ProductDTO(cat);

    }
    @Transactional
    public ProductDTO insert(ProductDTO category){
        Product cat = new Product(category.getName());
        cat =  repository.save(cat);
        return new ProductDTO(cat);
    }
    @Transactional
    public ProductDTO update(Long id, ProductDTO category){
        try {
            Product cat = repository.getOne(id);
            cat.setName(category.getName());
            cat = repository.save(cat);
            return new ProductDTO(cat);
        }
        catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id not found" + id);
        }
    }
    public void delete(Long id){
        try {
            repository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Id not found" + id);
        }
        catch (DataIntegrityViolationException e){
            throw new DataBaseException("Integrety violation");
        }
    }
}
