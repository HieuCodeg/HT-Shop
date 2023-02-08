package com.hieucodeg.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCustomerDTO {

    private Long id;
    private String username;

    @NotBlank(message = "Vui lòng nhập mật khẩu")
    @Size( max = 50, message = "Độ dài mật khẩu nằm trong khoảng 6-50 ký tự")
    private String password;



    public UserCustomerDTO(Long id, String username) {
        this.id = id;
        this.username = username;
    }

}
