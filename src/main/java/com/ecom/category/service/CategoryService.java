package com.ecom.category.service;

import com.ecom.category.domain.Category;
import com.ecom.category.exception.ApiException;
import com.ecom.category.exception.ResourceNotFoundException;
import com.ecom.category.dto.CategoryDTO;
import com.ecom.category.payload.CategoryResponse;
import com.ecom.category.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    public CategoryResponse getAll(Integer pageNumber, Integer pageSize) {

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        List<Category> categories = categoryPage.getContent(); //categoryRepository.findAll();
        if(categories.isEmpty()){
            throw new ApiException("No Categories Existed");
        }

        List<CategoryDTO> categoryDTOS = categories.stream()
                                         .map(category -> modelMapper.map(category, CategoryDTO.class))
                                         .toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);

        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());

        return categoryResponse;
    }

    public CategoryDTO getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("category","categoryid",id));

        return modelMapper.map(category,CategoryDTO.class);
    }

    public CategoryDTO save(CategoryDTO categoryDTO){

            Category category = modelMapper.map(categoryDTO, Category.class);

            Category categoryFromdb = categoryRepository.findByCategoryName(category.getCategoryName());

            if(categoryFromdb != null){
                throw new ApiException("category with the name "+categoryFromdb.getCategoryName()+" is already exists oops!!");
            }

            Category savedCategory = categoryRepository.save(category);

            CategoryDTO savedCategoryDTO = modelMapper.map(savedCategory, CategoryDTO.class);


        return savedCategoryDTO;
    }

    public CategoryDTO delete(Long id)  {

        Category category = categoryRepository.findById(id).orElse(null);
        if(category == null){
            throw new ApiException("No category is created");
        }
        else{
              categoryRepository.delete(category);
            return modelMapper.map(category, CategoryDTO.class);
        }
    }

    public CategoryDTO update(CategoryDTO categoryDTO, Long id)  {

        Category category1 = categoryRepository.findById(id).orElse(null);

        if(category1 != null){
                Category category = modelMapper.map(categoryDTO, Category.class);

                category1.setCategoryName(category.getCategoryName());
                Category savedCategory = categoryRepository.save(category1);

                CategoryDTO savedCategoryDTO = modelMapper.map(savedCategory,CategoryDTO.class);

            return savedCategoryDTO;
        }
        else{
            throw new ResourceNotFoundException("category","categoryid",id);
        }
    }


}
