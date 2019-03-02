CREATE TABLE event (
  `id` char(36) NOT NULL DEFAULT '',
  `event_type` varchar(100) DEFAULT NULL,
  `model_name` varchar(100) DEFAULT NULL,
  `model_id` char(36) DEFAULT NULL ,
  `create_time` bigint(11) DEFAULT NULL ,
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8

CREATE TABLE foo (
  `id` char(36) NOT NULL DEFAULT '',
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8


-- 在另一个库上执行
CREATE TABLE bar (
  `id` char(36) NOT NULL DEFAULT '',
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8