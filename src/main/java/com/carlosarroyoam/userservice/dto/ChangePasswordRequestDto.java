package com.carlosarroyoam.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordRequestDto {
  @NotBlank(message = "Current password should not be blank")
  private String currentPassword;

  @NotBlank(message = "New password  should not be blank")
  @Size(min = 3, max = 32, message = "New password  should be between 3 and 32")
  private String newPassword;

  @NotBlank(message = "Confirm password  should not be blank")
  @Size(min = 3, max = 32, message = "Confirm password  should be between 3 and 32")
  private String confirmPassword;
}
