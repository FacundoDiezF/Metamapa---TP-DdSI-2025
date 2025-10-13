package ar.utn.edu.frba.ddsi.controllers;

import ar.utn.edu.frba.ddsi.models.entities.event.values.location.Province;
import ar.utn.edu.frba.ddsi.services.ILocationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

  private final ILocationService locationService;

  public LocationController(ILocationService locationService) {
    this.locationService = locationService;
  }

  @GetMapping("/provinces")
  public List<Province> getAllProvinces() {
    return locationService.getAllProvinces();
  }
}
