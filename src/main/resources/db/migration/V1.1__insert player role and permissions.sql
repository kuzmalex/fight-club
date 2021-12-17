INSERT INTO `permission` VALUES (1,'/'),(2,'/login'),(3,'/start'),(4,'/hit'),(5,'/logout');
INSERT INTO `role` VALUES ('guest'),('player'),('admin');
INSERT INTO `role_permission` VALUES ('player',1),('player',2),('player',3),('player',4),('player',5);
INSERT INTO `role_permission` VALUES ('admin',1),('admin',2),('admin',3),('admin',4),('admin',5);
INSERT INTO `role_permission` VALUES ('guest',1),('guest',2);