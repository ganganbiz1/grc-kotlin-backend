package com.grc.platform.infrastructure.persistence.framework.entity

import com.grc.platform.domain.framework.model.Framework
import com.grc.platform.domain.framework.model.FrameworkId
import com.grc.platform.domain.framework.model.RequirementCategory
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "frameworks")
class FrameworkJpaEntity(
    @Id
    @Column(name = "id", nullable = false, length = 36)
    val id: String,

    @Column(name = "display_name", nullable = false, length = 255)
    val displayName: String,

    @Column(name = "shorthand_name", nullable = false, length = 50)
    val shorthandName: String,

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    val description: String,

    @Column(name = "version", nullable = false, length = 50)
    val version: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant
) {
    fun toDomain(requirementCategories: List<RequirementCategory>): Framework = Framework(
        id = FrameworkId(id),
        displayName = displayName,
        shorthandName = shorthandName,
        description = description,
        version = version,
        requirementCategories = requirementCategories
    )

    companion object {
        fun fromDomain(domain: Framework, now: Instant): FrameworkJpaEntity = FrameworkJpaEntity(
            id = domain.id.value,
            displayName = domain.displayName,
            shorthandName = domain.shorthandName,
            description = domain.description,
            version = domain.version,
            createdAt = now,
            updatedAt = now
        )

        fun fromDomain(domain: Framework, createdAt: Instant, updatedAt: Instant): FrameworkJpaEntity = FrameworkJpaEntity(
            id = domain.id.value,
            displayName = domain.displayName,
            shorthandName = domain.shorthandName,
            description = domain.description,
            version = domain.version,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
