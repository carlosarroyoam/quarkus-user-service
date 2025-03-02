package com.carlosarroyoam.userservice.mapper;

import com.carlosarroyoam.userservice.dto.RoleDto;
import com.carlosarroyoam.userservice.entity.Role;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMapper {
  RoleDto toDto(Role role);

  List<RoleDto> toDtos(List<Role> roles);
}
