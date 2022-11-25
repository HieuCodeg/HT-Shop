package com.hieucodeg.service.product;

import com.hieucodeg.model.Product;
import com.hieucodeg.model.dto.ProductCreateDTO;
import com.hieucodeg.service.IGeneralService;

import java.util.List;

public interface IProductService extends IGeneralService<Product> {

    Product create(ProductCreateDTO productCreateDTO);
    Product update(Product product, ProductCreateDTO productCreateDTO);
    List<Product> findAllByDeletedIsFalse();
}
