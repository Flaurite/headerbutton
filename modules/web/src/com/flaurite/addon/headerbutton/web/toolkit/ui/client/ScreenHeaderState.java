package com.flaurite.addon.headerbutton.web.toolkit.ui.client;

import com.vaadin.shared.annotations.NoLayout;
import com.vaadin.shared.communication.SharedState;

import java.util.ArrayList;
import java.util.List;

public class ScreenHeaderState extends SharedState {

    @NoLayout
    public List<ClientHeaderButton> buttons = new ArrayList<>();
}
