USE `otus_lesson9`;

DROP TABLE IF EXISTS `User`;

CREATE TABLE `User` (
  `id` int(20) NOT NULL AUTO_INCREMENT DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `age` int(3) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
