package com.grc.platform.infrastructure.persistence.framework.adapter

import com.grc.platform.domain.framework.model.Control
import com.grc.platform.domain.framework.model.ControlId
import com.grc.platform.domain.framework.repository.ControlRepository
import com.grc.platform.infrastructure.persistence.framework.entity.ControlJpaEntity
import com.grc.platform.infrastructure.persistence.framework.repository.ControlJpaRepository
import org.springframework.stereotype.Repository
import java.time.Clock
import java.time.Instant

@Repository
class ControlRepositoryImpl(
    private val controlJpaRepository: ControlJpaRepository,
    private val clock: Clock
) : ControlRepository {

    override fun findById(id: ControlId): Control? {
        return controlJpaRepository.findById(id.value)
            .map { it.toDomain() }
            .orElse(null)
    }

    override fun findAll(): List<Control> {
        return controlJpaRepository.findAll().map { it.toDomain() }
    }

    override fun save(control: Control): Control {
        val now = Instant.now(clock)
        val existingEntity = controlJpaRepository.findById(control.id.value).orElse(null)

        val entity = if (existingEntity != null) {
            ControlJpaEntity.fromDomain(control, existingEntity.createdAt, now)
        } else {
            ControlJpaEntity.fromDomain(control, now)
        }

        return controlJpaRepository.save(entity).toDomain()
    }

    override fun delete(id: ControlId) {
        controlJpaRepository.deleteById(id.value)
    }
}
