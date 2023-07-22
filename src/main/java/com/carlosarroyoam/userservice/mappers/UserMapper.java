package com.carlosarroyoam.userservice.mappers;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.carlosarroyoam.userservice.dto.CreateUserDto;
import com.carlosarroyoam.userservice.dto.UserDto;
import com.carlosarroyoam.userservice.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

	UserDto toDto(User user);

	List<UserDto> toDtos(List<User> users);

	@BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
	User toEntity(CreateUserDto createUserDto);

}
