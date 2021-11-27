/*---------------- 创建测试数据 ----------------*/
# CREATE DATABASE test_database;
USE test_database;
CREATE TABLE test_table (
    id       INT AUTO_INCREMENT,
    column_2 VARCHAR(64) NULL,
    column_3 VARCHAR(64) NULL,
    column_4 VARCHAR(64) NULL,
    column_5 VARCHAR(64) NULL,
    column_6 VARCHAR(64) NULL,
    column_7 VARCHAR(64) NULL,
    column_8 VARCHAR(64) NULL,
    column_9 VARCHAR(64) NULL,
    CONSTRAINT test_table_id_uindex
        UNIQUE (id)
)
    COMMENT '测试表'
    ENGINE = MyISAM;

ALTER TABLE test_table
    ADD PRIMARY KEY (id);

/*---------------- 使用游标循环插入测试数据 ----------------*/
# 一百万测试数据
DELIMITER %
DROP PROCEDURE IF EXISTS proc_insertfoodata;
CREATE PROCEDURE proc_insertfoodata()
BEGIN
    DECLARE count INT DEFAULT 0;
    loop_label:
    LOOP
        IF count > 1000 * 1000 THEN
            LEAVE loop_label;
        ELSE
            INSERT INTO test_table(column_2, column_3, column_4, column_5, column_6, column_7, column_8, column_9)
            VALUES ('中文2', '中文3', '中文4', '中文5', '中文6', '中文7', '中文8', '中文9');
            SET count = count + 1;
        END IF;
    END LOOP;
END %
DELIMITER ;

# InnoDB和MyISAM的插入效率差的也太多了，用InnoDB插入10万数据花了超过十秒，用MyISAM插入90万数据也只花了不到9秒
# 需要学习一下InnoDB和MyISAM存储引擎的差别
CALL proc_insertfoodata();

SELECT COUNT(1)
FROM test_table;

/*---------------- 数据库参数查询 ----------------*/
# 查看表的存储引擎
SHOW CREATE TABLE test_table;
# 查看数据库是否开启了缓存
# 可能是查询的数据量太大了，显示并没有命中缓存，因此就不需要关闭缓存了
SHOW STATUS LIKE '%qcache%';
SHOW TABLE STATUS LIKE 'test_table';

/*---------------- 业务查询 ----------------*/
# 测试结果都基于m1 pro芯片平台docker版mariadb，并不具备横向比较性，只是作参考使用
# InnoDB：每秒读取二十万左右
# MyISAM：每秒读取也在二十万左右，并没有什么区别
# 不过既然生辰数据库使用的是MyISAM引擎，这里也就使用它测试
SELECT *
FROM test_table;
SELECT *
FROM test_table
WHERE id BETWEEN 1 AND 1;
SELECT *
FROM test_table LIMIT 2;
# 存储引擎更换为MyISAM
ALTER TABLE test_table
    ENGINE =MyISAM;

