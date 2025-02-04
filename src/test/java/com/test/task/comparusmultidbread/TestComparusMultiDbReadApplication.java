package com.test.task.comparusmultidbread;

import org.springframework.boot.SpringApplication;

public class TestComparusMultiDbReadApplication {

    public static void main(String[] args) {
        SpringApplication.from(ComparusMultiDbReadApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
