package com.youxiu326.dao;

import com.youxiu326.entity.TestUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TestUserMapper {

    int insert(TestUser record);

    int insertSelective(TestUser record);

    /**
     * 批量新增方法
     * @param userList
     * @return 新增条数
     */
    int batchInsertList(@Param("userList") List<TestUser> userList);


    /**
     * 批量更新方法
     * @param userList
     * @return 更新条数
     */
    int batchUpdateList(@Param("userList") List<TestUser> userList);

}