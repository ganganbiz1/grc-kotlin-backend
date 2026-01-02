package com.grc.platform.domain.framework.model

import com.grc.platform.domain.shared.UUIDv7
import java.util.UUID

@JvmInline
value class FrameworkControlId(val value: UUID) {
    companion object {
        fun generate(): FrameworkControlId = FrameworkControlId(UUIDv7.generate())
        fun fromString(value: String): FrameworkControlId = FrameworkControlId(UUID.fromString(value))
    }
}

enum class MappingPolicy {
    AUTO_MIGRATE,
    MANUAL_REVIEW,
    DEPRECATED
}

/**
 * 規格上の実施項目定義
 * テナント非依存の定義情報のみを持つ
 */
class FrameworkControl(
    val id: FrameworkControlId,
    val requirementId: RequirementId,
    val frameworkVersionId: FrameworkVersionId,
    val canonicalKey: String,
    val displayCode: String,
    val title: String,
    val text: String?,
    val contentHash: String?,
    val mappingPolicy: MappingPolicy,
    val displayOrder: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FrameworkControl) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "FrameworkControl(id=$id, canonicalKey=$canonicalKey)"
}
