package com.ecom.product.controller;

import com.ecom.product.dto.ProductDTO;
import com.ecom.product.payload.ProductResponse;
import com.ecom.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/categories/{categoryId}/products")
    public ResponseEntity<ProductDTO> saveProduct(@RequestBody ProductDTO productDTO,
                                                  @PathVariable("categoryId") Long categoryId) {
        ProductDTO savedProductDTO = productService.save(productDTO, categoryId);
        return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
    }

    @GetMapping("/products")
    public ResponseEntity<ProductResponse> getAllProducts(
                                                @RequestParam(name = "pageNumber", defaultValue = "0",required = false) Integer pageNumber,
                                                @RequestParam(name = "pageSize", defaultValue = "50", required = false) Integer pageSize){

        ProductResponse productResponse = productService.getAll(pageNumber,pageSize);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable(name="id") Long id){

        ProductDTO productDTO = productService.getById(id);
        return new ResponseEntity<>(productDTO,HttpStatus.OK);
    }

    @GetMapping("/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategoryId(@PathVariable(name = "categoryId") Long categoryId,
                                                                   @RequestParam(name = "pageNumber", defaultValue = "0",required = false) Integer pageNumber,
                                                                   @RequestParam(name = "pageSize", defaultValue = "50", required = false) Integer pageSize){

        ProductResponse productResponse = productService.getByCategoryId(categoryId,pageNumber,pageSize);
        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/products/keyword")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@RequestParam(name = "name") String productName,
                                                                @RequestParam(name = "pageNumber", defaultValue = "0",required = false) Integer pageNumber,
                                                                @RequestParam(name = "pageSize", defaultValue = "50", required = false) Integer pageSize){

        ProductResponse productResponse = productService.getByKeyword(productName,pageNumber,pageSize);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductDTO> updateProductByProductId(@RequestBody ProductDTO productDTO,
                                                               @PathVariable(name = "productId") Long productId) {

        ProductDTO updatedProductDTO = productService.updateById(productId, productDTO);

        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }

    @DeleteMapping("/products/{productid}")
    public ResponseEntity<ProductDTO> deleteProductById(@PathVariable(name = "productid") Long productId) {

        ProductDTO deletedProductDTO = productService.deleteById(productId);

        return new ResponseEntity<>(deletedProductDTO, HttpStatus.OK);
    }
}
