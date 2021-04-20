package com.mydemo.project.service;

import com.mydemo.project.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author allen
 * @since 2021-04-13
 */
public interface RoleService extends IService<Role> {
    /**
     * 新增角色及角色所具有的资源
     * @param role
     * @return
     */
    boolean saveRole(Role role);

    /**
     * 修改角色及角色所具有的资源
     * @param role
     * @return
     */
    boolean updateRole(Role role);

}
