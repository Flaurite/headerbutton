package com.flaurite.addon.headerbutton.web.toolkit.ui.client;

import com.google.gwt.dom.client.Element;

public class ButtonComposition {

    protected Element button;
    protected Element icon;
    protected Element caption;
    protected Element description;

    public Element getButton() {
        return button;
    }

    public void setButton(Element button) {
        this.button = button;
    }

    public Element getIcon() {
        return icon;
    }

    public void setIcon(Element icon) {
        this.icon = icon;
    }

    public Element getCaption() {
        return caption;
    }

    public void setCaption(Element caption) {
        this.caption = caption;
    }

    public Element getDescription() {
        return description;
    }

    public void setDescription(Element description) {
        this.description = description;
    }

    public boolean isButtonDisabled() {
        return button == null || button.getPropertyBoolean("disabled");
    }

    public void addButtonStyleIfNotDisabled(String styleName) {
        if (isButtonDisabled()) {
            return;
        }

        button.removeClassName(styleName);
        button.addClassName(styleName);
    }
}
