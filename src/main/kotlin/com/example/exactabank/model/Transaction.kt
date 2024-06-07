package com.example.exactabank.model

import com.example.exactabank.enums.PixKeyType
import com.example.exactabank.enums.TransactionType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
data class Transaction(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val type: TransactionType,
    val amount: Double,
    val receiverPixKey: String? = null,
    val receiverPixKeyType: PixKeyType? = null,
    val phoneNumber: String? = null,
    val agencyNumber: String? = null,
    @ManyToOne @JoinColumn(name = "user_id")
    val user: User,
)
