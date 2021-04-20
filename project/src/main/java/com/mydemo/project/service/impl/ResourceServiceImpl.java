package com.mydemo.project.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mydemo.project.entity.Resource;
import com.mydemo.project.dao.ResourceMapper;
import com.mydemo.project.service.ResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mydemo.project.vo.ResourceVO;
import com.mydemo.project.vo.TreeVO;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 资源表 服务实现类
 * </p>
 *
 * @author allen
 * @since 2021-04-13
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    /**
     * 根据角色ID查询该角色所具有的资源
     * @param roleId 角色ID
     * @return 角色资源
     */
    @Override
    public List<ResourceVO> listResourceByRoleId(Long roleId) {
        QueryWrapper<Resource> query = Wrappers.<Resource>query();
        //查询第一级目录
        query.eq("rr.role_id",roleId).isNull("re.parent_id")
        .orderByAsc("re.sort");
        List<ResourceVO> resourceVOS = baseMapper.listResource(query);
        //查询下级资源
        resourceVOS.forEach(r->{
            Long resourceId = r.getResourceId();
            //使用条件构造器
            QueryWrapper<Resource>  subWrapper = Wrappers.<Resource>query();
            subWrapper.eq("rr.role_id",roleId)
                    .eq("re.parent_id",resourceId).orderByAsc("re.sort");
            List<ResourceVO> subResourceVOS = baseMapper.listResource(subWrapper);
            //判断是否为空
            if(CollectionUtils.isNotEmpty(resourceVOS)){
                r.setSubs(subResourceVOS);
            }
        });
        return resourceVOS;
    }

    /**
     * 查询系统资源
     * @return
     */
    @Override
    public List<TreeVO> listResource(Long roleId,Integer flag) {
        //判断角色ID
        if(null == roleId){
            //创建条件构造器，获取父类ID不为NULL的资源按Sort进行顺序排序
            LambdaQueryWrapper<Resource> wrapper =
                    Wrappers.<Resource>lambdaQuery().isNull(Resource::getParentId).orderByAsc(Resource::getSort);
            //获得查询到的结果集
            List<Resource> resources = list(wrapper);
            //
            List<TreeVO> treeVOS = resources.stream().map(r -> {
                TreeVO treeVO = new TreeVO();
                treeVO.setId(r.getResourceId());
                treeVO.setTitle(r.getResourceName());
                //通过条件构造器查询到上级ID等于资源ID
                LambdaQueryWrapper<Resource> subWrapper =
                        Wrappers.<Resource>lambdaQuery().eq(Resource::getParentId, r.getResourceId()).orderByAsc(Resource::getSort);
                //返回查询到的数据集
                List<Resource> subResources = list(subWrapper);
                //判断是否有下级
                //可以使用递归
                if (CollectionUtils.isNotEmpty(subResources)) {
                    List<TreeVO> children = subResources.stream().map(sub -> {
                        TreeVO subTreeVO = new TreeVO();
                        subTreeVO.setId(sub.getResourceId());
                        subTreeVO.setTitle(sub.getResourceName());
                        return subTreeVO;
                    }).collect(Collectors.toList());
                    treeVO.setChildren(children);
                }
                return treeVO;
            }).collect(Collectors.toList());
            return treeVOS;
            //修改方法查资源数
        }else {
            QueryWrapper<Resource> query = Wrappers.<Resource>query();
            query.eq(flag == 1,"rr.role_id",roleId)
                    .isNull("re.parent_id").orderByAsc("re.sort");
            List<TreeVO> treeVOS = baseMapper.listResourceByRoleId(query, roleId);
            treeVOS.forEach(t ->{
                t.setChecked(false);
                Long id = t.getId();
                QueryWrapper<Resource>  subWrapper = Wrappers.<Resource>query();
                subWrapper.eq(flag == 1,"rr.role_id",roleId)
                        .eq("re.parent_id",id).orderByAsc("re.sort");
                List<TreeVO> children = baseMapper.listResourceByRoleId(subWrapper, roleId);
                //判断下级是否存在
                if(CollectionUtils.isNotEmpty(children)){
                    t.setChildren(children);
                }
            });
            return treeVOS;
        }
    }

    /**
     * 权限拦截器方法
     * @param resourceVOS
     * @return
     */
    @Override
    public HashSet<String> convert(List<ResourceVO> resourceVOS) {
        HashSet<String> module = new HashSet<>();
        resourceVOS.forEach(r ->{
            String url = r.getUrl();
            if(StringUtils.isNotBlank(url)){
                module.add(url.substring(0,url.indexOf("/")));
            }

            List<ResourceVO> subResourceVOs = r.getSubs();
            if(CollectionUtils.isNotEmpty(subResourceVOs)){
                subResourceVOs.forEach(sub ->{
                    String subUrl = sub.getUrl();
                    if(StringUtils.isNotBlank(subUrl)){
                        module.add(subUrl.substring(0,subUrl.indexOf("/")));
                    }
                });
            }
        });
        return module;
    }
}
