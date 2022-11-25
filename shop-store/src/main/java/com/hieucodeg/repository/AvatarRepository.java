package com.hieucodeg.repository;

import com.hieucodeg.model.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, String> {

    Avatar getAvatarByCustomer_Id(Long id);
}
