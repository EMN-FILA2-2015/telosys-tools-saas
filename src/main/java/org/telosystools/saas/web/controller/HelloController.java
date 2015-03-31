package org.telosystools.saas.web.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telosystools.saas.domain.Hello;

@RestController
public class HelloController {

    private static final String template = "Hello, %s!";

    @RequestMapping("/hello/{name}")
    public Hello hello(@PathVariable("name") String name) {
        return new Hello(1, String.format(template, name));
    }

}