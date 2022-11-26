package com.hieucodeg.service.staff;

import com.hieucodeg.exception.DataInputException;
import com.hieucodeg.model.LocationRegion;
import com.hieucodeg.model.Staff;
import com.hieucodeg.model.StaffAvatar;
import com.hieucodeg.model.dto.StaffAvatarDTO;
import com.hieucodeg.model.dto.StaffDTO;
import com.hieucodeg.model.enums.FileType;
import com.hieucodeg.repository.LocationRegionRepositiry;
import com.hieucodeg.repository.StaffAvatarRepository;
import com.hieucodeg.repository.StaffRepository;
import com.hieucodeg.service.upload.UploadService;
import com.hieucodeg.utils.UploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
@Transactional
public class StaffServiceImpl implements IStaffService{
    @Autowired
    private StaffAvatarRepository staffAvatarRepository;

    @Autowired
    private LocationRegionRepositiry locationRegionRepositiry;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private UploadUtils uploadUtils;

    @Override
    public List<Staff> findAll() {
        return staffRepository.findAll();
    }

    @Override
    public Staff getById(Long id) {
        return null;
    }

    @Override
    public Optional<Staff> findById(Long id) {
        return staffRepository.findById(id);
    }

    @Override
    public Staff save(Staff staff) {
        locationRegionRepositiry.save(staff.getLocationRegion());
        return staffRepository.save(staff);
    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public Optional<Staff> findByEmail(String email) {
        return staffRepository.findByEmail(email);
    }

    @Override
    public Optional<Staff> findByEmailAndIdIsNot(String email, Long id) {
        return staffRepository.findByEmailAndIdIsNot(email,id);
    }

    @Override
    public List<Staff> findAllByDeletedIsFalse() {
        return staffRepository.findAllByDeletedIsFalse();
    }

    @Override
    public List<StaffDTO> getAllStaffDTO() {
        return staffRepository.getAllStaffDTO();
    }

    @Override
    public StaffDTO saveWithAvatar(Staff staff, MultipartFile avatarFile) {

        LocationRegion locationRegion = staff.getLocationRegion();
        locationRegion.setId(0L);
        LocationRegion newLocationRegion = locationRegionRepositiry.save(locationRegion);

        staff.setLocationRegion(newLocationRegion);

        Staff newStaff = staffRepository.save(staff);

        StaffAvatar avatar = new StaffAvatar();

        String fileType = avatarFile.getContentType();

        assert fileType != null;

        fileType = fileType.substring(0, 5);

        avatar.setFileType(fileType);
        avatar.setStaff(newStaff);

        StaffAvatar newAvatar = staffAvatarRepository.save(avatar);

        if (fileType.equals(FileType.IMAGE.getValue())) {
            uploadAndSaveProductImage(avatarFile, newStaff, newAvatar);
        }

        StaffDTO staffDTO = newStaff.toStaffDTO();
        staffDTO.setAvatarDTO(newAvatar.toStaffAvatarDTO());

        return staffDTO;
    }

    @Override
    public StaffDTO updateWithAvatar(Staff staff, MultipartFile avatarFile, StaffAvatarDTO avatarDTO) {

        LocationRegion locationRegion = staff.getLocationRegion();

        LocationRegion newLocationRegion = locationRegionRepositiry.save(locationRegion);

        staff.setLocationRegion(newLocationRegion);

        Staff newStaff = staffRepository.save(staff);

        StaffAvatar avatar = new StaffAvatar();

        String fileType = avatarFile.getContentType();

        assert fileType != null;

        fileType = fileType.substring(0, 5);

        avatar.setFileType(fileType);
        avatar.setStaff(newStaff);
        StaffAvatar newAvatar = staffAvatarRepository.save(avatar);


        if (avatarDTO.getId() == null) {
            if (fileType.equals(FileType.IMAGE.getValue())) {
                uploadAndSaveProductImage(avatarFile,newStaff,newAvatar);
            }
        } else {
            String cloudId = "avatar_images/" + avatarDTO.getId();
            staffAvatarRepository.deleteById(avatarDTO.getId());

            if (fileType.equals(FileType.IMAGE.getValue())) {
                deleteAndUploadImage(avatarFile, newStaff,cloudId, newAvatar);
            }
        }


        StaffDTO staffDTO = newStaff.toStaffDTO();
        staffDTO.setAvatarDTO(newAvatar.toStaffAvatarDTO());

        return staffDTO;
    }

    private void uploadAndSaveProductImage(MultipartFile avatarFile, Staff staff, StaffAvatar staffAvatar) {
        try {
            Map uploadResult = uploadService.uploadImage(avatarFile, uploadUtils.buildImageUploadParams(staffAvatar));
            String fileUrl = (String) uploadResult.get("secure_url");
            String fileFormat = (String) uploadResult.get("format");

            staffAvatar.setFileName(staffAvatar.getId() + "." + fileFormat);
            staffAvatar.setFileUrl(fileUrl);
            staffAvatar.setFileFolder(UploadUtils.IMAGE_UPLOAD_FOLDER);
            staffAvatar.setCloudId(staffAvatar.getFileFolder() + "/" + staffAvatar.getId());
            staffAvatar.setStaff(staff);
            staffAvatarRepository.save(staffAvatar);

        } catch (IOException e) {
            e.printStackTrace();
            throw new DataInputException("Upload hình ảnh thất bại");
        }
    }

    private void deleteAndUploadImage(MultipartFile avatarFile,Staff staff, String cloudId, StaffAvatar staffAvatar) {
        try {
            uploadService.destroyImage(cloudId, uploadUtils.buildImageDestroyParams(staff,cloudId));

            Map uploadResult = uploadService.uploadImage(avatarFile, uploadUtils.buildImageUploadParams(staffAvatar));
            String fileUrl = (String) uploadResult.get("secure_url");
            String fileFormat = (String) uploadResult.get("format");

            staffAvatar.setFileName(staffAvatar.getId() + "." + fileFormat);
            staffAvatar.setFileUrl(fileUrl);
            staffAvatar.setFileFolder(UploadUtils.IMAGE_UPLOAD_FOLDER);
            staffAvatar.setCloudId(staffAvatar.getFileFolder() + "/" + staffAvatar.getId());
            staffAvatar.setStaff(staff);
            staffAvatarRepository.save(staffAvatar);

        } catch (IOException e) {
            e.printStackTrace();
            throw new DataInputException("Upload hình ảnh thất bại");
        }
    }

    @Override
    public StaffDTO getStaffDTOById(Long id) {
        return staffRepository.getStaffDTOById(id);
    }
}
