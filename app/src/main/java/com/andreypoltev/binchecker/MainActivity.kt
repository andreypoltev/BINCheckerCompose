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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val scope = rememberCoroutineScope()

            val cardNumber = remember {
                mutableStateOf("45717360")
            }

            val response = remember {
                mutableStateOf(ResponseModel())
            }


//            val response = produceState(
//                initialValue = ResponseModel(),
//                producer = {
//                    value = fetchData(cardNumber.value, this@MainActivity)
//                })


            BINCheckerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(cardNumber, response, this) {
                        scope.launch {
                            fetchData(cardNumber.value, this@MainActivity, response)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    cardNumber: MutableState<String>,
    response: MutableState<ResponseModel>,
    context: Context,
    onClickRequest: () -> Unit
) {


//    val responseModel = response.value

//    val response = produceState(
//        initialValue = ResponseModel(),
//        producer = {
//            value = fetchData(cardNumber.value, context)
//        })

    val responseModel = response.value



    Column(
        Modifier
            .fillMaxSize()
            .padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Card(
                Modifier
                    .weight(1 / 4f)
            ) {
                Column(Modifier.padding(4.dp)) {
                    Text(text = "NETWORK")
                    Text(
                        text = responseModel.scheme ?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }

            Card(
                Modifier
                    .weight(1 / 4f)
            ) {
                Column(Modifier.padding(4.dp)) {
                    Text(text = "BRAND")
                    Text(
                        text = responseModel.brand ?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }

            Card(
                Modifier
                    .weight(1 / 4f)
            ) {
                Column(Modifier.padding(4.dp)) {
                    Text(text = "TYPE")
                    Text(
                        text = responseModel.type ?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

            }

            Card(
                Modifier
                    .weight(1 / 4f)
            ) {
                Column(Modifier.padding(4.dp)) {
                    Text(text = "PREPAID")
                    Text(
                        text = responseModel.prepaid.toString() ?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                }

            }

        }


        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
                                text = responseModel.number?.length.toString() ?: "",
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.size(8.dp))

                        Card {
                            Text(text = "LUHN")
                            Text(
                                text = responseModel.number?.luhn.toString() ?: "",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }


            Card(
                Modifier
                    .weight(1 / 2f)
            ) {
                Column(Modifier.padding(4.dp)) {
                    Text(text = "COUNTRY")
                    Row {
                        Text(text = responseModel.country?.emoji ?: "")
                        Text(text = responseModel.country?.name ?: "", fontWeight = FontWeight.Bold)
                    }
                    Text(text = "Latitude: ${responseModel.country?.latitude ?: ""}")
                    Text(text = "Longitude: ${responseModel.country?.longitude ?: ""}")
                }


            }

        }


        Row {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(4.dp)) {
                    Text(text = "BANK")
                    Text(
                        text = responseModel.bank?.name ?: "",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = responseModel.bank?.city ?: "",
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = responseModel.bank?.url ?: "",
                        fontWeight = FontWeight.Bold
                    )

                    Text(text = responseModel.bank?.phone ?: "")
                }
            }

        }

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {


            OutlinedTextField(singleLine = true,
                value = cardNumber.value,
                onValueChange = { cardNumber.value = it },
                label = {
                    Text("Card Number")
                })
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = { onClickRequest.invoke() }) {
                Text(text = "Request")

            }
        }


        val list = listOf(

            "text",
            "text",
            "text",
            "text",
            "text",
            "text",
            "text",
            "text",
            "text",
            "text",
            "text",
            "text",
            "text",
            "text",
            "text",
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

suspend fun fetchData(
    cardNumber: String,
    context: Context,
    responseModel: MutableState<ResponseModel>
): ResponseModel {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    val response = client.get("${Links.BASE_URL}/$cardNumber")

    when (response.status.value) {
        200 -> {
            Toast.makeText(context, "Data retrieved successfully.", Toast.LENGTH_SHORT)
                .show()
            responseModel.value = response.body()
            return response.body()
        }

        404 -> {
            Toast.makeText(context, "No matching card numbers are found.", Toast.LENGTH_SHORT)
                .show()
            responseModel.value = ResponseModel()
            return ResponseModel()
        }

        429 -> {
            Toast.makeText(context, "No more than 10 requests per minute.", Toast.LENGTH_SHORT)
                .show()
            responseModel.value = ResponseModel()
            return ResponseModel()
        }

        else -> {
            Toast.makeText(context, response.status.description, Toast.LENGTH_SHORT).show()
            responseModel.value = ResponseModel()
            return ResponseModel()

        }
    }
}