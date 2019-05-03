package com.example.demo.controllers;


import com.example.demo.models.Product;
import com.example.demo.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    ProductRepository repository;

    @ResponseStatus(HttpStatus.CONFLICT) // 409
    class ProductDoesNotExistException extends RuntimeException{
        public ProductDoesNotExistException(){
            return;
        }
    }

    // Obtener todos los productos - usar get
    @GetMapping("/products")
    public List<Product> getAllNotes() {
        return repository.findAll();
    }

    // Insertar un productos - usar POST
    @PostMapping("/product")
    Product storeProduct(@RequestBody Product newProduct) {
        return repository.save(newProduct);
    }

    // Obtener un producto - usar GET
    @GetMapping("/product/{id}")
    Product getProduct(@PathVariable Long id) {
        return repository.findProductById(id);
        //return repository.findById(id).orElseThrow(() -> new ProductDoesNotExistException());
    }

    // Actualizar un producto - usar PUT
    @PutMapping("/product/{id}")
    Product updateProduct(@RequestBody Product updatedProduct, @PathVariable Long id) {
      return repository.findById(id)
        .map(product -> {
          product.setName(updatedProduct.getName());
          product.setUrl(updatedProduct.getUrl());
          product.setCode(updatedProduct.getCode());
          product.setCategory(updatedProduct.getCategory());
          product.setPrice(updatedProduct.getPrice());
          product.setExpirationDate(updatedProduct.getExpirationDate());
          return repository.save(product);
        }).orElseGet(() -> {
          return repository.save(updatedProduct);
        });
    }

    // Eliminar un producto - usar DELETE
    @DeleteMapping("/product/{id}")
    void deleteProduct(@PathVariable Long id) {
      repository.deleteById(id);
    }
}
