package com.example.exactabank.repository

import com.example.exactabank.model.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository : JpaRepository<Transaction, Long> {
    fun findByUserId(userId: Long): List<Transaction>
}
