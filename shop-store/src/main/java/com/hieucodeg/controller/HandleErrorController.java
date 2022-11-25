package com.hieucodeg.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;


@Controller
public class HandleErrorController implements ErrorController {

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request) {
        ModelAndView errorPage = new ModelAndView();
        errorPage.setViewName("error/errorPage");

        String errorMsg = "";
        String title = "";
        int httpErrorCode = getErrorCode(request);

        switch (httpErrorCode) {
            case 400: {
                title = "Bad Request";
                errorMsg = "Http Status Code: 400. Bad Request";
                break;
            }
            case 401: {
                title = "Unauthorized";
                errorMsg = "Http Status Code: 401. Unauthorized";
                break;
            }
            case 404: {
                title = "Resource not found";
                errorMsg = "Http Status Code: 404. Resource not found";
                errorPage.setViewName("error/404");
                break;
            }
            case 405: {
                title = "Method Not Allowed";
                errorMsg = "Http Status Code: 405. Method Not Allowed";
                break;
            }
            case 409: {
                title = "Data Conflict";
                errorMsg = "Http Status Code: 409. Data Conflict";
                break;
            }
            case 500: {
                title = "Internal Server Error";
                errorMsg = "Http Status Code: 500. Internal Server Error";
                break;
            }
        }
        errorPage.addObject("title", title);
        errorPage.addObject("errorMsg", errorMsg);

        return errorPage;
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
    }

    @RequestMapping("/403")
    public ModelAndView accessDenied(Principal user) {
        ModelAndView model = new ModelAndView("error/403");
        String userName = user.getName();
        userName = userName.substring(0, userName.indexOf("@"));
        model.addObject("username", userName);
        return model;
    }

}