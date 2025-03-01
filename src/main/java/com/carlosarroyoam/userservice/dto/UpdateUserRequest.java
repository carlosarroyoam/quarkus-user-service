package com.carlosarroyoam.userservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {
  @NotBlank(message = "Name should not be blank")
  @Size(min = 3, max = 128, message = "Name should be between 3 and 128")
  private String name;

  @NotNull(message = "Age should not be null")
  @Min(message = "Age should be min 18", value = 18)
  @Max(message = "Age should be max 100", value = 100)
  private Byte age;
}
