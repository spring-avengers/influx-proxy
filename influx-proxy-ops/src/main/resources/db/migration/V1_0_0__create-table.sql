SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for backend_node
-- ----------------------------
-- DROP TABLE IF EXISTS `backend_node`;
CREATE TABLE `backend_node` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `health_check_interval` int(11) DEFAULT NULL,
  `node_name` varchar(255) DEFAULT NULL,
  `online` bit(1) NOT NULL,
  `query_timeout` int(11) DEFAULT NULL,
  `status_description` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `write_timeout` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for key_mapping
-- ----------------------------
-- DROP TABLE IF EXISTS `key_mapping`;
CREATE TABLE `key_mapping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `backend_node_names` varchar(255) DEFAULT NULL,
  `database_regex` varchar(255) DEFAULT NULL,
  `measurement_regex` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for topology
-- ----------------------------
-- DROP TABLE IF EXISTS `topology`;
CREATE TABLE `topology` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `database_name` varchar(255) DEFAULT NULL,
  `measurement` varchar(255) DEFAULT NULL,
  `node_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
