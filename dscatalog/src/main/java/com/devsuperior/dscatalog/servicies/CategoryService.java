package com.devsuperior.dscatalog.servicies;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.servicies.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll(){
        List<Category> obj = repository.findAll();
        List<CategoryDTO> listDTO =  obj.stream().map(x-> new CategoryDTO(x)).collect(Collectors.toList());
        return listDTO;
    }
    public CategoryDTO findById(Long id){
        Optional<Category> obj = repository.findById(id);
        Category cat = obj.orElseThrow(()-> new EntityNotFoundException("Entity not found"));
        CategoryDTO objDTO = new CategoryDTO(cat);
        return objDTO;
    }
}
