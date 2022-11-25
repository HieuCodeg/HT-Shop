package com.hieucodeg.model.dto;

import com.hieucodeg.model.Customer;
import com.hieucodeg.model.LocationRegion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerAvartasDTO {
//    @Pattern(regexp = "^\\d+$", message = "ID không hợp lệ")
    private Long id;
    @NotEmpty(message = "Tên không được trống")
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @NotEmpty(message = "Email không được trống")
    @Email(message = "Email không đúng định dạng (vd:HieuCodeg@gmail.com")
    private String email;
    @NotEmpty(message = "Số điện thoại không được trống")
    private String phone;

    private AvatarDTO avatarDTO;
    private Boolean deleted;
    @Valid
    private LocationRegionDTO locationRegion;

    public CustomerAvartasDTO(Long id, String fullName, String email, String phone, LocationRegion locationRegion, AvatarDTO avatarDTO) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.locationRegion = locationRegion.toLocationRegionDTO();
        this.avatarDTO = avatarDTO;
    }

    public CustomerAvartasDTO(Long id, String fullName, String email,
                              String phone, LocationRegion locationRegion,
                              String idAvr, String fileName, String fileFolder, String fileUrl,
                              String cloudId, String fileType) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.deleted = false;
        this.avatarDTO = new AvatarDTO(idAvr,fileName,fileFolder,fileUrl,cloudId,fileType);
        this.locationRegion = locationRegion.toLocationRegionDTO();
    }

    public Customer toCustomer() {
        return new Customer()
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setLocationRegion(locationRegion.toLocationRegion());
    }
}
