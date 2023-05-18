package com.andreypoltev.binchecker

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andreypoltev.binchecker.models.ResponseModel
import com.andreypoltev.binchecker.ui.theme.BINCheckerTheme
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.coroutineScope
import java.lang.Exception

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val cardNumber = remember {
                mutableStateOf("45717360")
            }

            val response = produceState(
                initialValue = ResponseModel(),
                producer = {
                    value = fetchData(cardNumber.value, this@MainActivity)
                })


            BINCheckerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(response)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(response: State<ResponseModel>) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Row {
            Card(
                Modifier
                    .weight(1 / 4f)
            ) {
                Column(Modifier.padding(4.dp)) {
                    Text(text = "NETWORK")
                    Text(
                        text = response.value.scheme,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.size(8.dp))

            Card(
                Modifier
                    .weight(1 / 4f)
            ) {
                Column(Modifier.padding(4.dp)) {
                    Text(text = "BRAND")
                    Text(
                        text = response.value.brand,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.size(8.dp))

            Card(
                Modifier
                    .weight(1 / 4f)
            ) {
                Column(Modifier.padding(4.dp)) {
                    Text(text = "TYPE")
                    Text(text = response.value.type, fontWeight = FontWeight.Bold, fontSize = 15.sp)

                }

            }

            Spacer(modifier = Modifier.size(8.dp))

            Card(
                Modifier
                    .weight(1 / 4f)
            ) {
                Column(Modifier.padding(4.dp)) {
                    Text(text = "PREPAID")
                    Text(
                        text = response.value.prepaid.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                }

            }

        }

        Spacer(modifier = Modifier.size(8.dp))

        Row {
            Card(
                Modifier
                    .weight(1 / 2f)
            ) {
                Column(Modifier.padding(4.dp)) {
                    Text(text = "CARD NUMBER")
                    Row {
                        Card {
                            Text(text = "LENGTH")
                            Text(
                                text = response.value.number.length.toString(),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.size(8.dp))

                        Card {
                            Text(text = "LUHN")
                            Text(
                                text = response.value.number.luhn.toString(),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.size(8.dp))

            Card(
                Modifier
                    .weight(1 / 2f)
            ) {
                Column(Modifier.padding(4.dp)) {
                    Text(text = "COUNTRY")
                    Row {
                        Text(text = response.value.country.emoji)
                        Text(text = response.value.country.name, fontWeight = FontWeight.Bold)
                    }
                    Text(text = "Latitude: ${response.value.country.latitude}")
                    Text(text = "Longitude: ${response.value.country.longitude}")
                }


            }

        }

        Spacer(modifier = Modifier.size(8.dp))

        Row {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(4.dp)) {
                    Text(text = "BANK")
                    Text(
                        text = response.value.bank.name,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = response.value.bank.city,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = response.value.bank.url,
                        fontWeight = FontWeight.Bold
                    )

                    Text(text = response.value.bank.phone)
                }
            }

        }

        Spacer(modifier = Modifier.size(8.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            var text by remember { mutableStateOf("") }
            OutlinedTextField(singleLine = true,
                value = text,
                onValueChange = { text = it },
                label = {
                    Text("Card Number")
                })
        }

        Spacer(modifier = Modifier.size(8.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = { TODO() }) {
                Text(text = "Request")

            }
        }

        Spacer(modifier = Modifier.size(8.dp))

        val list = listOf(
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
            "pizda",
        )

        LazyColumn(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            itemsIndexed(list) { index, item ->
                Text(text = item, fontSize = 24.sp, modifier = Modifier.padding(8.dp))
                if (index < list.lastIndex)
                    Divider()

            }

        }

    }
}

suspend fun fetchData(cardNumber: String, context: Context): ResponseModel {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    return try {
        client.get("${Links.BASE_URL}/$cardNumber").body()
    } catch (e: Exception) {
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        ResponseModel()
    }
}