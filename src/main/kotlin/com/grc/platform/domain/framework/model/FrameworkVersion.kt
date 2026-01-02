package com.grc.platform.domain.framework.model

import com.grc.platform.domain.shared.UUIDv7
import java.time.LocalDate
import java.util.UUID

@JvmInline
value class FrameworkVersionId(val value: UUID) {
    companion object {
        fun generate(): FrameworkVersionId = FrameworkVersionId(UUIDv7.generate())
        fun fromString(value: String): FrameworkVersionId = FrameworkVersionId(UUID.fromString(value))
    }
}

enum class FrameworkVersionStatus {
    DRAFT,
    ACTIVE,
    ARCHIVED
}

/**
 * 規格の版管理
 */
class FrameworkVersion(
    val id: FrameworkVersionId,
    val frameworkId: FrameworkId,
    val version: String,
    val status: FrameworkVersionStatus,
    val effectiveDate: LocalDate?,
    val categories: List<RequirementCategory> = emptyList()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FrameworkVersion) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "FrameworkVersion(id=$id, version=$version)"
}
