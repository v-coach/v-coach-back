package com.example.foodaiserver

import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import tools.jackson.databind.ObjectMapper

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
        if (apiKey.isNullOrBlank()) {
            return emptyList()
        }

        val prompt = """
            사용자가 특정 재료 또는 성분을 피하려고 한다.

            피하려는 재료/성분:
            $ingredient

            해당 성분이 들어가지 않은 대체 가능한 음식 5개를 추천해라.

            반드시 아래 JSON 형식의 배열만 반환해라.
            마크다운, 코드블록, 설명 문장은 절대 포함하지 마라.

            [
              {
                "name": "음식 이름",
                "content": "추천 이유"
              }
            ]
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
            ?: return emptyList()

        val output = response["output"] as? List<*> ?: return emptyList()
        val firstOutput = output.firstOrNull() as? Map<*, *> ?: return emptyList()
        val content = firstOutput["content"] as? List<*> ?: return emptyList()
        val firstContent = content.firstOrNull() as? Map<*, *> ?: return emptyList()
        val text = firstContent["text"]?.toString().orEmpty()

        if (text.isBlank()) {
            return emptyList()
        }

        val cleanedText = text
            .removePrefix("```json")
            .removePrefix("```")
            .removeSuffix("```")
            .trim()

        val listType = objectMapper.typeFactory
            .constructCollectionType(List::class.java, SetListData::class.java)

        return runCatching<List<SetListData>> {
            objectMapper.readValue(cleanedText, listType)
        }.getOrElse {
            emptyList()
        }
    }
}
