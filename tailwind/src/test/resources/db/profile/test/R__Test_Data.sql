insert into member (id, first_name, last_name) values
    (1, 'Lisa', 'Simpson'),
    (2, 'Maggie', 'Simpson'), -- User is deleted during tests 
    (3, 'Grandpa', 'Simpson'), -- User is deleted during tests 
    (4, 'Maggie', 'Simpson'); -- User is edited during tests 

insert into plan (id, name, organization) values
    (1, 'Gold', 'Healthy'),
    (2, 'Silver', 'Healthy'), -- Plan is deleted during tests 
    (3, 'Bronse', 'Healthy'), -- Plan is deleted during tests 
    (4, 'Gold', 'Happy'); -- Plan is edited during tests 

insert into member_plan (member_id, plan_id) values
    (1, 1);
