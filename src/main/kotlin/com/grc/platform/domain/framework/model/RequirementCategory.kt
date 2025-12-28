package com.grc.platform.domain.framework.model

import com.grc.platform.domain.shared.UUIDv7

@JvmInline
value class RequirementCategoryId(val value: String) {
    companion object {
        fun generate(): RequirementCategoryId = RequirementCategoryId(UUIDv7.generate().toString())
    }
}

class RequirementCategory(
    val id: RequirementCategoryId,
    val name: String,
    val shorthand: String,
    val requirements: List<Requirement>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RequirementCategory) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "RequirementCategory(id=$id, shorthand=$shorthand)"
}
