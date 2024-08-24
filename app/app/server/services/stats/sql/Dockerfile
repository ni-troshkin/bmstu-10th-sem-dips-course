FROM postgres:14.5

ENV CSVDIR /data/lib/postgresql/csvs/

COPY ./data/ ${CSVDIR}

COPY ./ddl/stats.sql /docker-entrypoint-initdb.d/01_stats.sql
