package interfaz.grafica.ddsi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LandingController {

  @GetMapping({"/", "/landing"})
  public String landing(Model model){
    model.addAttribute("pageTitle", "MetaMapa · Landing");
    return "landing";
  }

  // Placeholders de rutas futuras
  @GetMapping("/home")
  public String home() { return "home"; }
}
