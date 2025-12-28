package com.grc.platform.infrastructure.persistence.framework.repository

import com.grc.platform.infrastructure.persistence.framework.entity.RequirementControlJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RequirementControlJpaRepository : JpaRepository<RequirementControlJpaEntity, String> {
    fun findByRequirementId(requirementId: String): List<RequirementControlJpaEntity>
    fun findByRequirementIdIn(requirementIds: List<String>): List<RequirementControlJpaEntity>
    fun deleteByRequirementId(requirementId: String)
}
