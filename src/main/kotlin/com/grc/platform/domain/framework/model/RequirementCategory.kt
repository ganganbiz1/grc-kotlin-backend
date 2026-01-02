package com.grc.platform.domain.framework.model

import com.grc.platform.domain.shared.UUIDv7
import java.util.UUID

@JvmInline
value class RequirementCategoryId(val value: UUID) {
    companion object {
        fun generate(): RequirementCategoryId = RequirementCategoryId(UUIDv7.generate())
        fun fromString(value: String): RequirementCategoryId = RequirementCategoryId(UUID.fromString(value))
    }
}

/**
 * 章・ドメイン・カテゴリ
 */
class RequirementCategory(
    val id: RequirementCategoryId,
    val frameworkVersionId: FrameworkVersionId,
    val parentId: RequirementCategoryId?,
    val name: String,
    val displayOrder: Int,
    val requirements: List<Requirement> = emptyList()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RequirementCategory) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "RequirementCategory(id=$id, name=$name)"
}
