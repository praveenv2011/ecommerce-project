package com.ecom.cart.service;

import com.ecom.cart.domain.Cart;
import com.ecom.cart.domain.CartItem;
import com.ecom.cart.dto.CartDTO;
import com.ecom.cart.dto.ProductDTO;
import com.ecom.cart.dto.UserDTO;
import com.ecom.cart.exception.ApiException;
import com.ecom.cart.exception.ResourceNotFoundException;
import com.ecom.cart.feignclient.ProductClient;
import com.ecom.cart.feignclient.UserClient;
import com.ecom.cart.repository.CartItemRepository;
import com.ecom.cart.repository.CartRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
public class CartService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired(required = true)
    private ProductClient productClient;

    @Autowired
    private UserClient userClient;

    private Cart createCart(Long userId){
        Cart userCart = cartRepository.findByUserId(userId);
        if(userCart != null){
            return userCart;
        }
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setTotalPrice(0.0);
        Cart newCart = cartRepository.save(cart);
        return newCart;
    }

    @Transactional
    public CartDTO saveProduct(Long userId, Long productId, Integer quantity) {
        //checking whether user exists ot not
        UserDTO userDTO = userClient.getUserByid(userId);
        if(userDTO == null){
            throw new ResourceNotFoundException("user","userid",userId);
        }

        //check cart exists
        Cart cart = createCart(userId);

        //updating the cartid in the user domain model class because cartid is the part of user class also
        userDTO.setUsername(userDTO.getUsername());
        userDTO.setEmail(userDTO.getEmail());
        userDTO.setPassword(userDTO.getPassword());
        userDTO.setCartId(userId);
        userClient.updateUserByid(userDTO,userId);


        //retreive the product
        ProductDTO product = productClient.getProductByid(productId)
                                            .orElseThrow(()->new ResourceNotFoundException("product","productid",productId));


        //perform validation on quantity

        if(quantity>product.getQuantity()){
            throw new ApiException(quantity + " many proucts of this type are not available please make order less than or equal to"+" "+product.getQuantity());
        }
        if (product.getQuantity() == null) {
            throw new ApiException("Product is not available.");
        }
        if (product.getQuantity() == 0) {
            throw new ApiException("Product is not available.");
        }

        //checking if cartitem(product) exists in the cart or not

        CartItem cartItem = cartItemRepository.findByProductIdAndCart(productId,cart);
        if(cartItem != null){
            throw new ApiException("Product"+" "+ product.getProductName()+" "+"already exists if you want buy more increase the quanity");
        }

        //if not create cart item
        CartItem newCartItem = new CartItem();

        newCartItem.setQuantity(quantity);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setSpecialPrice(product.getSpecialPrice());
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductId(product.getProductId());
        cartItemRepository.save(newCartItem);

        //add it to cart
        // //cart.addCartItems(newCartItem);
        cart.setTotalPrice(cart.getTotalPrice()+ (product.getSpecialPrice()*quantity));
        cartRepository.save(cart);

        //mapping cart to cartDTO
        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);

    //{   this is extra for populating the product information in cartDTO
        //populating extra properties of cartDTO that is product inforamtion
        product.setQuantity(quantity);//changing the product quantity in the cart from orginal quantity of the product
        cartDTO.addProducts(product);
    //}

        return cartDTO;
    }


    public CartDTO getById(Long userId) {
        //checking whether user exists ot not
        UserDTO user = userClient.getUserByid(userId);
        if(user == null){
            throw new ResourceNotFoundException("user","userid",userId);
        }

        Cart cart = cartRepository.findByUserId(userId);
        if(cart == null){
            throw new ApiException("cart in this user account with id"+" "+userId+" "+" is empty");
        }
        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);

    //{
       //adding data to product information that is in cartDTO, since we need productDTO's to fill it but we don't have product id we are getting it by cartitemid's property in the cart
        List<CartItem> cartItems = cart.getCartItems();

        //getting the products to populate productInformation in cartDTO and also changing product quantity into quantiy of the product in the "cartitem" from original overall stock
        Stream<ProductDTO> productDTOS = cartItems.stream().map(cartItem-> {
                ProductDTO productDTO = productClient.getProductByid(cartItem.getProductId()).orElse(null);
                productDTO.setQuantity(cartItem.getQuantity());
                return productDTO;
                });

        cartDTO.setProductinforamtion(productDTOS.toList());
       //}
         return cartDTO;
    }

    @Transactional
    public CartDTO updateCartProductQuantity(Long userid, Long productid, int quantity) {
        //checking whether user exists ot not
        UserDTO user = userClient.getUserByid(userid);
        if (user == null) {
            throw new ResourceNotFoundException("user", "userid", userid);
        }

        //checking whether cart is there or not
        Cart cart = cartRepository.findByUserId(userid);
        if (cart == null) {
            throw new ResourceNotFoundException("cart", "cartid", userid);
        }

        ProductDTO product = productClient.getProductByid(productid)
                .orElseThrow(()->new ResourceNotFoundException("product","productid",productid));


        if (product.getQuantity() == null) {
            throw new ApiException("Product is not available.");
        }
        if (product.getQuantity() == 0) {
            throw new ApiException("Product is not available.");
        }

        //finding the cartitem
        CartItem cartItem = cartItemRepository.findByProductIdAndCart(productid,cart);
        if(cartItem == null){
            throw new ApiException("product"+" "+ product.getProductName()+" "+"is not available in the cart");
        }

        cartItem.setQuantity(cartItem.getQuantity() + quantity);

        //after increaing the quantiy we should check the total quantiy of a product in cart is >= real product quality
        if(cartItem.getQuantity()> product.getQuantity()){
            throw new ApiException(product.getProductName()+ " "+ "is not available any more");
        }

        cartItem.setProductId(product.getProductId());
        cartItem.setDiscount(product.getDiscount());
        cartItem.setSpecialPrice(product.getSpecialPrice());

        //changing quantiy of the product to all quantity in the cart
        cartItem.setQuantity(cartItem.getQuantity());


        //setting total price in cart and saving
        cart.setTotalPrice(cart.getTotalPrice()+(product.getSpecialPrice()*quantity));
        Cart updatedCart = cartRepository.save(cart);

        //saving the updated cartitem to cartitemrepo
        CartItem updatedCartItem = cartItemRepository.save(cartItem);

        if(updatedCartItem.getQuantity() == 0){
            cartItemRepository.delete(updatedCartItem);
        }


        CartDTO cartDTO = modelMapper.map(updatedCart,CartDTO.class);

        //{
        //adding data to product information that is in cartDTO, since we need product's to fill it but we don't have product id we are getting it by cartitemid's property in the cart
        List<CartItem> cartItems = updatedCart.getCartItems();

        //getting the products to populate productinformation in cartDTO and also changing product quantity into quantiy of the product in the "cartitem" from original overall stock
        Stream<ProductDTO> productDTOS = cartItems.stream().map(cartitem-> {
            ProductDTO product1 = productClient.getProductByid(cartitem.getProductId()).orElse(null);
            product1.setQuantity(updatedCartItem.getQuantity());
            return product1;
        });

        cartDTO.setProductinforamtion(productDTOS.toList());
        //}


        return cartDTO;
    }

    @Transactional
    public String deleteProduct(Long userid, Long productid) {
        //checking whether user exists ot not
        UserDTO user = userClient.getUserByid(userid);
        if (user == null) {
            throw new ResourceNotFoundException("user", "userid", userid);
        }

        //checking whether cart is there or not
        Cart cart = cartRepository.findByUserId(userid);
        if (cart == null) {
            throw new ResourceNotFoundException("cart", "cartid", userid);
        }

        CartItem cartItem = cartItemRepository.findByProductIdAndCart(productid,cart);
        if(cartItem == null){
            throw new ApiException("product does not exist in the cart");
        }

        cart.setTotalPrice(cart.getTotalPrice()- (cartItem.getSpecialPrice()*cartItem.getQuantity()));

        cartItemRepository.deleteByProductIdAndCart(productid,cart);

        return "Product is removed from the cart";
    }

    public List<CartDTO> getAll() {

        List<Cart> carts = cartRepository.findAll();
        if(carts == null){
            throw new ApiException("No carts Existed");
        }

        List<CartDTO> cartDTOS  = carts.stream().map(cart->modelMapper.map(cart,CartDTO.class)).toList();



        for(Cart cart:carts){
         for(CartDTO cartDTO:cartDTOS) {
             List<CartItem> cartItems = cart.getCartItems();
             Stream<ProductDTO> productDTOS = cartItems.stream().map(cartItem-> {
                 ProductDTO productDTO = productClient.getProductByid(cartItem.getProductId()).orElse(null);
                 productDTO.setQuantity(cartItem.getQuantity());
                 return productDTO;
             });

             cartDTO.setProductinforamtion(productDTOS.toList());
         }
        }

        return cartDTOS;
    }

}
