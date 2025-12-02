package interfaz.grafica.ddsi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController {

  @GetMapping("/403")
  public String accessDenied() {
    return "error/403";
  }

  //TODO: Traer la de 404 acá
}