package com.flaurite.addon.headerbutton.ext;

import com.flaurite.addon.headerbutton.impl.HeaderButton;

import java.util.function.Consumer;

public abstract class AttachableButton {

    protected abstract void attache(Consumer<HeaderButton> markAsDirtyCallback);
}
