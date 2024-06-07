package com.example.exactabank.controller

import com.example.exactabank.dto.TransactionDto
import com.example.exactabank.enums.PixKeyType
import com.example.exactabank.enums.TransactionType
import com.example.exactabank.model.Transaction
import com.example.exactabank.model.User
import com.example.exactabank.service.TransactionService
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(TransactionController::class)
class TransactionControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var transactionService: TransactionService

    private val user = User(1L, "John Doe", "john@example.com", "12345678901", "12345678901", 1000.0)

    @Test
    fun `createTransaction should return created transaction`() {
        val transactionDto =
            TransactionDto(
                userId = user.id,
                type = TransactionType.DEPOSIT,
                amount = 100.0,
                receiverPixKey = "pixKey123",
                receiverPixKeyType = PixKeyType.EMAIL,
                phoneNumber = "12345678901",
                agencyNumber = "1234",
            )
        val transaction =
            Transaction(
                id = 1L,
                type = TransactionType.DEPOSIT,
                amount = 100.0,
                receiverPixKey = "pixKey123",
                receiverPixKeyType = PixKeyType.EMAIL,
                phoneNumber = "12345678901",
                agencyNumber = "1234",
                user = user,
            )

        given(transactionService.createTransaction(transactionDto)).willReturn(transaction)

        val objectMapper = ObjectMapper()
        val json = objectMapper.writeValueAsString(transactionDto)

        mockMvc.perform(
            post("/api/v1/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json),
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(transaction.id))
            .andExpect(jsonPath("$.type").value(transaction.type.name))
            .andExpect(jsonPath("$.amount").value(transaction.amount))
            .andExpect(jsonPath("$.receiverPixKey").value(transaction.receiverPixKey))
            .andExpect(jsonPath("$.receiverPixKeyType").value(transaction.receiverPixKeyType?.name))
            .andExpect(jsonPath("$.phoneNumber").value(transaction.phoneNumber))
            .andExpect(jsonPath("$.agencyNumber").value(transaction.agencyNumber))
            .andExpect(jsonPath("$.user.id").value(transaction.user.id))
    }

    @Test
    fun `getUserTransactions should return list of transactions`() {
        val userId = user.id
        val transactions =
            listOf(
                Transaction(
                    id = 1L,
                    type = TransactionType.DEPOSIT,
                    amount = 100.0,
                    receiverPixKey = "pixKey123",
                    receiverPixKeyType = PixKeyType.EMAIL,
                    phoneNumber = "12345678901",
                    agencyNumber = "1234",
                    user = user,
                ),
                Transaction(
                    id = 2L,
                    type = TransactionType.DEPOSIT,
                    amount = 200.0,
                    receiverPixKey = null,
                    receiverPixKeyType = null,
                    phoneNumber = "12345678901",
                    agencyNumber = "1234",
                    user = user,
                ),
            )

        given(transactionService.getTransactions(userId)).willReturn(transactions)

        mockMvc.perform(get("/api/v1/transactions/user/$userId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.size()").value(transactions.size))
            .andExpect(jsonPath("$[0].id").value(transactions[0].id))
            .andExpect(jsonPath("$[0].type").value(transactions[0].type.name))
            .andExpect(jsonPath("$[0].amount").value(transactions[0].amount))
            .andExpect(jsonPath("$[0].receiverPixKey").value(transactions[0].receiverPixKey))
            .andExpect(jsonPath("$[0].receiverPixKeyType").value(transactions[0].receiverPixKeyType?.name))
            .andExpect(jsonPath("$[0].phoneNumber").value(transactions[0].phoneNumber))
            .andExpect(jsonPath("$[0].agencyNumber").value(transactions[0].agencyNumber))
            .andExpect(jsonPath("$[0].user.id").value(transactions[0].user.id))
            .andExpect(jsonPath("$[1].id").value(transactions[1].id))
            .andExpect(jsonPath("$[1].type").value(transactions[1].type.name))
            .andExpect(jsonPath("$[1].amount").value(transactions[1].amount))
            .andExpect(jsonPath("$[1].receiverPixKey").value(transactions[1].receiverPixKey))
            .andExpect(jsonPath("$[1].receiverPixKeyType").value(transactions[1].receiverPixKeyType?.name))
            .andExpect(jsonPath("$[1].phoneNumber").value(transactions[1].phoneNumber))
            .andExpect(jsonPath("$[1].agencyNumber").value(transactions[1].agencyNumber))
            .andExpect(jsonPath("$[1].user.id").value(transactions[1].user.id))
    }

    @Test
    fun `getTransaction should return the transaction`() {
        val transactionId = 1L
        val transaction =
            Transaction(
                id = transactionId,
                type = TransactionType.DEPOSIT,
                amount = 100.0,
                receiverPixKey = "pixKey123",
                receiverPixKeyType = PixKeyType.EMAIL,
                phoneNumber = "12345678901",
                agencyNumber = "1234",
                user = user,
            )

        given(transactionService.getTransactionById(transactionId)).willReturn(transaction)

        mockMvc.perform(get("/api/v1/transactions/$transactionId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(transaction.id))
            .andExpect(jsonPath("$.type").value(transaction.type.name))
            .andExpect(jsonPath("$.amount").value(transaction.amount))
            .andExpect(jsonPath("$.receiverPixKey").value(transaction.receiverPixKey))
            .andExpect(jsonPath("$.receiverPixKeyType").value(transaction.receiverPixKeyType?.name))
            .andExpect(jsonPath("$.phoneNumber").value(transaction.phoneNumber))
            .andExpect(jsonPath("$.agencyNumber").value(transaction.agencyNumber))
            .andExpect(jsonPath("$.user.id").value(transaction.user.id))
    }
}
