package com.mydemo.project.service.impl;

import com.mydemo.project.entity.Customer;
import com.mydemo.project.dao.CustomerMapper;
import com.mydemo.project.service.CustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户表 服务实现类
 * </p>
 *
 * @author allen
 * @since 2021-04-13
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

}
