package com.example.filter.presentation.ui

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Calendar
import java.util.Date

@Composable
fun FilterScreen(
    navigateToMailList: (
        startDate: String,
        endDate: String,
        sender: String,
        receiver: String,
        subject: String) -> Unit,
    popBackStack: () -> Unit
) {
    var isStartDateClicked by remember { mutableStateOf(false) }
    var isEndDateClicked by remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var sender by remember { mutableStateOf("") }
    var receiver by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }

    val mContext = LocalContext.current

    val mYear: Int
    val mMonth: Int
    val mDay: Int

    val mCalendar = Calendar.getInstance()

    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    val mDatePickerDialog = DatePickerDialog(
        mContext, { _: DatePicker, year: Int, month: Int, mDayOfMonth: Int ->
            if (isStartDateClicked) {
                startDate = "$year-${month + 1}-${mDayOfMonth}T00:00:00"
                isStartDateClicked = false
            } else if (isEndDateClicked) {
                endDate = "$year-${month + 1}-${mDayOfMonth}T00:00:00"
                isEndDateClicked = false
            }
        }, mYear, mMonth, mDay
    )

    mDatePickerDialog.setOnCancelListener {
        isStartDateClicked = false
        isEndDateClicked = false
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = "Filter",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
                    .padding(end = 55.dp),
                style = MaterialTheme.typography.titleLarge,
            )
        }, backgroundColor = Color.White, contentColor = Color.Black, navigationIcon = {
            IconButton(onClick = {
                popBackStack()
            }, modifier = Modifier.padding(start = 0.dp), content = {
                Icon(
                    imageVector = Icons.Default.ArrowBack, contentDescription = null
                )
            })
        })
    }, content = { padding ->
        if (isStartDateClicked) {
            mDatePickerDialog.show()
        } else if (isEndDateClicked) {
            mDatePickerDialog.show()
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(
                    rememberScrollState()
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .clickable { isStartDateClicked = true }
                .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = startDate.ifEmpty { "Start date" }
                )
            }

            Divider(color = Color(0xFFE8E8E8), thickness = 1.dp)

            Row(modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .clickable { isEndDateClicked = true }
                .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = endDate.ifEmpty { "End date" }
                )
            }

            Divider(color = Color(0xFFE8E8E8), thickness = 1.dp)

            // Sender
            BaseTextField("Sender", sender, onTextChanged = {
                sender = it
            })

            BaseTextField("Receiver", receiver, onTextChanged = {
                receiver = it
            })

            BaseTextField("Subject", subject, onTextChanged = {
                subject = it
            })
        }
    }, bottomBar = {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Button(
                onClick = {
                    navigateToMailList(
                        startDate, endDate, sender, receiver, subject
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(MaterialTheme.shapes.medium),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5DB075), disabledContainerColor = Color.White
                )
            ) {
                Text(text = "Apply", color = Color.White)
            }
        }
    })
}