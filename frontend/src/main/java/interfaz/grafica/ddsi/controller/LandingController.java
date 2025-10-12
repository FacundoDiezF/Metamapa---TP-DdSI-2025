package interfaz.grafica.ddsi.controller;

import interfaz.grafica.ddsi.dtos.CollectionDTO;
import interfaz.grafica.ddsi.services.CollectionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/landing")
public class LandingController {

  private final CollectionService collectionService;

  @Autowired
  public LandingController(CollectionService collectionService) {
    this.collectionService = collectionService;
  }

  @GetMapping
  public String landing(Model model){
    List<CollectionDTO> topCollections = collectionService.getTopCollections();
    model.addAttribute("topCollections", topCollections);
    model.addAttribute("pageTitle", "MetaMapa · Landing");
    return "landing";
  }
}
