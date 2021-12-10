package com.example.transactionalkeyvaluestore

data class Transaction(
    val transactionMap: HashMap<String, String> = hashMapOf()
)
