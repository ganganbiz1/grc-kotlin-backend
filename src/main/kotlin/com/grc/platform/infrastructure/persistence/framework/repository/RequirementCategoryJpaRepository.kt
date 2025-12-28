package com.grc.platform.infrastructure.persistence.framework.repository

import com.grc.platform.infrastructure.persistence.framework.entity.RequirementCategoryJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RequirementCategoryJpaRepository : JpaRepository<RequirementCategoryJpaEntity, String> {
    fun findByFrameworkIdOrderByDisplayOrder(frameworkId: String): List<RequirementCategoryJpaEntity>
}
