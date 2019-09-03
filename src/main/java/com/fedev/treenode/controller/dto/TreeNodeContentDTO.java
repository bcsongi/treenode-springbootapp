package com.fedev.treenode.controller.dto;

import com.fedev.treenode.model.Content;
import com.fedev.treenode.model.TreeNode;

/**
 *
 * @author csongor_balog
 */
public class TreeNodeContentDTO {
    
    private TreeNode treeNode;
    private Content content;

    public TreeNodeContentDTO() {
        
    }
    
    public TreeNodeContentDTO(final TreeNode treeNode, final Content content) {
        this.treeNode = treeNode;
        this.content = content;
    }

    public TreeNode getTreeNode() {
        return treeNode;
    }

    public void setTreeNode(final TreeNode treeNode) {
        this.treeNode = treeNode;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(final Content content) {
        this.content = content;
    }
    
}
