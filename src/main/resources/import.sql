INSERT INTO roles(id, name, description)
	VALUES(1, 'App//Admin', 'Role for admins users');
	
INSERT INTO roles(id, name, description)
	VALUES(2, 'App//User', 'Role for normal users');

INSERT INTO users(id, name, age, username, email, password, role_id, is_active, created_at, updated_at)
	VALUES(1, 'Carlos Alberto Arroyo Martínez', 28, 'carroyom', 'carroyom@mail.com', '$2a$10$eAksNP3QN8numBgJwshVpOg2ywD5o6YxOW/4WCrk/dZmV77pC6QqC', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
