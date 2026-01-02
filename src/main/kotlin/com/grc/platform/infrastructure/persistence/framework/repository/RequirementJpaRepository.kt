package com.grc.platform.infrastructure.persistence.framework.repository

import com.grc.platform.infrastructure.persistence.framework.entity.RequirementJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface RequirementJpaRepository : JpaRepository<RequirementJpaEntity, UUID> {
    fun findByCategoryIdOrderByDisplayOrder(categoryId: UUID): List<RequirementJpaEntity>
    fun findByCategoryIdInOrderByDisplayOrder(categoryIds: List<UUID>): List<RequirementJpaEntity>
}
