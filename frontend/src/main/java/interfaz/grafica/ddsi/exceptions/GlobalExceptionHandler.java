package interfaz.grafica.ddsi.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler({BackendException.class, ConnectionException.class})
  public String handleCustomExceptions(Exception e, Model model) {
    model.addAttribute("errorMessage", e.getMessage());
    return "redirect:/404"; //Uso esto para probar solamente.
  }
// TODO: Lo que hay que hacer es try catch en cada controller e inyectar el mensaje de error de conexión (o lo que corresponda) para que se vea dentro de la página, poner un popup, o lo que sea, pero que el front siga siendo funcional. Queda medio raro que si el back está caido te muestre una pantalla de error en todas las páginas a las que vas. (Ej: si quiero ver "explore" y el back está caido, tendría que mostrarme "explore" con un mensaje de que no se pudo conectar al back en donde se muestran los eventos (o algo así), no redirigirme a una página de error). Algo parecido se hace con la de /register.
}

//TODO !Teneemos un problems, cuando esto es llamado por un htmx, se inyecta donde el htmx le indica no en la pag real (duplica la pagina)