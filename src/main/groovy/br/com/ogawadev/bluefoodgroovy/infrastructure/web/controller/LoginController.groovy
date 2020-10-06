package br.com.ogawadev.bluefoodgroovy.infrastructure.web.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LoginController {

    @GetMapping( value = ["/","/login"] )
    String login() {
        return "login"
    }

    @GetMapping(path = "/login-error")
    String loginError(Model model) {
        model.addAttribute("msg","Credenciais inválidas")
        return "login"
    }
}
