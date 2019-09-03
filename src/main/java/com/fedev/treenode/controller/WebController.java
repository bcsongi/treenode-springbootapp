/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fedev.treenode.controller;

import com.fedev.treenode.model.TreeNode;
import com.fedev.treenode.service.ContentService;
import com.fedev.treenode.service.TreeNodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 *
 * @author csongor_balog
 */
@Controller
public class WebController {
    
    private final Logger log = LoggerFactory.getLogger(WebController.class);

    @Autowired
    public ContentService contentService;

    @Autowired
    public TreeNodeService treeNodeService;

    @GetMapping(value = "/index", produces = MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView getHomePage(){
        log.debug("Controller method to getHomePage");
        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        List<TreeNode> treeNodeList = treeNodeService.getAll();
        if (treeNodeList.isEmpty()) {
            contentService.createDefaultTree();
            treeNodeList = treeNodeService.createDefaultTree();
        }
        modelAndView.addObject("treeNodeList", treeNodeList);
        return modelAndView;    
    }
    
}
