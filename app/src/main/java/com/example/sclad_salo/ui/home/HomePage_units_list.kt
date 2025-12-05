package com.example.sclad_salo.ui.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.sclad_salo.models.UnitModel

@Composable
fun HomePage_units_list(

    onNavigateToDashboardPage: () -> Unit,
    navController : NavController,
    onNavigateToNofitications: (UnitModel) -> Unit,

    onNavigateToLoginPage: () -> Unit,

    viewModel: HomeViewModel = hiltViewModel()

) {

    val context = LocalContext.current

    val operatorCode by viewModel.operatorCode.collectAsState()

    var enteredCode by remember { mutableStateOf("") }

    val units by viewModel.units.collectAsState()


    Scaffold (

    ){paddingValues ->

        Column (
            modifier = Modifier
                .padding(paddingValues)
                .padding(vertical = 16.dp, horizontal = 12.dp)
                .fillMaxSize()
                .padding(6.dp)
        ){
            Spacer(modifier = Modifier.height(26.dp))
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ){
                Text(text = viewModel.currentUser?.email?:"No name")
                Button(onClick =  {
                    viewModel.signOut()
                    onNavigateToLoginPage()

                }){
                    Text("Log out")
                }


            }

            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = enteredCode,
                onValueChange = {enteredCode = it},
                label = {Text("Operators Code")},
                modifier = Modifier.fillMaxWidth()

            )

            Spacer(modifier = Modifier.height(4.dp))
            Button(
                onClick = {
                    if (enteredCode == operatorCode?.toString()){
                        onNavigateToDashboardPage

                    }else{
                        Toast.makeText(context,"Invalid code", Toast.LENGTH_SHORT).show()

                    }


                },
                modifier = Modifier.fillMaxWidth()

            ) {

                Text("Add New Unit")

            }

            Spacer(modifier = Modifier.height(6.dp))

            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 4.dp),
                contentPadding = PaddingValues(6.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)

            ){
                items(units){ unit_t ->
                    UnitItem(
                        unit = unit_t,
                        onNavigateToNofitications = onNavigateToNofitications,
                        onPinValidation = viewModel::isPinCodeValid


                    )

                }



            }


        }

    }


}

@Composable
fun UnitItem(
    unit: UnitModel,
    onNavigateToNofitications: (UnitModel) -> Unit,
    onPinValidation:(String)-> Boolean

){
    var showPinInput by remember { mutableStateOf(false) }
    var pinCode by remember { mutableStateOf("") }
    val context = LocalContext.current


    Card (
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)

    ){
        Column (modifier = Modifier.padding(16.dp)){
            AsyncImage(
                model = unit.image,
                contentDescription = unit.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop



            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = unit.name, style = MaterialTheme.typography.titleLarge)
            Text(text = "Price: ${unit.price}$", style = MaterialTheme.typography.labelLarge,
                fontSize = 18.sp, color = Color.Red)
            Text(text = "Count:${unit.count}", style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF0D2EE3)
            )
            Text(text = unit.comment, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedVisibility(visible = !showPinInput) {
                Button(
                    onClick = {showPinInput = true},
                    modifier = Modifier.align (Alignment.End )

                ) {
                    Text("Market Merch")

                }

            }

            AnimatedVisibility(visible = showPinInput) {
                Column (horizontalAlignment = Alignment.End){
                    OutlinedTextField(
                        value = pinCode,
                        onValueChange = {pinCode = it},
                        label = {Text("Sales Manager Code")},
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            if (onPinValidation(pinCode)){
                                onNavigateToNofitications(unit)
                                //Reset state
                                showPinInput = false
                                pinCode = ""


                            }else{
                                Toast.makeText(context,"Incorrect PIN", Toast.LENGTH_SHORT)

                            }

                        }


                    ) {

                    }



                }

            }




        }


    }

}