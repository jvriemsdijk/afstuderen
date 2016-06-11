--
-- Database create script
-- Created by: Joey van Riemsdijk | H2
--


-- Adjustment table
CREATE TABLE adjustment (
    adjustment_id BIGINT PRIMARY KEY NOT NULL,
    actual_cost DOUBLE PRECISION DEFAULT 1,
    adjustment_definition BIGINT NOT NULL,
    cost_subsidized DOUBLE PRECISION,
    contractor BIGINT,
    application BIGINT,
    CONSTRAINT adjustment_adjustment_definition_fkey FOREIGN KEY (adjustment_definition) REFERENCES adjustment_definition (adjustment_definition_id),
    CONSTRAINT adjustment_contractors_id_fk FOREIGN KEY (contractor) REFERENCES contractor (contractor_id),
    CONSTRAINT adjustment_application_id_fk FOREIGN KEY (application) REFERENCES application (application_id)
);
CREATE UNIQUE INDEX adjustment_id_uindex ON adjustment (adjustment_id);


-- Adjustment definition table
CREATE TABLE adjustment_definition (
    adjustment_definition_id BIGINT PRIMARY KEY NOT NULL,
    name VARCHAR(254) NOT NULL,
    average_cost DOUBLE PRECISION DEFAULT 0 NOT NULL,
    cost_margin DOUBLE PRECISION DEFAULT 0,
    creation_date TIMESTAMP,
    version INTEGER DEFAULT 1 NOT NULL
);
CREATE UNIQUE INDEX adjustment_id_uindex ON adjustment_definition (adjustment_definition_id);


-- Adjustment definition to Condition table
CREATE TABLE adjustment_definition_to_condition (
    adjustment BIGINT NOT NULL,
    condition BIGINT NOT NULL,
    start_date TIMESTAMP DEFAULT now() NOT NULL,
    end_date TIMESTAMP,
    CONSTRAINT adjustment_condition_adjustment_condition_pk PRIMARY KEY (adjustment, condition),
    CONSTRAINT adjustment_condition_adjustment_fkey FOREIGN KEY (adjustment) REFERENCES adjustment_definition (adjustment_definition_id),
    CONSTRAINT adjustment_condition_condition_fkey FOREIGN KEY (condition) REFERENCES condition (condition_id)
);
CREATE UNIQUE INDEX adjustment_condition_condition_pk ON adjustment_definition_to_condition (adjustment, condition);


-- Address table
CREATE TABLE address (
    address_id BIGINT PRIMARY KEY NOT NULL,
    street VARCHAR,
    zipcode VARCHAR,
    number INTEGER,
    number_addon VARCHAR,
    city VARCHAR,
    country VARCHAR(254)
);
CREATE UNIQUE INDEX address_id_uindex ON address (address_id);


-- Advice table
CREATE TABLE advice (
    advice_id BIGINT PRIMARY KEY NOT NULL,
    go_ahead BOOLEAN DEFAULT false NOT NULL,
    date_issued TIMESTAMP,
    application BIGINT,
    remarks VARCHAR,
    CONSTRAINT advice_application_id_fk FOREIGN KEY (application) REFERENCES application (application_id)
);
CREATE UNIQUE INDEX advice_id_uindex ON advice (advice_id);


-- Advice to current condition table
CREATE TABLE advice_to_current_condition(
    advice BIGINT NOT NULL,
    condition BIGINT NOT NULL,
    CONSTRAINT advice_current_condition_advice_condition_pk PRIMARY KEY (advice, condition),
    CONSTRAINT advice_current_condition_advice_fkey FOREIGN KEY (advice) REFERENCES advice (advice_id),
    CONSTRAINT advice_current_condition_condition_fkey FOREIGN KEY (condition) REFERENCES condition (condition_id)
);


-- Advice to future condition table
CREATE TABLE advice_to_future_condition (
    advice BIGINT NOT NULL,
    condition BIGINT NOT NULL,
    CONSTRAINT advice_future_condition_advice_condition_pk PRIMARY KEY (advice, condition),
    CONSTRAINT advice_future_condition_advice_fkey FOREIGN KEY (advice) REFERENCES advice (advice_id),
    CONSTRAINT advice_future_condition_condition_fkey FOREIGN KEY (condition) REFERENCES condition (condition_id)
);


-- Application table
CREATE TABLE application (
    application_id INTEGER PRIMARY KEY NOT NULL,
    applicant BIGINT NOT NULL,
    date_recieved TIMESTAMP,
    CONSTRAINT application_person_bsn_fk FOREIGN KEY (applicant) REFERENCES person (bsn)
);
CREATE UNIQUE INDEX application_id_uindex ON application (application_id);


-- BAG table
CREATE TABLE bag (
    bag_id BIGINT PRIMARY KEY NOT NULL,
    build_year SMALLINT NOT NULL,
    building_contour VARCHAR,
    usable_surface DOUBLE PRECISION,
    use_purpose VARCHAR,
    address BIGINT,
    x DOUBLE PRECISION,
    y DOUBLE PRECISION,
    CONSTRAINT bag_address_fkey FOREIGN KEY (address) REFERENCES address (address_id)
);
CREATE UNIQUE INDEX bag_id_uindex ON bag (bag_id);


-- Condition table
CREATE TABLE condition (
    condition_id BIGINT PRIMARY KEY NOT NULL,
    name VARCHAR(254),
    chronic BOOLEAN DEFAULT false NOT NULL
);
CREATE UNIQUE INDEX condition_id_uindex ON condition (condition_id);


-- Contractor table
CREATE TABLE contractor (
    contractor_id INTEGER PRIMARY KEY NOT NULL,
    name VARCHAR(254),
    approved BOOLEAN DEFAULT false NOT NULL,
    cost_modifier DOUBLE PRECISION DEFAULT 0 NOT NULL,
    date_added TIMESTAMP DEFAULT now() NOT NULL,
    date_removed TIMESTAMP
);
CREATE UNIQUE INDEX contractor_id_uindex ON contractor (contractor_id);


-- Housing situation table
CREATE TABLE housing_situation (
    housing_situation_id BIGINT PRIMARY KEY NOT NULL,
    bag BIGINT,
    floor SMALLINT NOT NULL,
    elevator BOOLEAN DEFAULT false NOT NULL,
    CONSTRAINT housing_situation_bag_fkey FOREIGN KEY (bag) REFERENCES bag (bag_id)
);
CREATE UNIQUE INDEX housing_situation_id_uindex ON housing_situation (housing_situation_id);


-- Housing situation to Adjustment table
CREATE TABLE housing_situation_to_adjustment (
    housing_situation BIGINT NOT NULL,
    adjustment BIGINT NOT NULL,
    date_placed TIMESTAMP DEFAULT now(),
    date_removed TIMESTAMP,
    CONSTRAINT housing_adjustments_housing_situation_adjustment_pk PRIMARY KEY (housing_situation, adjustment),
    CONSTRAINT housing_adjustments_housing_situation_fkey FOREIGN KEY (housing_situation) REFERENCES housing_situation (housing_situation_id) ,
    CONSTRAINT housing_adjustments_adjustment_fkey FOREIGN KEY (adjustment) REFERENCES adjustment (adjustment_id)
);


-- Person table
CREATE TABLE person (
    bsn BIGINT PRIMARY KEY NOT NULL
);
CREATE UNIQUE INDEX person_bsn_uindex ON person (bsn);


-- Person to Housing situation table
CREATE TABLE person_to_housing_situation (
    person BIGINT NOT NULL,
    housing_situation BIGINT NOT NULL,
    move_in_date TIMESTAMP,
    move_out_date TIMESTAMP,
    CONSTRAINT residents_person_housing_situation_pk PRIMARY KEY (person, housing_situation),
    CONSTRAINT residents_person_fkey FOREIGN KEY (person) REFERENCES person (bsn),
    CONSTRAINT residents_housing_situation_fkey FOREIGN KEY (housing_situation) REFERENCES housing_situation (housing_situation_id)
);


-- WMO decision table
CREATE TABLE wmo_decision (
    wmo_decision_id INTEGER PRIMARY KEY NOT NULL,
    granted BOOLEAN DEFAULT false NOT NULL,
    reason VARCHAR(1024),
    exception BOOLEAN DEFAULT false NOT NULL,
    advice BIGINT NOT NULL,
    date TIMESTAMP,
    CONSTRAINT wmo_decision_advice_id_fk FOREIGN KEY (advice) REFERENCES advice (advice_id)
);
CREATE UNIQUE INDEX wmo_decision_id_uindex ON wmo_decision (wmo_decision_id);