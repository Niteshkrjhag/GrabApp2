package com.example.first_app.Screen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun state(view_model: view_test_model) {
    // rememberSaveable saves data even after configuration changes
   // liveData  && view model concept are used to overcome the problem when we need to have
    // n number of variables. we cannot create all those variables in the screen itself
    // viewModel && livedata can be used to host the state for reusability

    // for creating viewModel file
//    class MyViewModel : ViewModel() {
//        private val _counter = MutableLiveData(0)
//        val counter: LiveData<Int> = _counter
//
//        fun incrementCounter() {
//            _counter.value = (_counter.value ?: 0) + 1
//        }
//    }


    // using viewmodel in activity
//    val viewModel: MyViewModel by viewModels()
//
//// Observe changes to the counter and update the UI
//    viewModel.counter.observe(viewLifecycleOwner) { newCount ->
//        // Update a TextView or other UI element with the new count
//    }
//
//// Call the ViewModel function to increment the counter
//    button.setOnClickListener {
//        viewModel.incrementCounter()
//    }

//    In Android development, a ViewModel is like a helpful assistant that holds and manages the data for your app's UI (User Interface).
    //    Think of it as a container that stores information your screens need to display and keeps it safe even when the screen rotates or is temporarily hidden.
//    Here's why ViewModels are awesome:
//    Survives Configuration Changes: When you rotate your phone, Android normally destroys and recreates your screen. This means any data you were showing could be lost. ViewModels prevent this by
    //    keeping the data alive and handing it back to the recreated screen.
//    Data Sharing: If multiple screens need the same information, a ViewModel can act as a central storage place, making it easy to share data without messy workarounds.
//    Testability: ViewModels are designed to be independent of the UI, making it easier to write tests for the logic that handles your app's data.
//
    val name2 by view_model.name.observeAsState(initial = "")
    val sur_name2 by view_model.sur_name.observeAsState(initial = "")

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Textt("$name2 $sur_name2")
        TextFieldd(name2, onNameChange = {
           view_model.setName(it)
        })
        Spacer(modifier = Modifier.height(20.dp))
        TextFieldd(sur_name2, onNameChange = {
            view_model.change_Surname(it)
        })
    }
}

@Composable
fun Textt(name: String) {
    Text(text = "Hello $name")
}

@Composable
fun TextFieldd(name3: String, onNameChange: (String) -> Unit) {

    OutlinedTextField(
        value = name3,
        onValueChange = {
            onNameChange(it)
        },
        label = {
            Text(text = "Enter your name")
        }
    )
}
