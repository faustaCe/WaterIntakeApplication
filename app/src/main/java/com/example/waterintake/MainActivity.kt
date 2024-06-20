package com.example.waterintake

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.waterintake.ui.theme.WaterIntakeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WaterIntakeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WaterIntakeCalculator(modifier = Modifier.padding(innerPadding), context = this)
                }
            }
        }
    }
}

@Composable
fun WaterIntakeCalculator(modifier: Modifier = Modifier, context: Context) {
    val height = rememberSaveable { mutableStateOf("") }
    val weight = rememberSaveable { mutableStateOf("") }
    val waterIntake = rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ){
        Text(text = "Enter your height (cm):")
        Row {
            TextField(
                value = height.value,
                onValueChange = { height.value = it },
                label = { Text("Height") }
            )
        }
        Text(text = "Enter your weight (kg):")
        Row {
            TextField(
                value = weight.value,
                onValueChange = { weight.value = it },
                label = { Text("Weight") }
            )
        }
        Button(onClick = {
            val heightValue = height.value.toDoubleOrNull()
            val weightValue = weight.value.toDoubleOrNull()
            if (heightValue != null && weightValue != null) {
                val calculatedWaterIntake = calculateWaterIntake(heightValue, weightValue)
                waterIntake.value = "Your daily water intake is: $calculatedWaterIntake ml"

                // Save values to SharedPreferences
                val sharedPreferences = context.getSharedPreferences("water_intake_prefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("height", height.value)
                editor.putString("weight", weight.value)
                editor.apply()
            } else {
                waterIntake.value = "Invalid input"
            }
        }) {
            Text(text = "Calculate")
        }
        Text(text = waterIntake.value)
    }
}

private fun calculateWaterIntake(height: Double, weight: Double): Int {
    // formula to calculate daily water intake based on height and weight
    val waterIntake = (height * weight) / 30
    return waterIntake.toInt()
}

@Preview(showBackground = true)
@Composable
fun WaterIntakeCalculatorPreview() {
    WaterIntakeTheme {
        // Providing a dummy context for the preview. Replace with actual context in real usage.
        WaterIntakeCalculator(context = LocalContext.current)
    }
}
