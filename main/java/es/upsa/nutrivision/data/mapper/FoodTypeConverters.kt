package es.upsa.nutrivision.data.mapper


import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import es.upsa.nutrivision.data.model.Food
import es.upsa.nutrivision.data.model.Measure
import es.upsa.nutrivision.data.model.Nutrients
import es.upsa.nutrivision.data.model.QualifiedMeasure
import es.upsa.nutrivision.data.model.Qualifier

class FoodTypeConverters {

    private val gson = Gson()

    @TypeConverter
    fun fromNutrients(nutrients: Nutrients): String {
        return gson.toJson(nutrients)
    }

    @TypeConverter
    fun toNutrients(nutrientsString: String): Nutrients {
        val nutrientsType = object : TypeToken<Nutrients>() {}.type
        return gson.fromJson(nutrientsString, nutrientsType)
    }

    @TypeConverter
    fun fromMeasures(measures: List<Measure>?): String {
        return gson.toJson(measures)
    }

    @TypeConverter
    fun toMeasures(measuresString: String): List<Measure>? {
        val measuresType = object : TypeToken<List<Measure>>() {}.type
        return gson.fromJson(measuresString, measuresType)
    }

    @TypeConverter
    fun fromQualifiedMeasures(qualifiedMeasures: List<QualifiedMeasure>?): String {
        return gson.toJson(qualifiedMeasures)
    }

    @TypeConverter
    fun toQualifiedMeasures(qualifiedMeasuresString: String): List<QualifiedMeasure>? {
        val qualifiedMeasuresType = object : TypeToken<List<QualifiedMeasure>>() {}.type
        return gson.fromJson(qualifiedMeasuresString, qualifiedMeasuresType)
    }

    @TypeConverter
    fun fromQualifiers(qualifiers: List<Qualifier>?): String {
        return gson.toJson(qualifiers)
    }

    @TypeConverter
    fun toQualifiers(qualifiersString: String): List<Qualifier>? {
        val qualifiersType = object : TypeToken<List<Qualifier>>() {}.type
        return gson.fromJson(qualifiersString, qualifiersType)
    }

    @TypeConverter
    fun fromFood(food: Food): String {
        return gson.toJson(food)
    }

    @TypeConverter
    fun toFood(foodString: String): Food {
        val foodType = object : TypeToken<Food>() {}.type
        return gson.fromJson(foodString, foodType)
    }
}
