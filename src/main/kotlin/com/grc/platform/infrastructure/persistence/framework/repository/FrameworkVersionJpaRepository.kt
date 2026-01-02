package com.grc.platform.infrastructure.persistence.framework.repository

import com.grc.platform.infrastructure.persistence.framework.entity.FrameworkVersionJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface FrameworkVersionJpaRepository : JpaRepository<FrameworkVersionJpaEntity, UUID> {
    fun findByFrameworkIdOrderByCreatedAtDesc(frameworkId: UUID): List<FrameworkVersionJpaEntity>
}
