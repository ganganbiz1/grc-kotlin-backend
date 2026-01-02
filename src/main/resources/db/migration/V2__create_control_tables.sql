-- =====================================================
-- V2: Control境界（テナント固有）
-- テナントの運用状態を管理する中核境界
-- =====================================================

-- テナントの運用管理対象
CREATE TABLE controls (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'NOT_STARTED',
    owner_id UUID,
    note TEXT,
    custom_fields JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_controls_status CHECK (status IN ('NOT_STARTED', 'IN_PROGRESS', 'COMPLETED'))
);

COMMENT ON TABLE controls IS 'テナントの運用管理対象';
COMMENT ON COLUMN controls.tenant_id IS 'テナントID';
COMMENT ON COLUMN controls.status IS 'NOT_STARTED / IN_PROGRESS / COMPLETED';
COMMENT ON COLUMN controls.owner_id IS '責任者ID（将来的にusersテーブルへFK）';
COMMENT ON COLUMN controls.note IS '補足情報';
COMMENT ON COLUMN controls.custom_fields IS 'カスタムフィールド（JSONB）';

CREATE INDEX idx_controls_tenant_id ON controls(tenant_id);
CREATE INDEX idx_controls_tenant_status ON controls(tenant_id, status);

-- Control ↔ FrameworkControl（多:多）
CREATE TABLE control_framework_mappings (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    control_id UUID NOT NULL REFERENCES controls(id) ,
    framework_control_id UUID NOT NULL REFERENCES framework_controls(id) ,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_control_framework_mapping UNIQUE (control_id, framework_control_id)
);

COMMENT ON TABLE control_framework_mappings IS 'Control ↔ FrameworkControl（多:多）';
COMMENT ON COLUMN control_framework_mappings.tenant_id IS 'テナントID（RLS・誤JOIN防止用）';

CREATE INDEX idx_control_framework_mappings_tenant_id ON control_framework_mappings(tenant_id);
CREATE INDEX idx_control_framework_mappings_control_id ON control_framework_mappings(control_id);
CREATE INDEX idx_control_framework_mappings_fc_id ON control_framework_mappings(framework_control_id);

-- Control ↔ Evidence（多:多）
-- ※ evidencesテーブルはV3で作成されるため、FK制約は後で追加
CREATE TABLE control_evidence_mappings (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    control_id UUID NOT NULL REFERENCES controls(id) ,
    evidence_id UUID NOT NULL,
    note TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_control_evidence_mapping UNIQUE (control_id, evidence_id)
);

COMMENT ON TABLE control_evidence_mappings IS 'Control ↔ Evidence（多:多）';
COMMENT ON COLUMN control_evidence_mappings.tenant_id IS 'テナントID（RLS・誤JOIN防止用）';
COMMENT ON COLUMN control_evidence_mappings.note IS '紐づけ理由・補足（なぜこの証跡で満たすか）';

CREATE INDEX idx_control_evidence_mappings_tenant_id ON control_evidence_mappings(tenant_id);
CREATE INDEX idx_control_evidence_mappings_control_id ON control_evidence_mappings(control_id);
CREATE INDEX idx_control_evidence_mappings_evidence_id ON control_evidence_mappings(evidence_id);

-- Control ↔ Test（多:多）
-- ※ testsテーブルは将来実装のため、FK制約なし
CREATE TABLE control_test_mappings (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    control_id UUID NOT NULL REFERENCES controls(id) ,
    test_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_control_test_mapping UNIQUE (control_id, test_id)
);

COMMENT ON TABLE control_test_mappings IS 'Control ↔ Test（多:多）';
COMMENT ON COLUMN control_test_mappings.tenant_id IS 'テナントID（RLS・誤JOIN防止用）';
COMMENT ON COLUMN control_test_mappings.test_id IS 'FK → tests.id（将来実装）';

CREATE INDEX idx_control_test_mappings_tenant_id ON control_test_mappings(tenant_id);
CREATE INDEX idx_control_test_mappings_control_id ON control_test_mappings(control_id);
CREATE INDEX idx_control_test_mappings_test_id ON control_test_mappings(test_id);

-- Control ↔ Policy（多:多）
-- ※ policiesテーブルはV4で作成されるため、FK制約は後で追加
CREATE TABLE control_policy_mappings (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    control_id UUID NOT NULL REFERENCES controls(id) ,
    policy_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_control_policy_mapping UNIQUE (control_id, policy_id)
);

COMMENT ON TABLE control_policy_mappings IS 'Control ↔ Policy（多:多）';
COMMENT ON COLUMN control_policy_mappings.tenant_id IS 'テナントID（RLS・誤JOIN防止用）';

CREATE INDEX idx_control_policy_mappings_tenant_id ON control_policy_mappings(tenant_id);
CREATE INDEX idx_control_policy_mappings_control_id ON control_policy_mappings(control_id);
CREATE INDEX idx_control_policy_mappings_policy_id ON control_policy_mappings(policy_id);
