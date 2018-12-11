insert into organization (name) values
    ('Berry Cloud Ltd'),
    ('Berry Cloud s.r.o'),
    ('ACME Health');

insert into plan (name, organization_id)
select 
    'Gold', id
from organization where name like 'Berry Cloud Ltd';

insert into plan (name, organization_id)
select 
    'Silver', id
from organization where name like 'Berry Cloud Ltd';

insert into plan (name, organization_id)
select 
    'Bronse', id
from organization where name like 'Berry Cloud Ltd';
