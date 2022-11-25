package com.hieucodeg.model;

import com.hieucodeg.model.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToOne
    @JoinColumn(name = "avatar_id", referencedColumnName = "id")
    private ProductAvatar productAvatar;

    @Column(precision = 12, scale = 0, nullable = false)
    private BigDecimal price;

    private Long quantity;

    private String description;


    public ProductDTO toProductDTO() {
        return new ProductDTO()
                .setId(id)
                .setTitle(title)
                .setPrice(price)
                .setQuantity(quantity)
                .setDescription(description)
                .setAvatar(productAvatar.toProductAvatarDTO());
    }


}
