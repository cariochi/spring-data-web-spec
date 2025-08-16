INSERT INTO organization (id, name, region)
VALUES (1, 'Org Alpha', 'US'),
       (2, 'Org Beta', 'EU'),
       (3, 'Org Gamma', 'CA');

INSERT INTO dummy_entity (id, name, status, organization_id)
VALUES (101, 'Test Project Alpha 1', 'STOPPED', 1),
       (102, 'Test Project Alpha 2', 'FAILED', 1),
       (103, 'Test Project Beta 1', 'ACTIVE', 2),
       (104, 'Test Project Beta 2', 'STOPPED', 2),
       (105, 'Test Project Gamma 1', 'FAILED', 3),
       (106, 'Test Project Gamma 2', 'ACTIVE', 3);

INSERT INTO dummy_entity_labels (dummy_entity_id, label)
VALUES (101, 'urgent'),
       (101, 'alpha'),
       (102, 'test'),
       (103, 'beta'),
       (104, 'archived'),
       (105, 'gamma'),
       (106, 'active');

INSERT INTO dummy_entity_properties (dummy_entity_id, prop_key, prop_value)
VALUES (101, 'color', 'red'),
       (101, 'priority', 'high'),
       (102, 'priority', 'low'),
       (103, 'color', 'blue'),
       (104, 'owner', 'teamA'),
       (105, 'color', 'green'),
       (106, 'priority', 'medium'),
       (106, 'owner', 'teamB');
