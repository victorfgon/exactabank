package com.example.exactabank.repository

import com.example.exactabank.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?

    fun findByCpf(cpf: String): User?

    fun findByPhone(phone: String): User?
}
