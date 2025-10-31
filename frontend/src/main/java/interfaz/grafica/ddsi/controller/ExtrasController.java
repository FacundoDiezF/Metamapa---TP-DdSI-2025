package interfaz.grafica.ddsi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ExtrasController {

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

}
