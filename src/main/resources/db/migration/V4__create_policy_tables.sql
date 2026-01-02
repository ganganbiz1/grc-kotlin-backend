-- =====================================================
-- V4: Policy境界（テナント固有）
-- テナントの社内規程を管理し、改訂履歴を保持する
-- =====================================================

-- 社内規程
CREATE TABLE policies (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE policies IS '社内規程';
COMMENT ON COLUMN policies.tenant_id IS 'テナントID';
COMMENT ON COLUMN policies.name IS '規程名';
COMMENT ON COLUMN policies.description IS '説明';

CREATE INDEX idx_policies_tenant_id ON policies(tenant_id);

-- 改訂・版
CREATE TABLE policy_revisions (
    id UUID PRIMARY KEY,
    policy_id UUID NOT NULL REFERENCES policies(id) ,
    version VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    effective_date DATE,
    content TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_policy_revisions_status CHECK (status IN ('DRAFT', 'ACTIVE', 'ARCHIVED'))
);

COMMENT ON TABLE policy_revisions IS '改訂・版';
COMMENT ON COLUMN policy_revisions.version IS '版番号（例：1.0、2.1）';
COMMENT ON COLUMN policy_revisions.status IS 'DRAFT / ACTIVE / ARCHIVED';
COMMENT ON COLUMN policy_revisions.effective_date IS '発効日';
COMMENT ON COLUMN policy_revisions.content IS '規程本文（または参照パス）';

CREATE INDEX idx_policy_revisions_policy_id ON policy_revisions(policy_id);

-- 章・条・項
-- MVP検討事項: 条文構造化は差別化点だが実装工数が大きい
-- MVPでは policy_revisions.content に本文を集約し、policy_sections は後から導入する段階設計も可
CREATE TABLE policy_sections (
    id UUID PRIMARY KEY,
    policy_revision_id UUID NOT NULL REFERENCES policy_revisions(id) ,
    parent_id UUID REFERENCES policy_sections(id) ,
    section_number VARCHAR(20) NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE policy_sections IS '章・条・項';
COMMENT ON COLUMN policy_sections.parent_id IS '自己参照（階層構造用、NULL可）';
COMMENT ON COLUMN policy_sections.section_number IS '条項番号（例：1.2.3）';
COMMENT ON COLUMN policy_sections.title IS 'タイトル';
COMMENT ON COLUMN policy_sections.content IS '本文';
COMMENT ON COLUMN policy_sections.display_order IS '表示順';

CREATE INDEX idx_policy_sections_revision_id ON policy_sections(policy_revision_id);
CREATE INDEX idx_policy_sections_parent_id ON policy_sections(parent_id);

-- control_policy_mappings に FK制約を追加
ALTER TABLE control_policy_mappings
    ADD CONSTRAINT fk_control_policy_mappings_policy
    FOREIGN KEY (policy_id) REFERENCES policies(id) ;
