package com.grc.platform.domain.framework.model

import com.grc.platform.domain.shared.UUIDv7

@JvmInline
value class ControlId(val value: String) {
    companion object {
        fun generate(): ControlId = ControlId(UUIDv7.generate().toString())
    }
}

data class Control(
    val id: ControlId,
    val externalId: String?,
    val name: String,
    val description: String
)
