package com.flaurite.addon.headerbutton.impl;

import com.flaurite.addon.headerbutton.ext.AttachableButton;
import com.haulmont.bali.events.Subscription;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.screen.Screen;

import java.util.EventObject;
import java.util.function.Consumer;

public class HeaderButton extends AttachableButton {

    protected Consumer<HeaderButton> markAsDirtyListener;

    protected String id;
    protected String caption;
    protected String icon;
    protected String description;

    protected Boolean sanitizeHtml;
    protected boolean descriptionAsHtml = false;

    protected Consumer<ButtonClickEvent> clickHandler;

    public HeaderButton(String id) {
        Preconditions.checkNotNullArgument(id);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        Preconditions.checkNotNullArgument(id);
        this.id = id;
    }

    public HeaderButton withId(String id) {
        Preconditions.checkNotNullArgument(id);

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

    public void setIcon(CubaIcon icon) {
        this.icon = icon.source();
        markAsDirty();
    }

    public HeaderButton withIcon(String icon) {
        this.icon = icon;
        markAsDirty();
        return this;
    }

    public HeaderButton withIcon(CubaIcon icon) {
        this.icon = icon.source();
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

    public Consumer<ButtonClickEvent> getClickHandler() {
        return clickHandler;
    }

    public void setClickHandler(Consumer<ButtonClickEvent> clickHandler) {
        this.clickHandler = clickHandler;

        getEventHub().subscribe(ButtonClickEvent.class, clickHandler);
    }

    public HeaderButton withClickHandler(Consumer<ButtonClickEvent> clickHandler) {
        this.clickHandler = clickHandler;

        getEventHub().subscribe(ButtonClickEvent.class, clickHandler);

        return this;
    }

    public Subscription addClickListener(Consumer<ButtonClickEvent> clickListener) {
        this.clickHandler = clickListener;

        return getEventHub().subscribe(ButtonClickEvent.class, clickHandler);
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

    public static class ButtonClickEvent extends EventObject {

        protected HeaderButton button;

        public ButtonClickEvent(Screen source, HeaderButton button) {
            super(source);
            this.button = button;
        }

        public HeaderButton getButton() {
            return button;
        }

        public String getButtonId() {
            return button.getId();
        }

        @Override
        public Screen getSource() {
            return (Screen) super.getSource();
        }
    }
}
