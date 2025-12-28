package com.grc.platform.infrastructure.persistence.framework.entity

import com.grc.platform.domain.framework.model.Control
import com.grc.platform.domain.framework.model.ControlId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "controls")
class ControlJpaEntity(
    @Id
    @Column(name = "id", nullable = false, length = 36)
    val id: String,

    @Column(name = "external_id", length = 255)
    val externalId: String?,

    @Column(name = "name", nullable = false, length = 255)
    val name: String,

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    val description: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant
) {
    fun toDomain(): Control = Control(
        id = ControlId(id),
        externalId = externalId,
        name = name,
        description = description
    )

    companion object {
        fun fromDomain(domain: Control, now: Instant): ControlJpaEntity = ControlJpaEntity(
            id = domain.id.value,
            externalId = domain.externalId,
            name = domain.name,
            description = domain.description,
            createdAt = now,
            updatedAt = now
        )

        fun fromDomain(domain: Control, createdAt: Instant, updatedAt: Instant): ControlJpaEntity = ControlJpaEntity(
            id = domain.id.value,
            externalId = domain.externalId,
            name = domain.name,
            description = domain.description,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
