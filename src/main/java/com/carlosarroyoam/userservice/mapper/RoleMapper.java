package com.carlosarroyoam.userservice.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.carlosarroyoam.userservice.dto.RoleResponse;
import com.carlosarroyoam.userservice.entity.Role;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMapper {

	RoleResponse toDto(Role role);

	List<RoleResponse> toDtos(List<Role> roles);

}
