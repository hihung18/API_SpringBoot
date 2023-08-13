package com.example.api_java.model.dto;

import com.example.api_java.model.RoleName;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserDetailDTO {

    private Long id;
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
    private String password;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    private RoleName role;

    private String firstName;

    private String lastName;
    private String address;
}
