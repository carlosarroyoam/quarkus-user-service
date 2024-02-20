package com.carlosarroyoam.userservice.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.carlosarroyoam.userservice.dto.CreateUserRequest;
import com.carlosarroyoam.userservice.dto.UserResponse;
import com.carlosarroyoam.userservice.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

	UserResponse toDto(User user);

	List<UserResponse> toDtos(List<User> users);

	User toEntity(CreateUserRequest createUserRequest);

}
