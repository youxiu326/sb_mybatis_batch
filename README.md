
###### mybatis批量 新增 修改操作


[MySql 中 case when then else end 的用法](https://www.cnblogs.com/renpei/p/5485730.html)

[批量保存 Jpa saveAll() 和 JdbcTemplate batchUpdate()效率对比](https://blog.csdn.net/alex_fung/article/details/83009100)



```
MySql 中 case when then else end 的用法

SELECT
    case                   -------------如果
    when sex='1' then '男' -------------sex='1'，则返回值'男'
    when sex='2' then '女' -------------sex='2'，则返回值'女'
    else 0                 -------------其他的返回'其他’
    end                    -------------结束
from   sys_user            --------整体理解： 在sys_user表中如果sex='1'，则返回值'男'如果sex='2'，则返回值'女' 否则返回'其他’

---用法一：
SELECT
            CASE WHEN STATE = '1' THEN '成功'
                 WHEN STATE = '2' THEN '失败'
            ELSE '其他' END
            FROM  SYS_SCHEDULER
---用法二：
SELECT STATE
            CASE WHEN '1' THEN '成功'
                 WHEN '2' THEN '失败'
            ELSE '其他' END
            FROM  SYS_SCHEDULER



 例子:

 UPDATE test_user
 SET user_name =
 CASE
  WHEN test_user.user_name = (?) THEN ?
  WHEN test_user.user_name = (?) THEN ?
 END
 WHERE
 	(test_user.user_name =(?))
 	OR
 	(test_user.user_name =(?))






```


```
假如数据量很大约1000万条；写一个你认为最高效的SQL，用一个SQL计算以下四种人：
fsalary>9999 and fage > 35
fsalary>9999 and fage < 35
fsalary <9999 and fage > 35
fsalary <9999 and fage < 35
每种员工的数量；

SELECT
	sum(
		CASE WHEN fsalary > 9999 AND fage > 35 THEN 1
		ELSE 0
		END
	) AS "fsalary>9999_fage>35",
	sum(
		CASE WHEN fsalary > 9999 AND fage < 35 THEN 1
			ELSE 0
		END
	) AS "fsalary>9999_fage<35",
	sum(
		CASE WHEN fsalary < 9999 AND fage > 35 THEN 1
		ELSE 0
		END
	) AS "fsalary<9999_fage>35",
	sum(
		CASE WHEN fsalary < 9999 AND fage < 35 THEN 1
		ELSE 0
		END
	) AS "fsalary<9999_fage<35"
FROM
	empinfo;

```


```
prefix:在trim标签内sql语句加上前缀。
suffix:在trim标签内sql语句加上后缀。
suffixOverrides:指定去除多余的后缀内容，
如：suffixOverrides=","，去除trim标签内sql语句多余的后缀","

<trim prefix="values (" suffix=")" suffixOverrides=",">

</trim>

```