package com.grc.platform.infrastructure.persistence.framework.entity

import com.grc.platform.domain.framework.model.FrameworkVersionId
import com.grc.platform.domain.framework.model.Requirement
import com.grc.platform.domain.framework.model.RequirementCategory
import com.grc.platform.domain.framework.model.RequirementCategoryId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "requirement_categories")
class RequirementCategoryJpaEntity(
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    val id: UUID,

    @Column(name = "framework_version_id", nullable = false, columnDefinition = "uuid")
    val frameworkVersionId: UUID,

    @Column(name = "parent_id", columnDefinition = "uuid")
    val parentId: UUID?,

    @Column(name = "name", nullable = false, length = 200)
    val name: String,

    @Column(name = "display_order", nullable = false)
    val displayOrder: Int,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant
) {
    fun toDomain(requirements: List<Requirement> = emptyList()): RequirementCategory = RequirementCategory(
        id = RequirementCategoryId(id),
        frameworkVersionId = FrameworkVersionId(frameworkVersionId),
        parentId = parentId?.let { RequirementCategoryId(it) },
        name = name,
        displayOrder = displayOrder,
        requirements = requirements
    )

    companion object {
        fun fromDomain(domain: RequirementCategory, now: Instant): RequirementCategoryJpaEntity = RequirementCategoryJpaEntity(
            id = domain.id.value,
            frameworkVersionId = domain.frameworkVersionId.value,
            parentId = domain.parentId?.value,
            name = domain.name,
            displayOrder = domain.displayOrder,
            createdAt = now,
            updatedAt = now
        )

        fun fromDomain(domain: RequirementCategory, createdAt: Instant, updatedAt: Instant): RequirementCategoryJpaEntity = RequirementCategoryJpaEntity(
            id = domain.id.value,
            frameworkVersionId = domain.frameworkVersionId.value,
            parentId = domain.parentId?.value,
            name = domain.name,
            displayOrder = domain.displayOrder,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
