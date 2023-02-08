package com.hieucodeg.controller.api;

import com.hieucodeg.exception.DataInputException;
import com.hieucodeg.exception.EmailExistsException;
import com.hieucodeg.model.*;
import com.hieucodeg.model.dto.*;
import com.hieucodeg.repository.AvatarRepository;
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
@RequestMapping("/web/api")
public class CustomerWebAPI {

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IStaffService staffService;

    @Autowired
    private IUserService userService;
    @Autowired
    private AppUtils appUtils;

    @Autowired
    private AvatarRepository avatarRepository;


    @GetMapping("/customers/{customerId}")
    public ResponseEntity<?> getCustomer(@PathVariable Long customerId) {

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (!customerOptional.isPresent()) {
            throw new DataInputException("ID khách hàng không hợp lệ");
        }

        return new ResponseEntity<>(customerOptional.get().toCustomerAvartasDTO(), HttpStatus.OK);
    }


    @PostMapping("/customers")
    public ResponseEntity<?> create(@Validated @ModelAttribute CustomerAvatarCreateDTO customerAvatarCreateDTO, BindingResult bindingResult,
                                    @Validated @ModelAttribute UserCustomerDTO userDTO, BindingResult bindingResult2) {

        new CustomerAvatarCreateDTO().validate(customerAvatarCreateDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }
        if (bindingResult2.hasErrors())
            return appUtils.mapErrorToResponse(bindingResult2);

        Optional<Staff> emailOptional1 = staffService.findByEmail(customerAvatarCreateDTO.getEmail());
        if (emailOptional1.isPresent()) {
            throw new EmailExistsException("Email đã tồn tại!");
        }
        Optional<Customer> emailOptional2 = customerService.findByEmail(customerAvatarCreateDTO.getEmail());

        if (emailOptional2.isPresent()) {
            throw new EmailExistsException("Email đã tồn tại!");
        }

        Boolean existsByUsername = userService.existsByUsername(customerAvatarCreateDTO.getEmail());
        if (existsByUsername) {
            throw new EmailExistsException("Email đã tồn tại!");
        }

        LocationRegion locationRegion = new LocationRegion();
        locationRegion.setId(0L);
        locationRegion.setProvinceId(customerAvatarCreateDTO.getProvinceId());
        locationRegion.setProvinceName(customerAvatarCreateDTO.getProvinceName());
        locationRegion.setDistrictId(customerAvatarCreateDTO.getDistrictId());
        locationRegion.setDistrictName(customerAvatarCreateDTO.getDistrictName());
        locationRegion.setWardId(customerAvatarCreateDTO.getWardId());
        locationRegion.setWardName(customerAvatarCreateDTO.getWardName());
        locationRegion.setAddress(customerAvatarCreateDTO.getAddress());

        customerAvatarCreateDTO.setId(0L);
        Customer customer = customerAvatarCreateDTO.toCustomer(locationRegion);

        User user = new User();
        user.setUsername(customerAvatarCreateDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        Role role= new Role();
        role.setId(3l);
        user.setRole(role);
        try {
            User newUser = userService.save(user);
            customer.setUser(newUser);
        } catch (DataIntegrityViolationException e) {
            throw new DataInputException("Thông tin tài khoản không đúng, vui lòng kiểm tra lại");
        }

        CustomerAvartasDTO newCustomer;
        if (customerAvatarCreateDTO.getFile() != null) {
            newCustomer = customerService.saveWithAvatar(customer, customerAvatarCreateDTO.getFile());
        } else {
            customer.getLocationRegion().setId(null);
            newCustomer = customerService.save(customer).toCustomerAvartasDTO();
            newCustomer.setAvatarDTO(new AvatarDTO());
        }

        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }


}
