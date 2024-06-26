package es.upsa.nutrivision.data.model

data class FoodResponse(
    val text: String,
    val parsed: List<ParsedFood>,
    val hints: List<Hint>,
    val _links: Links?
)

data class Links(
    val next: NextLink?
)

data class NextLink(
    val title: String,
    val href: String
)