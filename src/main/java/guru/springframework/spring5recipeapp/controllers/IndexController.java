package guru.springframework.spring5recipeapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // Remember, Due to the Default Spring Boot package scan by App file of any Bean underneath it, this will get picked up as a Bean
public class IndexController {

    @RequestMapping({"", "/", "index"})
    public String getIndexPage() {
        return "index"; // Resolves to the Thymeleaf Template filename we created (index.html)
    }
}
