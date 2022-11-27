package com.hieucodeg.controller;

import com.hieucodeg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
@Controller
@RequestMapping("/web")
public class webController {
    @Autowired
    private AppUtils appUtils;

    @GetMapping
    public ModelAndView showPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("web/index");

        String username = appUtils.getPrincipalUsername();

        modelAndView.addObject("username", username);

        return modelAndView;
    }
}
