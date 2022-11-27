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
    @Column(precision = 12, scale = 0, nullable = false)
    private BigDecimal price;
    private Long quantity;

    private String description;
    @Column(precision = 12, scale = 0, nullable = true)
    private BigDecimal oldPrice;

    @Column( nullable = true)
    private Long discount;

    @Column( nullable = true)
    private Boolean newCheck;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category;
    @OneToOne
    @JoinColumn(name = "avatar_id", referencedColumnName = "id")
    private ProductAvatar productAvatar;


    public ProductDTO toProductDTO() {
        return new ProductDTO()
                .setId(id)
                .setTitle(title)
                .setPrice(price)
                .setQuantity(quantity)
                .setDescription(description)
                .setOldPrice(oldPrice)
                .setDiscount(discount)
                .setNewCheck(newCheck)
                .setCategory(category)
                .setAvatar(productAvatar);
    }


}
