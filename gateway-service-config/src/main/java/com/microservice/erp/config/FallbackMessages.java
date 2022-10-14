package com.microservice.erp.config;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@RestController
@RequestMapping("/api/fallback/messages")
public class FallbackMessages {

    @RequestMapping(value = "/unreachable", method = GET, produces = "application/json")
    @ResponseBody
    public String fallBack(){
        return "Unfortunately api not reachable at this moment!";
    }

}
