package com.example.exactabank.controller

import com.example.exactabank.dto.UserDto
import com.example.exactabank.model.User
import com.example.exactabank.service.UserService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Api(tags = ["User Management"])
@RestController
@RequestMapping("/api/v1/users")
@Validated
class UserController(
    @Autowired private val userService: UserService,
) {
    @ApiOperation(value = "Create a new user")
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "User created successfully"),
            ApiResponse(code = 400, message = "Invalid input"),
            ApiResponse(code = 500, message = "Internal server error"),
        ],
    )
    @PostMapping
    fun createUser(
        @Valid @RequestBody userDto: UserDto,
    ): ResponseEntity<User> {
        val createdUser =
            userService.createUser(
                User(
                    name = userDto.name,
                    email = userDto.email,
                    cpf = userDto.cpf,
                    phone = userDto.phone,
                    balance = userDto.balance,
                ),
            )
        return ResponseEntity(createdUser, HttpStatus.CREATED)
    }

    @ApiOperation(value = "Get all users")
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Users fetched successfully"),
            ApiResponse(code = 500, message = "Internal server error"),
        ],
    )
    @GetMapping
    fun getAllUsers(): ResponseEntity<List<User>> {
        val users = userService.getAllUsers()
        return ResponseEntity(users, HttpStatus.OK)
    }

    @ApiOperation(value = "Get user balance by user ID")
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Balance fetched successfully"),
            ApiResponse(code = 404, message = "User not found"),
            ApiResponse(code = 500, message = "Internal server error"),
        ],
    )
    @GetMapping("/{userId}/balance")
    fun getUserBalance(
        @PathVariable userId: Long,
    ): ResponseEntity<Double> {
        val balance = userService.getUserBalance(userId)
        return ResponseEntity(balance, HttpStatus.OK)
    }
}
