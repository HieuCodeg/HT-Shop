package com.hieucodeg.repository;

import com.hieucodeg.model.Staff;
import com.hieucodeg.model.dto.StaffDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    @Query("SELECT NEW com.hieucodeg.model.dto.StaffDTO(" +
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
            "FROM Staff AS c " +
            "LEFT JOIN StaffAvatar AS a " +
            "ON a.staff = c " +
            "WHERE c.deleted = false"
    )
    List<StaffDTO> getAllStaffDTO();

    @Query("SELECT NEW com.hieucodeg.model.dto.StaffDTO(" +
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
            "FROM Staff AS c " +
            "LEFT JOIN StaffAvatar AS a " +
            "ON a.staff = c " +
            "WHERE c.deleted = false and c.id = :id"
    )
    StaffDTO getStaffDTOById(Long id);

    List<Staff> findAllByIdNot(Long senderId);
    Optional<Staff> findByEmail(String email);
    Optional<Staff> findByEmailAndIdIsNot(String email, Long id);
    List<Staff> findAllByDeletedIsFalse();

}
