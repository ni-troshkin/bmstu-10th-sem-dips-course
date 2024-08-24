\set csvdir `echo $CSVDIR`

\set copy_events '\\copy events from ' :csvdir 'events.csv' ' with (format csv);'

:copy_events

ALTER SEQUENCE public.event_id_seq RESTART 1;
