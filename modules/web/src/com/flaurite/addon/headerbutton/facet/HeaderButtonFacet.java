package com.flaurite.addon.headerbutton.facet;

import com.flaurite.addon.headerbutton.impl.HeaderButton;
import com.haulmont.cuba.gui.components.Facet;
import com.haulmont.cuba.gui.components.HasSubParts;

import java.util.List;

public interface HeaderButtonFacet extends Facet, HasSubParts {

    void setButtons(List<HeaderButton> headerButtons);

    List<HeaderButton> getButtons();

    void addButton(HeaderButton headerButton);

    void addButton(int position, HeaderButton headerButton);

    void removeButton(String id);
}
