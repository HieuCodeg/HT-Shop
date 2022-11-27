package com.hieucodeg.controller.api;

import com.hieucodeg.exception.DataInputException;
import com.hieucodeg.exception.EmailExistsException;
import com.hieucodeg.model.*;
import com.hieucodeg.model.dto.StaffAvatarDTO;
import com.hieucodeg.model.dto.StaffCreateDTO;
import com.hieucodeg.model.dto.StaffDTO;
import com.hieucodeg.repository.StaffAvatarRepository;
import com.hieucodeg.service.customer.ICustomerService;
import com.hieucodeg.service.staff.IStaffService;
import com.hieucodeg.service.user.IUserService;
import com.hieucodeg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
    private ICustomerService customerService;
    @Autowired
    private AppUtils appUtils;

    @Autowired
    private IUserService userService;

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
        Long idUser = 0l;
        if (staff.getUser() != null) {
            idUser = staff.getUser().getId();
        }
        staff.setUser(null);
        staff.setDeleted(true);
        staffService.save(staff);
        if (idUser != 0l) {
            userService.remove(idUser);
        }

        return new ResponseEntity<>(staffOptional.get().toStaffDTO(), HttpStatus.OK);
    }

    @PostMapping("/staffs")
    public ResponseEntity<?> create(@Validated @ModelAttribute StaffCreateDTO staffCreateDTO, BindingResult bindingResult) {

        new StaffCreateDTO().validate(staffCreateDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Optional<Staff> emailOptional1 = staffService.findByEmail(staffCreateDTO.getEmail());
        if (emailOptional1.isPresent()) {
            throw new EmailExistsException("Email đã tồn tại!");
        }
        Optional<Customer> emailOptional2 = customerService.findByEmail(staffCreateDTO.getEmail());

        if (emailOptional2.isPresent()) {
            throw new EmailExistsException("Email đã tồn tại!");
        }

        Boolean existsByUsername = userService.existsByUsername(staffCreateDTO.getEmail());
        if (existsByUsername) {
            throw new EmailExistsException("Email đã tồn tại!");
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

        if (staffCreateDTO.isAccount()) {
            User user = new User();
            user.setUsername(staffCreateDTO.getEmail());
            user.setPassword("123456");
            Role role= new Role();
            role.setId(2l);
            user.setRole(role);
            try {
                User newUser = userService.save(user);
                staff.setUser(newUser);
            } catch (DataIntegrityViolationException e) {
                throw new DataInputException("Thông tin tài khoản không đúng, vui lòng kiểm tra lại");
            }
        }

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

        if (!staff.getEmail().equals(staffCreateDTO.getEmail())) {
            Optional<Staff> emailOptional1 = staffService.findByEmailAndIdIsNot(staffCreateDTO.getEmail(), staffId);
            if (emailOptional1.isPresent()) {
                throw new EmailExistsException("Email đã tồn tại!");
            }

            Optional<Customer> emailOptional2 = customerService.findByEmailAndIdIsNot(staffCreateDTO.getEmail(), staffId);
            if (emailOptional2.isPresent()) {
                throw new EmailExistsException("Email đã tồn tại!");
            }

            Boolean existsByUsername = userService.existsByUsername(staffCreateDTO.getEmail());
            if (existsByUsername) {
                throw new EmailExistsException("Email đã tồn tại!");
            }
        }

        staff.setFullName(staffCreateDTO.getFullName());

        if (staff.getEmail() != staffCreateDTO.getEmail() && staff.getUser() != null) {
            User user = staff.getUser();
            user.setUsername(staffCreateDTO.getEmail());
            try {
                User newUser = userService.save(user);
                staff.setUser(newUser);
            } catch (DataIntegrityViolationException e) {
                throw new DataInputException("Thông tin tài khoản không đúng, vui lòng kiểm tra lại");
            }
        }
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
