package com.devsuperior.dscatalog.controllers;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.servicies.ProductService;
import com.devsuperior.dscatalog.servicies.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService service;
    @Autowired
    private ObjectMapper objectMapper;

    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;
    private Long existId;
    private Long nonExistId;
    private Long associateId;
    @BeforeEach
    void setUp() throws Exception{
        existId =1L;
        nonExistId =2L;
        associateId =3L;
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));
        //GET
        when(service.findAllPaged(any())).thenReturn(page);
        when(service.findById(existId)).thenReturn(productDTO);
        when(service.findById(nonExistId)).thenThrow(ResourceNotFoundException.class);
        //PUT
        when(service.update(eq(existId),any())).thenReturn(productDTO);
        when(service.update(eq(nonExistId),any())).thenThrow(ResourceNotFoundException.class);
        //POST
        when(service.insert(any())).thenReturn(productDTO);
        //DELETE
        doNothing().when(service).delete(existId);
        doThrow(ResourceNotFoundException.class).when(service).delete(nonExistId);
//      doThrow(DataAccessException.class).when(service).delete(associateId);
    }
    @Test
    public void deleteShouldDoExceptionWhenIdNotExist() throws Exception{
        ResultActions resultActions = mockMvc.perform(delete("/products/{id}",nonExistId)
                .accept(MediaType.APPLICATION_JSON)
        );
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldDonothindWhenIdExist() throws Exception{
        ResultActions resultActions = mockMvc.perform(delete("/products/{id}",existId)
                .accept(MediaType.APPLICATION_JSON)
        );
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    public void insertShouldReturnProduct() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);
        ResultActions resultActions = mockMvc.perform(post("/products")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );
        resultActions.andExpect(status().is2xxSuccessful());
    }
    @Test
    public void updateShouldNotWhenIdNotExist() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);
        ResultActions resultActions = mockMvc.perform(put("/products/{id}", nonExistId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );
        resultActions.andExpect(status().isNotFound());
    }
    @Test
    public void updateShouldReturnProductWhenIdExist() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions resultActions = mockMvc.perform(put("/products/{id}",existId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
    }


    @Test
    public void findByIdShouldReturnProductsFoundWhenIdExist() throws Exception {
        ResultActions resultActions =
                mockMvc.perform(get("/products/{id}",existId)
                        .accept(MediaType.APPLICATION_JSON)
                );
        resultActions.andExpect(status().isOk());
    }
    @Test
    public void findByIdShouldReturnNotFoundWhenIdNotExist() throws Exception{
            ResultActions resultActions =
                    mockMvc.perform(get("/products/{id}",nonExistId)
                            .accept(MediaType.APPLICATION_JSON)
                    );
            resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void findAllShouldReturnPage() throws Exception{
        ResultActions result = mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON)
        );
        result.andExpect(status().isOk());
    }

}
