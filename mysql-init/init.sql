DROP USER IF EXISTS 'newsuser'@'%';
CREATE USER 'newsuser'@'%' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON test.* TO 'newsuser'@'%';
FLUSH PRIVILEGES;
