-- Creates tables, indexes and fills db with 1mln generated values to test.
drop table if exists patients cascade;

create table patients (
    id bigint primary key
);

drop table if exists medical_events cascade;

create table medical_events (
    id bigint primary key,
    patient_id bigint not null,
    event_type text not null,
    event_time timestamptz not null,
    value text not null
);

-- keyset pagination
create index idx_patients_id on patients (id);

-- EXISTS / NOT EXISTS
create index idx_events_patient_type_time
    on medical_events (patient_id, event_type, event_time);

create index idx_events_type_value
    on medical_events (event_type, value);

-- Data generation
insert into patients (id)
select generate_series(1, 1000000);

-- DIAGNOSIS
insert into medical_events (id, patient_id, event_type, event_time, value)
select
    row_number() over () as id,
    p.id as patient_id,
    'DIAGNOSIS' as event_type,
    now() - (random() * interval '5 years') as event_time,
    case
        when random() < 0.1 then 'A'      -- 10% нужный диагноз
        else 'B'
    end as value
from patients p;

-- MEDICATION
insert into medical_events (id, patient_id, event_type, event_time, value)
select
    row_number() over () + 1000000 as id,
    p.id,
    'MEDICATION',
    now() - (random() * interval '3 years'),
    case
        when random() < 0.2 then 'X'      -- 20% нужный препарат
        else 'Y'
    end
from patients p
where random() < 0.7;

-- HOSPITALISATION
insert into medical_events (id, patient_id, event_type, event_time, value)
select
    row_number() over () + 2000000 as id,
    p.id,
    'HOSPITALIZATION',
    now() - (random() * interval '2 years'),
    'H'
from patients p
where random() < 0.1;

