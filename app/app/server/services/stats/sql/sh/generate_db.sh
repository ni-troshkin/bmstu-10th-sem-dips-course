#!/bin/bash
# This is a comment
sleep 1
PGPASSWORD=$1 psql statsservice -U dev -a -f /ddl/stats.sql
