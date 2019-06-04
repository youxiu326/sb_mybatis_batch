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
