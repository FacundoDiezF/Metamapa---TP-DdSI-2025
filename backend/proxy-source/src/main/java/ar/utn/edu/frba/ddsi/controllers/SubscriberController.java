package ar.utn.edu.frba.ddsi.controllers;

import ar.utn.edu.frba.ddsi.models.dtos.input.SubscriberInputDTO;
import ar.utn.edu.frba.ddsi.services.ISubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subscribers")
public class SubscriberController {

  @Autowired
  ISubscriberService subscriberService;

  @PostMapping()
  public void subscribe(@RequestBody SubscriberInputDTO dto) {
    subscriberService.subscribe(dto);
  }
}
