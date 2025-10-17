package ar.utn.edu.frba.ddsi.models.dtos.input.deletion_request;

import lombok.Data;

@Data
public class DeletionRequestCreationDTO {
  Long eventId;
  String argument;
  String applicantName; //TODO: Esto deberia ser un usuario
}
