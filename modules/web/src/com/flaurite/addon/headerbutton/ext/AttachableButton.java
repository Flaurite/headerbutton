package com.flaurite.addon.headerbutton.ext;

import com.flaurite.addon.headerbutton.impl.HeaderButton;
import com.haulmont.bali.events.EventHub;

import java.util.function.Consumer;

public abstract class AttachableButton {

    private final EventHub eventHub = new EventHub();

    protected abstract void attache(Consumer<HeaderButton> markAsDirtyCallback);

    protected EventHub getEventHub() {
        return eventHub;
    }
}
