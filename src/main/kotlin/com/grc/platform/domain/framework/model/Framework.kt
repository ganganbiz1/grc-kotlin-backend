package com.grc.platform.domain.framework.model

import com.grc.platform.domain.shared.UUIDv7

@JvmInline
value class FrameworkId(val value: String) {
    companion object {
        fun generate(): FrameworkId = FrameworkId(UUIDv7.generate().toString())
    }
}

data class Framework(
    val id: FrameworkId,
    val displayName: String,
    val shorthandName: String,
    val description: String,
    val version: String,
    val requirementCategories: List<RequirementCategory>
)
