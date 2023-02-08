package com.hieucodeg.model.dto;


import com.hieucodeg.model.LocationRegion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LocationRegionDTO {
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


    public LocationRegion toLocationRegion() {
        return new LocationRegion()
                .setProvinceId(provinceId)
                .setProvinceName(provinceName)
                .setDistrictId(districtId)
                .setDistrictName(districtName)
                .setWardId(wardId)
                .setWardName(wardName)
                .setAddress(address);
    }
}
