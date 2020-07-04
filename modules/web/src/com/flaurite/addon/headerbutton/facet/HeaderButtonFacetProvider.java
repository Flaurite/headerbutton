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
        ((WebHeaderButtonFacet) facet).setLoadedButtons(buttons);
    }

    protected List<HeaderButton> loadButtons(Element element, ComponentLoader.ComponentContext context) {
        List<HeaderButton> buttons = new ArrayList<>();

        for (Element btnElem : element.elements("button")) {
            String id = btnElem.attributeValue("id");
            if (Strings.isNullOrEmpty(id)) {
                continue;
            }

            HeaderButton button = new HeaderButton(id);

            String caption = btnElem.attributeValue("caption");
            button.withCaption(loadResourceString(context, caption));

            String icon = btnElem.attributeValue("icon");
            if (!Strings.isNullOrEmpty(icon)) {
                button.withIcon(iconsLoader.getIconPath(context, icon));
            }

            String description = btnElem.attributeValue("description");
            button.withDescription(loadResourceString(context, description));

            String descriptionAsHtml = btnElem.attributeValue("descriptionAsHtml");
            if (descriptionAsHtml != null) {
                button.withDescriptionAsHtml(Boolean.parseBoolean(descriptionAsHtml));
            }

            String sanitize = btnElem.attributeValue("sanitizeHtml");
            if (sanitize != null) {
                button.setSanitizeHtml(Boolean.parseBoolean(sanitize));
            }

            String enabled = btnElem.attributeValue("enabled");
            if (enabled != null) {
                button.setEnabled(Boolean.parseBoolean(enabled));
            }

            String styleName = btnElem.attributeValue("styleName");
            if (StringUtils.isNotBlank(styleName)) {
                button.setStyleName(styleName);
            }

            buttons.add(button);
        }

        return buttons;
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
