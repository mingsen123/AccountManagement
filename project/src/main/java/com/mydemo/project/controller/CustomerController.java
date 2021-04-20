package com.mydemo.project.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mydemo.project.entity.Customer;
import com.mydemo.project.service.CustomerService;
import com.mydemo.project.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.Map;

/**
 * <p>
 * 客户表 前端控制器
 * </p>
 *
 * @author allen
 * @since 2021-04-13
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    /**
     * 进入列表页的方法
     * @return 列表页路径
     */
    @GetMapping("toList")
    public String toList(){
        return "customer/customerList";
    }

    /**
     * 查询方法
     * @param realName 客户真实姓名
     * @param phone 客户手机号
     * @param page 数据页
     * @param limit 每页的限制数
     * @return 返回客户的list集合，就是客户信息的条数
     */
    //统一传参
    //其中这个R为JDK自带的一个类，以过期，如需要可以自己根据源码写一个类似的。
    @GetMapping("list")
    @ResponseBody
    public R<Map<String,Object>> list(String realName,String phone,Long page,Long limit){
        //条件构造器,mybatis-plus中特有的方法,可以查看MP官网的文档进行学习
        //条件构造器的作用是对sql语句进行再次封装,不用通过SQL语句进行CRUD操作,直接在代码中体现
        LambdaQueryWrapper<Customer> wrapper = Wrappers.<Customer>lambdaQuery()
                //like模糊查询，先判断realName是否为空StringUtils.isNotBlank方法，然后得到实体类中的realName
                .like(StringUtils.isNotBlank(realName), Customer::getRealName, realName)
                //判断手机号是否为空，得到Customer中的phone
                .like(StringUtils.isNotBlank(phone), Customer::getPhone, phone)
                //SQL语句中的order by,按Customer中的主键倒序排列
                .orderByDesc(Customer::getCustomerId);
        //通过customerService中的page方法（mybatis-plus自带的）,得到一个Page对象，泛型为Customer
        Page<Customer> myPage = customerService.page(new Page<>(page, limit), wrapper);
        /**以下代码都通过方法封装在util.ResultUtil中：传入Page对象参数，通过类中的方法得到返回值。
        因为泛型为Map,需要得到一个Map对象,通过HashMap得到
        Map<String, Object> data = new HashMap<>();
        添加元素
        添加总记录数
        data.put("count",myPage.getTotal());
        添加
        data.put("records",myPage.getRecords());
        返回数据,通过R.ok方法
        return R.ok(data);*/
        /**
         * //第二种查询方法，链式调用
         * //不用创建条件构造器
         * //返回一个page
         * Page<Customer> myPage = customerService.lambdaQuery
         *                 .like(StringUtils.isNotBlank(realName), Customer::getRealName, realName)
         *                 .like(StringUtils.isNotBlank(phone), Customer::getPhone, phone)
         *                 .orderByDesc(Customer::getCustomerId)
         *                 //直接.page,然后传入page对象
         *                 .page(new Page<>(page, limit));
         */
        //返回
        return ResultUtil.buildPageR(myPage);
    }

    //跳转到新增页,找到customerAdd.html
    /**
     * 进入新增页
     */
    @GetMapping("toAdd")
    public String toAdd(){
        return "customer/customerAdd";
    }

    /**
     * 新增客户方法
     * @param customer 客户数据信息
     * @return
     */
    @PostMapping
    @ResponseBody
    public R<Object> add(@RequestBody Customer customer){
        //新增客户信息数据返回是否保存成功
        //boolean success = customerService.save(customer);
        //判断是否成功
        //if(success){
            //保存成功不用返回数据
            //return R.ok(null);
        //}
        //保存失败
        //return R.failed("操作失败");
        return ResultUtil.buildR(customerService.save(customer));
    }


    /**
     * 进入修改页
     */
    @GetMapping("toUpdate/{id}")
    public String toUpdate(@PathVariable Long id, Model model){
        //传入id得到需要修改的客户信息
        Customer customer = customerService.getById(id);
        model.addAttribute("customer",customer);
        return "customer/customerUpdate";
    }

    /**
     * 修改客户方法
     * @param customer 客户数据信息
     * @return
     */
    @PutMapping
    @ResponseBody
    public R<Object> update(@RequestBody Customer customer){
        return ResultUtil.buildR(customerService.updateById(customer));
    }

    /**
     * 删除客户方法
     * @param id 客户id
     * @return
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public R<Object> delete(@PathVariable Long id){
        return ResultUtil.buildR(customerService.removeById(id));
    }

    /**
     * 进入详情页
     */
    @GetMapping("toDetail/{id}")
    public String toDetail(@PathVariable Long id, Model model){
        //传入id得到需要修改的客户信息
        Customer customer = customerService.getById(id);
        //把查询到的客户信息传入model
        model.addAttribute("customer",customer);
        //跳转页面
        return "customer/customerDetail";
    }
}
