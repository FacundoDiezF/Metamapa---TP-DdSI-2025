package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.dtos.input.SubscriberInputDTO;

public interface ISubscriberService {
  void subscribe(SubscriberInputDTO dto);
}
