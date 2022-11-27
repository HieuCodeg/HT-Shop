package com.hieucodeg.model.dto;

import com.hieucodeg.model.Product;
import com.hieucodeg.model.ProductAvatar;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductCreateDTO implements Validator{

    private Long id;
    @NotEmpty(message = "Tên sản phẩm không được trống")
    private String title;
    private String price;
    private String quantity;
    private String description;

    private String oldPrice;

    private String discount;

    private Boolean newCheck;

    @NotNull(message = "Thể loại là bắt buộc")
    @Pattern(regexp = "^\\d+$", message = "ID thể loại phải là số")
    private String categoryId;

    private MultipartFile file;


    public Product toProduct(ProductAvatar productAvatar) {
        return new Product()
                .setId(id)
                .setTitle(title)
                .setPrice(BigDecimal.valueOf(Long.valueOf(price)))
                .setQuantity(Long.valueOf(quantity))
                .setDescription(description)
                .setProductAvatar(productAvatar);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ProductCreateDTO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductCreateDTO productCreateDTO = (ProductCreateDTO) target;

        String price = productCreateDTO.getPrice();

        if (price.isEmpty()) {
            errors.rejectValue("price", "","Giá sản phẩm không đuợc trống");
        } else if (!price.matches("(^\\d+$)")){
            errors.rejectValue("price", "","Giá sản phẩm phải là số");
        } else if (Long.valueOf(price) < 500) {
            errors.rejectValue("price", "","Giá sản phẩm thấp nhất là 500");
        } else if (Long.valueOf(price) > 999999999999l) {
            errors.rejectValue("price", "","Giá sản phẩm quá lớn");
        }

        String oldPrice = productCreateDTO.getPrice();

        if (oldPrice != null) {
            if (!price.matches("(^\\d+$)")){
                errors.rejectValue("price", "","Giá sản phẩm phải là số");
            } else if (Long.valueOf(price) < 500) {
                errors.rejectValue("price", "","Giá sản phẩm thấp nhất là 500");
            } else if (Long.valueOf(price) > 999999999999l) {
                errors.rejectValue("price", "","Giá sản phẩm quá lớn");
            }
        }

        String quantity = productCreateDTO.getQuantity();

        if (quantity.isEmpty()) {
            errors.rejectValue("quantity", "","Số lượng sản phẩm không đuợc trống");
        } else if (!quantity.matches("(^\\d+$)")){
            errors.rejectValue("quantity", "","Số lượng sản phẩm phải là số");
        } else if (Long.valueOf(quantity) < 0) {
            errors.rejectValue("quantity", "","Số lượng sản phẩm không được âm");
        } else if (Long.valueOf(quantity) > 999999999999l) {
            errors.rejectValue("quantity", "","Số lượng sản phẩm quá lớn");
        }

        String discount = productCreateDTO.getDiscount();
        if (discount.isEmpty()) {
            discount = null;
        }

        if (discount != null) {
            if (!discount.matches("(^\\d+$)")){
                errors.rejectValue("discount", "","Phần trăm giảm giá phải là số");
            } else if (Long.valueOf(discount) < 0) {
                errors.rejectValue("discount", "","Phần trăm không hợp lệ 0-100%");
            } else if (Long.valueOf(discount) > 100) {
                errors.rejectValue("discount", "","Phần trăm không hợp lệ 0-100%");
            }
        }

    }

}
