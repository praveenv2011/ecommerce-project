package com.ecom.product.service;

import com.ecom.product.domain.Product;
import com.ecom.product.dto.CartDTO;
import com.ecom.product.dto.CartItemDTO;
import com.ecom.product.dto.CategoryDTO;
import com.ecom.product.dto.ProductDTO;
import com.ecom.product.exception.ApiException;
import com.ecom.product.exception.ResourceNotFoundException;
import com.ecom.product.feignclient.CartClient;
import com.ecom.product.feignclient.CategoryClient;
import com.ecom.product.payload.ProductResponse;
import com.ecom.product.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private CartClient cartClient;


    public ProductDTO save(ProductDTO productDTO, Long categoryId) {
        CategoryDTO categoryDTO = categoryClient.getCategoryById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category", "categoryid", categoryId));

        //checking whether product exist or not
        List<Product> products = productRepository.findAll();
        if(products.stream().anyMatch(product -> product.getProductName().equals(productDTO.getProductName()))){
            throw new ApiException("prodcut with the name already existed");
        }

        //mapping DTO to normal product
        Product product = modelMapper.map(productDTO,Product.class);
        product.setCategoryId(categoryId);
        product.setImage("image.png");

        double specialPrice = product.getPrice() - ((product.getPrice()*product.getDiscount())/100);
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    public ProductResponse getAll(Integer pageNumber,Integer pageSize) {

       Pageable pageDetails = PageRequest.of(pageNumber,pageSize);
       Page<Product> productPage = productRepository.findAll(pageDetails);

       List<Product> productList =  productPage.getContent();

       if(productList == null) throw new ApiException("no products existed yet");

       List<ProductDTO> productDTOList = productList.stream()
                                         .map(product -> modelMapper.map(product,ProductDTO.class))
                                         .toList();
       ProductResponse productResponse = new ProductResponse();
       productResponse.setContent(productDTOList);

        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());

       return productResponse;
    }

    public ProductDTO getById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("product","productid",id));
        ProductDTO productDTO = modelMapper.map(product,ProductDTO.class);
        return productDTO;
    }

    public ProductResponse getByCategoryId(Long categoryId, Integer pageNumber, Integer pageSize){

        CategoryDTO categoryDTO = categoryClient.getCategoryById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category", "categoryid", categoryId));


        Pageable pageDetails = PageRequest.of(pageNumber,pageSize);
        Page<Product> productPage = productRepository.findByCategoryIdOrderByPriceAsc(categoryDTO,pageDetails);


        List<Product> products = productPage.getContent();

        if(products == null) throw new ApiException("no products existed yet");

        List<ProductDTO> productDTOList = products.stream()
                                          .map(product -> modelMapper.map(product,ProductDTO.class))
                                          .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOList);

        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());

        return productResponse;
    }

    public ProductResponse getByKeyword(String productName,Integer pageNumber, Integer pageSize) {


        Pageable pageDetails = PageRequest.of(pageNumber,pageSize);
        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase(productName,pageDetails);


        List<Product> products = productPage.getContent();

        if(products == null) throw new ApiException("no products existed yet");

        List<ProductDTO> productDTOList = products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOList);

        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());


        return productResponse;
    }

    public ProductDTO updateById(Long productId, ProductDTO productDTO) {

        Product productFormDB = productRepository.findById(productId)
                                                 .orElseThrow(()->new ResourceNotFoundException("product","productid",productId));

        Product product = modelMapper.map(productDTO,Product.class);

        productFormDB.setProductId(productId);
        productFormDB.setProductName(product.getProductName());
        productFormDB.setQuantity(product.getQuantity());
        productFormDB.setImage(product.getImage());
        productFormDB.setPrice(product.getPrice());
        productFormDB.setDescription(product.getDescription());
        productFormDB.setDiscount(product.getDiscount());
        productFormDB.setCategoryId(product.getCategoryId());

        double specialPrice = product.getPrice() - ((product.getPrice()*product.getDiscount())/100);
        productFormDB.setSpecialPrice(specialPrice);

        Product updatedProduct = productRepository.save(productFormDB);

        //this product should be updated in all the user carts
        List<CartDTO> cartDTOS  = cartClient.getAllCarts();
        for(CartDTO cartDTO:cartDTOS){
            List<ProductDTO> productDTOS = cartDTO.getProductinforamtion();
            for(ProductDTO productDTO1 : productDTOS){
                if(productDTO1.getProductId().equals(productId)) {
                    productDTO1.setProductName(product.getProductName());
                    productDTO1.setImage(product.getImage());
                    productDTO1.setPrice(product.getPrice());
                    productDTO1.setDescription(product.getDescription());
                    productDTO1.setDiscount(product.getDiscount());
                    productDTO1.setSpecialPrice(specialPrice);
                    productDTO1.setQuantity(productDTO1.getQuantity());
                }
                cartDTO.setProductinforamtion(cartDTO.getProductinforamtion());
            }

        }

        ProductDTO updatedProductDTO = modelMapper.map(updatedProduct,ProductDTO.class);

        return productDTO;
    }

    public ProductDTO deleteById(Long productId) {

        Product productFormDB = productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("product","productid",productId));

        //when product is deleted from the database so that product in the all user carts should be deleted
        List<CartDTO> cartDTOS = cartClient.getAllCarts();
        for(CartDTO cartDTO:cartDTOS){
            cartClient.deleteProductInCarts(cartDTO.getUserId(),productId);
        }

        productRepository.deleteById(productId);

        return modelMapper.map(productFormDB,ProductDTO.class);
    }


}


