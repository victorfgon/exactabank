package com.example.exactabank.controller

import com.example.exactabank.dto.TransactionDto
import com.example.exactabank.model.Transaction
import com.example.exactabank.service.TransactionService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
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

@Api(tags = ["Transaction Management"])
@RestController
@RequestMapping("/api/v1/transactions")
@Validated
class TransactionController(private val transactionService: TransactionService) {
    @ApiOperation(value = "Create a new transaction")
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "Transaction created successfully"),
            ApiResponse(code = 400, message = "Invalid input"),
            ApiResponse(code = 500, message = "Internal server error"),
        ],
    )
    @PostMapping
    fun createTransaction(
        @Valid @RequestBody transactionDto: TransactionDto,
    ): ResponseEntity<Transaction> {
        val createdTransaction = transactionService.createTransaction(transactionDto)
        return ResponseEntity(createdTransaction, HttpStatus.CREATED)
    }

    @ApiOperation(value = "Get all transactions for a user")
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Transactions fetched successfully"),
            ApiResponse(code = 404, message = "User not found"),
            ApiResponse(code = 500, message = "Internal server error"),
        ],
    )
    @GetMapping("/user/{userId}")
    fun getUserTransactions(
        @PathVariable userId: Long,
    ): ResponseEntity<List<Transaction>> {
        val userTransactions = transactionService.getTransactions(userId)
        return ResponseEntity(userTransactions, HttpStatus.OK)
    }

    @ApiOperation(value = "Get a transaction by ID")
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Transaction fetched successfully"),
            ApiResponse(code = 404, message = "Transaction not found"),
            ApiResponse(code = 500, message = "Internal server error"),
        ],
    )
    @GetMapping("/{id}")
    fun getTransaction(
        @PathVariable id: Long,
    ): ResponseEntity<Transaction> {
        val transaction = transactionService.getTransactionById(id)
        return ResponseEntity(transaction, HttpStatus.OK)
    }
}
