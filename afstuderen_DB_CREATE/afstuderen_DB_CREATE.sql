CREATE TABLE adjustment
(
    id BIGINT PRIMARY KEY NOT NULL,
    actual_cost DOUBLE PRECISION DEFAULT 0,
    adjustment_definition INTEGER NOT NULL
);
CREATE TABLE adjustment_condition
(
    adjustment BIGINT NOT NULL,
    condition BIGINT NOT NULL,
    CONSTRAINT adjustment_condition_adjustment_condition_pk PRIMARY KEY (adjustment, condition)
);
CREATE TABLE adjustment_definitions
(
    id BIGINT PRIMARY KEY NOT NULL,
    name VARCHAR(254) NOT NULL,
    average_cost DOUBLE PRECISION DEFAULT 0 NOT NULL,
    cost_margin DOUBLE PRECISION DEFAULT 0
);
CREATE TABLE adresses
(
    id BIGINT PRIMARY KEY NOT NULL,
    street VARCHAR,
    zipcode VARCHAR,
    number INTEGER,
    number_addon VARCHAR,
    city VARCHAR,
    country VARCHAR(254)
);
CREATE TABLE advice
(
    id BIGINT PRIMARY KEY NOT NULL,
    go_ahead BOOLEAN DEFAULT false NOT NULL,
    applicant BIGINT
);
CREATE TABLE advice_current_condition
(
    advice BIGINT NOT NULL,
    condition BIGINT NOT NULL,
    CONSTRAINT advice_current_condition_advice_condition_pk PRIMARY KEY (advice, condition)
);
CREATE TABLE advice_future_condition
(
    advice BIGINT NOT NULL,
    condition BIGINT NOT NULL,
    CONSTRAINT advice_future_condition_advice_condition_pk PRIMARY KEY (advice, condition)
);
CREATE TABLE applicant_history
(
    person BIGINT NOT NULL,
    adjustment BIGINT NOT NULL,
    CONSTRAINT applicant_history_person_adjustment_pk PRIMARY KEY (person, adjustment)
);
CREATE TABLE bag
(
    id BIGINT PRIMARY KEY NOT NULL,
    build_year SMALLINT NOT NULL,
    building_contour VARCHAR,
    usable_surface DOUBLE PRECISION,
    use_purpose VARCHAR,
    coordinates VARCHAR,
    adress BIGINT
);
CREATE TABLE conditions
(
    id BIGINT PRIMARY KEY NOT NULL,
    name VARCHAR(254),
    chronic BOOLEAN DEFAULT false NOT NULL
);
CREATE TABLE housing_adjustments
(
    housing_situation BIGINT NOT NULL,
    adjustment BIGINT NOT NULL,
    CONSTRAINT housing_adjustments_housing_situation_adjustment_pk PRIMARY KEY (housing_situation, adjustment)
);
CREATE TABLE housing_situation
(
    id BIGINT PRIMARY KEY NOT NULL,
    bag BIGINT NOT NULL,
    floor SMALLINT NOT NULL,
    elevator BOOLEAN DEFAULT false NOT NULL
);
CREATE TABLE person
(
    bsn BIGINT PRIMARY KEY NOT NULL
);
CREATE TABLE residents
(
    person BIGINT NOT NULL,
    housing_situation BIGINT NOT NULL,
    CONSTRAINT residents_person_housing_situation_pk PRIMARY KEY (person, housing_situation)
);
ALTER TABLE adjustment ADD FOREIGN KEY (adjustment_definition) REFERENCES adjustment_definitions (id);
CREATE UNIQUE INDEX adjustment_id_uindex ON adjustment (id);
ALTER TABLE adjustment_condition ADD FOREIGN KEY (adjustment) REFERENCES adjustment_definitions (id);
ALTER TABLE adjustment_condition ADD FOREIGN KEY (condition) REFERENCES conditions (id);
CREATE UNIQUE INDEX adjustment_condition_condition_pk ON adjustment_condition (condition);
CREATE UNIQUE INDEX adjustments_id_uindex ON adjustment_definitions (id);
CREATE UNIQUE INDEX adresses_id_uindex ON adresses (id);
ALTER TABLE advice ADD FOREIGN KEY (applicant) REFERENCES person (bsn);
CREATE UNIQUE INDEX advice_id_uindex ON advice (id);
ALTER TABLE advice_current_condition ADD FOREIGN KEY (advice) REFERENCES advice (id);
ALTER TABLE advice_current_condition ADD FOREIGN KEY (condition) REFERENCES conditions (id);
ALTER TABLE advice_future_condition ADD FOREIGN KEY (advice) REFERENCES advice (id);
ALTER TABLE advice_future_condition ADD FOREIGN KEY (condition) REFERENCES conditions (id);
ALTER TABLE applicant_history ADD FOREIGN KEY (person) REFERENCES person (bsn);
ALTER TABLE applicant_history ADD FOREIGN KEY (adjustment) REFERENCES adjustment (id);
ALTER TABLE bag ADD FOREIGN KEY (adress) REFERENCES adresses (id);
CREATE UNIQUE INDEX bag_id_uindex ON bag (id);
CREATE UNIQUE INDEX conditions_id_uindex ON conditions (id);
ALTER TABLE housing_adjustments ADD FOREIGN KEY (housing_situation) REFERENCES housing_situation (id);
ALTER TABLE housing_adjustments ADD FOREIGN KEY (adjustment) REFERENCES adjustment (id);
ALTER TABLE housing_situation ADD FOREIGN KEY (bag) REFERENCES bag (id);
CREATE UNIQUE INDEX housing_situation_id_uindex ON housing_situation (id);
CREATE UNIQUE INDEX person_bsn_uindex ON person (bsn);
ALTER TABLE residents ADD FOREIGN KEY (person) REFERENCES person (bsn);
ALTER TABLE residents ADD FOREIGN KEY (housing_situation) REFERENCES housing_situation (id);