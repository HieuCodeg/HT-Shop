package com.hieucodeg.service.avatarStaff;

import com.hieucodeg.model.StaffAvatar;

public interface IStaffAvatarService {
    StaffAvatar getStaffAvatarByStaff_Id(Long id);
    StaffAvatar getById(String id);
}
