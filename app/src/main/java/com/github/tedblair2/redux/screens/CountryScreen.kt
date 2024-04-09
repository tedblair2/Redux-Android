package com.github.tedblair2.redux.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.tedblair2.redux.CountriesViewModel
import com.github.tedblair2.redux.action.CountriesAction
import com.github.tedblair2.redux.model.CountryScreenState
import com.github.tedblair2.redux.model.DetailedCountry
import com.github.tedblair2.redux.model.SimpleCountry

@Composable
fun CountriesScreen(
    countryState:CountryScreenState,
    onEvent:(CountriesAction)->Unit={}
) {

    Box(modifier = Modifier.fillMaxSize()){
        if (countryState.isLoading){
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }else{
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(items = countryState.countries){country->
                    SingleCountry(simpleCountry = country,
                        modifier = Modifier
                            .padding(15.dp)
                            .clickable { onEvent(CountriesAction.SelectCountry(country.code))})
                }
            }
        }

        if (countryState.selectedCountry != null){
            DetailedCountryScreen(
                detailedCountry = countryState.selectedCountry,
                onDismiss = {onEvent(CountriesAction.DismissDialog)},
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.White)
                    .padding(16.dp))
        }
    }
}


@Composable
fun SingleCountry(
    modifier: Modifier = Modifier,
    simpleCountry: SimpleCountry
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text(text = simpleCountry.emoji, fontSize = 30.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center) {
            Text(text = simpleCountry.name, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = simpleCountry.capital)
        }
    }
}

@Composable
fun DetailedCountryScreen(
    modifier: Modifier=Modifier ,
    detailedCountry: DetailedCountry ,
    onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Column (modifier = modifier.fillMaxWidth()) {
            Row (modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically){
                Text(text = detailedCountry.emoji, fontSize = 30.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = detailedCountry.name, fontSize = 24.sp)
            }
            Text(text = "Continent: ${detailedCountry.continent}")
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Capital: ${detailedCountry.capital}")
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Currency: ${detailedCountry.currency}")
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Language(s): ${detailedCountry.languages}")
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}