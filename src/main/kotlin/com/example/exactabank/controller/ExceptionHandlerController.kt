package com.example.exactabank.controller

import com.example.exactabank.exception.InsufficientBalanceException
import com.example.exactabank.exception.InvalidTransactionException
import com.example.exactabank.exception.TransactionNotFoundException
import com.example.exactabank.exception.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandlerController {
    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(ex: UserNotFoundException): ResponseEntity<String> {
        return ResponseEntity("User not found", HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(InsufficientBalanceException::class)
    fun handleInsufficientBalanceException(ex: InsufficientBalanceException): ResponseEntity<String> {
        return ResponseEntity("Insufficient balance", HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InvalidTransactionException::class)
    fun handleInvalidTransactionException(ex: InvalidTransactionException): ResponseEntity<String> {
        return ResponseEntity("Invalid transaction", HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<String> {
        return ResponseEntity("An unexpected error occurred: ${ex.message}", HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String>> {
        val errors: MutableMap<String, String> = HashMap()
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.getDefaultMessage()
            errors[fieldName] = errorMessage!!
        }
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(TransactionNotFoundException::class)
    fun handleTransactionNotFoundException(ex: TransactionNotFoundException): ResponseEntity<String> {
        return ResponseEntity("Transaction not found", HttpStatus.NOT_FOUND)
    }
}
