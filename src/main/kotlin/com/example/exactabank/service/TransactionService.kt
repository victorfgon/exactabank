package com.example.exactabank.service

import com.example.exactabank.dto.TransactionDto
import com.example.exactabank.enums.PixKeyType
import com.example.exactabank.enums.TransactionType
import com.example.exactabank.exception.InsufficientBalanceException
import com.example.exactabank.exception.InvalidTransactionException
import com.example.exactabank.exception.TransactionNotFoundException
import com.example.exactabank.exception.UserNotFoundException
import com.example.exactabank.model.Transaction
import com.example.exactabank.repository.TransactionRepository
import com.example.exactabank.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TransactionService(
    @Autowired
    private val userRepository: UserRepository,
    @Autowired
    private val transactionRepository: TransactionRepository,
) {
    private val logger = LoggerFactory.getLogger(TransactionService::class.java)

    @Transactional
    fun createTransaction(transactionDto: TransactionDto): Transaction {
        logger.info("Creating transaction: $transactionDto")
        try {
            val user =
                userRepository.findById(transactionDto.userId).orElseThrow {
                    logger.error("User not found with ID: ${transactionDto.userId}")
                    UserNotFoundException()
                }

            when (transactionDto.type) {
                TransactionType.PIX -> {
                    if (user.balance < transactionDto.amount) {
                        logger.error("Insufficient balance for user ID: ${transactionDto.userId}")
                        throw InsufficientBalanceException()
                    }

                    val receiverPixKey = transactionDto.receiverPixKey ?: throw InvalidTransactionException()

                    val receiverUser =
                        when (transactionDto.receiverPixKeyType) {
                            PixKeyType.EMAIL -> userRepository.findByEmail(receiverPixKey)
                            PixKeyType.CPF -> userRepository.findByCpf(receiverPixKey)
                            PixKeyType.PHONE -> userRepository.findByPhone(receiverPixKey)
                            else -> throw InvalidTransactionException()
                        } ?: throw UserNotFoundException()

                    user.balance -= transactionDto.amount
                    receiverUser.balance += transactionDto.amount
                    userRepository.save(receiverUser)
                }

                TransactionType.RECHARGE -> {
                    if (user.balance < transactionDto.amount) throw InsufficientBalanceException()
                    if (transactionDto.phoneNumber == null) throw InvalidTransactionException()

                    user.balance -= transactionDto.amount
                }

                TransactionType.WITHDRAWAL -> {
                    if (user.balance < transactionDto.amount) throw InsufficientBalanceException()
                    if (transactionDto.agencyNumber == null) throw InvalidTransactionException()
                    user.balance -= transactionDto.amount
                }

                TransactionType.DEPOSIT -> user.balance += transactionDto.amount
            }

            userRepository.save(user)
            val transaction =
                Transaction(
                    type = transactionDto.type,
                    amount = transactionDto.amount,
                    receiverPixKey = transactionDto.receiverPixKey,
                    receiverPixKeyType = transactionDto.receiverPixKeyType,
                    phoneNumber = transactionDto.phoneNumber,
                    agencyNumber = transactionDto.agencyNumber,
                    user = user,
                )
            val savedTransaction = transactionRepository.save(transaction)
            logger.info("Transaction created successfully: $savedTransaction")
            return savedTransaction
        } catch (e: UserNotFoundException) {
            logger.error("User not found with ID: ${transactionDto.userId}", e)
            throw e
        } catch (e: InsufficientBalanceException) {
            logger.error("Insufficient balance for user ID: ${transactionDto.userId}", e)
            throw e
        } catch (e: InvalidTransactionException) {
            logger.error("Invalid transaction: $transactionDto", e)
            throw e
        } catch (e: Exception) {
            logger.error("An unexpected error occurred while creating the transaction", e)
            throw RuntimeException("An unexpected error occurred while creating the transaction", e)
        }
    }

    fun getTransactions(userId: Long): List<Transaction> {
        logger.info("Fetching transactions for user ID: $userId")
        return try {
            val user =
                userRepository.findById(userId).orElseThrow {
                    logger.error("User not found with ID: $userId")
                    UserNotFoundException()
                }
            val transactions = transactionRepository.findByUserId(userId)
            logger.info("Fetched ${transactions.size} transactions for user ID: $userId")
            transactions
        } catch (e: UserNotFoundException) {
            logger.error("User not found with ID: $userId", e)
            throw e
        } catch (e: Exception) {
            logger.error("An unexpected error occurred while fetching transactions for user ID: $userId", e)
            throw RuntimeException("An unexpected error occurred while fetching transactions", e)
        }
    }

    fun getTransactionById(id: Long): Transaction {
        logger.info("Fetching transaction with ID: $id")
        return try {
            val transaction =
                transactionRepository.findById(id).orElseThrow {
                    logger.error("Transaction not found with ID: $id")
                    TransactionNotFoundException()
                }
            logger.info("Fetched transaction: $transaction")
            transaction
        } catch (e: TransactionNotFoundException) {
            logger.error("Transaction not found with ID: $id", e)
            throw e
        } catch (e: Exception) {
            logger.error("An unexpected error occurred while fetching the transaction with ID: $id", e)
            throw RuntimeException("An unexpected error occurred while fetching the transaction", e)
        }
    }
}
