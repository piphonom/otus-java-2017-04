CREATE DATABASE IF NOT EXISTS `otus_lesson9`;
CREATE DATABASE IF NOT EXISTS `otus_lesson9_hibernate`;
CREATE USER 'piphonom'@'localhost' IDENTIFIED BY 'monohpip';
GRANT CREATE, DROP, ALTER, SELECT, INSERT, UPDATE, EXECUTE, SHOW VIEW, LOCK TABLES ON `otus_%`.* to 'piphonom'@'%';
