package com.hieucodeg.service.role;

import com.hieucodeg.model.Role;
import com.hieucodeg.model.dto.RoleDTO;
import com.hieucodeg.service.IGeneralService;

import java.util.List;

public interface IRoleService extends IGeneralService<Role> {
    Role findByName(String name);
    List<RoleDTO> getAllRoleDTO();
}
