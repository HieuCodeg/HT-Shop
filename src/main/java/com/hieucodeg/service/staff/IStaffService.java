package com.hieucodeg.service.staff;

import com.hieucodeg.model.Staff;
import com.hieucodeg.model.dto.StaffAvatarDTO;
import com.hieucodeg.model.dto.StaffDTO;
import com.hieucodeg.service.IGeneralService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IStaffService extends IGeneralService<Staff> {
    Optional<Staff> findByEmail(String email);
    Optional<Staff> findByEmailAndIdIsNot(String email, Long id);
    List<Staff> findAllByDeletedIsFalse();

    List<StaffDTO> getAllStaffDTO();
    StaffDTO getStaffDTOById(Long id);

    StaffDTO saveWithAvatar(Staff staff, MultipartFile avatarFile);
    StaffDTO updateWithAvatar(Staff staff, MultipartFile avatarFile, StaffAvatarDTO staffAvatarDTO);

}
