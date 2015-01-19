package org.telosystools.saas.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.telosystools.saas.domain.Hello;

@RestController
public class HelloController {

    private static final String template = "Hello, %s!";

    @RequestMapping("/hello")
    public Hello hello(@RequestParam(value="name", defaultValue="World") String name) {
        return new Hello(1,
                String.format(template, name));
    }
}