package com.flaurite.addon.headerbutton.facet;

import com.flaurite.addon.headerbutton.ext.ScreenHeaderExtension;
import com.flaurite.addon.headerbutton.impl.HeaderButton;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.web.gui.WebAbstractFacet;
import com.haulmont.cuba.web.gui.components.WebDialogWindow;
import org.springframework.util.CollectionUtils;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class WebHeaderButtonFacet extends WebAbstractFacet implements HeaderButtonFacet {

    protected List<HeaderButton> loadedButtons = Collections.emptyList();
    protected ScreenHeaderExtension headerExtension;

    @Override
    public void setButtons(List<HeaderButton> headerButtons) {
        checkAttachedFrame();

        if (isDialogScreen())
            headerExtension.setHeaderButtons(headerButtons);
    }

    @Override
    public List<HeaderButton> getButtons() {
        checkAttachedFrame();

        return isDialogScreen() ?
                headerExtension.getHeaderButtons() : Collections.emptyList();
    }

    @Override
    public void addButton(HeaderButton headerButton) {
        checkAttachedFrame();

        if (isDialogScreen())
            headerExtension.addHeaderButton(headerButton);
    }

    @Override
    public void addButton(int position, HeaderButton headerButton) {
        checkAttachedFrame();

        if (isDialogScreen())
            headerExtension.addHeaderButton(position, headerButton);
    }

    @Override
    public void removeButton(HeaderButton headerButton) {
        removeButton(headerButton.getId());
    }

    @Override
    public void removeButton(String id) {
        checkAttachedFrame();

        if (isDialogScreen())
            headerExtension.removeHeaderButton(id);
    }

    @Override
    public boolean contains(HeaderButton headerButton) {
        return contains(headerButton.getId());
    }

    @Override
    public boolean contains(String id) {
        return headerExtension.contains(id);
    }

    @Override
    public void setOwner(@Nullable Frame owner) {
        super.setOwner(owner);

        if (owner == null) {
            throw new IllegalStateException("HeaderButtonFacet is not attached to Frame");
        }

        headerExtension = new ScreenHeaderExtension((Screen) owner.getFrameOwner());

        if (isDialogScreen()
                && !CollectionUtils.isEmpty(loadedButtons)) {
            headerExtension.setHeaderButtons(loadedButtons);
        }
    }

    @SuppressWarnings("NullableProblems")
    @Nullable
    @Override
    public Object getSubPart(String id) {
        // if screen opened not as dialog, but has injections for XML buttons,
        // we should provide loaded buttons in order to avoid exception
        if (!isDialogScreen()) {
            return loadedButtons.stream()
                    .filter(button -> Objects.equals(button.getId(), id))
                    .findFirst()
                    .orElse(null);
        }

        if (headerExtension == null) {
            return null;
        }

        return headerExtension.getHeaderButtons().stream()
                .filter(button -> Objects.equals(button.getId(), id))
                .findFirst()
                .orElse(null);
    }

    protected void setLoadedButtons(List<HeaderButton> buttons) {
        this.loadedButtons = buttons;
    }

    protected void checkAttachedFrame() {
        Frame frame = getOwner();
        if (frame == null) {
            throw new IllegalStateException("HeaderButtonFacet is not attached to the screen");
        }
    }

    protected boolean isDialogScreen() {
        Screen screen = (Screen) owner.getFrameOwner();
        return screen.getWindow() instanceof WebDialogWindow;
    }
}
