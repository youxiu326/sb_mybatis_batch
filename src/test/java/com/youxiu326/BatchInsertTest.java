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
