package com.example.foodaiserver

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/alternative-foods")
class FoodController(
    private val openAIService: OpenAIService,
) {
    @PostMapping
    fun getAlternativeFoods(
        @RequestBody request: IngredientRequest,
    ): List<SetListData> {
        return openAIService.getAlternativeFoods(request.ingredient)
    }
}

data class IngredientRequest(
    val ingredient: String,
)

data class SetListData(
    val name: String,
    val content: String,
)