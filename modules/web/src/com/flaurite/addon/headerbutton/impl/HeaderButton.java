package com.flaurite.addon.headerbutton.impl;

import com.flaurite.addon.headerbutton.ext.AttachableButton;
import com.haulmont.bali.events.Subscription;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.screen.Screen;

import java.util.EventObject;
import java.util.function.Consumer;

/**
 * Describes a button that can be added to the dialog header.
 */
public class HeaderButton extends AttachableButton {

    protected Consumer<HeaderButton> markAsDirtyListener;

    protected String id;
    protected String caption;
    protected String icon;
    protected String description;
    protected String styleName;

    protected Boolean sanitizeHtml;
    protected boolean descriptionAsHtml = false;
    protected boolean enabled = true;

    protected Consumer<ButtonClickEvent> clickHandler;

    /**
     * @param id button's id, not null
     */
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

    /**
     * Sets button's caption. If is used in runtime, button will be recreated with new caption.
     *
     * @param caption button's caption
     */
    public void setCaption(String caption) {
        this.caption = caption;
        markAsDirty();
    }

    /**
     * Sets button's caption. If is used in runtime, button will be recreated with new caption.
     *
     * @param caption button's caption
     * @return current instance
     */
    public HeaderButton withCaption(String caption) {
        this.caption = caption;
        markAsDirty();
        return this;
    }

    public String getIcon() {
        return icon;
    }

    /**
     * Sets button's icon. If is used in runtime, button will be recreated with new icon. Example:
     * <ul>
     *     <li>BITCOIN</li>
     *     <li>font-icon:BITCOIN</li>
     *     <li>icons/chain.png</li>
     * </ul>
     *
     * @param icon icon source or name
     */
    public void setIcon(String icon) {
        this.icon = icon;
        markAsDirty();
    }

    /**
     * Sets button's icon. If is used in runtime, button will be recreated with new icon.
     *
     * @param icon icon
     */
    public void setIcon(CubaIcon icon) {
        this.icon = icon.source();
        markAsDirty();
    }

    /**
     * Sets button's icon. If is used in runtime, button will be recreated with new icon. Example:
     * <ul>
     *     <li>BITCOIN</li>
     *     <li>font-icon:BITCOIN</li>
     *     <li>icons/chain.png</li>
     * </ul>
     *
     * @param icon icon source or name
     * @return current instance
     */
    public HeaderButton withIcon(String icon) {
        this.icon = icon;
        markAsDirty();
        return this;
    }

    /**
     * Sets button's icon. If is used in runtime, button will be recreated with new icon.
     *
     * @param icon icon
     * @return current instance
     */
    public HeaderButton withIcon(CubaIcon icon) {
        this.icon = icon.source();
        markAsDirty();
        return this;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Sets button's description. If is used in runtime, button will be recreated with new description.
     *
     * @param description description
     */
    public void setDescription(String description) {
        this.description = description;
        markAsDirty();
    }

    /**
     * Sets button's description. If is used in runtime, button will be recreated with new description.
     *
     * @param description description
     * @return current instance
     */
    public HeaderButton withDescription(String description) {
        this.description = description;
        markAsDirty();
        return this;
    }

    public String getStyleName() {
        return styleName;
    }

    /**
     * Sets button's styleName. If is used in runtime, button will be recreated with new styleName.
     *
     * @param styleName styleName
     */
    public void setStyleName(String styleName) {
        this.styleName = styleName;
        markAsDirty();
    }

    /**
     * Sets button's styleName. If is used in runtime, button will be recreated with new styleName.
     *
     * @param styleName styleName
     * @return current instance
     */
    public HeaderButton withtStyleName(String styleName) {
        this.styleName = styleName;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        markAsDirty();
    }

    public HeaderButton withEnabled(boolean enabled) {
        this.enabled = enabled;
        markAsDirty();
        return this;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        HeaderButton hButton = (HeaderButton) obj;
        return id.equals(hButton.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
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
