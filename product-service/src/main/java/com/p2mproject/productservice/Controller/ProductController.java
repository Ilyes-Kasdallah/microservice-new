package com.p2mproject.productservice.Controller;
import com.p2mproject.productservice.dto.ProductRequest;
import com.p2mproject.productservice.dto.ProductResponse;
import com.p2mproject.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController /// declare a class as a restful controller // to indicate that the class will handle HTTP request and return the responses
@RequestMapping("/api/product") // any http request in this path with be handled by the methods of this controller
@RequiredArgsConstructor /// inject dependencies into the controller
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)  // indicate that a new resource has been successfully created as a result of the post request
    public void createProduct(@RequestBody ProductRequest productRequest){
         productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
         return productService.getAllProducts();
    }
}
