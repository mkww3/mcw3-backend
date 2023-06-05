FROM postgres:14.5-bullseye

COPY db.sql /docker-entrypoint-initdb.d