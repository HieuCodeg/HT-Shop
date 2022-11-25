package com.hieucodeg.model.dto;

import com.hieucodeg.model.Customer;
import com.hieucodeg.model.LocationRegion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.Column;
import javax.persistence.MapKeyClass;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerDTO {
//    @Pattern(regexp = "^\\d+$", message = "ID không hợp lệ")
    private Long id;
    @NotEmpty(message = "Tên không được trống")
    private String fullName;
    @NotEmpty(message = "Email không được trống")
    @Email(message = "Email không đúng định dạng (vd:HieuCodeg@gmail.com")
    private String email;
    @NotEmpty(message = "Số điện thoại không được trống")
    private String phone;

    private BigDecimal balance;
    private Boolean deleted;
    @Valid
    private LocationRegionDTO locationRegion;

    public CustomerDTO(Long id, String fullName, String email, String phone, BigDecimal balance, LocationRegion locationRegion) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.balance = balance;
        this.deleted = false;
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
