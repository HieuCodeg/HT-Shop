package com.hieucodeg.service.product;

import com.hieucodeg.exception.DataInputException;
import com.hieucodeg.model.Product;
import com.hieucodeg.model.ProductAvatar;
import com.hieucodeg.model.dto.ProductCreateDTO;
import com.hieucodeg.model.enums.FileType;
import com.hieucodeg.repository.ProductAvatarRepository;
import com.hieucodeg.repository.ProductRepository;
import com.hieucodeg.service.upload.UploadService;
import com.hieucodeg.utils.UploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductAvatarRepository productAvatarRepository;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private UploadUtils uploadUtils;


    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findAllByDeletedIsFalse() {
        return productRepository.findAllByDeletedIsFalse();
    }

    @Override
    public Product getById(Long id) {
        return productRepository.getById(id);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product create(ProductCreateDTO productCreateDTO) {

        ProductAvatar productAvatar = new ProductAvatar();
        productAvatar = productAvatarRepository.save(productAvatar);

        ProductAvatar newProductAvatar = uploadAndSaveProductAvatar(productCreateDTO, productAvatar);

        Product product = productRepository.save(productCreateDTO.toProduct(newProductAvatar));

        return product;
    }

    @Override
    public Product update(Product product, ProductCreateDTO productCreateDTO) {

        productAvatarRepository.deleteById(product.getProductAvatar().getId());
        String cloudId = "product_images/" + product.getProductAvatar().getId();

        ProductAvatar productAvatar = new ProductAvatar();
        productAvatar = productAvatarRepository.save(productAvatar);

        ProductAvatar newProductAvatar = deleteAndUploadProductAvatar(productCreateDTO,product,cloudId, productAvatar);

        product.setProductAvatar(newProductAvatar);

        Product newProduct = productRepository.save(product);

        return newProduct;
    }


    private ProductAvatar uploadAndSaveProductAvatar(ProductCreateDTO productCreateDTO, ProductAvatar productAvatar) {
        try {
            Map uploadResult = uploadService.uploadImage(productCreateDTO.getFile(), uploadUtils.buildImageUploadParams(productAvatar));
            String fileUrl = (String) uploadResult.get("secure_url");
            String fileFormat = (String) uploadResult.get("format");

            productAvatar.setFileName(productAvatar.getId() + "." + fileFormat);
            productAvatar.setFileType(FileType.IMAGE.getValue());
            productAvatar.setFileUrl(fileUrl);
            productAvatar.setFileFolder(UploadUtils.PRODUCT_IMAGE_UPLOAD_FOLDER);
            productAvatar.setCloudId(productAvatar.getFileFolder() + "/" + productAvatar.getId());
            productAvatar = productAvatarRepository.save(productAvatar);
            return productAvatar;

        } catch (IOException e) {
            e.printStackTrace();
            throw new DataInputException("Upload hình ảnh thất bại");
        }
    }

    private ProductAvatar deleteAndUploadProductAvatar(ProductCreateDTO productCreateDTO,Product product, String cloudId, ProductAvatar productAvatar) {
        try {
            uploadService.destroyImage(cloudId, uploadUtils.buildImageDestroyParams(product,cloudId));

            Map uploadResult = uploadService.uploadImage(productCreateDTO.getFile(), uploadUtils.buildImageUploadParams(productAvatar));
            String fileUrl = (String) uploadResult.get("secure_url");
            String fileFormat = (String) uploadResult.get("format");

            productAvatar.setFileName(productAvatar.getId() + "." + fileFormat);
            productAvatar.setFileType(FileType.IMAGE.getValue());
            productAvatar.setFileUrl(fileUrl);
            productAvatar.setFileFolder(UploadUtils.PRODUCT_IMAGE_UPLOAD_FOLDER);
            productAvatar.setCloudId(productAvatar.getFileFolder() + "/" + productAvatar.getId());
            productAvatar = productAvatarRepository.save(productAvatar);
            return productAvatar;

        } catch (IOException e) {
            e.printStackTrace();
            throw new DataInputException("Upload hình ảnh thất bại");
        }
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void remove(Long id) {

    }
}
