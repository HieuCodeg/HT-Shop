package com.hieucodeg.repository;

import com.hieucodeg.model.Customer;
import com.hieucodeg.model.dto.CustomerAvartasDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT NEW com.hieucodeg.model.dto.CustomerAvartasDTO(" +
                "c.id, " +
                "c.fullName, " +
                "c.email, " +
                "c.phone, " +
                "c.locationRegion, " +
                "a.id, " +
                "a.fileName, " +
                "a.fileFolder, " +
                "a.fileUrl, " +
                "a.cloudId, " +
                "a.fileType " +
                ") " +
            "FROM Customer AS c " +
            "LEFT JOIN Avatar AS a " +
            "ON a.customer = c " +
            "WHERE c.deleted = false"
    )
    List<CustomerAvartasDTO> getAllCustomersAvartaDTO();

    @Query("SELECT NEW com.hieucodeg.model.dto.CustomerAvartasDTO(" +
            "c.id, " +
            "c.fullName, " +
            "c.email, " +
            "c.phone, " +
            "c.locationRegion, " +
            "a.id, " +
            "a.fileName, " +
            "a.fileFolder, " +
            "a.fileUrl, " +
            "a.cloudId, " +
            "a.fileType " +
            ") " +
            "FROM Customer AS c " +
            "LEFT JOIN Avatar AS a " +
            "ON a.customer = c " +
            "WHERE c.deleted = false and c.id = :id"
    )
    CustomerAvartasDTO getCustomersAvartaDTOById(Long id);

    List<Customer> findAllByIdNot(Long senderId);
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByEmailAndIdIsNot(String email, Long id);
    List<Customer> findAllByDeletedIsFalse();

}
