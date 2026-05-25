package com.example.foodaiserver

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/alternative-foods")
class FoodController(
    private val openAIService: OpenAIService,
) {
    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAlternativeFoods(
        @RequestBody request: IngredientRequest,
    ): String {
        return openAIService.getAlternativeFoodsJson(request.ingredient)
    }
}

data class IngredientRequest(
    val ingredient: String,
)