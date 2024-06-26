package es.upsa.nutrivision.domain.usecase

class CalculateNutritionalGoalsUseCase {
    fun calculate(weight: Int, height: Int, age: Int, gender: String, activityLevel: String, goal: String): NutritionalGoals {
        val tmb = if (gender == "Male") {
            88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age)
        } else {
            447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age)
        }

        val tdee = when (activityLevel) {
            "Sedentary" -> tmb * 1.2
            "Somewhat active" -> tmb * 1.375
            "Active" -> tmb * 1.55
            "Very active" -> tmb * 1.725
            else -> tmb * 1.2
        }

        val finalCalories = when (goal) {
            "Lose weight" -> tdee * 0.8
            "Gain muscle mass" -> tdee * 1.2
            "Maintain weight" -> tdee
            else -> tdee
        }

        val proteinCalories = finalCalories * 0.25
        val fatCalories = finalCalories * 0.30
        val carbCalories = finalCalories * 0.45

        return NutritionalGoals(
            calories = finalCalories,
            proteins = proteinCalories / 4, // 1g de proteína = 4 kcal
            fats = fatCalories / 9, // 1g de grasa = 9 kcal
            carbs = carbCalories / 4 // 1g de carbohidrato = 4 kcal
        )
    }
}

data class NutritionalGoals(
    val calories: Double,
    val proteins: Double,
    val fats: Double,
    val carbs: Double
)