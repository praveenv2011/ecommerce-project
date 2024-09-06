package com.ecom.product.repository;

import com.ecom.product.domain.Product;
import com.ecom.product.dto.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    //List<Product> findByCategoryIdOrderByPriceAsc(Category category, Pageable pageDetails);

    //List<Product> findByProductNameLikeIgnoreCase(String productName,Pageable pageDetails);

    Page<Product> findByProductNameLikeIgnoreCase(String productName,Pageable pageDetails);

    Page<Product> findByCategoryIdOrderByPriceAsc(CategoryDTO categoryDTO, Pageable pageDetails);
}