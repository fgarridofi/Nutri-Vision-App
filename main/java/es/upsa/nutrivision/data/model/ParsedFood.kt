package es.upsa.nutrivision.data.model

data class ParsedFood(
    val food: Food,
    val quantity: Float,
    val measure: Measure
)
