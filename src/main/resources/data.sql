INSERT INTO member (member_id, email, password, name, role, auth_provider, phone_number, create_date_time, birth_date)
VALUES (1, 'admin@gmail.com', '$2a$12$x6/Du1H6pxfjkabIk9OINOj4AnEGFGGRZt7t2VtkSXYAqC1UoxRtK', '홍길동', 'ADMIN', '', '010-1234-5678', NOW(), '2000-01-01'),
       (2, 'kookmin@gmail.com', '$2a$12$x6/Du1H6pxfjkabIk9OINOj4AnEGFGGRZt7t2VtkSXYAqC1UoxRtK', '등록금', 'USER', '', '010-5678-1234', NOW(), '1999-09-09'),
       (3, 'noworkspace@naver.com', '$2a$12$x6/Du1H6pxfjkabIk9OINOj4AnEGFGGRZt7t2VtkSXYAqC1UoxRtK', '일안함', 'USER', '', '010-9999-9999', NOW(), '1998-08-08');

INSERT INTO workspace (workspace_id, name, owner_id)
VALUES (1, 'Korea Air', 1),
       (2, 'SK innovation', 1),
       (3, 'kookmin University', 2);

INSERT INTO participant (member_id, workspace_id, scope)
VALUES (1, 1, 'OWNER'),
       (1, 2, 'OWNER'),
       (2, 1, 'MANAGER'),
       (2, 2, 'USER'),
       (2, 3, 'OWNER');