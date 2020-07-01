package com.flaurite.addon.headerbutton.web.toolkit.ui.client;

import java.io.Serializable;

public class ClientHeaderButton implements Serializable {

    protected String id;
    protected String caption;
    protected String icon;
    protected String description;
    protected String styleName;

    protected boolean descriptionAsHtml = false;

    public ClientHeaderButton() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDescriptionAsHtml() {
        return descriptionAsHtml;
    }

    public void setDescriptionAsHtml(boolean descriptionAsHtml) {
        this.descriptionAsHtml = descriptionAsHtml;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }
}
