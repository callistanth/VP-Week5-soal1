package com.example.w5.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.w5.model.Expense
import com.example.w5.model.ExpenseUiState
import com.example.w5.viewmodel.ExpenseViewModel
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit



@Composable
fun ExpenseScreen (
    viewModel: ExpenseViewModel = viewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ExpenseContent(
        uiState = uiState,
        onNameChange = viewModel::onNameChange,
        onAmountChange = viewModel::onAmountChange,
        onSubmitExpense = viewModel::onSubmitExpense,
        onStartEdit = viewModel::onStartEdit,
        onDeleteExpense = viewModel::onDeleteExpense
    )
}




@Composable
fun ExpenseContent(
    uiState: ExpenseUiState,
    onNameChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onSubmitExpense: () -> Unit,
    onStartEdit: (Expense) -> Unit,
    onDeleteExpense: (Expense) -> Unit
){
    val rupiahFormat = NumberFormat.getCurrencyInstance(Locale("in", "ID")).apply { maximumFractionDigits = 0 }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                    containerColor = Color.White,
                    shape = RoundedCornerShape(50.dp)
            ) {
                Text("=",
                    fontSize = 20.sp, fontWeight = FontWeight.Bold
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Start
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Expenses",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text("Total Expense: ${rupiahFormat.format(uiState.totalExpense).replace(",00", "")}",
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            OutlinedTextField(
                value = uiState.nameInput,
                onValueChange = onNameChange,
                label = { Text("Expense Name")},
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = uiState.amountInput,
                    onValueChange = onAmountChange,
                    label = {Text("Amount of Expense")},
                    singleLine = true,
                    isError = uiState.amountError != null,
                    supportingText = {
                        uiState.amountError?.let {
                            Text(text = it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = onSubmitExpense,
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFFE2E4F6), shape = RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Submit Expense",
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.expenses, key = { it.id }) { expense ->
                    ExpenseItem(
                        expense = expense,
                        formattedAmount = rupiahFormat.format(expense.amount).replace(",00" , ""),
                        onEdit = { onStartEdit(expense) },
                        onDelete = { onDeleteExpense(expense) }
                    )
                }
            }
        }
    }
}




@Composable
fun ExpenseItem(
    expense: Expense,
    formattedAmount: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit
){
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8ECF8))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column() {
                Text("Name: ${expense.name}",
                    fontWeight = FontWeight.Bold,
                    fontSize =  14.sp
                )
                Text(text = formattedAmount,
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }

            Row() {
                IconButton(
                    onClick = onEdit
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Expense",
                        tint = Color(0xFFD3A13B)
                    )
                }
                IconButton(
                    onClick = onDelete
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Expense",
                        tint = Color(0xFFC62828)
                    )
                }
            }
        }
    }
}




@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ExpenseContentPreview(){
    ExpenseContent(
        uiState = ExpenseUiState (
            expenses = listOf(
                Expense(name = "Makan Nasi", amount = 90000),
                Expense(name = "Investasi", amount = 10000000),
                Expense(name = "Kopi", amount = 18000)
            )
        ),
        onNameChange = {},
        onAmountChange = {},
        onSubmitExpense = {},
        onStartEdit = {},
        onDeleteExpense = {}
    )
}

