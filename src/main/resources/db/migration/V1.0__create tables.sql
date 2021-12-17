CREATE TABLE `permission` (
  `id` int NOT NULL AUTO_INCREMENT,
  `resource` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `user` (
  `name` varchar(40) NOT NULL,
  `password` varchar(80) DEFAULT NULL,
  `rate` int DEFAULT '0',
  PRIMARY KEY (`name`)
);

CREATE INDEX rate_index ON user(rate);

CREATE TABLE `role` (
  `name` varchar(40) NOT NULL,
  PRIMARY KEY (`name`)
);

CREATE TABLE `player_stats` (
  `name` varchar(40) NOT NULL,
  `health` double DEFAULT NULL,
  `strength` double DEFAULT NULL,
  UNIQUE KEY `name_UNIQUE` (`name`),
  CONSTRAINT `foreign_name` FOREIGN KEY (`name`) REFERENCES `user` (`name`)
);

CREATE TABLE `role_permission` (
  `role_name` varchar(40) NOT NULL,
  `permission_id` int NOT NULL,
  UNIQUE KEY `role_name` (`role_name`,`permission_id`),
  KEY `role_permission_ibfk_2_idx` (`permission_id`),
  CONSTRAINT `role_permission_ibfk_1` FOREIGN KEY (`role_name`) REFERENCES `role` (`name`),
  CONSTRAINT `role_permission_ibfk_2` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`)
);

CREATE TABLE `user_role` (
  `user_name` varchar(40) NOT NULL,
  `role_name` varchar(40) NOT NULL,
  UNIQUE KEY `user_name` (`user_name`,`role_name`),
  KEY `role_name` (`role_name`),
  CONSTRAINT `user_role_ibfk_1` FOREIGN KEY (`user_name`) REFERENCES `user` (`name`),
  CONSTRAINT `user_role_ibfk_2` FOREIGN KEY (`role_name`) REFERENCES `role` (`name`)
);