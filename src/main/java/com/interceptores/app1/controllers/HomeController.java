package com.interceptores.app1.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/", ""})
public class HomeController {
    
    @GetMapping("")
    public String Home(Model model) {
        model.addAttribute("hola", "Hola mundo");
        return "home";
    }

    @GetMapping("/resources")
    public String Resources() {
        return "resources";
    }

    @GetMapping("/docs")
    public String Docs() {
        return "docs";
    }

}
