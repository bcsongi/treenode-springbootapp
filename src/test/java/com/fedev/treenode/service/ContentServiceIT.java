package com.fedev.treenode.service;

import com.fedev.treenode.model.Content;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author csongor_balog
 */
@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class ContentServiceIT {
    
    @Autowired
    private ContentService contentService;

    @Before
    public void setup() {
        contentService.setFileNameForITTest();
    }

    @Test
    public void testCreateDefaultTree() {
        List<Content> rootContent = contentService.createDefaultTree();

        assertFalse(rootContent.isEmpty());
    }

    @Test
    public void testGetById_exists()  {
        final Long contentId = 1L;
        final String mainContent = "rootNode_MainContent";

        Content content = contentService.getById(contentId);

        assertNotNull(content);
        assertEquals(content.getId(), contentId);
        assertEquals(content.getMainContent(), mainContent);
    }

    @Test
    public void test_Create_Update()  {
        // Create new Content
        final String newMainContent = "newMainContent";

        final Content resultNewContent = contentService.create(newMainContent);

        assertNotNull(resultNewContent);
        assertNotNull(resultNewContent.getId());
        assertEquals(resultNewContent.getMainContent(), newMainContent);

        // Update the new Content
        final String editMainContent = "editMainContent";
        final Content editedContent = new Content();
        editedContent.setId(resultNewContent.getId());
        editedContent.setMainContent(editMainContent);

        final Content editedNewContent = contentService.update(editedContent);

        assertNotNull(editedNewContent);
        assertNotNull(editedNewContent.getId());
        assertEquals(editedNewContent.getMainContent(), editMainContent);
    }
     
}
