package com.grc.platform.domain.framework.model

import com.grc.platform.domain.shared.UUIDv7

@JvmInline
value class RequirementId(val value: String) {
    companion object {
        fun generate(): RequirementId = RequirementId(UUIDv7.generate().toString())
    }
}

data class Requirement(
    val id: RequirementId,
    val name: String,
    val shorthand: String,
    val description: String,
    val controls: List<Control>
)
