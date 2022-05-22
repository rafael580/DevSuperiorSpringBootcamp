package com.devsuperior.dscatalog.servicies;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.servicies.exceptions.DataBaseException;
import com.devsuperior.dscatalog.servicies.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(Pageable pageAble){
        Page<Product> obj = repository.findAll( pageAble);
        return obj.map(x-> new ProductDTO(x));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id){
        Optional<Product> obj = repository.findById(id);
        Product cat = obj.orElseThrow(()-> new ResourceNotFoundException("Entity not found"));
        return new ProductDTO(cat,cat.getCategories());

    }
    @Transactional
    public ProductDTO insert(ProductDTO dto){
        Product cat = new Product();
        copyDtoToEntity(dto,cat);
        cat =  repository.save(cat);
        return  new ProductDTO(cat);
    }
    @Transactional
    public ProductDTO update(Long id, ProductDTO dto){
        try {
            Product cat = repository.getOne(id);
            copyDtoToEntity(dto,cat);
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
    private void copyDtoToEntity(ProductDTO dto, Product pro){
        pro.setName(dto.getName());
        pro.setDescription(dto.getDescription());
        pro.setDate(dto.getDate());
        pro.setPrice(dto.getPrice());
        pro.setImgUrl(dto.getImgUrl());

        pro.getCategories().clear();
        for (CategoryDTO catDTO:
             dto.getCategoryDTOS()) {
           Category category = categoryRepository.getOne(catDTO.getId());
           pro.getCategories().add(category);
        }
    }
}
