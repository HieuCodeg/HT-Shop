package com.hieucodeg.model.dto;


import com.hieucodeg.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    @NotBlank(message = "Vui lòng nhập tên đăng nhập")
    @Email(message = "Tên đăng nhập phải là email đăng ký của tài khoản")
    @Size(min = 6, max = 50, message = "Độ dài tên đăng nhập nằm trong khoảng 6-50 ký tự")
    private String username;

    @NotBlank(message = "Vui lòng nhập mật khẩu")
    @Size( max = 50, message = "Độ dài mật khẩu nằm trong khoảng 6-50 ký tự")
    private String password;

    @Valid
    private RoleDTO role;

    public UserDTO(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public User toUser() {
        return new User()
                .setId(id)
                .setUsername(username)
                .setPassword(password)
                .setRole(role.toRole());
    }

}
