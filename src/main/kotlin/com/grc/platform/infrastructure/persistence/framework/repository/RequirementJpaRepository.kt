package com.grc.platform.infrastructure.persistence.framework.repository

import com.grc.platform.infrastructure.persistence.framework.entity.RequirementJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RequirementJpaRepository : JpaRepository<RequirementJpaEntity, String> {
    fun findByCategoryIdOrderByDisplayOrder(categoryId: String): List<RequirementJpaEntity>
    fun findByCategoryIdInOrderByDisplayOrder(categoryIds: List<String>): List<RequirementJpaEntity>
}
