package es.upsa.nutrivision.data.model


data class Food(
    val foodId: String,
    val label: String,
    val knownAs: String,
    val nutrients: Nutrients,
    val category: String,
    val categoryLabel: String,
    val image: String?,
    val measures: List<Measure>?
)
