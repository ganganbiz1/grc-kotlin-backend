package com.grc.platform.infrastructure.persistence.framework.entity

import com.grc.platform.domain.framework.model.FrameworkId
import com.grc.platform.domain.framework.model.FrameworkVersion
import com.grc.platform.domain.framework.model.FrameworkVersionId
import com.grc.platform.domain.framework.model.FrameworkVersionStatus
import com.grc.platform.domain.framework.model.RequirementCategory
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "framework_versions")
class FrameworkVersionJpaEntity(
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    val id: UUID,

    @Column(name = "framework_id", nullable = false, columnDefinition = "uuid")
    val frameworkId: UUID,

    @Column(name = "version", nullable = false, length = 50)
    val version: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    val status: FrameworkVersionStatus,

    @Column(name = "effective_date")
    val effectiveDate: LocalDate?,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant
) {
    fun toDomain(categories: List<RequirementCategory> = emptyList()): FrameworkVersion = FrameworkVersion(
        id = FrameworkVersionId(id),
        frameworkId = FrameworkId(frameworkId),
        version = version,
        status = status,
        effectiveDate = effectiveDate,
        categories = categories
    )

    companion object {
        fun fromDomain(domain: FrameworkVersion, now: Instant): FrameworkVersionJpaEntity = FrameworkVersionJpaEntity(
            id = domain.id.value,
            frameworkId = domain.frameworkId.value,
            version = domain.version,
            status = domain.status,
            effectiveDate = domain.effectiveDate,
            createdAt = now,
            updatedAt = now
        )

        fun fromDomain(domain: FrameworkVersion, createdAt: Instant, updatedAt: Instant): FrameworkVersionJpaEntity = FrameworkVersionJpaEntity(
            id = domain.id.value,
            frameworkId = domain.frameworkId.value,
            version = domain.version,
            status = domain.status,
            effectiveDate = domain.effectiveDate,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
