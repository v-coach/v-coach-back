package com.example.foodaiserver

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class OpenAIService(
    private val objectMapper: ObjectMapper,
) {
    private val apiKey = System.getenv("OPENAI_API_KEY")

    private val client = RestClient.builder()
        .baseUrl("https://api.openai.com/v1")
        .defaultHeader("Authorization", "Bearer $apiKey")
        .defaultHeader("Content-Type", "application/json")
        .build()

    fun getAlternativeFoods(ingredient: String): List<SetListData> {
        val prompt = """
            사용자가 특정 재료 또는 성분을 피하려고 한다.

            피하려는 재료/성분: $ingredient

            해당 성분이 들어가지 않은 대체 가능한 음식 5개를 추천해라.

            응답은 반드시 JSON 배열만 반환해라.
            각 항목은 다음 필드만 가진다:
            - name: 음식 이름
            - content: 추천 이유

            다른 설명 문장은 절대 포함하지 마라.
        """.trimIndent()

        val requestBody = mapOf(
            "model" to "gpt-4.1-mini",
            "input" to prompt,
        )

        val response = client.post()
            .uri("/responses")
            .body(requestBody)
            .retrieve()
            .body(Map::class.java)

        val output = response?.get("output") as List<*>
        val firstOutput = output[0] as Map<*, *>
        val content = firstOutput["content"] as List<*>
        val firstContent = content[0] as Map<*, *>
        val text = firstContent["text"].toString()

        val listType = objectMapper.typeFactory
            .constructCollectionType(List::class.java, SetListData::class.java)

        return objectMapper.readValue(text, listType)
    }
}