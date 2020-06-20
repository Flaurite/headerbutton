package com.flaurite.addon.headerbutton.web.toolkit.ui.client;

import com.flaurite.addon.headerbutton.ext.ScreenHeaderExtension;
import com.google.gwt.aria.client.Roles;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.haulmont.cuba.web.widgets.client.window.CubaWindowWidget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.client.ui.Icon;
import com.vaadin.shared.ui.Connect;

import java.util.ArrayList;
import java.util.List;

@Connect(ScreenHeaderExtension.class)
public class ScreenHeaderConnector extends AbstractExtensionConnector {

    public static final String WINDOW_STYLE = "t-window";
    public static final String OUTERHEADER_STYLE = "t-outerheader";

    public static final String BUTTON_STYLE = "t-header-action";
    public static final String BUTTON_PRESSED_STYLE = "t-pressed";
    public static final String BUTTON_ICON_STYLE = "t-icon";
    public static final String BUTTON_CAPTION_STYLE = "t-caption";

    public static final String CAPTION_STYLE = "t-action-caption";
    public static final String ICON_STYLE = "t-action-icon";
    public static final String TOOLTIP_STYLE = "t-tooltip";

    protected List<ButtonComposition> buttons = new ArrayList<>();
    protected CubaWindowWidget window;
    protected ServerConnector serverConnector;
    protected LazyDescriptionManager descriptionManager = new LazyDescriptionManager();

    @Override
    protected void extend(ServerConnector target) {
        serverConnector = target;
        window = (CubaWindowWidget) ((ComponentConnector) target).getWidget();
        window.header.addClassName(OUTERHEADER_STYLE);

        createButtons(window);
    }

    protected void createButtons(CubaWindowWidget window) {
        if (window == null) {
            return;
        }

        for (ClientHeaderButton button : getState().buttons) {
            Element toolBarBtn = createButtonElement(button, serverConnector);

            addButtonEventListener(toolBarBtn);

            DOM.appendChild(window.header, toolBarBtn);
        }
    }

    protected Element createButtonElement(ClientHeaderButton button, ServerConnector connector) {
        ButtonComposition composition = new ButtonComposition();

        Element toolBarBtn = DOM.createDiv();
        toolBarBtn.addClassName(BUTTON_STYLE);
        composition.setButton(toolBarBtn);

        if (button.getIcon() != null) {
            Element icon = createIcon(button.getIcon());
            composition.setIcon(icon);

            toolBarBtn.addClassName(BUTTON_ICON_STYLE);
            DOM.appendChild(toolBarBtn, icon);
        }

        if (button.getCaption() != null) {
            Element caption = createCaption(button.getCaption());
            composition.setCaption(caption);

            toolBarBtn.addClassName(BUTTON_CAPTION_STYLE);
            DOM.appendChild(toolBarBtn, caption);
        }

        if (button.getDescription() != null) {
            Element description = createDescription(button);
            composition.setDescription(description);
            DOM.appendChild(toolBarBtn, description);

            addWindowDescriptionStyle(connector);
        }

        // todo check
        // Make the header button accessible for assistive devices
        Roles.getButtonRole().set(toolBarBtn);
        Roles.getButtonRole().setAriaLabelProperty(toolBarBtn, "header button");

        buttons.add(composition);

        return toolBarBtn;
    }

    protected Element createCaption(String caption) {
        Element captionElement = DOM.createSpan();
        captionElement.setInnerText(caption);
        captionElement.addClassName(CAPTION_STYLE);
        return captionElement;
    }

    protected Element createIcon(String iconKey) {
        String resourceUrl = getResourceUrl(iconKey);
        Icon icon = getConnection().getIcon(resourceUrl);
        icon.getElement().addClassName(ICON_STYLE);
        return icon.getElement();
    }

    protected Element createDescription(ClientHeaderButton button) {
        Element descriptionElement = DOM.createDiv();

        if (button.isDescriptionAsHtml()) {
            descriptionElement.setInnerHTML(button.getDescription());
        } else {
            descriptionElement.setInnerText(button.getDescription());
        }

        descriptionElement.addClassName(TOOLTIP_STYLE);
        return descriptionElement;
    }

    protected void addWindowDescriptionStyle(ServerConnector connector) {
        CubaWindowWidget cubaWindow =
                (CubaWindowWidget) ((ComponentConnector) connector).getWidget();

        if (!cubaWindow.getStyleName().contains(WINDOW_STYLE)) {
            cubaWindow.addStyleName(WINDOW_STYLE);
        }
    }

    protected void addButtonEventListener(Element button) {
        Event.sinkEvents(button, Event.getEventsSunk(button)
                | Event.ONCLICK | Event.ONMOUSEDOWN | Event.ONMOUSEOUT | Event.ONMOUSEUP | Event.ONMOUSEOVER);
        Event.setEventListener(button, event -> {
            switch (event.getTypeInt()) {
                case Event.ONMOUSEDOWN:
                    onButtonMouseDown(event);
                    break;
                case Event.ONCLICK:
                    onButtonClickEvent(event);
                    break;
                case Event.ONMOUSEUP:
                    onButtonMouseUp(event);
                    break;
                case Event.ONMOUSEOUT:
                    onButtonMouseOutEvent(event);
                    break;
                case Event.ONMOUSEOVER:
                    onButtonMouseOverEvent(event);
                    break;
            }
        });
    }

    protected void onButtonMouseDown(Event event) {
        ButtonComposition composition = getTargetComposition(event);
        if (composition == null) {
            return;
        }

        descriptionManager.cancelLazyShowing();

        // remove if exists
        composition.getButton().removeClassName(BUTTON_PRESSED_STYLE);
        composition.getButton().addClassName(BUTTON_PRESSED_STYLE);

        event.stopPropagation();
    }

    protected void onButtonMouseUp(Event event) {
        ButtonComposition composition = getTargetComposition(event);
        if (composition == null) {
            return;
        }

        composition.getButton().removeClassName(BUTTON_PRESSED_STYLE);
    }

    protected void onButtonClickEvent(Event event) {
        ButtonComposition composition = getTargetComposition(event);
        if (composition == null) {
            return;
        }

        descriptionManager.cancelLazyShowing();

        ClientHeaderButton toolbarButton = getState().buttons.get(buttons.indexOf(composition));
        getRpcProxy(ScreenHeaderServerRpc.class).buttonClicked(toolbarButton.getId());
    }

    protected void onButtonMouseOverEvent(Event event) {
        Element target = DOM.eventGetTarget(event);
        if (target.getClassName().contains(TOOLTIP_STYLE)) {
            return;
        }

        ButtonComposition composition = getTargetComposition(event);
        if (composition == null) {
            return;
        }

        if (target.getParentElement() == composition.getButton()) {
            return;
        }

        descriptionManager.show(composition);
    }

    protected void onButtonMouseOutEvent(Event event) {
        ButtonComposition composition = getTargetComposition(event);
        Element target = getTargetElement(event);
        if (composition == null || target == null) {
            return;
        }

        if (target == composition.getCaption() || target == composition.getIcon()) {
            return;
        }

        descriptionManager.cancelLazyShowing();

        composition.getButton().removeClassName(BUTTON_PRESSED_STYLE);
    }

    protected Element getTargetElement(Event event) {
        Element target = DOM.eventGetTarget(event);
        if (target.getClassName().contains(TOOLTIP_STYLE)) {
            return null;
        }

        for (ButtonComposition composition : buttons) {
            if (target == composition.getButton() || target.getParentElement() == composition.getButton()) {
                return target;
            }
        }

        return null;
    }

    protected ButtonComposition getTargetComposition(Event event) {
        Element target = DOM.eventGetTarget(event);
        if (target.getClassName().contains(TOOLTIP_STYLE)) {
            return null;
        }

        for (ButtonComposition composition : buttons) {
            if (target == composition.getButton() || target.getParentElement() == composition.getButton()) {
                return composition;
            }
        }

        return null;
    }

    @Override
    public ScreenHeaderState getState() {
        return (ScreenHeaderState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("buttons")) {
            clearButtons();
            createButtons(window);
        }
    }

    protected void clearButtons() {
        if (window != null) {
            for (ButtonComposition composition : buttons) {
                window.header.removeChild(composition.getButton());
            }

            buttons.clear();
        }
    }
}
