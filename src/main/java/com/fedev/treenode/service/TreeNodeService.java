package com.fedev.treenode.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fedev.treenode.model.TreeNode;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.UUID;

/**
 *
 * @author csongor_balog
 */
@Service
public class TreeNodeService {

    private final Logger log = LoggerFactory.getLogger(TreeNodeService.class);

    private static final String TREE_NODE_BACKUP_FILE = "src/main/resources/treeNodeBackup.json";
    private static final String IT_TEST_FILE = "src/main/resources/itTreeNode.json";

    private String treeNodeFile = "src/main/resources/treeNode.json";

    public void setFileNameForITTest() {
        treeNodeFile = IT_TEST_FILE;
    }

    public TreeNode create(final TreeNode newTreeNode) {
        log.debug("Service method to TreeNodeService.create");
        final List<TreeNode> treeNodeList = getAll();
        if (newTreeNode.getId() == null) {
            final Long newId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
            newTreeNode.setId(newId);
        }
        treeNodeList.add(newTreeNode);
        saveTreeNode(treeNodeList);
        return newTreeNode;
    }
    
    public TreeNode update(final TreeNode newTreeNode) {
        log.debug("Service method to TreeNodeService.update");
        final List<TreeNode> treeNodeList = getAll();
        for (TreeNode treeNode : treeNodeList) {
            if (treeNode.getId().equals(newTreeNode.getId())) {
                treeNode.setName(newTreeNode.getName());
                treeNode.setContentId(newTreeNode.getContentId());
                treeNode.setParentId(newTreeNode.getParentId());
                saveTreeNode(treeNodeList);
                return treeNode;
            }
        }
        return null;
    }
    
    public List<TreeNode> getAll() {
        log.debug("Service method to TreeNodeService.getAll");
        List<TreeNode> treeNodeList = new ArrayList<>();
        final ObjectMapper mapper = new ObjectMapper();
        final File file = new File(treeNodeFile);
        try {
            if(file.exists()) {
                final JsonNode array = mapper.readTree(file);
                for (int i = 0; i < array.size(); i++) {
                    treeNodeList.add(mapper.treeToValue(array.get(i), TreeNode.class));
                }
            }
        } catch (IOException ex) {
            throw new ServiceException("TreeNodeService.getAll doesn't work");
        }  
        return treeNodeList;
    }

    public TreeNode getById(final Long id) {
        log.debug("Service method to TreeNodeService.getById");
        final ObjectMapper mapper = new ObjectMapper();
        final File file = new File(treeNodeFile);
        try {
            final JsonNode array = mapper.readTree(file);
            for(int i=0; i < array.size(); i++){
                JsonNode jsonNode = array.get(i);
                JsonNode idNode = jsonNode.get("id");
                if (id.equals(idNode.longValue())) {
                    return mapper.treeToValue(jsonNode, TreeNode.class);
                }
            }
        } catch (IOException ex) {
            throw new ServiceException("TreeNodeService.getById doesn't work");
        }     
        return null;
    }

    public Boolean deleteById(final Long id) {
        log.debug("Service method to TreeNodeService.deleteById");
        final List<TreeNode> treeNodeList = Collections.synchronizedList(getAll());
        final ListIterator<TreeNode> iter = treeNodeList.listIterator();
        while(iter.hasNext()){
            if(iter.next().getId().equals(id)){
                iter.remove();
                deleteByParentId(treeNodeList, id);
                saveTreeNode(treeNodeList);
                return true;
            }
        }
        return false;
    }
    
    private void deleteByParentId(final List<TreeNode> treeNodeList, final Long parentId) {
        ListIterator<TreeNode> iter = treeNodeList.listIterator();
        while(iter.hasNext()){
            TreeNode node = iter.next();
            Long id = node.getId();
            if(parentId != null && parentId.equals(node.getParentId())){
                iter.remove();
                deleteByParentId(treeNodeList, parentId);
                deleteByParentId(treeNodeList, id);
                break;
            }
        }
    }

    public List<TreeNode> reorganizeByParentId(final Long id, final Long parentId) {
        log.debug("Service method to TreeNodeService.reorganizeByParentId");
        final List<TreeNode> treeNodeList = getAll();
        for (TreeNode treeNode : treeNodeList) {
            if (treeNode.getId().equals(id)) {
                treeNode.setParentId(parentId);
                break;
            }
        }
        saveTreeNode(treeNodeList);
        return treeNodeList;
    }
    
    private void saveTreeNode(final List<TreeNode> treeNodeList) {
        log.debug("Service method to TreeNodeService.saveTreeNode");
        final File file = new File(treeNodeFile);
        final ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(file, treeNodeList);
        } catch (IOException ex) {
            throw new ServiceException("TreeNodeService.saveTreeNode doesn't work");
        }
    }

    public List<TreeNode> createDefaultTree()  {
        log.debug("Service method to TreeNodeService.createDefaultTree");
        final List<TreeNode> treeNodeList;
        final ObjectMapper mapper = new ObjectMapper();
        final File file = new File(treeNodeFile);
        try {
            treeNodeList = new ArrayList<>();
            TreeNode rootNode = new TreeNode();
            rootNode.setId(1L);
            rootNode.setName("rootNode");
            rootNode.setContentId(1L);
            treeNodeList.add(rootNode);
            mapper.writeValue(file, treeNodeList);
        } catch (IOException ex) {
            throw new ServiceException("TreeNodeService.createDefaultTree doesn't work");
        }
        return treeNodeList;
    }
    
    public Boolean generateTestData() {
        log.debug("Service method to TreeNodeService.generateTestData");
        final Path original = Paths.get(TREE_NODE_BACKUP_FILE);
        final Path copied = Paths.get(treeNodeFile);
        try {
            Files.copy(original, copied, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException ex) {
            throw new ServiceException("TreeNodeService.generateTestData doesn't work");
        }
    }
    
}
