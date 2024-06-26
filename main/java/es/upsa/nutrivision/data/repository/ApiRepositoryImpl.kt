package es.upsa.nutrivision.data.repository

import es.upsa.nutrivision.data.api.FoodApi
import es.upsa.nutrivision.domain.repository.ApiRepository
import javax.inject.Inject

class ApiRepositoryImpl @Inject constructor(
    private val foodApi: FoodApi
) : ApiRepository {



}