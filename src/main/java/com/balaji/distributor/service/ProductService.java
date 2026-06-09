package com.balaji.distributor.service;


import com.balaji.distributor.exceptionHandler.BadRequestException;
import com.balaji.distributor.exceptionHandler.ResourceNotFoundException;
import com.balaji.distributor.repository.ProductRepository;
import com.balaji.distributor.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

   @Autowired
   private ProductRepository productrepo;


   // ADMIN PANAL==============================================

   // Add Product
   @Transactional
   public Product addProduct(Product product) {

      if (
              product.getName() == null
                      ||
                      product.getName().isBlank()
      ) {

         throw new BadRequestException(
                 "Product name is required"
         );
      }

      if (productrepo.existsByNameIgnoreCase(product.getName())) {

         // LATER ADD CUSTOM EXCEPTION
         throw new BadRequestException(
                 "Product already exists"
         );

      }

      if (

                      product.getPrice() < 0
                      ||
                      product.getStock() < 0
                      ||
                      product.getDiscount() < 0
                      ||
                      product.getDiscount() > 100
      ) {

         throw new BadRequestException(
                 "Invalid product values"
         );
      }

      return productrepo.save(product);
   }


   // GET ALL
   public List<Product> getAllProducts() {
      return productrepo.findAll();
   }


   @Transactional
   public Product update(Long id, Product newProduct) {
      Product existing = productrepo.findById(id)
              .orElseThrow(() ->
                      new ResourceNotFoundException(
                              "Product not found"
                      ));


      //  validate names
      if (
              newProduct.getName() == null
                      ||
                      newProduct.getName().isBlank()
      ) {

         throw new BadRequestException(
                 "Product name is required"
         );
      }

      // check duplicate name
      if (
              productrepo.existsByNameIgnoreCase(
                      newProduct.getName()
              )
                      &&
                      !existing.getName().equalsIgnoreCase(
                              newProduct.getName()
                      )
      ) {

         throw new BadRequestException(
                 "Product already exists"
         );
      }



      //  negative values check
      if (
              newProduct.getPrice() < 0
                      ||
                      newProduct.getStock() < 0
                      ||
                      newProduct.getDiscount() < 0
                      ||
                      newProduct.getDiscount() > 100
      ) {

         throw new BadRequestException(
                 "Invalid product values"
         );
      }

      existing.setName(newProduct.getName());
      existing.setCategory(newProduct.getCategory());
      existing.setPrice(newProduct.getPrice());
      existing.setDiscount(newProduct.getDiscount());
      existing.setStock(newProduct.getStock());
      existing.setUnit(newProduct.getUnit());
      existing.setImageUrl(newProduct.getImageUrl());
      existing.setDescription(newProduct.getDescription());

      return productrepo.save(existing);
   }



   @Transactional
   public void delete(Long id) {
      Product existing = productrepo.findById(id)
              .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

      productrepo.delete(existing);
   }

}
