package com.flaurite.addon.headerbutton.impl;

import com.flaurite.addon.headerbutton.ext.AttachableButton;
import com.flaurite.addon.headerbutton.facet.HeaderButtonFacet;

import java.util.function.Consumer;

public class HeaderButton extends AttachableButton {

    protected Consumer<HeaderButton> markAsDirtyListener;

    protected String id;
    protected String caption;
    protected String icon;
    protected String description;

    protected Boolean sanitizeHtml;
    protected boolean descriptionAsHtml = false;

    protected Consumer<HeaderButtonFacet.ButtonClickEvent> clickListener;

    public HeaderButton(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HeaderButton withId(String id) {
        this.id = id;
        return this;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
        markAsDirty();
    }

    public HeaderButton withCaption(String caption) {
        this.caption = caption;
        markAsDirty();
        return this;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
        markAsDirty();
    }

    public HeaderButton withIcon(String icon) {
        this.icon = icon;
        markAsDirty();
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        markAsDirty();
    }

    public HeaderButton withDescription(String description) {
        this.description = description;
        markAsDirty();
        return this;
    }

    public Boolean isSanitizeHtml() {
        return sanitizeHtml;
    }

    public void setSanitizeHtml(Boolean sanitizeHtml) {
        this.sanitizeHtml = sanitizeHtml;
    }

    public HeaderButton withSanitizeHtml(Boolean sanitizeHtml) {
        this.sanitizeHtml = sanitizeHtml;
        return this;
    }

    public boolean isDescriptionAsHtml() {
        return descriptionAsHtml;
    }

    public void setDescriptionAsHtml(boolean descriptionAsHtml) {
        this.descriptionAsHtml = descriptionAsHtml;
    }

    public HeaderButton withDescriptionAsHtml(boolean descriptionAsHtml) {
        this.descriptionAsHtml = descriptionAsHtml;
        return this;
    }

    public Consumer<HeaderButtonFacet.ButtonClickEvent> getClickListener() {
        return clickListener;
    }

    public void setClickListener(Consumer<HeaderButtonFacet.ButtonClickEvent> clickListener) {
        this.clickListener = clickListener;
    }

    public HeaderButton withClickListener(Consumer<HeaderButtonFacet.ButtonClickEvent> clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    @Override
    protected void attache(Consumer<HeaderButton> markAsDirtyListener) {
        this.markAsDirtyListener = markAsDirtyListener;
    }

    protected void markAsDirty() {
        if (markAsDirtyListener != null) {
            markAsDirtyListener.accept(this);
        }
    }
}
