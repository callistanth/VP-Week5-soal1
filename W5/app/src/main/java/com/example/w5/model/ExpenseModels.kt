package com.example.w5.model

import java.util.UUID

data class Expense (
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val amount: Long
)

data class ExpenseUiState(
    val expenses: List<Expense> = emptyList(),
    val nameInput: String = "",
    val amountInput: String = "",
    val amountError: String? = null,
    val editingExpenseId: String? = null
) {
    val totalExpense: Long
        get() = expenses.sumOf { it.amount }
}