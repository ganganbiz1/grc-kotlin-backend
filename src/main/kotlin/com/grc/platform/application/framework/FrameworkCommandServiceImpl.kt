package com.grc.platform.application.framework

import com.grc.platform.domain.framework.model.Framework
import com.grc.platform.domain.framework.model.FrameworkId
import com.grc.platform.domain.framework.model.FrameworkVersion
import com.grc.platform.domain.framework.model.FrameworkVersionId
import com.grc.platform.domain.framework.model.FrameworkVersionStatus
import com.grc.platform.domain.framework.repository.FrameworkRepository
import com.grc.platform.presentation.framework.model.FrameworkDetail
import com.grc.platform.presentation.framework.model.FrameworkVersionSummary
import com.grc.platform.presentation.framework.model.VersionStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional
class FrameworkCommandServiceImpl(
    private val frameworkRepository: FrameworkRepository
) : FrameworkCommandService {

    override fun createFramework(name: String, description: String?): FrameworkId {
        val framework = Framework(
            id = FrameworkId.generate(),
            name = name,
            description = description
        )
        val saved = frameworkRepository.save(framework)
        return saved.id
    }

    override fun updateFramework(id: FrameworkId, name: String, description: String?): FrameworkDetail? {
        val existing = frameworkRepository.findById(id) ?: return null

        val updated = Framework(
            id = existing.id,
            name = name,
            description = description,
            versions = existing.versions
        )
        val saved = frameworkRepository.save(updated)
        return toFrameworkDetail(saved)
    }

    override fun deleteFramework(id: FrameworkId): Boolean {
        val existing = frameworkRepository.findById(id) ?: return false
        frameworkRepository.delete(existing.id)
        return true
    }

    override fun createVersion(
        frameworkId: FrameworkId,
        versionNumber: String,
        effectiveDate: LocalDate?
    ): FrameworkVersionId? {
        val framework = frameworkRepository.findById(frameworkId) ?: return null

        val newVersion = FrameworkVersion(
            id = FrameworkVersionId.generate(),
            frameworkId = frameworkId,
            version = versionNumber,
            status = FrameworkVersionStatus.DRAFT,
            effectiveDate = effectiveDate
        )

        val updatedFramework = Framework(
            id = framework.id,
            name = framework.name,
            description = framework.description,
            versions = framework.versions + newVersion
        )

        frameworkRepository.save(updatedFramework)
        return newVersion.id
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
