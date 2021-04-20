package com.mydemo.project.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mydemo.project.entity.Account;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mydemo.project.entity.Resource;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 账号表 Mapper 接口
 * </p>
 *
 * @author allen
 * @since 2021-04-13
 */
public interface AccountMapper extends BaseMapper<Account> {

    /**
     * 分页查询账号
     * @param page 页
     * @param wrapper 条件构造器
     * @return 页数据
     */
    IPage<Account> accountPage(Page<Account> page,@Param(Constants.WRAPPER) Wrapper<Account> wrapper);

    /**
     * 根据角色ID查找信息
     * @param id ID
     * @return 角色信息集
     */
    Account selectAccountById(Long id);

}
