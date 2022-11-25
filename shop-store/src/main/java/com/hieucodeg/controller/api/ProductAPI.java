package com.hieucodeg.controller.api;

import com.hieucodeg.exception.DataInputException;
import com.hieucodeg.exception.EmailExistsException;
import com.hieucodeg.model.Customer;
import com.hieucodeg.model.LocationRegion;
import com.hieucodeg.model.Product;
import com.hieucodeg.model.ProductAvatar;
import com.hieucodeg.model.dto.*;
import com.hieucodeg.service.product.IProductService;
import com.hieucodeg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductAPI {

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

    @PostMapping
    public ResponseEntity<?> create(@Validated @ModelAttribute ProductCreateDTO productCreateDTO, BindingResult bindingResult) {

        new ProductCreateDTO().validate(productCreateDTO, bindingResult);
        if (productCreateDTO.getFile() ==null) {
            throw new DataInputException("Vui lòng chọn ảnh sản phẩm!");
        }

        if (bindingResult.hasErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        productCreateDTO.setId(null);


        Product newProduct = productService.create(productCreateDTO);

        return new ResponseEntity<>(newProduct.toProductDTO(), HttpStatus.CREATED);
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
            throw new DataInputException("ID khách hàng không hợp lệ");
        }

        Product product = productOptional.get();
        product.setDeleted(true);
        productService.save(product);

        return new ResponseEntity<>(productOptional.get().toProductDTO(), HttpStatus.OK);
    }
}
