package ar.utn.edu.frba.ddsi.models.dtos;

import ar.utn.edu.frba.ddsi.models.entities.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
public class UserSummaryDTO {
  private String publicId; // UUID
  private String username;
//  private String email;
//  private String firstName;
//  private String lastName;
//  private String avatarUrl;
  private Set<String> roles; // Ej: ["CONTRIBUTOR", "ADMIN"]

  public static UserSummaryDTO from(User user) {
    return UserSummaryDTO.builder()
        .publicId(user.getPublicId())
        .username(user.getUsername())
        .roles(user.getRoles().stream()
            .map(Enum::name)
            .collect(Collectors.toSet()))
        .build();
  }
}
