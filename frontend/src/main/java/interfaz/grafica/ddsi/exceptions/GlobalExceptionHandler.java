package interfaz.grafica.ddsi.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler({BackendException.class, ConnectionException.class})
  public String handleCustomExceptions(Exception e, Model model) {
    model.addAttribute("errorMessage", e.getMessage());
    return "error/404"; //TODO Mostrar lo que corresponde. Uso esto para probar solamente.
  }
}