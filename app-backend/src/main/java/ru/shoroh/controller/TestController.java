package ru.shoroh.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/test")
public class TestController {


    @GetMapping("/test1")
    public void testController() {
        System.out.println("Тестовый контроллер");
    }
}
