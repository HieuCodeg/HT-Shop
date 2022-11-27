package com.hieucodeg.controller.api;

import com.hieucodeg.exception.DataInputException;
import com.hieucodeg.model.Product;
import com.hieucodeg.model.dto.ProductDTO;
import com.hieucodeg.service.product.IProductService;
import com.hieucodeg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/web/api/products")
public class ProductsWebAPI {

    @Autowired
    private IProductService productService;

    @Autowired
    private AppUtils appUtils;


    @GetMapping
    public ResponseEntity<?> getAll() {

        List<Product> products = productService.findAllByDeletedIsFalse();

        List<ProductDTO> productDTOS = new ArrayList<>();

        for (Product item : products) {
            ProductDTO productDTO = item.toProductDTO();
            productDTOS.add(productDTO);
        }

        return new ResponseEntity<>(productDTOS, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable Long productId) {

        Optional<Product> productOptional = productService.findById(productId);

        if (!productOptional.isPresent()) {
            throw new DataInputException("ID sản phẩm không hợp lệ");
        }
        return new ResponseEntity<>(productOptional.get().toProductDTO(), HttpStatus.OK);
    }

}
