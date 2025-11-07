CREATE TABLE outbox_event
(
    id             UUID PRIMARY KEY,
    event_id       VARCHAR(255) NOT NULL UNIQUE,
    aggregate_id   VARCHAR(255) NOT NULL,
    event_type     VARCHAR(255) NOT NULL,
    schema_version VARCHAR(50)  NOT NULL,
    trace_id       VARCHAR(255) NOT NULL,
    occurred_at    TIMESTAMP    NOT NULL,
    source         VARCHAR(255) NOT NULL,
    payload        JSONB        NOT NULL,
    status         VARCHAR(50)  NOT NULL DEFAULT 'NEW',
    published_at   TIMESTAMP NULL,
    retry_count    INT          NOT NULL DEFAULT 0
);