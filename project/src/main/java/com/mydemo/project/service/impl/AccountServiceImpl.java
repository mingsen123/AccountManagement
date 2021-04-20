package com.mydemo.project.service.impl;

import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mydemo.project.dto.LoginDTO;
import com.mydemo.project.entity.Account;
import com.mydemo.project.dao.AccountMapper;
import com.mydemo.project.service.AccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 账号表 服务实现类
 * </p>
 *
 * @author allen
 * @since 2021-04-13
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Override
    public LoginDTO login(String username, String password) {

        //创建返回类型对象
        LoginDTO loginDTO = new LoginDTO();
        //默认页面跳转到登陆页
        loginDTO.setPath("redirect:/");
        //根据用户名查询一条记录，返回一条记录
        Account account = lambdaQuery().eq(Account::getUsername,username).one();
        //判断用户名是否存在
        if(null == account){
            loginDTO.setError("用户名不存在");
            return loginDTO;
        }

        //判断密码，使用加密盐
        MD5 md5 = new MD5(account.getSalt().getBytes());
        String digestHex = md5.digestHex(password);
        //判断密码是否与传入的加密盐一致（不一致的处理判断）
        if(!digestHex.equals(account.getPassword())){
            loginDTO.setError("密码错误");
            return loginDTO;
        }
        //设置跳转路径(后台主页)
        //把正确的记录传入到loginDTO
        loginDTO.setAccount(account);
        loginDTO.setPath("login/main");
        return loginDTO;

    }


    /**
     * 分页查询账号
     * @param page 页
     * @param wrapper 条件构造器
     * @return 页数据
     */
    @Override
    public IPage<Account> accountPage(Page<Account> page, Wrapper<Account> wrapper) {
        return baseMapper.accountPage(page,wrapper);
    }

    @Override
    public Account getAccountById(Long id) {
        return baseMapper.selectAccountById(id);
    }
}
