package interfaz.grafica.ddsi.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler({BackendException.class, ConnectionException.class})
  public String handleCustomExceptions(Exception e, Model model) {
    model.addAttribute("errorMessage", e.getMessage());
    return "redirect:/404"; //TODO Mostrar lo que corresponde. Uso esto para probar solamente.
  }
}

//TODO !Teneemos un problems, cuando esto es llamado por un htmx, se inyecta donde el htmx le indica no en la pag real (duplica la pagina)