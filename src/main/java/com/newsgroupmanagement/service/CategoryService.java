package com.newsgroupmanagement.service;

import com.newsgroupmanagement.dto.CategoryDto;
import com.newsgroupmanagement.model.Category;
import com.newsgroupmanagement.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    // Convert Category entity to CategoryDTO
    public CategoryDto convertToDTO(Category category) {
        return modelMapper.map(category, CategoryDto.class);
    }

    // Convert CategoryDTO to Category entity
    public Category convertToEntity(CategoryDto categoryDto) {
        return modelMapper.map(categoryDto, Category.class);
    }

    public List<CategoryDto> getAllCategories(){
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDto> categoryDto = new ArrayList<>();

        for(Category category : categories){
            categoryDto.add(convertToDTO(category));
        }
        return categoryDto;
    }
}
