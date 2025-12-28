package com.grc.platform.domain.framework.model

import com.grc.platform.domain.shared.UUIDv7

@JvmInline
value class RequirementCategoryId(val value: String) {
    companion object {
        fun generate(): RequirementCategoryId = RequirementCategoryId(UUIDv7.generate().toString())
    }
}

data class RequirementCategory(
    val id: RequirementCategoryId,
    val name: String,
    val shorthand: String,
    val requirements: List<Requirement>
)
