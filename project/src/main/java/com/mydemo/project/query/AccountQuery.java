package com.mydemo.project.query;

import lombok.Data;

/**
 * @author allen
 */
@Data
public class AccountQuery {

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 时间范围
     */
    private String createTimeRange;

    /**
     * 页码
     */
    private Long page;

    /**
     * 每页的限制数据
     */
    private Long limit;
}
