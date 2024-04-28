package com.test.file.data;

import lombok.Data;

import java.util.List;

/**
 * 脑图节点对象
 * @author amos.tong
 */
@Data
public class ProcessOnNode {
    private String parent;
    private List<ProcessOnNode> children;
    private String id;
    private String title;
}
