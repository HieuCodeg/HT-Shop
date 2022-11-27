package com.hieucodeg.model;

import com.hieucodeg.model.dto.CategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(targetEntity = User.class, fetch = FetchType.EAGER)
    private List<Product> products;

    public CategoryDTO toCategoryDTO() {
        return new CategoryDTO()
                .setId(String.valueOf(id))
                .setName(name);
    }
}
