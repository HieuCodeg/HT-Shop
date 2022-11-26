package com.hieucodeg.controller;

import com.hieucodeg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class adminController {

    @Autowired
    private AppUtils appUtils;

    @GetMapping
    public ModelAndView showPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/index");

        String username = appUtils.getPrincipalUsername();

        modelAndView.addObject("username", username);

        return modelAndView;
    }

    @GetMapping("/products")
    public ModelAndView showProductPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/products");

        String username = appUtils.getPrincipalUsername();

        modelAndView.addObject("username", username);

        return modelAndView;
    }

    @GetMapping("/customers")
    public ModelAndView showCustomerPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/customers");

        String username = appUtils.getPrincipalUsername();

        modelAndView.addObject("username", username);

        return modelAndView;
    }

    @GetMapping("/staffs")
    public ModelAndView showStaffPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/staffs");

        String username = appUtils.getPrincipalUsername();

        modelAndView.addObject("username", username);

        return modelAndView;
    }
}
