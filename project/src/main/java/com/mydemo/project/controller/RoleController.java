package com.mydemo.project.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mydemo.project.entity.Account;
import com.mydemo.project.entity.Role;
import com.mydemo.project.query.AccountQuery;
import com.mydemo.project.service.AccountService;
import com.mydemo.project.service.ResourceService;
import com.mydemo.project.service.RoleService;
import com.mydemo.project.util.ResultUtil;
import com.mydemo.project.vo.TreeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author allen
 * @since 2021-04-13
 */
@Controller
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private AccountService accountService;

    /**
     * 进入角色详情页
     * @return
     */
    @GetMapping("toList")
    public String toList(){
        return "role/roleList";
    }

    /**
     * 查询方法
     * @param roleName 角色名称
     * @param page 页
     * @param limit 每页限制数
     * @return
     */
    @GetMapping("list")
    @ResponseBody
    public R<Map<String, Object>> list(String roleName, Long page, Long limit) {

        LambdaQueryWrapper<Role> wrapper = Wrappers.<Role>lambdaQuery().like(StringUtils.isNotBlank(roleName),
                Role::getRoleName, roleName).orderByDesc(Role::getRoleId);
        Page<Role> rolePage = roleService.page(new Page<>(page, limit), wrapper);
        return ResultUtil.buildPageR(rolePage);
    }

    /**
     * 进入新增页
     * @return
     */
    @GetMapping("toAdd")
    public String intoAdd(){
        return "role/roleAdd";
    }

    /**
     * 新增操作
     * @param role 角色对象
     * @return
     */
    @PostMapping
    @ResponseBody
    public R<Object> add(@RequestBody Role role){


        return ResultUtil.buildR(roleService.saveRole(role));
    }

    /**
     * 进入修改页
     * @param id
     * @param model
     * @return
     */
    @GetMapping("toUpdate/{id}")
    public String toUpdate(@PathVariable Long id, Model model){

        System.out.println("更新页 id = " + id);
        Role role = roleService.getById(id);
        model.addAttribute("role", role);
        return "role/roleUpdate";
    }

    /**
     * 修改操作
     * @param role
     * @return 修改的结果
     */
    @PutMapping
    @ResponseBody
    public R<Object> updateSubmit(@RequestBody Role role){

        return ResultUtil.buildR(roleService.updateRole(role));
    }

    /**
     * 进入详情页
     */
    @GetMapping("toDetail/{id}")
    public String toDetail(@PathVariable Long id, Model model){
        Role role = roleService.getById(id);
        System.out.println("role =" + role);
        model.addAttribute("role", role);
        //跳转页面
        return "role/roleDetail";
    }

    /**
     * 删除角色方法
     * @param id 角色id
     * @return
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public R<Object> delete(@PathVariable Long id){

        //获取账号占有该角色的数量
        Integer count = accountService.lambdaQuery().eq(Account::getRoleId, id).count();
        if (count > 0) {
            return R.failed("有账号正拥有该角色");
        }
        return ResultUtil.buildR(roleService.removeById(id));
    }

    @GetMapping({"listResource","listResource/{roleId}","listResource/{roleId}/{flag}"})
    @ResponseBody
    public R<List<TreeVO>> listResource(@PathVariable(required = false) Long roleId
            ,@PathVariable(required = false) Integer flag){
        return R.ok(resourceService.listResource(roleId,flag));
    }
}
