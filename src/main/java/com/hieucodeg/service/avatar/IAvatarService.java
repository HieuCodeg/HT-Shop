package com.hieucodeg.service.avatar;

import com.hieucodeg.model.Avatar;

public interface IAvatarService {
    Avatar getAvatarByCustomer_Id(Long id);
    Avatar getById(String id);
}
