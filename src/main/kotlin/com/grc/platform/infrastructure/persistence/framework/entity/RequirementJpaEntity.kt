package com.grc.platform.infrastructure.persistence.framework.entity

import com.grc.platform.domain.framework.model.FrameworkControl
import com.grc.platform.domain.framework.model.Requirement
import com.grc.platform.domain.framework.model.RequirementCategoryId
import com.grc.platform.domain.framework.model.RequirementId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "requirements")
class RequirementJpaEntity(
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    val id: UUID,

    @Column(name = "category_id", nullable = false, columnDefinition = "uuid")
    val categoryId: UUID,

    @Column(name = "code", nullable = false, length = 50)
    val code: String,

    @Column(name = "title", nullable = false, length = 255)
    val title: String,

    @Column(name = "text", columnDefinition = "TEXT")
    val text: String?,

    @Column(name = "display_order", nullable = false)
    val displayOrder: Int,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant
) {
    fun toDomain(frameworkControls: List<FrameworkControl> = emptyList()): Requirement = Requirement(
        id = RequirementId(id),
        categoryId = RequirementCategoryId(categoryId),
        code = code,
        title = title,
        text = text,
        displayOrder = displayOrder,
        frameworkControls = frameworkControls
    )

    companion object {
        fun fromDomain(domain: Requirement, now: Instant): RequirementJpaEntity = RequirementJpaEntity(
            id = domain.id.value,
            categoryId = domain.categoryId.value,
            code = domain.code,
            title = domain.title,
            text = domain.text,
            displayOrder = domain.displayOrder,
            createdAt = now,
            updatedAt = now
        )

        fun fromDomain(domain: Requirement, createdAt: Instant, updatedAt: Instant): RequirementJpaEntity = RequirementJpaEntity(
            id = domain.id.value,
            categoryId = domain.categoryId.value,
            code = domain.code,
            title = domain.title,
            text = domain.text,
            displayOrder = domain.displayOrder,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
