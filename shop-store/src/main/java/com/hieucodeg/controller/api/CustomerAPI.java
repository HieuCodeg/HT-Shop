package com.hieucodeg.controller.api;

import com.hieucodeg.exception.DataInputException;
import com.hieucodeg.exception.EmailExistsException;
import com.hieucodeg.model.Customer;
import com.hieucodeg.model.LocationRegion;
import com.hieucodeg.model.dto.*;
import com.hieucodeg.repository.AvatarRepository;
import com.hieucodeg.service.customer.ICustomerService;
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
public class CustomerAPI {

    @Autowired
    private ICustomerService customerService;
    @Autowired
    private AppUtils appUtils;

    @Autowired
    private AvatarRepository avatarRepository;

    @GetMapping("/customers")
    public ResponseEntity<?> findAllByDeletedIsFalse() {

        List<CustomerAvartasDTO> customers = customerService.getAllCustomersAvartaDTO();

        if (customers.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<?> getCustomer(@PathVariable Long customerId) {

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (!customerOptional.isPresent()) {
            throw new DataInputException("ID khách hàng không hợp lệ");
        }

        return new ResponseEntity<>(customerOptional.get().toCustomerAvartasDTO(), HttpStatus.OK);
    }

    @PatchMapping("/customers/delete/{customerId}")
    public ResponseEntity<?> delete(@PathVariable Long customerId) {

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (!customerOptional.isPresent()) {
            throw new DataInputException("ID khách hàng không hợp lệ");
        }

        Customer customer = customerOptional.get();
        customer.setDeleted(true);
        customerService.save(customer);

        return new ResponseEntity<>(customerOptional.get().toCustomerAvartasDTO(), HttpStatus.OK);
    }

    @PostMapping("/customers")
    public ResponseEntity<?> create(@Validated @ModelAttribute CustomerAvatarCreateDTO customerAvatarCreateDTO, BindingResult bindingResult) {

        new CustomerAvatarCreateDTO().validate(customerAvatarCreateDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
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

    @PatchMapping("/customers/{customerId}")
    public ResponseEntity<?> update(@PathVariable Long customerId,@Validated @ModelAttribute CustomerAvatarCreateDTO customerAvatarUpdateDTO, BindingResult bindingResult) {

        new CustomerAvatarCreateDTO().validate(customerAvatarUpdateDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (!customerOptional.isPresent()) {
            throw new DataInputException("ID khách hàng không hợp lệ");
        }
        Customer customer = customerOptional.get();
        Long idLocation = customer.getLocationRegion().getId();

        Optional<Customer> emailOptional = customerService.findByEmailAndIdIsNot(customerAvatarUpdateDTO.getEmail(), customerId);

        if (emailOptional.isPresent()) {
            throw new EmailExistsException("Email đã tồn tại!");
        }

        customer.setFullName(customerAvatarUpdateDTO.getFullName());
        customer.setEmail(customerAvatarUpdateDTO.getEmail());
        customer.setPhone(customerAvatarUpdateDTO.getPhone());

        LocationRegion locationRegion = new LocationRegion();
        locationRegion.setId(idLocation);
        locationRegion.setProvinceId(customerAvatarUpdateDTO.getProvinceId());
        locationRegion.setProvinceName(customerAvatarUpdateDTO.getProvinceName());
        locationRegion.setDistrictId(customerAvatarUpdateDTO.getDistrictId());
        locationRegion.setDistrictName(customerAvatarUpdateDTO.getDistrictName());
        locationRegion.setWardId(customerAvatarUpdateDTO.getWardId());
        locationRegion.setWardName(customerAvatarUpdateDTO.getWardName());
        locationRegion.setAddress(customerAvatarUpdateDTO.getAddress());

        customer.setLocationRegion(locationRegion);
        AvatarDTO avatarDTO = new AvatarDTO();
        try {
            avatarDTO = avatarRepository.getAvatarByCustomer_Id(customerId).toAvatarDTO();
        } catch (Exception e) {
            System.out.println(e);
        }

        if (customerAvatarUpdateDTO.getFile() == null) {
            CustomerAvartasDTO updatedCustomer = customerService.save(customer).toCustomerAvartasDTO();
                updatedCustomer.setAvatarDTO(avatarDTO);

            return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
        } else {

            CustomerAvartasDTO updatedCustomer = customerService.updateWithAvatar(customer,customerAvatarUpdateDTO.getFile(),avatarDTO);
            return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
        }

    }



}
