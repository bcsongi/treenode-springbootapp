package com.fedev.treenode.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fedev.treenode.model.Content;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
/**
 *
 * @author csongor_balog
 */
@Service
public class ContentService {
        
    private final Logger log = LoggerFactory.getLogger(ContentService.class);

    private static final String CONTENT_BACKUP_FILE = "src/main/resources/contentBackup.json";
    private static final String IT_TEST_FILE = "src/main/resources/itTreeNode.json";

    private String contentFile = "src/main/resources/content.json";

    public void setFileNameForITTest() {
        contentFile = IT_TEST_FILE;
    }

    public Content create(final String mainContent) {
        log.debug("Service method to ContentService.create");
        final List<Content> contentList = getAll();
        final Long newId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        final Content newContent = new Content();
        newContent.setId(newId);
        newContent.setMainContent(mainContent);
        contentList.add(newContent);
        saveContent(contentList);    
        return newContent;
    }
    
    public Content update(final Content newContent) {
        log.debug("Service method to ContentService.update");
        final List<Content> contentList = getAll();
        for (Content content : contentList) {
            if (content.getId().equals(newContent.getId())) {
                content.setMainContent(newContent.getMainContent());
                saveContent(contentList);
                return content;
            }
        }
        return null;
    }
    
    public Content getById(final Long id) {
        log.debug("Service method to ContentService.getById");
        final ObjectMapper mapper = new ObjectMapper();
        final File file = new File(contentFile);
        try {
            final JsonNode array = mapper.readTree(file);
            for(int i=0; i < array.size(); i++){
                JsonNode jsonNode = array.get(i);
                JsonNode idNode = jsonNode.get("id");
                if (id.equals(idNode.longValue())) {
                    return mapper.treeToValue(jsonNode, Content.class);
                }
            }
        } catch (IOException ex) {
            throw new ServiceException("ContentService.getById doesn't work");
        }     
        return null;
    }
    
    public List<Content> getAll() {
        log.debug("Service method to ContentService.getAll");
        List<Content> contentList = new ArrayList<>();
        final ObjectMapper mapper = new ObjectMapper();
        final File file = new File(contentFile);
        try {
            final JsonNode array = mapper.readTree(file);
            for(int i = 0; i < array.size(); i++){
                contentList.add(mapper.treeToValue(array.get(i), Content.class));
            }
        } catch (IOException ex) {
            throw new ServiceException("ContentService.getAll doesn't work");
        }  
        return contentList;
    }
    
    private void saveContent(final List<Content> contentList) {
        log.debug("Service method to ContentService.saveContent");
        final File file = new File(contentFile);
        final ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(file, contentList);
        } catch (IOException ex) {
            throw new ServiceException("ContentService.saveContent doesn't work");
        }
    }
    
    public List<Content> createDefaultTree()  {
        log.debug("Service method to ContentService.createDefaultTree");
        final List<Content> contentList;
        final ObjectMapper mapper = new ObjectMapper();
        final File file = new File(contentFile);
        try {
            contentList = new ArrayList<>();
            Content content = new Content();
            content.setId(1L);
            content.setMainContent("rootNode_MainContent");
            contentList.add(content);
            mapper.writeValue(file, contentList);
        } catch (IOException ex) {
            throw new ServiceException("ContentService.createDefaultTree doesn't work");
        }
        return contentList;
    }
    
    public boolean generateTestData() {
        log.debug("Service method to ContentService.generateTestData");
        final Path original = Paths.get(CONTENT_BACKUP_FILE);
        final Path copied = Paths.get(contentFile);
        try {
            Files.copy(original, copied, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException ex) {
            throw new ServiceException("ContentService.generateTestData doesn't work");
        }
    }
    
}
