package com.fedev.treenode.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fedev.treenode.controller.dto.TreeNodeContentDTO;
import com.fedev.treenode.model.Content;
import com.fedev.treenode.model.TreeNode;
import com.fedev.treenode.service.ContentService;
import com.fedev.treenode.service.TreeNodeService;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.mockito.Mockito.*;

/**
 *
 * @author csongor_balog
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@AutoConfigureMockMvc
public class TreeNodeControllerTest {
    
    @InjectMocks
    public TreeNodeController treeNodeController;
    
    @Mock
    private ContentService contentService;
    
    @Mock
    private TreeNodeService treeNodeService;
    
    private MockMvc mockMvc;

    private Long id;
    private String name;
    private Long contentId;
    private Long parentId;
    private String mainContent;
    private TreeNode treeNode;
    private Content content;

    @Before
    public void setup()  {
       MockitoAnnotations.initMocks(this);
       this.mockMvc = MockMvcBuilders.standaloneSetup(treeNodeController).build();

        this.id = 1L;
        this.name = "nameExample";
        this.contentId = 2L;
        this.parentId = 3L;
        this.mainContent = "mainContentExample";
        this.treeNode = new TreeNode();
        this.treeNode.setId(id);
        this.treeNode.setName(name);
        this.treeNode.setContentId(contentId);
        this.treeNode.setParentId(parentId);
        this.content = new Content();
        this.content.setId(contentId);
        this.content.setMainContent(mainContent);
    }

    @Test
    public void create_shouldReturnCreatedNode() throws Exception {
        final String jsonData = new JSONObject()
                .put("name", name)
                .put("parentId", parentId)
                .put("mainContent", mainContent).toString();

        when(treeNodeService.create(any(TreeNode.class))).thenReturn(treeNode);
        when(contentService.create(anyString())).thenReturn(content);

        mockMvc.perform(post("/treenode/create")
            .contentType(APPLICATION_JSON_UTF8)
            .content(jsonData))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$['treeNode']['id']").value(id))
            .andExpect(jsonPath("$['treeNode']['name']").value(name))
            .andExpect(jsonPath("$['treeNode']['contentId']").value(contentId))
            .andExpect(jsonPath("$['treeNode']['parentId']").value(parentId))
            .andExpect(jsonPath("$['content']['id']").value(contentId))
            .andExpect(jsonPath("$['content']['mainContent']").value(mainContent));
    }

    @Test
    public void update_shouldReturnUpdatedNode() throws Exception {
        TreeNodeContentDTO treeNodeContentDTO = new TreeNodeContentDTO(treeNode, content);

        when(treeNodeService.update(any(TreeNode.class))).thenReturn(treeNode);
        when(contentService.update(any(Content.class))).thenReturn(content);
        
        final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        mockMvc.perform(put("/treenode/update")
            .contentType(APPLICATION_JSON_UTF8)
            .content(ow.writeValueAsString(treeNodeContentDTO)))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$['treeNode']['id']").value(id))
            .andExpect(jsonPath("$['treeNode']['name']").value(name))
            .andExpect(jsonPath("$['treeNode']['contentId']").value(contentId))
            .andExpect(jsonPath("$['treeNode']['parentId']").value(parentId))
            .andExpect(jsonPath("$['content']['id']").value(contentId))
            .andExpect(jsonPath("$['content']['mainContent']").value(mainContent));
    }

    @Test
    public void getAll_shouldReturnTreeInList() throws Exception {
        final List<TreeNode> treeNodeList = new ArrayList<>();
        treeNodeList.add(treeNode);

        when(treeNodeService.getAll()).thenReturn(treeNodeList);

        mockMvc.perform(get("/treenode/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]['id']").value(id))
                .andExpect(jsonPath("$[0]['name']").value(name))
                .andExpect(jsonPath("$[0]['contentId']").value(contentId))
                .andExpect(jsonPath("$[0]['parentId']").value(parentId));
    }

    @Test
    public void getById_shouldReturnRequestedNode() throws Exception {
        when(treeNodeService.getById(anyLong())).thenReturn(treeNode);

        mockMvc.perform(get("/treenode/getById?id=1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$['id']").value(id))
            .andExpect(jsonPath("$['name']").value(name))
            .andExpect(jsonPath("$['contentId']").value(contentId))
            .andExpect(jsonPath("$['parentId']").value(parentId));
    }
    
    @Test
    public void deleteById_shouldReturnIsDeleted() throws Exception {
        when(treeNodeService.deleteById(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/treenode/deleteById?id=1"))
            .andExpect(status().isOk())
            .andExpect(content().string("true"));
    }
    
    @Test
    public void reorganizeByParentId_shouldReturnModefiedTreeList() throws Exception {
        final List<TreeNode> treeNodeList = new ArrayList<>();
        treeNodeList.add(treeNode);
        final JSONObject json = new JSONObject();
        json.put("id", "4");
        json.put("parentId", "3");

        when(treeNodeService.reorganizeByParentId(anyLong(), anyLong())).thenReturn(treeNodeList);

        mockMvc.perform(put("/treenode/reorganizeByParentId")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0]['id']").value(id))
            .andExpect(jsonPath("$[0]['name']").value(name))
            .andExpect(jsonPath("$[0]['contentId']").value(contentId))
            .andExpect(jsonPath("$[0]['parentId']").value(parentId));
    }

    @Test
    public void reorganizeByParentId_shouldReturnStatusBadRequest() throws Exception {
        final JSONObject json = new JSONObject();

        mockMvc.perform(put("/treenode/reorganizeByParentId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createDefaultTree_shouldReturnTreeList() throws Exception {
        final List<TreeNode> treeNodeList = new ArrayList<>();
        treeNodeList.add(treeNode);

        when(treeNodeService.createDefaultTree()).thenReturn(treeNodeList);

        mockMvc.perform(post("/treenode/createDefaultTree"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0]['id']").value(id))
            .andExpect(jsonPath("$[0]['name']").value(name))
            .andExpect(jsonPath("$[0]['contentId']").value(contentId))
            .andExpect(jsonPath("$[0]['parentId']").value(parentId));
    }

    @Test
    public void generateTestData_shouldReturnStatusOk() throws Exception {
        mockMvc.perform(get("/treenode/generateTestData"))
            .andExpect(status().isOk());
    }
    
}
