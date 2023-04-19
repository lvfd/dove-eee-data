package com.dovepay.doveEditor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class DoveFetchApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoveFetchApplication.class, args);
    }

}
