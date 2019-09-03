package com.fedev.treenode.service;

import com.fedev.treenode.model.TreeNode;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author csongor_balog
 */
@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class TreeNodeServiceIT {
    
    @Autowired
    private TreeNodeService treeNodeService;

    @Before
    public void setup() {
        treeNodeService.setFileNameForITTest();
    }

    @Test
    public void testCreateDefaultTree() {
        List<TreeNode> rootNode = treeNodeService.createDefaultTree();

        assertFalse(rootNode.isEmpty());
    }

    @Test
    public void testGetById_exists()  {
        final Long id = 1L;
        final Long contentId = 1L;

        TreeNode node = treeNodeService.getById(id);

        assertNotNull(node);
        assertEquals(node.getId(), id);
        assertEquals(node.getName(), "rootNode");
        assertEquals(node.getContentId(), contentId);
        assertNull(node.getParentId());
    }

    @Test
    public void test_Create_Update_Reorganize_Delete()  {
        // Create new TreeNode
        final String newName = "newName";
        final Long newContentId = 4L;
        final Long newParentId = 5L;
        final TreeNode newTreeNode = new TreeNode();
        newTreeNode.setName(newName);
        newTreeNode.setContentId(newContentId);
        newTreeNode.setParentId(newParentId);

        final TreeNode resultNewTreeNode = treeNodeService.create(newTreeNode);

        assertNotNull(resultNewTreeNode);
        assertNotNull(resultNewTreeNode.getId());
        assertEquals(resultNewTreeNode.getName(), newName);
        assertEquals(resultNewTreeNode.getContentId(), newContentId);
        assertEquals(resultNewTreeNode.getParentId(), newParentId);

        // Update the new TreeNode
        final String editedName = "editedName";
        final Long editedContentId = 6L;
        final Long editedParentId = 7L;
        final TreeNode editedTreeNode = new TreeNode();
        editedTreeNode.setId(resultNewTreeNode.getId());
        editedTreeNode.setName(editedName);
        editedTreeNode.setContentId(editedContentId);
        editedTreeNode.setParentId(editedParentId);

        final TreeNode editedNewTreeNode = treeNodeService.update(editedTreeNode);

        assertNotNull(editedNewTreeNode);
        assertNotNull(editedNewTreeNode.getId());
        assertEquals(editedNewTreeNode.getName(), editedName);
        assertEquals(editedNewTreeNode.getContentId(), editedContentId);
        assertEquals(editedNewTreeNode.getParentId(), editedParentId);

        // Reorganize ByParentId
        final Long reorganizeParentId = 8L;

        List<TreeNode> resultList = treeNodeService.reorganizeByParentId(editedNewTreeNode.getId(), reorganizeParentId);

        TreeNode node = resultList.stream()
                .filter(n -> editedNewTreeNode.getId().equals(n.getId()))
                .findAny()
                .orElse(null);
        assertNotNull(node);
        if (node != null)
            assertEquals(node.getParentId(), reorganizeParentId);

        // Delete TreeNode
        Boolean result = treeNodeService.deleteById(editedNewTreeNode.getId());
        assertTrue(result);
    }
    
}
