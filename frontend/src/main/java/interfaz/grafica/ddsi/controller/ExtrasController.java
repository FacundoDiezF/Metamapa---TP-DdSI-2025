package interfaz.grafica.ddsi.controller;

import interfaz.grafica.ddsi.dtos.output.collection.ConditionDTO;
import interfaz.grafica.ddsi.dtos.output.collection.ConditionType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ExtrasController {

  // Devuelve el fragmento HTML con la categoria seleccionada (new Event Form)
  @PostMapping("/category-input-fragment")
  public String getCategoryInputFragment(
      @RequestParam("itemSelected") String item,
      Model model) {

    String[] parts = item.split("::");
    Long catId = Long.valueOf(parts[0]);
    String catName = parts[1];

    model.addAttribute("catName", catName);
    model.addAttribute("catId", catId);
    return "fragments/modals/_moreOptions :: categoryInputForOneCategoryModal(catId=${catId}, catName=${catName})";
  }

  // Devuelve el fragmento HTML con la ubicación seleccionada en el mapa (new Event Form)
  @PostMapping("/location-input-fragment")
  public String getLocationInputFragment(
      @RequestParam Double latitude,
      @RequestParam Double longitude,
      Model model) {

    model.addAttribute("lat", latitude);
    model.addAttribute("lng", longitude);

    return "fragments/modals/_mapModal :: locationSelectedFragment";
  }

  // Agrega una nueva fila de condición al formulario de creación de colección
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/new-collection/add-condition-row")
  public String addConditionRow(@RequestParam("type") ConditionType type,
                                @RequestParam("index") int index,
                                Model model) {

    ConditionDTO conditionDto = new ConditionDTO();
    conditionDto.setConditionType(type);

    model.addAttribute("condition", conditionDto);
    model.addAttribute("idx", index); //Para th:field (conditions[0], conditions[1]...)

    return "fragments/modals/_newCollectionModal :: conditionRow";
  }
}
