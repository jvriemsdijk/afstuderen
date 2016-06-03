
CREATE TABLE adjustment
(
    id BIGINT PRIMARY KEY NOT NULL,
    actual_cost DOUBLE PRECISION DEFAULT 1,
    adjustment_definition BIGINT NOT NULL,
    wmo_decision BIGINT,
    CONSTRAINT adjustment_adjustment_definition_fkey FOREIGN KEY (adjustment_definition) REFERENCES adjustment_definitions (id),
    CONSTRAINT adjustment_wmo_decisions_id_fk FOREIGN KEY (wmo_decision) REFERENCES wmo_decisions (id)
);
CREATE UNIQUE INDEX adjustment_id_uindex ON adjustment (id);
CREATE TABLE adjustment_condition
(
    adjustment BIGINT NOT NULL,
    condition BIGINT NOT NULL,
    CONSTRAINT adjustment_condition_adjustment_condition_pk PRIMARY KEY (adjustment, condition)
);
CREATE UNIQUE INDEX adjustment_condition_condition_pk ON adjustment_condition (condition);
CREATE TABLE adjustment_definitions
(
    id BIGINT PRIMARY KEY NOT NULL,
    name VARCHAR(254) NOT NULL,
    average_cost DOUBLE PRECISION DEFAULT 0 NOT NULL,
    cost_margin DOUBLE PRECISION DEFAULT 0
);
CREATE UNIQUE INDEX adjustments_id_uindex ON adjustment_definitions (id);
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
CREATE UNIQUE INDEX adresses_id_uindex ON adresses (id);
CREATE TABLE advice
(
    id BIGINT PRIMARY KEY NOT NULL,
    go_ahead BOOLEAN DEFAULT false NOT NULL,
    applicant BIGINT,
    CONSTRAINT advice_applicant_fkey FOREIGN KEY (applicant) REFERENCES person (bsn)
);
CREATE UNIQUE INDEX advice_id_uindex ON advice (id);
CREATE TABLE advice_current_condition
(
    advice BIGINT NOT NULL,
    condition BIGINT NOT NULL,
    CONSTRAINT advice_current_condition_advice_condition_pk PRIMARY KEY (advice, condition),
    CONSTRAINT advice_current_condition_advice_fkey FOREIGN KEY (advice) REFERENCES ,
    CONSTRAINT advice_current_condition_condition_fkey FOREIGN KEY (condition) REFERENCES conditions (id)
);
CREATE TABLE advice_future_condition
(
    advice BIGINT NOT NULL,
    condition BIGINT NOT NULL,
    CONSTRAINT advice_future_condition_advice_condition_pk PRIMARY KEY (advice, condition),
    CONSTRAINT advice_future_condition_advice_fkey FOREIGN KEY (advice) REFERENCES ,
    CONSTRAINT advice_future_condition_condition_fkey FOREIGN KEY (condition) REFERENCES conditions (id)
);
CREATE TABLE applicant_history
(
    person BIGINT NOT NULL,
    adjustment BIGINT NOT NULL,
    CONSTRAINT applicant_history_person_adjustment_pk PRIMARY KEY (person, adjustment),
    CONSTRAINT applicant_history_person_fkey FOREIGN KEY (person) REFERENCES person (bsn),
    CONSTRAINT applicant_history_adjustment_fkey FOREIGN KEY (adjustment) REFERENCES
);
CREATE TABLE bag
(
    id BIGINT PRIMARY KEY NOT NULL,
    build_year SMALLINT NOT NULL,
    building_contour VARCHAR,
    usable_surface DOUBLE PRECISION,
    use_purpose VARCHAR,
    coordinates VARCHAR,
    adress BIGINT,
    CONSTRAINT bag_adress_fkey FOREIGN KEY (adress) REFERENCES adresses (id)
);
CREATE UNIQUE INDEX bag_id_uindex ON bag (id);
CREATE TABLE conditions
(
    id BIGINT PRIMARY KEY NOT NULL,
    name VARCHAR(254),
    chronic BOOLEAN DEFAULT false NOT NULL
);
CREATE UNIQUE INDEX conditions_id_uindex ON conditions (id);
CREATE TABLE housing_adjustments
(
    housing_situation BIGINT NOT NULL,
    adjustment BIGINT NOT NULL,
    CONSTRAINT housing_adjustments_housing_situation_adjustment_pk PRIMARY KEY (housing_situation, adjustment),
    CONSTRAINT housing_adjustments_housing_situation_fkey FOREIGN KEY (housing_situation) REFERENCES housing_situation (id),
    CONSTRAINT housing_adjustments_adjustment_fkey FOREIGN KEY (adjustment) REFERENCES
);
CREATE TABLE housing_situation
(
    id BIGINT PRIMARY KEY NOT NULL,
    bag BIGINT NOT NULL,
    floor SMALLINT NOT NULL,
    elevator BOOLEAN DEFAULT false NOT NULL,
    CONSTRAINT housing_situation_bag_fkey FOREIGN KEY (bag) REFERENCES bag (id)
);
CREATE UNIQUE INDEX housing_situation_id_uindex ON housing_situation (id);
CREATE TABLE person
(
    bsn BIGINT PRIMARY KEY NOT NULL
);
CREATE UNIQUE INDEX person_bsn_uindex ON person (bsn);
CREATE TABLE residents
(
    person BIGINT NOT NULL,
    housing_situation BIGINT NOT NULL,
    CONSTRAINT residents_person_housing_situation_pk PRIMARY KEY (person, housing_situation),
    CONSTRAINT residents_person_fkey FOREIGN KEY (person) REFERENCES person (bsn),
    CONSTRAINT residents_housing_situation_fkey FOREIGN KEY (housing_situation) REFERENCES housing_situation (id)
);
CREATE TABLE wmo_decisions
(
    id INTEGER PRIMARY KEY NOT NULL,
    granted BOOLEAN DEFAULT false NOT NULL,
    reason VARCHAR(1024),
    exception BOOLEAN DEFAULT false NOT NULL,
    advice BIGINT NOT NULL,
    CONSTRAINT wmo_decisions_advice_id_fk FOREIGN KEY (advice) REFERENCES advice (id)
);
CREATE UNIQUE INDEX wmo_decisions_id_uindex ON wmo_decisions (id);