package com.github.tedblair2.redux

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.tedblair2.redux.action.AnotherCounterAction
import com.github.tedblair2.redux.action.CounterAction
import com.github.tedblair2.redux.screens.CountriesScreen
import com.github.tedblair2.redux.ui.theme.ReduxTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReduxTheme {
                val viewModel= hiltViewModel<AppViewModel>()
                val anotherViewModel= hiltViewModel<CounterViewModel>()
                val counterState by viewModel.counterState.collectAsState()
                val anotherCounterState by anotherViewModel.counterState.collectAsState()
                val countriesViewModel= hiltViewModel<CountriesViewModel>()
                val countryState by countriesViewModel.countryState.collectAsState()

                CountriesScreen(countryState = countryState,
                    onEvent = countriesViewModel::onEvent)

//                Column(modifier = Modifier
//                    .fillMaxSize(),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally) {
//
//                    Text(text = counterState.count.toString())
//                    Button(onClick = { viewModel.onEvent(CounterAction.Increment) }) {
//                        Text(text = "Increase by 1")
//                    }
//                    Button(onClick = { viewModel.onEvent(CounterAction.Decrement) }) {
//                        Text(text = "Decrease by 1")
//                    }
//                    Button(onClick = { viewModel.onEvent(CounterAction.LoadData) }) {
//                        Text(text = "Increase by 20")
//                    }
//
//                    Spacer(modifier = Modifier.height(100.dp))
//
//                    Text(text = anotherCounterState.count.toString())
//                    Button(onClick = { anotherViewModel.onEvent(AnotherCounterAction.Increment) }) {
//                        Text(text = "Increase by 1")
//                    }
//                    Button(onClick = { anotherViewModel.onEvent(AnotherCounterAction.Decrement) }) {
//                        Text(text = "Decrease by 1")
//                    }
//                    Button(onClick = { anotherViewModel.onEvent(AnotherCounterAction.LoadData) }) {
//                        Text(text = "Increase by 30")
//                    }
//                    Button(onClick = { anotherViewModel.onEvent(AnotherCounterAction.LoadNewData) }) {
//                        Text(text = "Increase by 50")
//                    }
//                }
            }
        }
    }
}