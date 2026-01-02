package com.grc.platform.application.framework

import com.grc.platform.domain.framework.model.FrameworkId
import com.grc.platform.presentation.framework.model.FrameworkDetail

/**
 * Framework更新系サービスのインターフェース
 */
interface FrameworkCommandService {
    /**
     * 規格を新規作成する
     */
    fun createFramework(name: String, description: String?): FrameworkId

    /**
     * 規格を更新する
     */
    fun updateFramework(id: FrameworkId, name: String, description: String?): FrameworkDetail?

    /**
     * 規格を削除する
     */
    fun deleteFramework(id: FrameworkId): Boolean
}
