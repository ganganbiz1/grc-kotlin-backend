package com.grc.platform.infrastructure.persistence.framework.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "requirement_controls")
class RequirementControlJpaEntity(
    @Id
    @Column(name = "id", nullable = false, length = 36)
    val id: String,

    @Column(name = "requirement_id", nullable = false, length = 36)
    val requirementId: String,

    @Column(name = "control_id", nullable = false, length = 36)
    val controlId: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant
) {
    companion object {
        fun create(id: String, requirementId: String, controlId: String, now: Instant): RequirementControlJpaEntity =
            RequirementControlJpaEntity(
                id = id,
                requirementId = requirementId,
                controlId = controlId,
                createdAt = now,
                updatedAt = now
            )
    }
}
