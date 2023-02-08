package com.hieucodeg.repository;

import com.hieucodeg.model.Product;
import com.hieucodeg.model.dto.CustomerAvartasDTO;
import com.hieucodeg.model.dto.ProductDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByDeletedIsFalse();
    @Query("SELECT NEW com.hieucodeg.model.dto.ProductDTO(" +
            "c.id, " +
            "c.title, " +
            "c.price, " +
            "c.quantity, " +
            "c.description, " +
            "c.oldPrice, " +
            "c.discount, " +
            "c.newCheck, " +
            "c.category, " +
            "c.productAvatar " +
            ") " +
            "FROM Product AS c " +
            "WHERE c.deleted = false"
    )
    List<ProductDTO> getAllProductDTO();


    @Query("SELECT NEW com.hieucodeg.model.dto.ProductDTO(" +
            "c.id, " +
            "c.title, " +
            "c.price, " +
            "c.quantity, " +
            "c.description, " +
            "c.oldPrice, " +
            "c.discount, " +
            "c.newCheck, " +
            "c.category, " +
            "c.productAvatar " +
            ") " +
            "FROM Product AS c " +
            "WHERE c.deleted = false " +
            "ORDER BY c.createdAt desc "
    )
    List<ProductDTO> getNewProductDTO(Pageable pageable);
}
