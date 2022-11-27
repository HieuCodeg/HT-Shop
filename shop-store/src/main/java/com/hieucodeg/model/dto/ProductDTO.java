package com.hieucodeg.model.dto;

import com.hieucodeg.model.Category;
import com.hieucodeg.model.ProductAvatar;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDTO {

    private Long id;

    private String title;
    private BigDecimal price;
    private Long quantity;
    private String description;

    private BigDecimal oldPrice;
    private Long discount;
    private Boolean newCheck;
    private Category category;
    private ProductAvatar avatar;


}
