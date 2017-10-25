package com.line.sidebardemo.bean;

import com.line.sidebardemo.widget.sidebar.SideBarSupport;

/**
 * Created by chenliu on 17/10/25.
 */

public class Contact implements SideBarSupport.SectionSupport {

    private String section;
    private String name;

    public Contact(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getSection() {
        return section;
    }

    @Override
    public String getSectionSrc() {
        return name;
    }

    @Override
    public void setSection(String sction) {
        this.section = sction;
    }
}
