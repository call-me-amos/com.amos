package com.test.file.data;

import lombok.Data;

import java.util.List;

/**
 * @author amos.tong
 */
@Data
public class ProcessOnNode {
    private String parent;
    private List<ProcessOnNode> children;
    private String id;
    private String title;
}
