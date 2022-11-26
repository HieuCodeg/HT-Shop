package com.hieucodeg.repository;

import com.hieucodeg.model.StaffAvatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffAvatarRepository extends JpaRepository<StaffAvatar, String> {

    StaffAvatar getStaffAvatarByStaff_Id(Long id);
}
