
###### mybatis批量 新增 修改操作


[MySql 中 case when then else end 的用法](https://www.cnblogs.com/renpei/p/5485730.html)

[批量保存 Jpa saveAll() 和 JdbcTemplate batchUpdate()效率对比](https://blog.csdn.net/alex_fung/article/details/83009100)


_________________________________________________________________________

```
package com.youxiu326;

import com.youxiu326.dao.TestUserMapper;
import com.youxiu326.entity.TestUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BatchInsertTest {


    @Autowired
    private TestUserMapper userMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 测试批量新增
     */
    @Test
    public void testBatchInsert() {

        List<TestUser> userList = new ArrayList<TestUser>();

        for (int i = 1; i <= 5000; i++) {
            TestUser user = new TestUser();
            user.setUserName("a" + i);
            user.setCreateDate(new Date());
            userList.add(user);
        }
        int count = userMapper.batchInsertList(userList);
        System.out.println("此次新增了:" + count);
        System.out.println("此次新增了:" + count);
    }


    /**
     * jdbc实现批量新增
     */
    @Test
    public void testBatchJDBC() {

        Connection conn;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://youxiu326.xin:3306/mybatis", "root", "zz123456.ZZ");
            conn.setAutoCommit(false);//禁止自动提交

            String sql = "insert into test_user (user_name,create_date) value (?,SYSDATE())";

            PreparedStatement prest = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            for (int i = 1; i <= 100; i++) {
                prest.setString(1, "b" + i);
                prest.addBatch();
            }

            prest.executeBatch();
            conn.commit();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * JdbcTemplate批量新增
     */
    @Test
    public void testBatchJdbcTemplate() {

        List<TestUser> userList = new ArrayList<TestUser>();

        for (int i = 1; i <= 2; i++) {
            TestUser user = new TestUser();
            user.setUserName("c" + i);
            user.setCreateDate(new Date());
            userList.add(user);
        }
        Integer count = batchInsert(userList);

        System.out.println("本次新增了："+count);
        System.out.println("本次新增了："+count);

    }

    public Integer batchInsert(List<TestUser> userList) {

        String sql = "insert test_user(user_name,create_date) values(?,?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                String name = userList.get(i).getUserName();
                Date createDate = userList.get(i).getCreateDate();
                // util.date 转 sql.date
                java.sql.Date sqlDate = new java.sql.Date(createDate.getTime());
                ps.setString(1, name);
                ps.setDate(2, sqlDate);
            }
            public int getBatchSize() {
                return userList.size();
            }
        });
        return 0;
    }


    // 也可以自定义字段对应，但是要注意Object[]中元素的位置
//    public Integer batchInsertUsers(List<IK> list) {
//        String sql = "insert SWJ_IK1(name,count) values(?,?)";
//        jdbcTemplate.batchUpdate(sql,setParameters(list));
//        return 0;
//    }
//    private List<Object[]> setParameters(List<IK> list){
//        List<Object[]> parameters = new ArrayList<Object[]>();
//        for (IK ik : list) {
//            parameters.add(new Object[] { ik.getName(),ik.getCount()});
//        }
//        return parameters;
//    }


}

```



```
package com.youxiu326;

import com.youxiu326.dao.TestUserMapper;
import com.youxiu326.entity.TestUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BatchUpdateTest {

    @Autowired
    private TestUserMapper userMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 测试批量更新
     */
    @Test
    public void testBatchUpdate() {

        List<TestUser> userList = new ArrayList<TestUser>();

        for (int i = 1; i <= 2; i++) {
            TestUser user = new TestUser();
            user.setUserName("bb" + i);
            user.setUpdateUserName("cc" + i);
            userList.add(user);
        }
        int count = userMapper.batchUpdateList(userList);
        System.out.println("此次更新了:" + count);
        System.out.println("此次更新了:" + count);
    }

    /**
     * jdbc实现批量更新
     */
    @Test
    public void testBatchJDBC() {

        Connection conn;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://youxiu326.xin:3306/mybatis", "root", "zz123456.ZZ");
            conn.setAutoCommit(false);
            // 保存当前自动提交模式
            boolean autoCommit = conn.getAutoCommit();
            // 关闭自动提交
            conn.setAutoCommit(false);
            Statement stmt =conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            for (int i = 1; i <= 500; i++) {
                stmt.addBatch("update test_user set user_name = ('aaa"+i+"') where user_name = ('bb"+i+"')");
            }

            stmt.executeBatch();
            conn.commit();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * JdbcTemplate批量更新
     */
    @Test
    public void testBatchJdbcTemplate() {

        List<TestUser> userList = new ArrayList<TestUser>();

        for (int i = 1; i <= 2; i++) {
            TestUser user = new TestUser();
            user.setUserName("c" + i);
            user.setUpdateUserName("cc"+i);
            userList.add(user);
        }
        Integer count = batchUpdate(userList);

        System.out.println("本次更新了："+count);
        System.out.println("本次更新了："+count);

    }

     public Integer batchUpdate(List<TestUser> userList) {

        String sql = "update test_user set user_name=? where user_name=?";

         jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
             public void setValues(PreparedStatement ps, int i) throws SQLException {
                 String updateUserName = userList.get(i).getUpdateUserName();
                 String userName = userList.get(i).getUserName();
                 ps.setString(1, updateUserName);
                 ps.setString(2, userName);
             }

             public int getBatchSize() {
                 return userList.size();
             }
         });
         return 0;
    }

}

```










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