package com.fedev.treenode.controller;

import com.fedev.treenode.controller.dto.TreeNodeContentDTO;
import com.fedev.treenode.model.Content;
import com.fedev.treenode.model.TreeNode;
import com.fedev.treenode.service.ContentService;
import com.fedev.treenode.service.ServiceException;
import com.fedev.treenode.service.TreeNodeService;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author csongor_balog
 */
@Controller
@RequestMapping("/treenode")
public class TreeNodeController {
    
    private final Logger log = LoggerFactory.getLogger(TreeNodeController.class);

    @Autowired
    public ContentService contentService;
    
    @Autowired
    public TreeNodeService treeNodeService;
    
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TreeNodeContentDTO> create(final @RequestBody Map<String, String> createData){
        log.debug("Controller method to TreeNodeController.create");
        if (!createData.containsKey("name") || !createData.containsKey("parentId")
                || !createData.containsKey("mainContent")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            final Content newContent = contentService.create(createData.get("mainContent"));
            final TreeNode newTreeNode = new TreeNode();
            newTreeNode.setName(createData.get("name"));
            newTreeNode.setParentId(Long.parseLong(createData.get("parentId")));
            newTreeNode.setContentId(newContent.getId());
            return new ResponseEntity<>(new TreeNodeContentDTO(treeNodeService.create(newTreeNode), newContent), HttpStatus.OK);
        } catch (ServiceException se) {
            log.error("Exception in TreeNodeController.create", se);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TreeNodeContentDTO> update(final @RequestBody TreeNodeContentDTO treeNodeContentDTO){
        log.debug("Controller method to TreeNodeController.update");
        try {
            return new ResponseEntity<>(new TreeNodeContentDTO(treeNodeService.update(treeNodeContentDTO.getTreeNode()),
                    contentService.update(treeNodeContentDTO.getContent())), HttpStatus.OK);
        } catch (ServiceException se) {
            log.error("Exception in TreeNodeController.update", se);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TreeNode>> getAll(){
        log.debug("Controller method to TreeNodeController.getAll");
        try {
            return new ResponseEntity<>(treeNodeService.getAll(), HttpStatus.OK);
        } catch (ServiceException se) {
            log.error("Exception in TreeNodeController.getAll", se);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping(value = "/getById", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TreeNode> getById(final @RequestParam(value = "id") Long id){
        log.debug("Controller method to TreeNodeController.getById");
        try {
            return new ResponseEntity<>(treeNodeService.getById(id), HttpStatus.OK);
        } catch (ServiceException se) {
            log.error("Exception in TreeNodeController.getById", se);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping(value = "/deleteById", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> deleteById(final @RequestParam(value = "id") Long id){
        log.debug("Controller method to TreeNodeController.deleteById");
        try {
            return new ResponseEntity<>(treeNodeService.deleteById(id), HttpStatus.OK);
        } catch (ServiceException se) {
            log.error("Exception in TreeNodeController.deleteById", se);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }    
    }
    
    @PutMapping(value = "/reorganizeByParentId", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TreeNode>> reorganizeByParentId(final @RequestBody Map<String, Long> param){
        log.debug("Controller method to TreeNodeController.reorganizeByParentId");
        if (!param.containsKey("id") || !param.containsKey("parentId")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            return new ResponseEntity<>(treeNodeService.reorganizeByParentId(param.get("id"),  param.get("parentId")), HttpStatus.OK);
        } catch (ServiceException se) {
            log.error("Exception in TreeNodeController.reorganizeByParentId", se);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }        
    }
    
    @PostMapping(value = "/createDefaultTree", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TreeNode>> createDefaultTree(){
        log.debug("Controller method to TreeNodeController.createDefaultTree");
        try {
            return new ResponseEntity<>(treeNodeService.createDefaultTree(), HttpStatus.OK);
        } catch (ServiceException se) {
            log.error("Exception in TreeNodeController.createDefaultTree", se);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping(value = "/generateTestData")
    public ResponseEntity generateTestData() {
        log.debug("Controller method to TreeNodeController.generateTestData");
        try {
            contentService.generateTestData();
            treeNodeService.generateTestData();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ServiceException se) {
            log.error("Exception in TreeNodeController.generateTestData", se);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }        
    }
    
}
