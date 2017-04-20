CREATE TABLE `legal_base` (
  `id` varchar(32) NOT NULL DEFAULT '' COMMENT '主键：md5(case_id+law_file+law_file_item)',
  `case_id` varchar(50) DEFAULT NULL COMMENT '案件信息表外键',
  `law_file` varchar(200) DEFAULT NULL COMMENT '法律文件',
  `law_file_item` varchar(200) DEFAULT NULL COMMENT '第几条',
  `law_file_item_content` text COMMENT '法条内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='案件引用法律信息';

CREATE TABLE `law_case` (
  `case_id` varchar(50) NOT NULL COMMENT '主键',
  `title` varchar(200) DEFAULT NULL COMMENT '标题',
  `content` longtext COMMENT '内容',
  `pub_date` char(8) DEFAULT NULL COMMENT '发布日期：yyyyMMdd',
  `court` varchar(200) DEFAULT NULL COMMENT '审理法院',
  `case_ah` varchar(255) DEFAULT '' COMMENT '案号',
  `case_type` varchar(100) DEFAULT NULL COMMENT '案件类型',
  `cause_action` varchar(250) DEFAULT NULL COMMENT '案由',
  `case_date` char(8) DEFAULT NULL COMMENT '裁判日期:yyyyMMdd',
  `case_dsr` varchar(250) DEFAULT NULL COMMENT '当事人',
  `case_slcx` varchar(100) DEFAULT NULL COMMENT '审理程序',
  `create_time` char(14) DEFAULT NULL COMMENT '创建时间：yyyyMMddHHmmss',
  `crawler_url` varchar(250) DEFAULT NULL COMMENT '页面地址',
  `crawler_task_id` varchar(255) DEFAULT NULL COMMENT '抽取任务id外键',
  PRIMARY KEY (`case_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='案件信息表';

CREATE TABLE `crawler_log` (
  `id` varchar(14) NOT NULL DEFAULT '' COMMENT '主键，yyyyMMddHHmmss如20160415113900',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `status` char(1) DEFAULT '0' COMMENT '状态：0开始，1完成',
  `finish_time` datetime DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='爬虫日志表';

CREATE TABLE `zui_ming` (
  `bh` char(6) NOT NULL DEFAULT '' COMMENT '编码',
  `mc` varchar(200) DEFAULT NULL COMMENT '名称',
  `parent_bh` varchar(10) DEFAULT NULL COMMENT '父级编号，顶级为000000',
  PRIMARY KEY (`bh`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='罪名表';


