package com.fsp.coreserver.repository

import com.fsp.coreserver.conversation.summary.SumarryRepository
import com.fsp.coreserver.conversation.summary.Summary
import com.fsp.coreserver.user.User
import jakarta.persistence.EntityManagerFactory
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.hibernate.SessionFactory
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
@DataJpaTest
@Transactional
class SummaryRepositoryTest(

    @Autowired val summaryRepository: SumarryRepository,
    @Autowired val entityManagerFactory: EntityManagerFactory

) {

    @Test
    fun `Summary 조회 시 User N+1 발생`() {
        // given
        val user1 = User(
            email = "a@test.com",
            password = "pw",
            name = "A"
        )
        val user2 = User(
            email = "b@test.com",
            password = "pw",
            name = "B"
        )

        val em = entityManagerFactory.createEntityManager()
        em.transaction.begin()
        em.persist(user1)
        em.persist(user2)

        em.persist(Summary(content = "요약1", user = user1))
        em.persist(Summary(content = "요약2", user = user2))
        em.transaction.commit()
        em.clear()
        //when
        val stats = entityManagerFactory
            .unwrap(SessionFactory::class.java)
            .statistics

        stats.clear()
        stats.isStatisticsEnabled = true

        val summaries = summaryRepository.findAllWithUser()

        summaries.forEach {
            it.getUser().getName()
        }
        //then
        val queryCount = stats.prepareStatementCount
        assertThat(queryCount).isEqualTo(1)
        }

    }
