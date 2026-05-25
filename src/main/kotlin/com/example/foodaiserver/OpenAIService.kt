package com.example.foodaiserver

import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class OpenAIService {

    private val apiKey =
        System.getenv("OPENAI_API_KEY")

    private val client = RestClient.builder()
        .baseUrl("https://api.openai.com/v1")
        .defaultHeader(
            "Authorization",
            "Bearer $apiKey"
        )
        .defaultHeader(
            "Content-Type",
            "application/json"
        )
        .build()

    fun getAlternativeFoodsJson(
        ingredient: String,
    ): String {

        val prompt = """
            사용자가 특정 재료 또는 성분을 피하려고 한다.

            피하려는 재료/성분:
            $ingredient

            일반 파스타를 대체할 수 있는 음식 위주로 추천해라.

            해당 성분이 들어가지 않은 대체 가능한 음식 5개를 추천해라.

            응답은 반드시 JSON 배열만 반환해라.

            형식:
            [
              {
                "name": "음식 이름",
                "content": "추천 이유"
              }
            ]

            다른 설명 절대 금지.
        """.trimIndent()

        val requestBody = mapOf(
            "model" to "gpt-5.5-mini",
            "input" to prompt
        )

        val response = client.post()
            .uri("/responses")
            .body(requestBody)
            .retrieve()
            .body(Map::class.java)

        val output =
            response?.get("output") as List<*>

        val firstOutput =
            output.first() as Map<*, *>

        val content =
            firstOutput["content"] as List<*>

        val firstContent =
            content.first() as Map<*, *>

        return firstContent["text"].toString()
    }
}