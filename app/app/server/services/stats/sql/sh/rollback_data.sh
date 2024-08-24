#!/bin/bash
# This is a comment
PGPASSWORD=$1 psql statsservice -U dev -a -f /dml/truncate_tables.sql

