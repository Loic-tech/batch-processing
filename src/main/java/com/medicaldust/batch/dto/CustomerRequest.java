package com.medicaldust.batch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class CustomerRequest {

    @NotNull(message = "FIRSTNAME IS REQUIRED")
    @NotEmpty(message = "FIRSTNAME CAN'T BE EMPTY")
    private String firstname;

    @NotNull(message = "LASTNAME IS REQUIRED")
    @NotEmpty(message = "LASTNAME CAN'T BE EMPTY")
    private String lastname;

    @Email(regexp = "^(.+)@(.+)$", message = "INVALID EMAIL ADDRESS")
    @NotEmpty(message = "EMAIL CAN'T BE EMPTY")
    private String email;

    @NotBlank(message = "PHONE NUMBER IS REQUIRED")
    private String phone;

    @NotBlank(message = "COUNTRY IS REQUIRED")
    private String country;
}
