package com.example.exactabank.service

import com.example.exactabank.exception.UserNotFoundException
import com.example.exactabank.model.User
import com.example.exactabank.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService(
    @Autowired private val userRepository: UserRepository,
) {
    private val logger = LoggerFactory.getLogger(UserService::class.java)

    fun createUser(user: User): User {
        logger.info("Creating user: $user")
        return try {
            val savedUser = userRepository.save(user)
            logger.info("User created successfully: $savedUser")
            savedUser
        } catch (e: Exception) {
            logger.error("An unexpected error occurred while creating the user", e)
            throw RuntimeException("An unexpected error occurred while creating the user", e)
        }
    }

    fun getAllUsers(): List<User> {
        logger.info("Fetching all users")
        return try {
            val users = userRepository.findAll()
            logger.info("Fetched ${users.size} users")
            users
        } catch (e: Exception) {
            logger.error("An unexpected error occurred while fetching all users", e)
            throw RuntimeException("An unexpected error occurred while fetching all users", e)
        }
    }

    fun getUserBalance(userId: Long): Double {
        logger.info("Fetching balance for user with ID: $userId")
        return try {
            val user = userRepository.findById(userId).orElseThrow { UserNotFoundException() }
            logger.info("Fetched balance for user with ID: $userId")
            user.balance
        } catch (e: UserNotFoundException) {
            logger.error("User not found with ID: $userId", e)
            throw e
        } catch (e: Exception) {
            logger.error("An unexpected error occurred while fetching the user balance", e)
            throw RuntimeException("An unexpected error occurred while fetching the user balance", e)
        }
    }
}
