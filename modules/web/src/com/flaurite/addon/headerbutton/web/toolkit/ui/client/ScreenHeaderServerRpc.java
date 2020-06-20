package com.flaurite.addon.headerbutton.web.toolkit.ui.client;

import com.vaadin.shared.communication.ServerRpc;

public interface ScreenHeaderServerRpc extends ServerRpc {

    void buttonClicked(String buttonId);
}
