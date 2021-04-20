package com.mydemo.project.dao;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.mydemo.project.entity.Resource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mydemo.project.vo.ResourceVO;
import com.mydemo.project.vo.TreeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 资源表 Mapper 接口
 * </p>
 *
 * @author allen
 * @since 2021-04-13
 */
public interface ResourceMapper extends BaseMapper<Resource> {

    /**
     * 查询当前登录人的资源
     * @param wrapper
     * @return 资源
     */
    List<ResourceVO> listResource(@Param(Constants.WRAPPER) Wrapper<Resource> wrapper);

    List<TreeVO> listResourceByRoleId(@Param(Constants.WRAPPER) Wrapper<Resource> wrapper
            ,@Param("roleId") Long roleId);
}
