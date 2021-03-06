package com.flaurite.addon.headerbutton.ext;

import com.flaurite.addon.headerbutton.impl.HeaderButton;
import com.flaurite.addon.headerbutton.web.toolkit.ui.client.ClientHeaderButton;
import com.flaurite.addon.headerbutton.web.toolkit.ui.client.ScreenHeaderServerRpc;
import com.flaurite.addon.headerbutton.web.toolkit.ui.client.ScreenHeaderState;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.gui.components.WebDialogWindow;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.sys.sanitizer.HtmlSanitizer;
import com.vaadin.server.AbstractExtension;
import com.vaadin.server.KeyMapper;
import com.vaadin.server.Resource;
import com.vaadin.ui.Window;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Header button extension. Creates an extension of dialog window and adds buttons to the header.
 */
public class ScreenHeaderExtension extends AbstractExtension {

    protected HtmlSanitizer sanitizer;
    protected WebConfig webConfig;

    protected Screen screen;
    protected List<HeaderButton> hButtons;

    protected KeyMapper<Resource> iconsKeyMapper = new KeyMapper<>();
    protected Set<String> iconsKey = new HashSet<>();

    /**
     * Screen should be created as dialog, for instance:
     * <pre>{@code
     * OrderEdit orderEdit = screens.create(OrderEdit.class, OpenMode.DIALOG);
     * ScreenHeaderExtension extension = new ScreenHeaderExtension(orderEdit);
     * }</pre>
     *
     * @param screen dialog screen
     */
    public ScreenHeaderExtension(Screen screen) {
        Preconditions.checkNotNullArgument(screen);

        if (!(screen.getWindow() instanceof WebDialogWindow)) {
            return;
        }

        WebDialogWindow dialogWindow = (WebDialogWindow) screen.getWindow();
        Window cubaWindow = (Window) dialogWindow.getComposition();

        screen.addAfterCloseListener(this::afterCloseScreen);

        registerRpc((ScreenHeaderServerRpc) this::fireClickEvent);

        super.extend(cubaWindow);

        this.screen = screen;

        sanitizer = AppBeans.get(HtmlSanitizer.NAME);
        webConfig = AppBeans.get(Configuration.class).getConfig(WebConfig.class);
    }

    /**
     * Sets list of header buttons. If extension contains buttons, they will be replaced.
     *
     * @param headerButtons list of header buttons
     */
    public void setHeaderButtons(List<HeaderButton> headerButtons) {
        Preconditions.checkNotNullArgument(headerButtons);
        ScreenHeaderTools.checkUniqueIds(headerButtons);

        hButtons = new ArrayList<>(headerButtons);

        for (HeaderButton hButton : hButtons) {
            ((AttachableButton) hButton).attache(this::recreateButton);
        }

        getState().buttons = convertToClientButtons(hButtons);
    }

    /**
     * @return current list of buttons in the dialog header
     */
    public List<HeaderButton> getHeaderButtons() {
        if (hButtons == null) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableList(hButtons);
    }

    /**
     * Adds a button to the dialog header.
     * <p>
     * <br>
     * Example:
     * <pre>{@code
     * extension.addHeaderButton(new HeaderButton("infoBtn")
     *         .withCaption("Info")
     *         .withIcon(CubaIcon.INFO)
     *         .withClickHandler(clickEvent -> {
     *             notifications.create()
     *                     .withCaption("Info dialog")
     *                     .show();
     *         }));
     * }</pre>
     *
     * @param hButton header button
     */
    public void addHeaderButton(HeaderButton hButton) {
        if (hButtons == null) {
            hButtons = new ArrayList<>();
        }

        addHeaderButton(hButtons.size(), hButton);
    }

    /**
     * Adds a button to the dialog header at a position index. 0 - index is a leftmost position in the header.
     * <p>
     * <br>
     * Example:
     * <pre>{@code
     * extension.addHeaderButton(0, new HeaderButton("infoBtn")
     *         .withCaption("Info")
     *         .withIcon(CubaIcon.INFO)
     *         .withClickHandler(clickEvent -> {
     *             notifications.create()
     *                     .withCaption("Info dialog")
     *                     .show();
     *         }));
     * }</pre>
     *
     * @param hButton header button
     */
    public void addHeaderButton(int position, HeaderButton hButton) {
        Preconditions.checkNotNullArgument(hButton);
        ScreenHeaderTools.checkUniqueId(hButtons, hButton.getId());
        ScreenHeaderTools.checkPosition(position);

        if (hButtons == null) {
            hButtons = new ArrayList<>();
        }

        ((AttachableButton) hButton).attache(this::recreateButton);

        hButtons.add(position, hButton);
        getState().buttons.add(position, convertToClientButton(hButton));
    }

    /**
     * Removes button by its id
     *
     * @param id button's id
     */
    public void removeHeaderButton(String id) {
        if (CollectionUtils.isEmpty(hButtons)) {
            return;
        }

        int position = -1;
        for (int i = 0; i < hButtons.size(); i++) {
            HeaderButton hButton = hButtons.get(i);
            if (hButton.getId().equals(id)) {
                position = i;
                break;
            }
        }

        if (position >= 0) {
            hButtons.remove(position);
            getState().buttons.remove(position);
        }
    }

    /**
     * @param id header button's id
     * @return true if header button with given id has already added
     */
    public boolean contains(String id) {
        if (CollectionUtils.isEmpty(hButtons)) {
            return false;
        }

        return hButtons.stream().anyMatch(button -> button.getId().equals(id));
    }

    @Override
    protected ScreenHeaderState getState() {
        return (ScreenHeaderState) super.getState();
    }

    @Override
    protected ScreenHeaderState getState(boolean markAsDirty) {
        return (ScreenHeaderState) super.getState(markAsDirty);
    }

    protected List<ClientHeaderButton> convertToClientButtons(List<HeaderButton> hButtons) {
        List<ClientHeaderButton> clientButtons = new ArrayList<>(hButtons.size());
        for (HeaderButton hButton : hButtons) {
            clientButtons.add(convertToClientButton(hButton));
        }
        return clientButtons;
    }

    protected ClientHeaderButton convertToClientButton(HeaderButton hButton) {
        ClientHeaderButton clientButton = new ClientHeaderButton();
        clientButton.setId(hButton.getId());
        clientButton.setCaption(hButton.getCaption());
        clientButton.setStyleName(hButton.getStyleName());
        clientButton.setEnabled(hButton.isEnabled());

        boolean sanitize = false;
        if (hButton.isDescriptionAsHtml()) {
            clientButton.setDescriptionAsHtml(hButton.isDescriptionAsHtml());

            sanitize = hButton.isSanitizeHtml() != null ?
                    hButton.isSanitizeHtml() : webConfig.getHtmlSanitizerEnabled();
        }
        clientButton.setDescription(sanitize ? sanitizer.sanitize(hButton.getDescription()) : hButton.getDescription());

        if (hButton.getIcon() != null) {
            Resource iconResource = AppBeans.get(IconResolver.class)
                    .getIconResource(hButton.getIcon());
            String resourceKey = iconsKeyMapper.key(iconResource);

            iconsKey.add(resourceKey);

            setResource(resourceKey, iconResource);

            clientButton.setIcon(resourceKey);
        }
        return clientButton;
    }

    protected void recreateButton(HeaderButton hButton) {
        int position = -1;
        for (HeaderButton button : hButtons) {
            if (button.getId().equals(hButton.getId())) {
                position = hButtons.indexOf(button);
                hButtons.remove(button);
                break;
            }
        }

        hButtons.add(position, hButton);

        List<ClientHeaderButton> buttons = getState(false).buttons;
        buttons.remove(position);
        buttons.add(position, convertToClientButton(hButton));

        markAsDirty();
    }

    protected void fireClickEvent(String buttonId) {
        if (hButtons != null && !hButtons.isEmpty()) {
            Optional<HeaderButton> buttonOptional = hButtons.stream()
                    .filter(e -> e.getId().equals(buttonId))
                    .findFirst();

            buttonOptional.ifPresent(hButton ->
                    hButton.getEventHub().publish(
                            HeaderButton.ButtonClickEvent.class,
                            new HeaderButton.ButtonClickEvent(screen, hButton)));
        }
    }

    protected void afterCloseScreen(Screen.AfterCloseEvent event) {
        for (String iconsKey : iconsKey) {
            Resource resource = getResource(iconsKey);
            iconsKeyMapper.remove(resource);
            setResource(iconsKey, null);
        }

        iconsKey.clear();
    }
}
