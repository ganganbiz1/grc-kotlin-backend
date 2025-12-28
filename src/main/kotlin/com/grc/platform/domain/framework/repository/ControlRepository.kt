package com.grc.platform.domain.framework.repository

import com.grc.platform.domain.framework.model.Control
import com.grc.platform.domain.framework.model.ControlId

interface ControlRepository {
    fun findById(id: ControlId): Control?
    fun findAll(): List<Control>
    fun save(control: Control): Control
    fun delete(id: ControlId)
}
