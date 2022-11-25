package com.hieucodeg.model.dto;

import com.hieucodeg.model.Customer;
import com.hieucodeg.model.LocationRegion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerAvatarUpdateDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;

    MultipartFile  file;
    private Boolean deleted;

    private String provinceId;
    private String provinceName;
    private String districtId;
    private String districtName;
    private String wardId;
    private String wardName;
    private String address;

    public Customer toCustomer(LocationRegion locationRegion){
        return new Customer()
                .setId(id)
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setLocationRegion(locationRegion);
    }

}
