package com.example.exactabank.controller

import com.example.exactabank.dto.UserDto
import com.example.exactabank.model.User
import com.example.exactabank.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(UserController::class)
class UserControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userService: UserService

    @Test
    fun `createUser should return created user`() {
        val userDto =
            UserDto(
                "John Doe",
                "john@example.com",
                "12345678901",
                "12345678901",
                1000.0,
            )
        val user =
            User(
                1L,
                "John Doe",
                "john@example.com",
                "12345678901",
                "12345678901",
                1000.0,
            )

        given(userService.createUser(user)).willReturn(user)

        val objectMapper = ObjectMapper()
        val json = objectMapper.writeValueAsString(userDto)

        mockMvc.perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json),
        )
            .andExpect(status().isCreated)
    }

    @Test
    fun `getAllUsers should return list of users`() {
        val users =
            listOf(
                User(1L, "John Doe", "john@example.com", "123456789", "1234567890", 1000.0),
                User(
                    2L,
                    "Jane Doe",
                    "jane@example.com",
                    "987654321",
                    "0987" +
                        "654321",
                    2000.0,
                ),
            )

        given(userService.getAllUsers()).willReturn(users)

        mockMvc.perform(get("/api/v1/users"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.size()").value(users.size))
            .andExpect(jsonPath("$[0].id").value(users[0].id))
            .andExpect(jsonPath("$[0].name").value(users[0].name))
            .andExpect(jsonPath("$[0].email").value(users[0].email))
            .andExpect(jsonPath("$[0].cpf").value(users[0].cpf))
            .andExpect(jsonPath("$[0].phone").value(users[0].phone))
            .andExpect(jsonPath("$[0].balance").value(users[0].balance))
            .andExpect(jsonPath("$[1].id").value(users[1].id))
            .andExpect(jsonPath("$[1].name").value(users[1].name))
            .andExpect(jsonPath("$[1].email").value(users[1].email))
            .andExpect(jsonPath("$[1].cpf").value(users[1].cpf))
            .andExpect(jsonPath("$[1].phone").value(users[1].phone))
            .andExpect(jsonPath("$[1].balance").value(users[1].balance))
    }

    @Test
    fun `getUserBalance should return user balance`() {
        val user =
            User(
                1L,
                "John Doe",
                "john@example.com",
                "123456789",
                "" +
                    "1234567890",
                1000.0,
            )

        given(userService.getUserBalance(1L)).willReturn(user.balance)

        mockMvc.perform(get("/api/v1/users/1/balance"))
            .andExpect(status().isOk)
            .andExpect(content().string(user.balance.toString()))
    }
}
