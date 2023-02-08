package com.hieucodeg.service.user;

import com.hieucodeg.model.User;
import com.hieucodeg.model.dto.UserDTO;
import com.hieucodeg.service.IGeneralService;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface IUserService extends IGeneralService<User>, UserDetailsService {
    User getByUsername(String username);

    Optional<User> findByUsername(String username);

    Optional<UserDTO> findUserDTOByUsername(String username);

    Boolean existsByUsername(String email);
}
