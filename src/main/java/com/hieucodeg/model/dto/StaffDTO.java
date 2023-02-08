package com.hieucodeg.model.dto;

import com.hieucodeg.model.LocationRegion;
import com.hieucodeg.model.Staff;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StaffDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private StaffAvatarDTO avatarDTO;
    private Boolean deleted;
    private LocationRegionDTO locationRegion;

    public StaffDTO(Long id, String fullName, String email, String phone, LocationRegion locationRegion, StaffAvatarDTO avatarDTO) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.locationRegion = locationRegion.toLocationRegionDTO();
        this.avatarDTO = avatarDTO;
    }

    public StaffDTO(Long id, String fullName, String email,
                    String phone, LocationRegion locationRegion,
                    String idAvr, String fileName, String fileFolder, String fileUrl,
                    String cloudId, String fileType) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.deleted = false;
        this.avatarDTO = new StaffAvatarDTO(idAvr,fileName,fileFolder,fileUrl,cloudId,fileType);
        this.locationRegion = locationRegion.toLocationRegionDTO();
    }

    public Staff toStaff() {
        return new Staff()
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setLocationRegion(locationRegion.toLocationRegion());
    }
}
