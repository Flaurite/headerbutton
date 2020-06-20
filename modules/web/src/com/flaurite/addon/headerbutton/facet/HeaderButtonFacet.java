package com.flaurite.addon.headerbutton.facet;

import com.flaurite.addon.headerbutton.impl.HeaderButton;
import com.haulmont.cuba.gui.components.Facet;
import com.haulmont.cuba.gui.components.HasSubParts;
import com.haulmont.cuba.gui.screen.Screen;

import java.util.EventObject;
import java.util.List;

public interface HeaderButtonFacet extends Facet, HasSubParts {

    void setButtons(List<HeaderButton> headerButtons);

    List<HeaderButton> getButtons();

    void addButton(HeaderButton headerButton);

    void addButton(int position, HeaderButton headerButton);

    void removeButton(String id);

    class ButtonClickEvent extends EventObject {

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
