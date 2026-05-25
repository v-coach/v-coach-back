package com.example.foodaiserver

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/alternative-foods")
class FoodController {

    @PostMapping
    fun getAlternativeFoods(
        @RequestBody request: IngredientRequest,
    ): List<FoodResponse> =
        listOf(
            FoodResponse(
                name = "햄버거",
                content = "${request.ingredient}을/를 피해야 할 때 단백질을 보충하기 좋은 음식입니다.",
            ),
        )
}

data class IngredientRequest(
    val ingredient: String,
)

data class FoodResponse(
    val name: String,
    val content: String,
)
