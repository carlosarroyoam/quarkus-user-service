package com.carlosarroyoam.userservice.mapper;

import com.carlosarroyoam.userservice.dto.CreateUserRequestDto;
import com.carlosarroyoam.userservice.dto.UserDto;
import com.carlosarroyoam.userservice.entity.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
  UserDto toDto(User user);

  List<UserDto> toDtos(List<User> users);

  User toEntity(CreateUserRequestDto createUserRequestDto);
}
