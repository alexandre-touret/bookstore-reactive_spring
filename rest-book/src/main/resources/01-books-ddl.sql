drop table if exists book;
CREATE TABLE book
(
    id                  SERIAL PRIMARY KEY,
    title               VARCHAR(255),
    isbn_13             VARCHAR(255),
    isbn_10             VARCHAR(255),
    author              VARCHAR(255),
    year_of_publication NUMERIC,
    nb_of_pages         NUMERIC,
    rank                NUMERIC,
    price               NUMERIC,
    small_image_url     VARCHAR(255),
    medium_image_url    VARCHAR(255),
    description         VARCHAR
);
