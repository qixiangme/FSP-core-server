package com.fsp.coreserver.service

import com.fsp.coreserver.ai.ollama.AiService
import com.fsp.coreserver.poem.Poem
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest



@SpringBootTest(classes = [AiService::class])
@EnableAutoConfiguration(
    exclude = [
        DataSourceAutoConfiguration::class,
        HibernateJpaAutoConfiguration::class
    ]
)
class OllamaElaborateTest(
    @Autowired private val aiService: AiService
) {

    @Test
    fun `Ollama Elaborate - 시와 느낀점 기반 확장 테스트`() {
        // given
        val poem = Poem(
            title = "낙엽",
            author = "김현수",
            content = "바람에 흔들리며 떨어지는 낙엽 하나가 가을의 끝을 조용히 알린다."
        )

        val userFeeling = "왠지 모르게 혼자 남겨진 기분이 들었어요."

        val prompt = """
            [시]
            제목: ${poem.getTitle()}
            작가: ${poem.getAuthor()}
            본문: ${poem.getContent()}

            느낀점: $userFeeling
        """.trimIndent()

        // when
        val result = aiService.elaborate(prompt)

        // then
        assertTrue(result.isNotBlank())
        assertTrue(result.length > 20)

        println("=== Ollama Response ===")
        println(result)
    }
}
