package com.grc.platform.domain.framework.repository

import com.grc.platform.domain.framework.model.Framework
import com.grc.platform.domain.framework.model.FrameworkId

interface FrameworkRepository {
    fun findById(id: FrameworkId): Framework?
    fun findAll(): List<Framework>
    fun save(framework: Framework): Framework
    fun delete(id: FrameworkId)
}
