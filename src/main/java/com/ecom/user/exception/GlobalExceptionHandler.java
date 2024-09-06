package com.ecom.user.exception;


import com.ecom.user.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> resourceNotFoundException(ResourceNotFoundException e){
    	
        String message = e.getMessage();
        ApiResponse apiResponse = new ApiResponse(message,false);
        
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }
    

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> apiException(ApiException e){
    	
        String message = e.getMessage();
        ApiResponse apiResponse = new ApiResponse(message,false);
        
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
        
    }

}
