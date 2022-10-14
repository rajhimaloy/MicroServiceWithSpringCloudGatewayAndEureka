package com.microservice.erp.controller;

import org.springframework.web.bind.annotation.*;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@RestController
@RequestMapping("/v1")
public class SecondController {

    @GetMapping("/message")
    public String test(){
        return "Hello JavaInUse Called in Second Service";
    }

}
