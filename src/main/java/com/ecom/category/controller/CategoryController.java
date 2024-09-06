package com.ecom.category.controller;

import com.ecom.category.service.CategoryService;
import com.ecom.category.dto.CategoryDTO;
import com.ecom.category.payload.CategoryResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public  ResponseEntity<CategoryResponse> getAllCategories(
                             @RequestParam(name = "pageNumber", defaultValue = "0",required = false) Integer pageNumber,
                             @RequestParam(name = "pageSize", defaultValue = "50", required = false) Integer pageSize){


        CategoryResponse categoryResponse = categoryService.getAll(pageNumber,pageSize);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable(name ="id") Long id){
        CategoryDTO categoryDTO =  categoryService.getById(id);
        return new ResponseEntity<>(categoryDTO,HttpStatus.OK);
    }

    @PostMapping("/categories")
    public ResponseEntity<CategoryDTO> saveCategory(@RequestBody CategoryDTO categoryDTO){

        CategoryDTO savedCategoryDTO = categoryService.save(categoryDTO);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long id){

        CategoryDTO deletedCategoryDTO = categoryService.delete(id);

            return new ResponseEntity<>(deletedCategoryDTO, HttpStatus.OK);

    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO,
                                                   @PathVariable Long id){

                CategoryDTO savedCategoryDTO = categoryService.update(categoryDTO, id);

            return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);


    }

}
