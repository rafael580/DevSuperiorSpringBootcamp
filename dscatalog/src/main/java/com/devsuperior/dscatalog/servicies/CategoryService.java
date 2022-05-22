package com.devsuperior.dscatalog.servicies;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
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
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(Pageable pageAble){
        Page<Category> obj = repository.findAll(pageAble);
        return obj.map(x-> new CategoryDTO(x));
    }
    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id){
        Optional<Category> obj = repository.findById(id);
        Category cat = obj.orElseThrow(()-> new ResourceNotFoundException("Entity not found"));
        CategoryDTO objDTO = new CategoryDTO(cat);
        return objDTO;
    }
    @Transactional
    public CategoryDTO insert(CategoryDTO category){
        Category cat = new Category(category.getName());
        cat =  repository.save(cat);
        return new CategoryDTO(cat);
    }
    @Transactional
    public CategoryDTO update(Long id, CategoryDTO category){
        try {
            Category cat = repository.getOne(id);
            cat.setName(category.getName());
            cat = repository.save(cat);
            return new CategoryDTO(cat);
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
