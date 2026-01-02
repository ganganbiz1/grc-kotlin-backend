package com.grc.platform.infrastructure.persistence.framework.repository

import com.grc.platform.infrastructure.persistence.framework.entity.RequirementCategoryJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface RequirementCategoryJpaRepository : JpaRepository<RequirementCategoryJpaEntity, UUID> {
    fun findByFrameworkVersionIdOrderByDisplayOrder(frameworkVersionId: UUID): List<RequirementCategoryJpaEntity>
}
