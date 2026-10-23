package com.example.w5.viewmodel

import androidx.lifecycle.ViewModel
import com.example.w5.model.Expense
import com.example.w5.model.ExpenseUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ExpenseViewModel(): ViewModel() {
    private val _uiState = MutableStateFlow(ExpenseUiState())
    val uiState: StateFlow<ExpenseUiState> = _uiState.asStateFlow()

    fun onNameChange(name: String) {
        _uiState.update { it.copy(nameInput = name) }
    }


    fun onAmountChange(amount: String){
        _uiState.update { currentState ->
            val errorMessage = if (currentState.amountError != null) validateAmount(amount) else null
            currentState.copy(amountInput = amount, amountError = errorMessage)
        }
    }

    private fun validateAmount(amountStr: String): String? {
        val amount = amountStr.toLongOrNull()
        return if (amount == null || amount <= 0){
            "Amount must be greater than 0"
        } else {
            null
        }
    }


    fun onSubmitExpense(){
        val currentState = _uiState.value
        val amountError = validateAmount(currentState.amountInput)

        if (amountError != null) {
            _uiState.update { it.copy(amountError = amountError) }
            return
        }

        if (currentState.nameInput.isBlank()) return

        val amount = currentState.amountInput.toLong()

        if (currentState.editingExpenseId != null){
            val updateExpense = currentState.expenses.map { expense ->
                if (expense.id == currentState.editingExpenseId) {
                    expense.copy(name = currentState.nameInput, amount = amount)
                } else {
                    expense
                }
            }
            _uiState.update {
                it.copy(
                    expenses = updateExpense,
                    nameInput = "",
                    amountInput = "",
                    amountError = null,
                    editingExpenseId = null
                )
            }
        } else {
            val newExpense = Expense(name = currentState.nameInput, amount = amount)
            _uiState.update {
                it.copy(
                    expenses = it.expenses + newExpense,
                    nameInput = "",
                    amountInput = "",
                    amountError = null
                )
            }
        }
    }


    fun onDeleteExpense(expense: Expense){
        _uiState.update { currentState ->
            currentState.copy(
                expenses = currentState.expenses.filter { it.id != expense.id }
            )
        }
    }



    fun onStartEdit(expense: Expense) {
        _uiState.update {
            it.copy(
                editingExpenseId = expense.id,
                nameInput = expense.name,
                amountInput = expense.amount.toString(),
                amountError = null
            )
        }
    }
}