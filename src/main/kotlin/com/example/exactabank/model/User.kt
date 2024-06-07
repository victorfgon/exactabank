package com.example.exactabank.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.UniqueConstraint

const val UNIQUE_EMAIL = "email"
const val UNIQUE_CPF = "cpf"
const val UNIQUE_PHONE = "phone"

@Entity
@Table(
    name = "bank_user",
    uniqueConstraints = [
        UniqueConstraint(columnNames = [UNIQUE_EMAIL]),
        UniqueConstraint(columnNames = [UNIQUE_CPF]),
        UniqueConstraint(columnNames = [UNIQUE_PHONE]),
    ],
)
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    val email: String,
    val cpf: String,
    val phone: String,
    var balance: Double = 0.0,
)
