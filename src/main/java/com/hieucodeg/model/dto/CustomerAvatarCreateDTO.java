package com.hieucodeg.model.dto;

import com.hieucodeg.model.Customer;
import com.hieucodeg.model.LocationRegion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class CustomerAvatarCreateDTO implements Validator {

    private Long id;
    private String fullName;
    @NotEmpty(message = "Email chưa được nhập")
    @Email(message = "Email không đúng (vd: HieuCodeg@codegym.com)")
    private String email;
    @NotEmpty(message = "Số điện thoại chưa được nhập")
    private String phone;

    MultipartFile file;

    private Long locationId;
    @Pattern(regexp = "^\\d+$", message = "ID tỉnh phải là số")
    private String provinceId;
    @NotEmpty(message = "Tên tỉnh không được trống")
    private String provinceName;
    @Pattern(regexp = "^\\d+$", message = "ID huyện/thị phải là số")
    @NotEmpty(message = "ID huyện/thị xã không được trống")
    private String districtId;
    @NotEmpty(message = "Tên huyện/thị xã không được trống")
    private String districtName;
    @Pattern(regexp = "^\\d+$", message = "ID phường/xã phải là số")
    @NotEmpty(message = "ID phường/xã không được trống")
    private String wardId;
    @NotEmpty(message = "Tên phường/xã không được trống")
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

    @Override
    public boolean supports(Class<?> aClass) {
        return CustomerAvatarCreateDTO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CustomerAvatarCreateDTO customerAvatarCreateDTO = (CustomerAvatarCreateDTO) target;

        String fullName = customerAvatarCreateDTO.getFullName();

        if (fullName.isEmpty()) {
            errors.rejectValue("fullName", "","Họ tên chưa được nhập");
        } else if (fullName.length() < 3) {
            errors.rejectValue("fullName", "","Tên không dưới 3 ký tự");
        } else if (fullName.length() > 100) {
            errors.rejectValue("fullName", "","Tên của bạn quá dài");
        }
    }
}
