package es.upsa.nutrivision.data.model

data class Measure(
    val uri: String,
    val label: String,
    val weight: Float,
    val qualified: List<QualifiedMeasure>?
)

data class QualifiedMeasure(
    val qualifiers: List<Qualifier>,
    val weight: Float
)

data class Qualifier(
    val uri: String,
    val label: String
)