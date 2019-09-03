package com.fedev.treenode.model;

/**
 *
 * @author csongor_balog
 */
public class Content {
    
    private Long id;
    private String mainContent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMainContent() {
        return mainContent;
    }

    public void setMainContent(String text) {
        this.mainContent = text;
    }
    
}
