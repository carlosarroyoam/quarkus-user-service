package com.carlosarroyoam.userservice.user.dto;

import com.carlosarroyoam.userservice.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/** Representa un {@link User} expuesto por la API, sin su contraseña. */
@Getter
@Setter
@Builder
public class UserDto {
  private Long id;
  private String name;
  private Byte age;
  private String email;
  private String username;
  private Boolean isActive;
  private Integer roleId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  /** Mapeador MapStruct entre {@link User} y sus DTO. */
  @Mapper(
      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
      unmappedTargetPolicy = ReportingPolicy.IGNORE)
  public interface UserDtoMapper {
    UserDtoMapper INSTANCE = Mappers.getMapper(UserDtoMapper.class);

    UserDto toDto(User user);

    List<UserDto> toDtos(List<User> users);

    User toEntity(CreateUserRequestDto requestDto);
  }
}
