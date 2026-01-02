-- =====================================================
-- V1: Framework境界（共通マスタ）
-- 規格カタログとして全テナントで共有されるテーブル群
-- =====================================================

-- 規格マスタ（SOC2、ISO27001、ISMAP等）
CREATE TABLE frameworks (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE frameworks IS '規格マスタ（SOC2、ISO27001、ISMAP等）';
COMMENT ON COLUMN frameworks.id IS 'UUID v7';
COMMENT ON COLUMN frameworks.name IS '規格名（例：SOC 2）';
COMMENT ON COLUMN frameworks.description IS '説明';

-- 規格の版管理
CREATE TABLE framework_versions (
    id UUID PRIMARY KEY,
    framework_id UUID NOT NULL REFERENCES frameworks(id) ,
    version VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    effective_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_framework_versions_status CHECK (status IN ('DRAFT', 'ACTIVE', 'ARCHIVED'))
);

COMMENT ON TABLE framework_versions IS '規格の版管理';
COMMENT ON COLUMN framework_versions.version IS '版名（例：2022、Type II）';
COMMENT ON COLUMN framework_versions.status IS 'DRAFT / ACTIVE / ARCHIVED';
COMMENT ON COLUMN framework_versions.effective_date IS '発効日';

CREATE INDEX idx_framework_versions_framework_id ON framework_versions(framework_id);

-- 章・ドメイン・カテゴリ
CREATE TABLE requirement_categories (
    id UUID PRIMARY KEY,
    framework_version_id UUID NOT NULL REFERENCES framework_versions(id) ,
    parent_id UUID REFERENCES requirement_categories(id) ,
    name VARCHAR(200) NOT NULL,
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE requirement_categories IS '章・ドメイン・カテゴリ';
COMMENT ON COLUMN requirement_categories.parent_id IS '自己参照（階層構造用、NULL可）';
COMMENT ON COLUMN requirement_categories.display_order IS '表示順';

CREATE INDEX idx_requirement_categories_version_id ON requirement_categories(framework_version_id);
CREATE INDEX idx_requirement_categories_parent_id ON requirement_categories(parent_id);

-- 規格本文上の要求（意味単位）
CREATE TABLE requirements (
    id UUID PRIMARY KEY,
    category_id UUID NOT NULL REFERENCES requirement_categories(id) ,
    code VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    text TEXT,
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE requirements IS '規格本文上の要求（意味単位）';
COMMENT ON COLUMN requirements.code IS '要求コード（例：CC6.1）';
COMMENT ON COLUMN requirements.title IS 'タイトル';
COMMENT ON COLUMN requirements.text IS '規格本文';

CREATE INDEX idx_requirements_category_id ON requirements(category_id);

-- 規格上の実施項目定義
CREATE TABLE framework_controls (
    id UUID PRIMARY KEY,
    requirement_id UUID NOT NULL REFERENCES requirements(id) ,
    framework_version_id UUID NOT NULL REFERENCES framework_versions(id) ,
    canonical_key VARCHAR(100) NOT NULL,
    display_code VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    text TEXT,
    content_hash VARCHAR(64),
    mapping_policy VARCHAR(20) NOT NULL DEFAULT 'AUTO_MIGRATE',
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_framework_controls_mapping_policy CHECK (mapping_policy IN ('AUTO_MIGRATE', 'MANUAL_REVIEW', 'DEPRECATED'))
);

COMMENT ON TABLE framework_controls IS '規格上の実施項目定義';
COMMENT ON COLUMN framework_controls.framework_version_id IS '冗長だがクエリ効率化用';
COMMENT ON COLUMN framework_controls.canonical_key IS '規格側の安定キー（版込み必須、例：ISO27001:2022:A.5.15）';
COMMENT ON COLUMN framework_controls.display_code IS '画面表示用番号（例：A.5.15）';
COMMENT ON COLUMN framework_controls.content_hash IS '正規化テキストのハッシュ（SHA-256）';
COMMENT ON COLUMN framework_controls.mapping_policy IS 'AUTO_MIGRATE / MANUAL_REVIEW / DEPRECATED';

CREATE UNIQUE INDEX idx_framework_controls_canonical_key ON framework_controls(canonical_key);
CREATE INDEX idx_framework_controls_requirement_id ON framework_controls(requirement_id);
CREATE INDEX idx_framework_controls_version_id ON framework_controls(framework_version_id);

-- FrameworkControlの前版との対応（改訂引き継ぎ用）
CREATE TABLE framework_control_predecessors (
    id UUID PRIMARY KEY,
    framework_control_id UUID NOT NULL REFERENCES framework_controls(id) ,
    predecessor_id UUID NOT NULL REFERENCES framework_controls(id) ,
    status VARCHAR(20) NOT NULL DEFAULT 'SUGGESTED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_predecessors_status CHECK (status IN ('SUGGESTED', 'CONFIRMED', 'REJECTED')),
    CONSTRAINT uq_framework_control_predecessor UNIQUE (framework_control_id, predecessor_id)
);

COMMENT ON TABLE framework_control_predecessors IS 'FrameworkControlの前版との対応（改訂引き継ぎ用）';
COMMENT ON COLUMN framework_control_predecessors.framework_control_id IS '新版側';
COMMENT ON COLUMN framework_control_predecessors.predecessor_id IS '旧版側';
COMMENT ON COLUMN framework_control_predecessors.status IS 'SUGGESTED / CONFIRMED / REJECTED';

CREATE INDEX idx_predecessors_control_id ON framework_control_predecessors(framework_control_id);
CREATE INDEX idx_predecessors_predecessor_id ON framework_control_predecessors(predecessor_id);
