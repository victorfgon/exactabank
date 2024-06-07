package com.example.exactabank.dto

import com.example.exactabank.enums.PixKeyType
import com.example.exactabank.enums.TransactionType
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive

data class TransactionDto(
    @field:NotNull val userId: Long,
    @field:NotNull val type: TransactionType,
    @field:Positive val amount: Double,
    val receiverPixKey: String?,
    val receiverPixKeyType: PixKeyType?,
    @field:Pattern(regexp = "\\d{11}", message = "Invalid phone format. Phone should have 11 digits.")
    val phoneNumber: String?,
    @field:Pattern(regexp = "\\d{4}", message = "Invalid agency format. Phone should have 4 digits.")
    val agencyNumber: String?,
)
