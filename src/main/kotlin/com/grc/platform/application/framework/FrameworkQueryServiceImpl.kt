package com.grc.platform.application.framework

import com.grc.platform.domain.framework.model.Framework
import com.grc.platform.domain.framework.model.FrameworkId
import com.grc.platform.domain.framework.repository.FrameworkRepository
import com.grc.platform.presentation.framework.model.FrameworkDetail
import com.grc.platform.presentation.framework.model.FrameworkSummary
import com.grc.platform.presentation.framework.model.FrameworkVersionSummary
import com.grc.platform.presentation.framework.model.VersionStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class FrameworkQueryServiceImpl(
    private val frameworkRepository: FrameworkRepository
) : FrameworkQueryService {

    override fun findAll(): List<FrameworkSummary> {
        return frameworkRepository.findAll().map { framework ->
            FrameworkSummary(
                id = framework.id.value,
                name = framework.name,
                description = framework.description
            )
        }
    }

    override fun findById(id: FrameworkId): FrameworkDetail? {
        val framework = frameworkRepository.findById(id) ?: return null
        return toFrameworkDetail(framework)
    }

    private fun toFrameworkDetail(framework: Framework): FrameworkDetail {
        return FrameworkDetail(
            id = framework.id.value,
            name = framework.name,
            description = framework.description,
            versions = framework.versions.map { version ->
                FrameworkVersionSummary(
                    id = version.id.value,
                    versionNumber = version.version,
                    status = VersionStatus.valueOf(version.status.name),
                    releaseDate = version.effectiveDate
                )
            }
        )
    }
}
