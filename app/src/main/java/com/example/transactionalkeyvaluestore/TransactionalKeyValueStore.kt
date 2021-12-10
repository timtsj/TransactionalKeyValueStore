package com.example.transactionalkeyvaluestore

import java.util.*
import kotlin.collections.HashMap

class TransactionalKeyValueStore {

    companion object {
        const val SUCCESS = "success"
        const val NO_TRANSACTION = "no transaction"
        const val KEY_NOT_SET = "key not set"
    }

    private val globalStore: HashMap<String, String> = HashMap()
    private val transactionsStack: Stack<Transaction> = Stack()

    operator fun set(key: String, value: String) {
        if (transactionsStack.isEmpty()) {
            globalStore[key] = value
        } else {
            transactionsStack.peek().transactionMap[key] = value
        }
    }

    operator fun get(key: String): String {
        return if (transactionsStack.isEmpty()) {
            globalStore.getOrDefault(key, KEY_NOT_SET)
        } else {
            transactionsStack.peek().transactionMap.getOrDefault(key, KEY_NOT_SET)
        }
    }

    fun delete(key: String) {
        if (transactionsStack.isEmpty()) {
            globalStore.remove(key)
        } else {
            transactionsStack.peek().transactionMap.remove(key)
        }
    }

    fun count(value: String): Int {
        return if (transactionsStack.isEmpty()) {
            globalStore.filter { it.value == value }.size
        } else {
            transactionsStack.peek().transactionMap.filter { it.value == value }.size
        }
    }

    fun beginTransaction() {
        transactionsStack.push(Transaction())
    }

    fun commitTransaction(): String {
        return if (transactionsStack.isEmpty()) {
            NO_TRANSACTION
        } else {
            transactionsStack.pop().transactionMap.forEach { (k, v) ->
                set(k, v)
            }
            SUCCESS
        }
    }

    fun rollbackTransaction(): String {
        return if (transactionsStack.isEmpty()) {
            NO_TRANSACTION
        } else {
            transactionsStack.pop()
            SUCCESS
        }
    }
}
