package es.upsa.nutrivision.data.api

import es.upsa.nutrivision.data.model.FoodResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodApi {

    @GET("api/food-database/v2/parser")
    suspend fun searchFood(
        @Query("ingr") query: String,
        @Query("app_id") appId: String = "37509826",
        @Query("app_key") appKey: String = "KEY"
    ): FoodResponse

    @GET("api/food-database/v2/parser")
    suspend fun searchFoodbyUPC(
        @Query("upc") query: String,
        @Query("app_id") appId: String = "37509826",
        @Query("app_key") appKey: String = "KEY"
    ): FoodResponse

    @GET("auto-complete")
    suspend fun autocomplete(
        @Query("q") query: String,
        @Query("app_id") appId: String = "37509826",
        @Query("app_key") appKey: String = "KEY"
    ): List<String>


}