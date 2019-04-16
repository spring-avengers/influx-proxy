-- ----------------------------
-- Table structure for management_info
-- ----------------------------
-- DROP TABLE IF EXISTS `management_info`;
CREATE TABLE `management_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `node_url` varchar(255)  NOT NULL,
  `database_name` varchar(255)  NOT NULL,
  `retention_name` varchar(255)  NOT NULL,
  `retention_duration` int(11)  NOT NULL,
  `delete_tag` int(4) NOT NULL DEFAULT 0,
  `is_default` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;