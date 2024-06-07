package com.example.exactabank.service

import com.example.exactabank.exception.UserNotFoundException
import com.example.exactabank.model.User
import com.example.exactabank.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.Optional

class UserServiceTest {
    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var userService: UserService

    init {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `createUser should save and return the user`() {
        val user = User(1L, "John Doe", "john@example.com", "123456789", "1234567890", 1000.0)
        `when`(userRepository.save(user)).thenReturn(user)

        val result = userService.createUser(user)

        assertNotNull(result)
        assertEquals(user.id, result.id)
        verify(userRepository, times(1)).save(user)
    }

    @Test
    fun `getAllUsers should return list of users`() {
        val users =
            listOf(
                User(
                    1L,
                    "John Doe",
                    "john@example.com",
                    "123456789",
                    "1234567890",
                    1000.0,
                ),
                User(
                    2L,
                    "Jane Doe",
                    "jane@example.com",
                    "987654321",
                    "0987654321",
                    2000.0,
                ),
            )
        `when`(userRepository.findAll()).thenReturn(users)

        val result = userService.getAllUsers()

        assertNotNull(result)
        assertEquals(2, result.size)
        verify(userRepository, times(1)).findAll()
    }

    @Test
    fun `getUserBalance should return user balance when user is found`() {
        val user =
            User(
                1L,
                "John Doe",
                "john@example.com",
                "123456789",
                "1234567890",
                1000.0,
            )
        `when`(userRepository.findById(1L)).thenReturn(Optional.of(user))

        val result = userService.getUserBalance(1L)

        assertEquals(1000.0, result)
        verify(userRepository, times(1)).findById(1L)
    }

    @Test
    fun `getUserBalance should throw UserNotFoundException when user is not found`() {
        `when`(userRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<UserNotFoundException> {
            userService.getUserBalance(1L)
        }

        verify(userRepository, times(1)).findById(1L)
    }
}
