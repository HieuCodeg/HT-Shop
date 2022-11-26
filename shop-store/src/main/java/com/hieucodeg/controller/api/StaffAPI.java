package com.hieucodeg.controller.api;

import com.hieucodeg.exception.DataInputException;
import com.hieucodeg.exception.EmailExistsException;
import com.hieucodeg.model.Customer;
import com.hieucodeg.model.LocationRegion;
import com.hieucodeg.model.Staff;
import com.hieucodeg.model.dto.*;
import com.hieucodeg.repository.AvatarRepository;
import com.hieucodeg.repository.StaffAvatarRepository;
import com.hieucodeg.repository.StaffRepository;
import com.hieucodeg.service.customer.ICustomerService;
import com.hieucodeg.service.staff.IStaffService;
import com.hieucodeg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class StaffAPI {

    @Autowired
    private IStaffService staffService;
    @Autowired
    private AppUtils appUtils;

    @Autowired
    private StaffAvatarRepository staffAvatarRepository;

    @GetMapping("/staffs")
    public ResponseEntity<?> findAllByDeletedIsFalse() {

        List<StaffDTO> staffDTOS = staffService.getAllStaffDTO();

        if (staffDTOS.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(staffDTOS, HttpStatus.OK);
    }

    @GetMapping("/staffs/{staffId}")
    public ResponseEntity<?> getCustomer(@PathVariable Long staffId) {

        Optional<Staff> staffOptional = staffService.findById(staffId);

        if (!staffOptional.isPresent()) {
            throw new DataInputException("ID nhân viên không hợp lệ");
        }

        return new ResponseEntity<>(staffOptional.get().toStaffDTO(), HttpStatus.OK);
    }

    @PatchMapping("/staffs/delete/{staffId}")
    public ResponseEntity<?> delete(@PathVariable Long staffId) {

        Optional<Staff> staffOptional = staffService.findById(staffId);

        if (!staffOptional.isPresent()) {
            throw new DataInputException("ID nhân viên không hợp lệ");
        }

        Staff staff = staffOptional.get();
        staff.setDeleted(true);
        staffService.save(staff);

        return new ResponseEntity<>(staffOptional.get().toStaffDTO(), HttpStatus.OK);
    }

    @PostMapping("/staffs")
    public ResponseEntity<?> create(@Validated @ModelAttribute StaffCreateDTO staffCreateDTO, BindingResult bindingResult) {

        new StaffCreateDTO().validate(staffCreateDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        LocationRegion locationRegion = new LocationRegion();
        locationRegion.setId(0L);
        locationRegion.setProvinceId(staffCreateDTO.getProvinceId());
        locationRegion.setProvinceName(staffCreateDTO.getProvinceName());
        locationRegion.setDistrictId(staffCreateDTO.getDistrictId());
        locationRegion.setDistrictName(staffCreateDTO.getDistrictName());
        locationRegion.setWardId(staffCreateDTO.getWardId());
        locationRegion.setWardName(staffCreateDTO.getWardName());
        locationRegion.setAddress(staffCreateDTO.getAddress());

        staffCreateDTO.setId(0L);
        Staff staff = staffCreateDTO.toStaff(locationRegion);
        StaffDTO newStaffDTO;
        if (staffCreateDTO.getFile() != null) {
            newStaffDTO = staffService.saveWithAvatar(staff, staffCreateDTO.getFile());
        } else {
            staff.getLocationRegion().setId(null);
            newStaffDTO = staffService.save(staff).toStaffDTO();
            newStaffDTO.setAvatarDTO(new StaffAvatarDTO());
        }

        return new ResponseEntity<>(newStaffDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/staffs/{staffId}")
    public ResponseEntity<?> update(@PathVariable Long staffId,@Validated @ModelAttribute StaffCreateDTO staffCreateDTO, BindingResult bindingResult) {

        new StaffCreateDTO().validate(staffCreateDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Optional<Staff> staffOptional = staffService.findById(staffId);

        if (!staffOptional.isPresent()) {
            throw new DataInputException("ID nhân viên không hợp lệ");
        }
        Staff staff = staffOptional.get();
        Long idLocation = staff.getLocationRegion().getId();

        Optional<Staff> emailOptional = staffService.findByEmailAndIdIsNot(staffCreateDTO.getEmail(), staffId);

        if (emailOptional.isPresent()) {
            throw new EmailExistsException("Email đã tồn tại!");
        }

        staff.setFullName(staffCreateDTO.getFullName());
        staff.setEmail(staffCreateDTO.getEmail());
        staff.setPhone(staffCreateDTO.getPhone());

        LocationRegion locationRegion = new LocationRegion();
        locationRegion.setId(idLocation);
        locationRegion.setProvinceId(staffCreateDTO.getProvinceId());
        locationRegion.setProvinceName(staffCreateDTO.getProvinceName());
        locationRegion.setDistrictId(staffCreateDTO.getDistrictId());
        locationRegion.setDistrictName(staffCreateDTO.getDistrictName());
        locationRegion.setWardId(staffCreateDTO.getWardId());
        locationRegion.setWardName(staffCreateDTO.getWardName());
        locationRegion.setAddress(staffCreateDTO.getAddress());

        staff.setLocationRegion(locationRegion);
        StaffAvatarDTO staffAvatarDTO = new StaffAvatarDTO();
        try {
            staffAvatarDTO = staffAvatarRepository.getStaffAvatarByStaff_Id(staffId).toStaffAvatarDTO();
        } catch (Exception e) {
            System.out.println(e);
        }

        if (staffCreateDTO.getFile() == null) {
            StaffDTO updatedStaff = staffService.save(staff).toStaffDTO();
            updatedStaff.setAvatarDTO(staffAvatarDTO);

            return new ResponseEntity<>(updatedStaff, HttpStatus.OK);
        } else {

            StaffDTO updatedStaff = staffService.updateWithAvatar(staff,staffCreateDTO.getFile(),staffAvatarDTO);
            return new ResponseEntity<>(updatedStaff, HttpStatus.OK);
        }

    }



}
