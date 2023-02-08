package com.hieucodeg.service.product;

import com.hieucodeg.model.Product;
import com.hieucodeg.model.dto.ProductCreateDTO;
import com.hieucodeg.model.dto.ProductDTO;
import com.hieucodeg.service.IGeneralService;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface IProductService extends IGeneralService<Product> {

    Product create(ProductCreateDTO productCreateDTO) throws IOException;
    Product update(Product product, ProductCreateDTO productCreateDTO);
    List<Product> findAllByDeletedIsFalse();
    List<ProductDTO> getAllProductDTO();
    List<ProductDTO> getNewProductDTO(Pageable pageable);
}
