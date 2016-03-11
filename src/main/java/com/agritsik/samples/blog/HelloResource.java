package com.agritsik.samples.blog;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class HelloResource {


    @RequestMapping("/")
    public String hello(){
        return "Hello from spring";
    }

    public static void main(String[] args) {
        SpringApplication.run(HelloResource.class, args);
    }


}
