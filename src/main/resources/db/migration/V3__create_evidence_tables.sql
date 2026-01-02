-- =====================================================
-- V3: Evidence境界（テナント固有）
-- 監査に耐える形で証跡を保全・管理する
-- =====================================================

-- 証跡の論理単位
CREATE TABLE evidences (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE evidences IS '証跡の論理単位';
COMMENT ON COLUMN evidences.tenant_id IS 'テナントID';
COMMENT ON COLUMN evidences.name IS '証跡名';
COMMENT ON COLUMN evidences.description IS '説明';

CREATE INDEX idx_evidences_tenant_id ON evidences(tenant_id);

-- ファイル/URL/スナップショット等の実体
CREATE TABLE evidence_artifacts (
    id UUID PRIMARY KEY,
    evidence_id UUID NOT NULL REFERENCES evidences(id) ,
    artifact_type VARCHAR(20) NOT NULL,
    file_path VARCHAR(500),
    url VARCHAR(2000),
    hash VARCHAR(64),
    size_bytes BIGINT,
    collected_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_artifact_type CHECK (artifact_type IN ('FILE', 'URL', 'SNAPSHOT'))
);

COMMENT ON TABLE evidence_artifacts IS 'ファイル/URL/スナップショット等の実体';
COMMENT ON COLUMN evidence_artifacts.artifact_type IS 'FILE / URL / SNAPSHOT';
COMMENT ON COLUMN evidence_artifacts.file_path IS 'ファイルパス（S3キー等）';
COMMENT ON COLUMN evidence_artifacts.url IS '外部URL';
COMMENT ON COLUMN evidence_artifacts.hash IS 'SHA-256ハッシュ';
COMMENT ON COLUMN evidence_artifacts.size_bytes IS 'ファイルサイズ';
COMMENT ON COLUMN evidence_artifacts.collected_at IS '取得日時';

-- TODO（MVP後）: CHECK制約追加
-- artifact_type と file_path/url の排他性をDBレベルで保証
-- FILE → file_path NOT NULL, url NULL
-- URL → url NOT NULL, file_path NULL
-- SNAPSHOT → file_path NOT NULL
-- MVPではアプリ層でバリデーション

CREATE INDEX idx_evidence_artifacts_evidence_id ON evidence_artifacts(evidence_id);

-- control_evidence_mappings に FK制約を追加
ALTER TABLE control_evidence_mappings
    ADD CONSTRAINT fk_control_evidence_mappings_evidence
    FOREIGN KEY (evidence_id) REFERENCES evidences(id) ;
