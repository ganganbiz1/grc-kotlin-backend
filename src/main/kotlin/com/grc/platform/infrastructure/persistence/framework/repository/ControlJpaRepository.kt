package com.grc.platform.infrastructure.persistence.framework.repository

import com.grc.platform.infrastructure.persistence.framework.entity.ControlJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ControlJpaRepository : JpaRepository<ControlJpaEntity, String> {
    fun findByIdIn(ids: List<String>): List<ControlJpaEntity>
}
