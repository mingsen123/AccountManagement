package com.mydemo.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mydemo.project.dao.RoleResourceMapper;
import com.mydemo.project.entity.Role;
import com.mydemo.project.dao.RoleMapper;
import com.mydemo.project.entity.RoleResource;
import com.mydemo.project.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author allen
 * @since 2021-04-13
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleResourceMapper roleResourceMapper;

    /**
     * 新增角色及角色所具有的资源
     * @param role
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRole(Role role) {
        //保存role对象
        save(role);
        //获取role对象的ID
        Long roleId = role.getRoleId();
        //获取选中的资源
        List<Long> resourceIds = role.getResourceIds();
        //判断选中资源数
        if(CollectionUtils.isNotEmpty(resourceIds)){
            //将资源ID遍历
            for(Long resourceId :resourceIds){
                //创建角色资源对象
                RoleResource roleResource = new RoleResource();
                //设置角色ID及资源ID
                roleResource.setRoleId(roleId);
                roleResource.setResourceId(resourceId);
                roleResourceMapper.insert(roleResource);
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(Role role) {
        Long roleId = role.getRoleId();

        updateById(role);

        System.out.println("role = " + role);
        System.out.println("role.getRoleId() = " + role.getRoleId());

        roleResourceMapper.delete(Wrappers.<RoleResource>lambdaQuery().eq(RoleResource::getRoleId, roleId));

        List<Long> resourceIds = role.getResourceIds();

        if (CollectionUtils.isNotEmpty(resourceIds)) {
            for (Long resourceId : resourceIds) {
                RoleResource roleResource = new RoleResource();
                roleResource.setRoleId(roleId);
                roleResource.setResourceId(resourceId);
                roleResourceMapper.insert(roleResource);
            }
        }

        return true;
    }
}
