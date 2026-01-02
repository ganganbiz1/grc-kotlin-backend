package com.grc.platform.domain.framework.model

import com.grc.platform.domain.shared.UUIDv7
import java.util.UUID

@JvmInline
value class RequirementId(val value: UUID) {
    companion object {
        fun generate(): RequirementId = RequirementId(UUIDv7.generate())
        fun fromString(value: String): RequirementId = RequirementId(UUID.fromString(value))
    }
}

/**
 * 規格本文上の要求（意味単位）
 */
class Requirement(
    val id: RequirementId,
    val categoryId: RequirementCategoryId,
    val code: String,
    val title: String,
    val text: String?,
    val displayOrder: Int,
    val frameworkControls: List<FrameworkControl> = emptyList()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Requirement) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "Requirement(id=$id, code=$code)"
}
