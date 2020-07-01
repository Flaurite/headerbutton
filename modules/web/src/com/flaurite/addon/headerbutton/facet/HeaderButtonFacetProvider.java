package com.flaurite.addon.headerbutton.facet;

import com.flaurite.addon.headerbutton.impl.HeaderButton;
import com.google.common.base.Strings;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.gui.xml.FacetProvider;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Component(HeaderButtonFacetProvider.NAME)
public class HeaderButtonFacetProvider implements FacetProvider<HeaderButtonFacet> {

    public static final String NAME = "headerbutton_HeaderButtonFacetProvider";

    @Inject
    protected IconLoader iconsLoader;

    @Inject
    protected MessageTools messageTools;

    @Override
    public Class<HeaderButtonFacet> getFacetClass() {
        return HeaderButtonFacet.class;
    }

    @Override
    public HeaderButtonFacet create() {
        return new WebHeaderButtonFacet();
    }

    @Override
    public String getFacetTag() {
        return "headerButtons";
    }

    @Override
    public void loadFromXml(HeaderButtonFacet facet, Element element, ComponentLoader.ComponentContext context) {
        Optional<String> id = loadId(element);
        id.ifPresent(facet::setId);

        List<HeaderButton> buttons = loadButtons(element, context);
        ((WebHeaderButtonFacet)facet).setLoadedButtons(buttons);
    }

    protected List<HeaderButton> loadButtons(Element element, ComponentLoader.ComponentContext context) {
        List<HeaderButton> actions = new ArrayList<>();

        for (Element actionElem : element.elements("button")) {
            String id = actionElem.attributeValue("id");
            if (Strings.isNullOrEmpty(id)) {
                continue;
            }

            HeaderButton action = new HeaderButton(id);

            String caption = actionElem.attributeValue("caption");
            action.withCaption(loadResourceString(context, caption));

            String icon = actionElem.attributeValue("icon");
            if (!Strings.isNullOrEmpty(icon)) {
                action.withIcon(iconsLoader.getIconPath(context, icon));
            }

            String description = actionElem.attributeValue("description");
            action.withDescription(loadResourceString(context, description));

            String descriptionAsHtml = actionElem.attributeValue("descriptionAsHtml");
            if (descriptionAsHtml != null) {
                action.withDescriptionAsHtml(Boolean.parseBoolean(descriptionAsHtml));
            }

            String sanitize = actionElem.attributeValue("sanitizeHtml");
            if (sanitize != null) {
                action.setSanitizeHtml(Boolean.parseBoolean(sanitize));
            }

            String styleName = actionElem.attributeValue("styleName");
            if (StringUtils.isNotBlank(styleName)) {
                action.setStyleName(styleName);
            }

            actions.add(action);
        }

        return actions;
    }

    protected Optional<String> loadId(Element element) {
        String id = element.attributeValue("id");
        return Optional.ofNullable(id);
    }

    protected String loadResourceString(ComponentLoader.ComponentContext context, String caption) {
        if (isEmpty(caption)) {
            return caption;
        }

        Class screenClass = context.getFrame()
                .getFrameOwner()
                .getClass();

        return messageTools.loadString(screenClass.getPackage().getName(), caption);
    }
}
