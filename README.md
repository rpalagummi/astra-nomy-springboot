# astranomy-app
Application powered by DataStax Astra 

 ![architecture](/src/main/resources/static/img/architecture.png)

## Data Model
``` sql
create table pendingtransactions_by_correlationid
(
transaction_id text,
correlation_id int,
user_id int,
message text,
status  text,
created_by text,
created_dt text,
PRIMARY KEY (transaction_id, correlation_id)
) WITH CLUSTERING ORDER BY (correlation_id DESC);

CREATE CUSTOM INDEX status_idx ON enterprise.pendingtransactions_by_correlationid (status) USING 'StorageAttachedIndex' WITH OPTIONS = {'normalize': 'true', 'ascii': 'true', 'case_sensitive': 'false'};

create table events_by_correlationid
(
event_id uuid,
correlation_id int,
originator_reference text,
originator_timestamp timestamp,
description text,
http_referer text,
PRIMARY KEY (event_id, correlation_id)
) WITH CLUSTERING ORDER BY (correlation_id DESC);
```

## References
* [Astra Streaming](https://www.datastax.com/products/astra-streaming)
* [Astra DB](https://www.datastax.com/products/datastax-astra)
