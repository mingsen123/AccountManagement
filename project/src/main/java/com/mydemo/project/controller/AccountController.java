package com.mydemo.project.controller;


import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mydemo.project.entity.Account;
import com.mydemo.project.entity.Customer;
import com.mydemo.project.entity.Role;
import com.mydemo.project.query.AccountQuery;
import com.mydemo.project.service.AccountService;
import com.mydemo.project.service.RoleService;
import com.mydemo.project.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 账号表 前端控制器
 * </p>
 *
 * @author allen
 * @since 2021-04-13
 */
@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private RoleService roleService;

    /**
     * 进入账号列表页
     * @return
     */
    @GetMapping("toList")
    public String toList(){
        return "account/accountList";
    }

    /**
     * 查询账号列表
     * @return
     */
    @GetMapping("list")
    @ResponseBody
    public R<Map<String,Object>> list(AccountQuery accountQuery){

        //创建条件构造器
        QueryWrapper<Account> wrapper = Wrappers.<Account>query();
        //模糊查询真实姓名以及邮箱
        wrapper.like(StringUtils.isNotBlank(accountQuery.getRealName()),"a.real_name",accountQuery.getRealName())
                .like(StringUtils.isNotBlank(accountQuery.getEmail()),"a.email",accountQuery.getEmail())
                .orderByDesc("a.account_id").eq("a.deleted", 0);
        //获取时间范围
        String createTimeRange = accountQuery.getCreateTimeRange();
        //判断时间范围
        if(StringUtils.isNotBlank(createTimeRange)){
            //得到时间范围的两个数据
            String[] timeArray = createTimeRange.split(" - ");
            //条件构造器判断创建时间大于数组中第一个元素,并且小于数组中第二个元素
            wrapper.ge("a.create_time",timeArray[0])
                    .le("a.create_time",timeArray[1]);
        }
        //加入逻辑删除标识
        wrapper.eq("a.deleted",0).orderByDesc("a.account_id");
        //调用service中的方法，传入页、每页条数，以及条件构造器
        IPage<Account> myPage = accountService.accountPage(new Page<>(accountQuery.getPage(), accountQuery.getLimit()), wrapper);
        return ResultUtil.buildPageR(myPage);
    }
    /**
     * 进入新增页
     */
    @GetMapping("toAdd")
    public String toAdd(Model model){
        //通过条件构造器查询到role数据集
        List<Role> roles = roleService.list(Wrappers.<Role>lambdaQuery().orderByAsc(Role::getRoleId));
        //存进model中，让前端拿到
        model.addAttribute("roles",roles);
        return "account/accountAdd";
    }

    /**
     * 新增操作
     * @param account 账号
     * @return
     */
    @PostMapping
    @ResponseBody
    public R<Object> add(@RequestBody Account account){
        //通过公共方法
        setPasswordAndSalt(account);

        return ResultUtil.buildR(accountService.save(account));
    }

    /**
     * 设置加密密码和加密盐公共方法
     * @param account
     */
    private void setPasswordAndSalt(Account account){
        //获取密码
        String password = account.getPassword();
        String salt = UUID.fastUUID().toString().replaceAll("-", "");
        //创建MD5对象
        MD5 md5 = new MD5(salt.getBytes());
        //对密码进行加密
        String digestHex = md5.digestHex(password);
        //把密码重置成密文的
        account.setPassword(digestHex);
        account.setSalt(salt);
    }
    /**
     * 进入修改页
     */
    @GetMapping("toUpdate/{id}")
    public String toUpdate(@PathVariable Long id, Model model){
        //传入id得到需要修改的客户信息
        Account account = accountService.getById(id);
        model.addAttribute("account",account);
        //通过条件构造器查询到role数据集
        List<Role> roles = roleService.list(Wrappers.<Role>lambdaQuery().orderByAsc(Role::getRoleId));
        //存进model中，让前端拿到
        model.addAttribute("roles",roles);
        return "account/accountUpdate";
    }
    /**
     * 修改操作
     * @param account 账号信息
     * @return
     */
    @PutMapping
    @ResponseBody
    public R<Object> updateSubmit(@RequestBody Account account){
        //判断密码是否为空
        if(StringUtils.isNotBlank(account.getPassword())){
            //通过公共方法
            setPasswordAndSalt(account);
        }else {
            //不进行密码修改，避免不必要的错误
            account.setPassword(null);
        }

        return ResultUtil.buildR(accountService.updateById(account));
    }

    /**
     * 删除角色方法(不能删除自己)
     * @param id 角色id
     * @return
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public R<Object> delete(@PathVariable Long id, HttpSession session){

        //通过容器得到当前账户信息
        Account account = (Account) session.getAttribute("account");
        if(account.getAccountId().equals(id)){
            return R.failed("删除失败,不得删除自己的账号！");
        }
        return ResultUtil.buildR(accountService.removeById(id));
    }

    /**
     * 进入详情页
     */
    @GetMapping("toDetail/{id}")
    public String toDetail(@PathVariable Long id, Model model){
        //传入id得到需要修改的客户信息
        Account account = accountService.getAccountById(id);
        //把查询到的客户信息传入model
        model.addAttribute("account",account);
        //跳转页面
        return "account/accountDetail";
    }

    /**
     * 新增检验用户名统一方法
     * @param username 用户名
     * @return
     */
    @GetMapping("/{username}")
    @ResponseBody
    public R<Object> checkUsername(@PathVariable String username) {

        //根据用户名与数据库进行匹对，返回几个
        Integer count = accountService.lambdaQuery()
                .eq(Account::getUsername, username)
                .count();
        //返回数量，交由前端判断
        return R.ok(count);
    }
    /**
     * 更新时检验用户名统一方法
     * @param username 用户名
     * @return
     */
    @GetMapping("/{username}/{accountId}")
    @ResponseBody
    public R<Object> checkedUsername(@PathVariable String username
            ,@PathVariable Long accountId) {
        //根据用户名与数据库进行匹对，返回几个
        Integer count = accountService.lambdaQuery()
                .eq(Account::getUsername, username)
                //更新时排除自己
                .ne(accountId != null , Account::getAccountId,accountId)
                .count();
        //返回数量，交由前端判断
        return R.ok(count);
    }
}
