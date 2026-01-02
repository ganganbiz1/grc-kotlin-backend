package com.grc.platform.infrastructure.persistence.framework.repository

import com.grc.platform.infrastructure.persistence.framework.entity.FrameworkControlJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface FrameworkControlJpaRepository : JpaRepository<FrameworkControlJpaEntity, UUID> {
    fun findByRequirementIdOrderByDisplayOrder(requirementId: UUID): List<FrameworkControlJpaEntity>
    fun findByRequirementIdInOrderByDisplayOrder(requirementIds: List<UUID>): List<FrameworkControlJpaEntity>
    fun findByFrameworkVersionId(frameworkVersionId: UUID): List<FrameworkControlJpaEntity>
    fun findByCanonicalKey(canonicalKey: String): FrameworkControlJpaEntity?
}
