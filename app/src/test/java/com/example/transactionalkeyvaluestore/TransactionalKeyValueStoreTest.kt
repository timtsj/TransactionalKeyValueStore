package com.example.transactionalkeyvaluestore

import com.example.transactionalkeyvaluestore.TransactionalKeyValueStore.Companion.KEY_NOT_SET
import com.example.transactionalkeyvaluestore.TransactionalKeyValueStore.Companion.NO_TRANSACTION
import org.junit.Assert.assertEquals
import org.junit.Test

class TransactionalKeyValueStoreTest {

    @Test
    fun set_and_get_a_value() {
        with(TransactionalKeyValueStore()) {
            set("foo", "123")
            assertEquals("123", get("foo"))
        }
    }

    @Test
    fun delete_value() {
        with(TransactionalKeyValueStore()) {
            set("foo", "123")
            delete("foo")
            assertEquals(KEY_NOT_SET, get("foo"))
        }
    }

    @Test
    fun count_the_number_of_occurrences_of_a_value() {
        with(TransactionalKeyValueStore()) {
            set("foo", "123")
            set("bar", "456")
            set("baz", "123")
            assertEquals(2, count("123"))
            assertEquals(1, count("456"))
        }
    }

    @Test
    fun commit_a_transaction() {
        with(TransactionalKeyValueStore()) {
            beginTransaction()
            set("foo", "456")
            commitTransaction()
            assertEquals(NO_TRANSACTION, rollbackTransaction())
            assertEquals("456", get("foo"))
        }
    }

    @Test
    fun rollback_a_transaction() {
        with(TransactionalKeyValueStore()) {
            set("foo", "123")
            set("bar", "abc")
            beginTransaction()
            set("foo", "456")
            assertEquals("456", get("foo"))

            set("bar", "def")
            assertEquals("def", get("bar"))

            rollbackTransaction()
            assertEquals("123", get("foo"))
            assertEquals("abc", get("bar"))
            assertEquals(NO_TRANSACTION, commitTransaction())
        }
    }

    @Test
    fun rollback_nested_transactions() {
        with(TransactionalKeyValueStore()) {
            set("foo", "123")
            beginTransaction()
            set("foo", "456")
            beginTransaction()
            set("foo", "789")
            assertEquals("789", get("foo"))

            rollbackTransaction()
            assertEquals("456", get("foo"))

            rollbackTransaction()
            assertEquals("123", get("foo"))
        }
    }

    @Test
    fun commit_nested_transactions() {
        with(TransactionalKeyValueStore()) {
            set("foo", "123")
            beginTransaction()
            set("foo", "456")
            beginTransaction()
            set("foo", "789")
            assertEquals("789", get("foo"))

            commitTransaction()
            assertEquals("789", get("foo"))

            set("foo", "456")
            commitTransaction()
            assertEquals("456", get("foo"))
        }
    }
}
