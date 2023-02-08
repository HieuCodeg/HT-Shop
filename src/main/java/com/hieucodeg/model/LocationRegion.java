package com.hieucodeg.model;

import com.hieucodeg.model.dto.LocationRegionDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Pattern;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "location_region")

public class LocationRegion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "province_id", nullable = false)
    private String provinceId;

    @Column(name = "province_name", nullable = false)
    private String provinceName;

    @Column(name = "district_id", nullable = false)
    private String districtId;

    @Column(name = "district_name", nullable = false)
    private String districtName;

    @Column(name = "ward_id", nullable = false)
    private String wardId;

    @Column(name = "ward_name", nullable = false)
    private String wardName;

    private String address;

    public LocationRegionDTO toLocationRegionDTO() {
        return new LocationRegionDTO()
            .setProvinceId(provinceId)
            .setProvinceName(provinceName)
            .setDistrictId(districtId)
            .setDistrictName(districtName)
            .setWardId(wardId)
            .setWardName(wardName)
            .setAddress(address);
    }

}
