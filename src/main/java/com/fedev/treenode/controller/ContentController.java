package com.fedev.treenode.controller;

import com.fedev.treenode.model.Content;
import com.fedev.treenode.service.ContentService;
import com.fedev.treenode.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author csongor_balog
 */
@Controller
@RequestMapping("/content")
public class ContentController {

    private final Logger log = LoggerFactory.getLogger(ContentController.class);

    @Autowired
    public ContentService contentService;
    
    @GetMapping(value = "/getById", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Content> getById(final @RequestParam(value = "id") Long id){
        log.debug("Controller method to ContentController.getById");
        try {
            return new ResponseEntity<>(contentService.getById(id), HttpStatus.OK);
        } catch (ServiceException se) {
            log.error("Exception in ContentController.getById", se);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
