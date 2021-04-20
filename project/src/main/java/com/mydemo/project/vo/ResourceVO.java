package com.mydemo.project.vo;

import lombok.Data;

import java.util.List;

/**
 * @author allen
 * @since 2021-04-14
 */
@Data
public class ResourceVO {

    /**
     * 主键
     */
    private Long resourceId;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 请求地址
     */
    private String url;

    /**
     * 下级资源
     */
    private List<ResourceVO> subs;
}
