-- Framework tables for GRC Platform
-- V1: Initial schema

-- Controls (independent entity)
CREATE TABLE controls (
    id VARCHAR(36) PRIMARY KEY,
    external_id VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

COMMENT ON TABLE controls IS 'セキュリティ統制。複数のRequirementで再利用可能な独立エンティティ';
COMMENT ON COLUMN controls.id IS '主キー（UUID v7）';
COMMENT ON COLUMN controls.external_id IS '外部システムとの連携用ID';
COMMENT ON COLUMN controls.name IS '統制名';
COMMENT ON COLUMN controls.description IS '統制の詳細説明';
COMMENT ON COLUMN controls.created_at IS '作成日時';
COMMENT ON COLUMN controls.updated_at IS '更新日時';

-- Frameworks
CREATE TABLE frameworks (
    id VARCHAR(36) PRIMARY KEY,
    display_name VARCHAR(255) NOT NULL,
    shorthand_name VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    version VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

COMMENT ON TABLE frameworks IS 'コンプライアンスフレームワーク（SOC2, ISO27001, GDPR等）';
COMMENT ON COLUMN frameworks.id IS '主キー（UUID v7）';
COMMENT ON COLUMN frameworks.display_name IS '表示名（例: SOC 2 Type II）';
COMMENT ON COLUMN frameworks.shorthand_name IS '略称（例: SOC2）';
COMMENT ON COLUMN frameworks.description IS 'フレームワークの説明';
COMMENT ON COLUMN frameworks.version IS 'バージョン';
COMMENT ON COLUMN frameworks.created_at IS '作成日時';
COMMENT ON COLUMN frameworks.updated_at IS '更新日時';

-- Requirement Categories (belongs to Framework)
CREATE TABLE requirement_categories (
    id VARCHAR(36) PRIMARY KEY,
    framework_id VARCHAR(36) NOT NULL REFERENCES frameworks(id),
    name VARCHAR(255) NOT NULL,
    shorthand VARCHAR(50) NOT NULL,
    display_order INT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

COMMENT ON TABLE requirement_categories IS '要件カテゴリ。フレームワーク内の要件をグループ化する見出し';
COMMENT ON COLUMN requirement_categories.id IS '主キー（UUID v7）';
COMMENT ON COLUMN requirement_categories.framework_id IS '所属するフレームワークのID';
COMMENT ON COLUMN requirement_categories.name IS 'カテゴリ名（例: Common Criteria）';
COMMENT ON COLUMN requirement_categories.shorthand IS '略称（例: CC）';
COMMENT ON COLUMN requirement_categories.display_order IS '表示順序（昇順）';
COMMENT ON COLUMN requirement_categories.created_at IS '作成日時';
COMMENT ON COLUMN requirement_categories.updated_at IS '更新日時';

-- Requirements (belongs to RequirementCategory)
CREATE TABLE requirements (
    id VARCHAR(36) PRIMARY KEY,
    category_id VARCHAR(36) NOT NULL REFERENCES requirement_categories(id),
    name VARCHAR(255) NOT NULL,
    shorthand VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    display_order INT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

COMMENT ON TABLE requirements IS '要件。フレームワークの最小単位となる具体的な要求事項';
COMMENT ON COLUMN requirements.id IS '主キー（UUID v7）';
COMMENT ON COLUMN requirements.category_id IS '所属するカテゴリのID';
COMMENT ON COLUMN requirements.name IS '要件名';
COMMENT ON COLUMN requirements.shorthand IS '略称（例: CC1.1）';
COMMENT ON COLUMN requirements.description IS '要件の詳細説明';
COMMENT ON COLUMN requirements.display_order IS '表示順序（昇順）';
COMMENT ON COLUMN requirements.created_at IS '作成日時';
COMMENT ON COLUMN requirements.updated_at IS '更新日時';

-- Requirement-Control mapping (N:N)
CREATE TABLE requirement_controls (
    id VARCHAR(36) PRIMARY KEY,
    requirement_id VARCHAR(36) NOT NULL REFERENCES requirements(id),
    control_id VARCHAR(36) NOT NULL REFERENCES controls(id),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    UNIQUE (requirement_id, control_id)
);

COMMENT ON TABLE requirement_controls IS '要件と統制のマッピング。マッピング自体が監査対象となるEntity';
COMMENT ON COLUMN requirement_controls.id IS '主キー（UUID v7）';
COMMENT ON COLUMN requirement_controls.requirement_id IS '要件ID';
COMMENT ON COLUMN requirement_controls.control_id IS '統制ID';
COMMENT ON COLUMN requirement_controls.created_at IS '作成日時';
COMMENT ON COLUMN requirement_controls.updated_at IS '更新日時';

-- Indexes
CREATE INDEX idx_requirement_categories_framework_id ON requirement_categories(framework_id);
CREATE INDEX idx_requirements_category_id ON requirements(category_id);
CREATE INDEX idx_requirement_controls_control_id ON requirement_controls(control_id);

-- Unique constraints for display_order within parent
CREATE UNIQUE INDEX idx_requirement_categories_framework_order ON requirement_categories(framework_id, display_order);
CREATE UNIQUE INDEX idx_requirements_category_order ON requirements(category_id, display_order);
