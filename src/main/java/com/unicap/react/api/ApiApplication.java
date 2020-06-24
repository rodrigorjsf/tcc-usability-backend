package com.unicap.react.api;

import com.unicap.react.api.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication implements CommandLineRunner {

    @Autowired
    private S3Service s3Service;

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        s3Service.uploadFile("E:\\Documentos\\Faculdade\\REACT\\Imagens\\distileriesImgs\\caol_ila_original.png");
    }
}
