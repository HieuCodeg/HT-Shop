package com.hieucodeg.controller.api;

import com.hieucodeg.exception.DataInputException;
import com.hieucodeg.model.Category;
import com.hieucodeg.model.Product;
import com.hieucodeg.model.dto.CategoryDTO;
import com.hieucodeg.model.dto.ProductDTO;
import com.hieucodeg.service.category.ICategoryService;
import com.hieucodeg.service.product.IProductService;
import com.hieucodeg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

    @Autowired
    private ICategoryService categoryService;


    @GetMapping
    public ResponseEntity<?> getAll() {

        List<ProductDTO> productDTOS = productService.getAllProductDTO();

        return new ResponseEntity<>(productDTOS, HttpStatus.OK);
    }

    @GetMapping("/top5")
    public ResponseEntity<?> getTop5() {

        List<ProductDTO> productDTOS = productService.getNewProductDTO(PageRequest.of(0,5));

        return new ResponseEntity<>(productDTOS, HttpStatus.OK);
    }


    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {

        List<Category> categories = categoryService.findAll();

        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        for (Category item : categories) {
            CategoryDTO categoryDTO = item.toCategoryDTO();
            categoryDTOS.add(categoryDTO);
        }

        return new ResponseEntity<>(categoryDTOS, HttpStatus.OK);
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
