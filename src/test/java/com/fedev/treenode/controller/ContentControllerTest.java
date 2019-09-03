package com.fedev.treenode.controller;

import com.fedev.treenode.model.Content;
import com.fedev.treenode.service.ContentService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author csongor_balog
 */
public class ContentControllerTest {
    
    @InjectMocks
    public ContentController contentController;

    @Mock
    private ContentService contentService;

    private MockMvc mockMvc;

    @Before
    public void setup() {
       MockitoAnnotations.initMocks(this);
       this.mockMvc = MockMvcBuilders.standaloneSetup(contentController).build();
    }

    @Test
    public void getById_shouldReturnRequestedContent() throws Exception {
        final Long contentId = 1L;
        final String mainContent = "mainContentExample";
        final Content content = new Content();
        content.setId(contentId);
        content.setMainContent(mainContent);

        when(contentService.getById(anyLong())).thenReturn(content);

        mockMvc.perform(get("/content/getById?id=1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$['id']").value(contentId))
            .andExpect(jsonPath("$['mainContent']").value(mainContent));
    }
    
}
