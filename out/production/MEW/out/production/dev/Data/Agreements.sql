CREATE TABLE Agreements (
    agreement_id       VARCHAR(50)   PRIMARY KEY,
    supplier_id        VARCHAR(50)   NOT NULL,
    start_date         DATE          NOT NULL,
    supports_delivery  BOOLEAN       NOT NULL
);