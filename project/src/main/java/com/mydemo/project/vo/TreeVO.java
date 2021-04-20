package com.mydemo.project.vo;

import lombok.Data;

import java.util.List;

/**
 * @author allen
 */
@Data
public class TreeVO {

    private String title;

    private Long id;

    private List<TreeVO> children;

    private boolean checked;
}
