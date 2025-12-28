package com.grc.platform.infrastructure.persistence.framework.entity

import com.grc.platform.domain.framework.model.Requirement
import com.grc.platform.domain.framework.model.RequirementCategory
import com.grc.platform.domain.framework.model.RequirementCategoryId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "requirement_categories")
class RequirementCategoryJpaEntity(
    @Id
    @Column(name = "id", nullable = false, length = 36)
    val id: String,

    @Column(name = "framework_id", nullable = false, length = 36)
    val frameworkId: String,

    @Column(name = "name", nullable = false, length = 255)
    val name: String,

    @Column(name = "shorthand", nullable = false, length = 50)
    val shorthand: String,

    @Column(name = "display_order", nullable = false)
    val displayOrder: Int,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant
) {
    fun toDomain(requirements: List<Requirement>): RequirementCategory = RequirementCategory(
        id = RequirementCategoryId(id),
        name = name,
        shorthand = shorthand,
        displayOrder = displayOrder,
        requirements = requirements
    )

    companion object {
        fun fromDomain(domain: RequirementCategory, frameworkId: String, now: Instant): RequirementCategoryJpaEntity = RequirementCategoryJpaEntity(
            id = domain.id.value,
            frameworkId = frameworkId,
            name = domain.name,
            shorthand = domain.shorthand,
            displayOrder = domain.displayOrder,
            createdAt = now,
            updatedAt = now
        )

        fun fromDomain(domain: RequirementCategory, frameworkId: String, createdAt: Instant, updatedAt: Instant): RequirementCategoryJpaEntity = RequirementCategoryJpaEntity(
            id = domain.id.value,
            frameworkId = frameworkId,
            name = domain.name,
            shorthand = domain.shorthand,
            displayOrder = domain.displayOrder,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
