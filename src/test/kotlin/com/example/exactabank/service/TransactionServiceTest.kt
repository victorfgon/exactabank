package com.example.exactabank.service

import com.example.exactabank.dto.TransactionDto
import com.example.exactabank.enums.PixKeyType
import com.example.exactabank.enums.TransactionType
import com.example.exactabank.exception.InsufficientBalanceException
import com.example.exactabank.exception.TransactionNotFoundException
import com.example.exactabank.exception.UserNotFoundException
import com.example.exactabank.model.Transaction
import com.example.exactabank.model.User
import com.example.exactabank.repository.TransactionRepository
import com.example.exactabank.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.Optional

class TransactionServiceTest {
    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var transactionRepository: TransactionRepository

    @InjectMocks
    private lateinit var transactionService: TransactionService

    init {
        MockitoAnnotations.openMocks(this)
    }

    private val user =
        User(
            1L,
            "John Doe",
            "john@example.com",
            "12345678901",
            "12345678901",
            1000.0,
        )
    private val receiverUser =
        User(
            2L,
            "Jane Roe",
            "jane@example.com",
            "98765432101",
            "98765432101",
            500.0,
        )

    @Test
    fun `createTransaction should create PIX transaction successfully`() {
        val transactionDto =
            TransactionDto(
                userId = user.id,
                type = TransactionType.PIX,
                amount = 100.0,
                receiverPixKey = "jane@example.com",
                receiverPixKeyType = PixKeyType.EMAIL,
                phoneNumber = null,
                agencyNumber = null,
            )

        `when`(userRepository.findById(user.id)).thenReturn(Optional.of(user))
        `when`(userRepository.findByEmail("jane@example.com")).thenReturn(receiverUser)
        `when`(transactionRepository.save(any(Transaction::class.java))).thenAnswer { it.arguments[0] }

        val initialReceiverBalance = receiverUser.balance

        val transaction = transactionService.createTransaction(transactionDto)

        assertNotNull(transaction)
        assertEquals(receiverUser.balance, initialReceiverBalance + transactionDto.amount)
        verify(userRepository).save(receiverUser)
        verify(transactionRepository).save(any(Transaction::class.java))
    }

    @Test
    fun `createTransaction should throw InsufficientBalanceException for PIX transaction`() {
        val transactionDto =
            TransactionDto(
                userId = user.id,
                type = TransactionType.PIX,
                amount = 2000.0,
                receiverPixKey = "jane@example.com",
                receiverPixKeyType = PixKeyType.EMAIL,
                phoneNumber = null,
                agencyNumber = null,
            )

        `when`(userRepository.findById(user.id)).thenReturn(Optional.of(user))

        assertThrows<InsufficientBalanceException> {
            transactionService.createTransaction(transactionDto)
        }
    }

    @Test
    fun `getTransactions should return user transactions`() {
        val transactions =
            listOf(
                Transaction(
                    1L,
                    TransactionType.PIX,
                    100.0,
                    "pixKey123",
                    PixKeyType.EMAIL,
                    null,
                    null,
                    user,
                ),
                Transaction(
                    1L,
                    TransactionType.DEPOSIT,
                    200.0,
                    null,
                    null,
                    null,
                    null,
                    user,
                ),
            )

        `when`(userRepository.findById(user.id)).thenReturn(Optional.of(user))
        `when`(transactionRepository.findByUserId(user.id)).thenReturn(transactions)

        val result = transactionService.getTransactions(user.id)

        assertEquals(transactions.size, result.size)
        assertEquals(transactions[0].id, result[0].id)
        assertEquals(transactions[1].id, result[1].id)
    }

    @Test
    fun `getTransactions should throw UserNotFoundException`() {
        `when`(userRepository.findById(user.id)).thenReturn(Optional.empty())

        assertThrows<UserNotFoundException> {
            transactionService.getTransactions(user.id)
        }
    }

    @Test
    fun `getTransactionById should return transaction`() {
        val transaction =
            Transaction(
                1L,
                TransactionType.PIX,
                100.0,
                "pixKey123",
                PixKeyType.EMAIL,
                null,
                null,
                user,
            )

        `when`(transactionRepository.findById(transaction.id)).thenReturn(Optional.of(transaction))

        val result = transactionService.getTransactionById(transaction.id)

        assertNotNull(result)
        assertEquals(transaction.id, result.id)
        assertEquals(transaction.amount, result.amount)
    }

    @Test
    fun `getTransactionById should throw TransactionNotFoundException`() {
        `when`(transactionRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<TransactionNotFoundException> {
            transactionService.getTransactionById(1L)
        }
    }
}
