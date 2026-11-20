package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.MealCategory
import com.example.data.model.MealLogEntity
import com.example.ui.components.BotanicalCard
import com.example.ui.components.DateSelectorStrip
import com.example.ui.theme.CitrusAmber
import com.example.ui.theme.CitrusLight
import com.example.ui.theme.DangerRed
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ForestDeep
import com.example.ui.theme.HydrationBlue
import com.example.ui.theme.MintContainerLight
import com.example.viewmodel.NutriFitViewModel

@Composable
fun NutritionScreen(
    viewModel: NutriFitViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val selectedDateKey by viewModel.selectedDateKey.collectAsStateWithLifecycle()
    val meals by viewModel.mealsForSelectedDate.collectAsStateWithLifecycle()

    var activeCategoryFilter by remember { mutableStateOf<MealCategory?>(null) }
    var showAddMealDialog by remember { mutableStateOf(false) }
    var initialMealCategory by remember { mutableStateOf(MealCategory.BREAKFAST) }

    val totalCalories = meals.sumOf { it.calories }
    val calorieGoal = profile?.dailyCalorieGoal ?: 2000
    val totalProtein = meals.sumOf { it.proteinGrams }
    val totalCarbs = meals.sumOf { it.carbsGrams }
    val totalFat = meals.sumOf { it.fatGrams }

    val visibleMeals = if (activeCategoryFilter == null) meals else meals.filter { it.mealType == activeCategoryFilter!!.name }
    val filteredSuggestions = if (activeCategoryFilter == null) {
        viewModel.foodSuggestions
    } else {
        viewModel.foodSuggestions.filter { it.mealCategory == activeCategoryFilter }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Column {
                    Text(
                        text = "Daily Nutrition",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        text = "Record breakfast, lunch, dinner, snacks & healthy choices",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                DateSelectorStrip(
                    selectedDateKey = selectedDateKey,
                    onPreviousDay = { viewModel.stepDate(-1) },
                    onNextDay = { viewModel.stepDate(1) },
                    onJumpToday = { viewModel.selectDate(com.example.data.local.DateUtils.todayKey()) }
                )
            }

            // Calories & Macros Summary Card
            item {
                BotanicalCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column {
                                Text(
                                    text = "Calories Consumed",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "$totalCalories kcal",
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = EmeraldPrimary
                                )
                            }
                            Text(
                                text = "Goal: $calorieGoal kcal",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = { (totalCalories.toFloat() / calorieGoal).coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(CircleShape),
                            color = if (totalCalories > calorieGoal) CitrusAmber else EmeraldPrimary,
                            trackColor = MintContainerLight
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            MacroPill("Protein", "${totalProtein}g", EmeraldPrimary, MintContainerLight)
                            MacroPill("Carbs", "${totalCarbs}g", HydrationBlue, Color(0xFFE0F2FE))
                            MacroPill("Fats", "${totalFat}g", CitrusAmber, CitrusLight)
                        }
                    }
                }
            }

            // Meal Category Filter Chips
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        MealCategoryChip(
                            label = "All Meals",
                            selected = activeCategoryFilter == null,
                            onClick = { activeCategoryFilter = null }
                        )
                    }
                    items(MealCategory.entries) { category ->
                        MealCategoryChip(
                            label = category.displayName,
                            selected = activeCategoryFilter == category,
                            onClick = { activeCategoryFilter = category }
                        )
                    }
                }
            }

            // Logged Meals Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Logged Meals (${visibleMeals.size})",
                        style = MaterialTheme.typography.titleLarge
                    )
                    TextButton(
                        onClick = {
                            initialMealCategory = activeCategoryFilter ?: MealCategory.LUNCH
                            showAddMealDialog = true
                        }
                    ) {
                        Text("+ Add Entry", fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                    }
                }
            }

            if (visibleMeals.isEmpty()) {
                item {
                    BotanicalCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Restaurant,
                                contentDescription = null,
                                tint = EmeraldPrimary.copy(alpha = 0.4f),
                                modifier = Modifier.size(44.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No meals recorded for this view",
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Tap + Log Meal or choose a healthy food suggestion below.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(visibleMeals, key = { it.id }) { meal ->
                    MealRowCard(
                        meal = meal,
                        onDelete = { viewModel.deleteMeal(meal) }
                    )
                }
            }

            // Healthy Food Choices Carousel
            item {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Healthy Food Choices",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Tap + to quickly add nutritious meals to today's log",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(filteredSuggestions) { item ->
                        BotanicalCard(
                            modifier = Modifier.width(235.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MintContainerLight
                                    ) {
                                        Text(
                                            text = item.mealCategory.displayName,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ForestDeep,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                    Text(
                                        text = "${item.calories} kcal",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 13.sp,
                                        color = EmeraldPrimary
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = item.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    maxLines = 2
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = item.benefitTag,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = EmeraldPrimary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = item.description,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = { viewModel.logSuggestedFood(item) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                                ) {
                                    Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp), tint = Color.White)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Log Meal", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {
                initialMealCategory = activeCategoryFilter ?: MealCategory.BREAKFAST
                showAddMealDialog = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("add_meal_fab"),
            containerColor = EmeraldPrimary,
            contentColor = Color.White
        ) {
            Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Add, null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Log Meal", fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showAddMealDialog) {
        var foodName by remember { mutableStateOf("") }
        var caloriesStr by remember { mutableStateOf("350") }
        var proteinStr by remember { mutableStateOf("18") }
        var carbsStr by remember { mutableStateOf("40") }
        var fatStr by remember { mutableStateOf("10") }
        var selectedCat by remember { mutableStateOf(initialMealCategory) }
        var isHealthy by remember { mutableStateOf(true) }
        var notes by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddMealDialog = false },
            title = { Text("Record Meal", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        MealCategory.entries.forEach { cat ->
                            val selected = selectedCat == cat
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (selected) EmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.clickable { selectedCat = cat }
                            ) {
                                Text(
                                    text = cat.displayName,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = foodName,
                        onValueChange = { foodName = it },
                        label = { Text("Food / Meal Name") },
                        placeholder = { Text("E.g. Brown Rice & Grilled Fish") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("meal_name_input")
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = caloriesStr,
                            onValueChange = { caloriesStr = it },
                            label = { Text("Calories") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = proteinStr,
                            onValueChange = { proteinStr = it },
                            label = { Text("Prot (g)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = carbsStr,
                            onValueChange = { carbsStr = it },
                            label = { Text("Carbs (g)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = fatStr,
                            onValueChange = { fatStr = it },
                            label = { Text("Fat (g)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Healthy Food Choice", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        Switch(
                            checked = isHealthy,
                            onCheckedChange = { isHealthy = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = EmeraldPrimary)
                        )
                    }

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Notes (optional)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (foodName.isNotBlank()) {
                            viewModel.addMeal(
                                mealCategory = selectedCat,
                                foodName = foodName,
                                calories = caloriesStr.toIntOrNull() ?: 300,
                                protein = proteinStr.toIntOrNull() ?: 0,
                                carbs = carbsStr.toIntOrNull() ?: 0,
                                fat = fatStr.toIntOrNull() ?: 0,
                                isHealthyChoice = isHealthy,
                                notes = notes
                            )
                            showAddMealDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                    modifier = Modifier.testTag("save_meal_button")
                ) {
                    Text("Save Meal", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddMealDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun MacroPill(
    label: String,
    value: String,
    color: Color,
    bgColor: Color
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, fontSize = 11.sp, color = color, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.width(6.dp))
            Text(value, fontSize = 12.sp, color = color, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
private fun MealCategoryChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (selected) EmeraldPrimary else MaterialTheme.colorScheme.surface,
        border = if (!selected) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)) else null,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun MealRowCard(
    meal: MealLogEntity,
    onDelete: () -> Unit
) {
    BotanicalCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (meal.isHealthyChoice) MintContainerLight else CitrusLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (meal.isHealthyChoice) Icons.Default.Eco else Icons.Default.Restaurant,
                    contentDescription = null,
                    tint = if (meal.isHealthyChoice) EmeraldPrimary else CitrusAmber,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = meal.mealType.lowercase().replaceFirstChar { it.uppercase() },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary
                    )
                    if (meal.isHealthyChoice) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(shape = RoundedCornerShape(6.dp), color = MintContainerLight) {
                            Text(
                                text = "Healthy Choice",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = ForestDeep,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                            )
                        }
                    }
                }
                Text(
                    text = meal.foodName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = "${meal.calories} kcal • P ${meal.proteinGrams}g • C ${meal.carbsGrams}g • F ${meal.fatGrams}g",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (meal.notes.isNotBlank()) {
                    Text(
                        text = meal.notes,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete meal",
                    tint = DangerRed.copy(alpha = 0.7f)
                )
            }
        }
    }
}
