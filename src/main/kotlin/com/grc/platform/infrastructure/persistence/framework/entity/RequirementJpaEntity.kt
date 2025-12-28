package com.grc.platform.infrastructure.persistence.framework.entity

import com.grc.platform.domain.framework.model.Control
import com.grc.platform.domain.framework.model.Requirement
import com.grc.platform.domain.framework.model.RequirementId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "requirements")
class RequirementJpaEntity(
    @Id
    @Column(name = "id", nullable = false, length = 36)
    val id: String,

    @Column(name = "category_id", nullable = false, length = 36)
    val categoryId: String,

    @Column(name = "name", nullable = false, length = 255)
    val name: String,

    @Column(name = "shorthand", nullable = false, length = 50)
    val shorthand: String,

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    val description: String,

    @Column(name = "display_order", nullable = false)
    val displayOrder: Int,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant
) {
    fun toDomain(controls: List<Control>): Requirement = Requirement(
        id = RequirementId(id),
        name = name,
        shorthand = shorthand,
        description = description,
        displayOrder = displayOrder,
        controls = controls
    )

    companion object {
        fun fromDomain(domain: Requirement, categoryId: String, now: Instant): RequirementJpaEntity = RequirementJpaEntity(
            id = domain.id.value,
            categoryId = categoryId,
            name = domain.name,
            shorthand = domain.shorthand,
            description = domain.description,
            displayOrder = domain.displayOrder,
            createdAt = now,
            updatedAt = now
        )

        fun fromDomain(domain: Requirement, categoryId: String, createdAt: Instant, updatedAt: Instant): RequirementJpaEntity = RequirementJpaEntity(
            id = domain.id.value,
            categoryId = categoryId,
            name = domain.name,
            shorthand = domain.shorthand,
            description = domain.description,
            displayOrder = domain.displayOrder,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
