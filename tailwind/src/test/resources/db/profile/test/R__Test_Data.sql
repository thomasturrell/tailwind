insert into member (id, first_name, last_name) values
    (1, 'Lisa', 'Simpson'),
    (2, 'Maggie', 'Simpson'), -- User is deleted during tests 
    (3, 'Grandpa', 'Simpson'), -- User is deleted during tests 
    (4, 'Maggie', 'Simpson'); -- User is edited during tests 

insert into organization (id, name) values
    (4, 'Berry Cloud Ltd');

insert into plan (id, name, organization_id) values
    (4, 'Gold', 4),
    (5, 'Silver', null), -- Plan is deleted during tests 
    (6, 'Bronse', null), -- Plan is deleted during tests 
    (7, 'Gold', null); -- Plan is edited during tests

insert into member_plan (member_id, plan_id) values
    (1, 4);
