package interfaz.grafica.ddsi.controller;

import interfaz.grafica.ddsi.dtos.auth.RegisterRequestDTO;
import interfaz.grafica.ddsi.exceptions.BackendException;
import interfaz.grafica.ddsi.exceptions.ConnectionException;
import interfaz.grafica.ddsi.services.ClientAuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

  private final ClientAuthService authService; // Usamos el nuevo servicio

  public AuthController(ClientAuthService authService) {
    this.authService = authService;
  }

  @GetMapping("/login")
  public String showLoginForm() {
    return "login";
  }

  @GetMapping("/register")
  public String showRegisterForm(Model model) {
    model.addAttribute("registerRequest", new RegisterRequestDTO());
    return "register";
  }

  @PostMapping("/register")
  public String processRegister(@ModelAttribute("registerRequest") RegisterRequestDTO registerRequest,
                                Model model,
                                HttpServletRequest request) {
    try {
      authService.registerAndAutoLogin(registerRequest, request.getSession());

      return "redirect:/";

    } catch (BackendException e) {
      String errorMessage = "Error registering user.";
      if (e.getErrorInfo() != null && e.getErrorInfo().getMessage() != null) {
        errorMessage = e.getErrorInfo().getMessage();
      }
      model.addAttribute("error", errorMessage);
      return "register";

    } catch (ConnectionException e) {
      model.addAttribute("error", "Server unavailable. Please try again later.");
      return "register";
    }
  }
}