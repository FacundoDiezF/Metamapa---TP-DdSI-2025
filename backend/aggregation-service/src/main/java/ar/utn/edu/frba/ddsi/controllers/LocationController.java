package ar.utn.edu.frba.ddsi.controllers;

import ar.utn.edu.frba.ddsi.models.dtos.output.event.ProvinceDTO;
import ar.utn.edu.frba.ddsi.services.ILocationService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/locations")
public class LocationController {

  private final ILocationService locationService;

  public LocationController(ILocationService locationService) {
    this.locationService = locationService;
  }

  @GetMapping("/provinces")
  public Page<ProvinceDTO> getAllProvinces(
      @RequestParam(name= "province_in", required = false) List<Long> province_in,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

    if(province_in != null && !province_in.isEmpty()) {
      return locationService.getAllProvincesByIds(province_in, pageable);
    }

    return locationService.getAllProvinces(pageable);
  }
}
