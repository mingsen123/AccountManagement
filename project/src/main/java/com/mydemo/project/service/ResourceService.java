package com.mydemo.project.service;

import com.mydemo.project.entity.Resource;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mydemo.project.vo.ResourceVO;
import com.mydemo.project.vo.TreeVO;

import java.util.HashSet;
import java.util.List;

/**
 * <p>
 * 资源表 服务类
 * </p>
 *
 * @author allen
 * @since 2021-04-13
 */
public interface ResourceService extends IService<Resource> {

    /**
     * 根据角色ID查询该角色所具有的资源
     * @param roleId 角色ID
     * @return 角色资源
     */
    List<ResourceVO> listResourceByRoleId(Long roleId);

    /**
     * 查询系统中所有资源
     * @return
     */
    List<TreeVO> listResource(Long roleId,Integer flag);

    /**
     * 资源拦截器
     * @param resourceVOS
     * @return
     */
    HashSet<String> convert(List<ResourceVO> resourceVOS);
}
