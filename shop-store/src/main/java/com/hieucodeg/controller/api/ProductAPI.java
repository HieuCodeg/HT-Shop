package com.hieucodeg.controller.api;

import com.hieucodeg.exception.DataInputException;
import com.hieucodeg.model.Category;
import com.hieucodeg.model.Product;
import com.hieucodeg.model.ProductAvatar;
import com.hieucodeg.model.Role;
import com.hieucodeg.model.dto.CategoryDTO;
import com.hieucodeg.model.dto.ProductCreateDTO;
import com.hieucodeg.model.dto.ProductDTO;
import com.hieucodeg.service.category.ICategoryService;
import com.hieucodeg.service.product.IProductService;
import com.hieucodeg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/api/products")
public class ProductAPI {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private AppUtils appUtils;


    @GetMapping
    public ResponseEntity<?> getAll() {

        List<ProductDTO> productDTOS = productService.getAllProductDTO();

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

    @PostMapping
    public ResponseEntity<?> create(@Validated @ModelAttribute ProductCreateDTO productCreateDTO, BindingResult bindingResult) throws IOException {

        new ProductCreateDTO().validate(productCreateDTO, bindingResult);
        if (productCreateDTO.getFile() ==null) {
            throw new DataInputException("Vui lòng chọn ảnh sản phẩm!");
        }

        if (bindingResult.hasErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Long idCategory= Long.valueOf(productCreateDTO.getCategoryId());

        Optional<Category> optionalCategory = categoryService.findById(idCategory);

        if (!optionalCategory.isPresent()) {
            throw new DataInputException("Thể loại không hợp lệ");
        }

        productCreateDTO.setId(null);
        if (productCreateDTO.getDiscount().isEmpty()) {
            productCreateDTO.setDiscount(null);
        }

        try {
            Product newProduct = productService.create(productCreateDTO);

            return new ResponseEntity<>(newProduct.toProductDTO(), HttpStatus.CREATED);

        } catch (DataIntegrityViolationException e) {
            throw new DataInputException("Thông tin sản phẩm không đúng, vui lòng kiểm tra lại");
        }

    }

    @PatchMapping("/update/{productId}")
    public ResponseEntity<?> update(@PathVariable Long productId,@Validated @ModelAttribute ProductCreateDTO productCreateDTO, BindingResult bindingResult) {

        new ProductCreateDTO().validate(productCreateDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Optional<Product> productOptional = productService.findById(productId);

        if (!productOptional.isPresent()) {
            throw new DataInputException("ID sản phẩm không tồn tại");
        }
        Product product = productOptional.get();

        product.setTitle(productCreateDTO.getTitle());
        product.setPrice(BigDecimal.valueOf(Long.valueOf(productCreateDTO.getPrice())));
        product.setQuantity(Long.valueOf(productCreateDTO.getQuantity()));
        product.setDescription(productCreateDTO.getDescription());


        if (productCreateDTO.getFile() == null) {
            Product updatedProduct = productService.save(product);

            return new ResponseEntity<>(product.toProductDTO(), HttpStatus.OK);
        } else {

            Product newProduct = productService.update(product,productCreateDTO);
            return new ResponseEntity<>(newProduct.toProductDTO(), HttpStatus.OK);
        }

    }

    @PatchMapping("/delete/{productId}")
    public ResponseEntity<?> delete(@PathVariable Long productId) {

        Optional<Product> productOptional = productService.findById(productId);

        if (!productOptional.isPresent()) {
            throw new DataInputException("ID sản phẩm không hợp lệ");
        }

        Product product = productOptional.get();
        product.setDeleted(true);
        productService.save(product);

        return new ResponseEntity<>(productOptional.get().toProductDTO(), HttpStatus.OK);
    }
}
