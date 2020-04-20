CREATE TABLE cinema (
    c_id                  INTEGER NOT NULL,
    c_name                VARCHAR(25) NOT NULL,
    c_location_postcode   CHAR(4) NOT NULL
);

ALTER TABLE cinema ADD CONSTRAINT cinema_pk PRIMARY KEY ( c_id );

CREATE TABLE credentials (
    username        VARCHAR(30) NOT NULL,
    password_hash   VARCHAR(32) NOT NULL,
    sign_up_date    DATE NOT NULL,
    p_id            INTEGER NOT NULL
);

ALTER TABLE credentials ADD CONSTRAINT credentials_pk PRIMARY KEY ( username );
ALTER TABLE credentials ADD CONSTRAINT p_id_unique UNIQUE ( p_id );

CREATE TABLE memoir (
    m_id                   INTEGER NOT NULL,
    m_movie_name           VARCHAR(20) NOT NULL,
    m_movie_release_date   DATE NOT NULL,
    m_watching_datetime    TIMESTAMP NOT NULL,
    m_comment              VARCHAR(100),
    m_rating               NUMERIC(2, 1) NOT NULL,
    p_id                   INTEGER NOT NULL,
    c_id                   INTEGER NOT NULL
);

ALTER TABLE memoir
    ADD CONSTRAINT chk_date CHECK (DATE(m_watching_datetime) > m_movie_release_date);

ALTER TABLE memoir
    ADD CONSTRAINT chk_rating CHECK ( m_rating IN (
        0,
        0.5,
        1,
        1.5,
        2,
        2.5,
        3,
        3.5,
        4,
        4.5,
        5
    ) );

ALTER TABLE memoir ADD CONSTRAINT memoir_pk PRIMARY KEY ( m_id );

ALTER TABLE memoir ADD CONSTRAINT memoir_alternate_pk UNIQUE ( m_watching_datetime,
                                                                              p_id );

CREATE TABLE person (
    p_id           INTEGER NOT NULL,
    p_first_name   VARCHAR(10) NOT NULL,
    p_surname      VARCHAR(10) NOT NULL,
    p_gender       CHAR(1) NOT NULL,
    p_dob          DATE NOT NULL,
    p_address      VARCHAR(30) NOT NULL,
    p_state        CHAR(3) NOT NULL,
    p_postcode     CHAR(4) NOT NULL
);

ALTER TABLE person
    ADD CONSTRAINT chk_gender CHECK ( p_gender IN (
        'F',
        'M'
    ) );

ALTER TABLE person
    ADD CONSTRAINT chk_state CHECK ( p_state IN (
        'ACT',
        'NSW',
        'NT',
        'QLD',
        'SA',
        'TAS',
        'VIC',
        'WA'
    ) );

ALTER TABLE person ADD CONSTRAINT person_pk PRIMARY KEY ( p_id );

ALTER TABLE credentials
    ADD CONSTRAINT credentials_person_fk FOREIGN KEY ( p_id )
        REFERENCES person ( p_id )
            ON DELETE CASCADE;

ALTER TABLE memoir
    ADD CONSTRAINT memoir_cinema_fk FOREIGN KEY ( c_id )
        REFERENCES cinema ( c_id );

ALTER TABLE memoir
    ADD CONSTRAINT memoir_person_fk FOREIGN KEY ( p_id )
        REFERENCES person ( p_id )
            ON DELETE CASCADE;