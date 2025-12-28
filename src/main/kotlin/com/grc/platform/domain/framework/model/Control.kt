package com.grc.platform.domain.framework.model

import com.grc.platform.domain.shared.UUIDv7

@JvmInline
value class ControlId(val value: String) {
    companion object {
        fun generate(): ControlId = ControlId(UUIDv7.generate().toString())
    }
}

class Control(
    val id: ControlId,
    val externalId: String?,
    val name: String,
    val description: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Control) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "Control(id=$id, name=$name)"
}
