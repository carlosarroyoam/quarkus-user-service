package com.carlosarroyoam.userservice.user.dto;

import com.carlosarroyoam.userservice.user.entity.Role;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/** Representa un {@link Role} expuesto por la API. */
@Getter
@Setter
@Builder
public class RoleDto {
  private Integer id;
  private String title;
  private String description;

  /** Mapeador MapStruct entre {@link Role} y sus DTO. */
  @Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  public interface RoleDtoMapper {
    RoleDtoMapper INSTANCE = Mappers.getMapper(RoleDtoMapper.class);

    RoleDto toDto(Role role);

    List<RoleDto> toDtos(List<Role> roles);
  }
}
