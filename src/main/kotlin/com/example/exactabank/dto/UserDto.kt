package com.example.exactabank.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

data class UserDto(
    @field:NotBlank
    val name: String,
    @field:Email(message = "Invalid email format")
    @field:NotBlank
    val email: String,
    @field:Pattern(regexp = "\\d{11}", message = "Invalid CPF format. CPF should have 11 digits.")
    @field:NotBlank
    val cpf: String,
    @field:Pattern(regexp = "\\d{11}", message = "Invalid phone format. Phone should have 11 digits.")
    @field:NotBlank
    val phone: String,
    @NotNull
    val balance: Double = 0.0,
)
