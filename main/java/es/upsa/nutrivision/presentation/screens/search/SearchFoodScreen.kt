package es.upsa.nutrivision.presentation.screens.search

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter.*
import coil.compose.rememberAsyncImagePainter
import com.google.gson.Gson
import es.upsa.nutrivision.R
import es.upsa.nutrivision.data.model.Food
import es.upsa.nutrivision.ui.theme.CustomFontFamily

@Composable
fun SearchFoodScreen(navController: NavController, viewModel: SearchFoodViewModel = hiltViewModel()) {
    val query by viewModel.query.collectAsState()
    val foodList by viewModel.foodList.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        focusManager.clearFocus()
                    }
                )
            }
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.onQueryChanged(it) },
                placeholder = { Text("Insert Food Name",style = TextStyle(fontFamily = CustomFontFamily, fontWeight = FontWeight.Thin))},
                singleLine = true,
                label = { Text("Search Food",style = TextStyle(fontFamily = CustomFontFamily, fontWeight = FontWeight.Thin))},
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        if (!focusState.isFocused) {
                            viewModel.clearSuggestions()
                        }
                    },
                textStyle = TextStyle(fontFamily = CustomFontFamily, fontWeight = FontWeight.Normal),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.searchFood()
                        viewModel.clearSuggestions()
                        focusManager.clearFocus()
                    }
                ),
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = {
                            viewModel.searchFood()
                            viewModel.clearSuggestions()
                            focusManager.clearFocus()
                        }) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))



            if (query.isNotEmpty() && suggestions.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(4.dp)
                        .height(200.dp)
                ) {
                    items(suggestions) { suggestion ->
                        SuggestionItem(suggestion = suggestion) {
                            viewModel.onSuggestionClick(suggestion)
                            focusManager.clearFocus()
                        }
                    }
                }
            }

            Button(
                onClick = { navController.navigate("AddCustomFoodScreen") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Custom Food")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                viewModel.clearSuggestions()
                                focusManager.clearFocus()
                            }
                        )
                    }
            ) {
                items(foodList) { food ->
                    FoodItem(navController, food)
                }
            }
        }
    }
}

@Composable
fun FoodItem(navController: NavController, food: Food) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                val foodJson = Uri.encode(Gson().toJson(food))
                Log.d("FoodItem", "Food: $food")
                navController.navigate("addFood/$foodJson")
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            val painter = rememberAsyncImagePainter(
                model = food.image,
                error = painterResource(id = R.drawable.no_img),
            )

            Box(
                modifier = Modifier.size(64.dp)
            ) {
                Image(
                    painter = painter,
                    contentDescription = "food image",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(4.dp))
                )
                if (painter.state is State.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween

                )
                {
                    Text(
                        text = food.label,
                        style = TextStyle(fontFamily = CustomFontFamily, fontWeight = FontWeight.ExtraBold),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${String.format("%.0f", food.nutrients.ENERC_KCAL)} KCal/100g",
                        style = TextStyle(fontFamily = CustomFontFamily, fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Protein: ${String.format("%.1f",food.nutrients.PROCNT)}g", style = TextStyle(fontFamily = CustomFontFamily, fontWeight = FontWeight.Normal))
                        Text(text = "Fat: ${String.format("%.1f",food.nutrients.FAT)}g", style = TextStyle(fontFamily = CustomFontFamily, fontWeight = FontWeight.Normal))
                        Text(text = "Carbs: ${String.format("%.1f",food.nutrients.CHOCDF)}g", style = TextStyle(fontFamily = CustomFontFamily, fontWeight = FontWeight.Normal))
                    }
                    Button(
                        onClick = {
                            val foodJson = Uri.encode(Gson().toJson(food))
                            navController.navigate("addFood/$foodJson")
                        }
                    ) {
                        Text(text = "Add", style = TextStyle(fontFamily = CustomFontFamily, fontWeight = FontWeight.Bold))
                    }
                }
            }
        }
    }
}

@Composable
fun SuggestionItem(suggestion: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(text = suggestion, fontSize = 16.sp, color = Color.Black, style = TextStyle(fontFamily = CustomFontFamily, fontWeight = FontWeight.Normal))
        Divider(color = Color.LightGray)
    }
}
