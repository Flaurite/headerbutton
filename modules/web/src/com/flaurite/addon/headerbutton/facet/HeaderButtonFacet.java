package com.flaurite.addon.headerbutton.facet;

import com.flaurite.addon.headerbutton.impl.HeaderButton;
import com.haulmont.cuba.gui.components.Facet;
import com.haulmont.cuba.gui.components.HasSubParts;

import java.util.List;

/**
 * Manages buttons in the dialog header: add, remove, get.
 * <p>
 * Example 1:
 * <pre>{@code
 * @Inject
 * private HeaderButtonFacet headerButtons;
 *
 * @Subscribe
 * public void onInit(InitEvent event) {
 *     headerButtons.addButton(new HeaderButton("editBtn")
 *             .withCaption("Edit")
 *             .withIcon(CubaIcon.EDIT)
 *             .withClickHandler(clickEvent -> {
 *                 // make fields editable
 *             }));
 * }
 * }</pre>
 * <p>
 * Example 2:
 * <pre>{@code
 * xmlns:hb="http://schemas.headerbutton/headerbutton/0.1/headerbutton.xsd"
 *
 * <facets>
 *     <hb:headerButtons id="headerButtons">
 *         <hb:button id="infoBtn" icon="font-icon:INFO"/>
 *     </hb:headerButtons>
 * </facets>
 *
 * @Subscribe("headerButtons.info")
 * public void onInfoBtnClick(HeaderButton.ButtonClickEvent event) {
 *     notifications.create()
 *             .withCaption("Dialog information")
 *             .show();
 * }
 * }</pre>
 */
public interface HeaderButtonFacet extends Facet, HasSubParts {

    /**
     * Sets list of header buttons. If facet contains buttons, they will be replaced.
     *
     * @param headerButtons list of header buttons
     */
    void setButtons(List<HeaderButton> headerButtons);

    /**
     * @return current list of buttons in the dialog header
     */
    List<HeaderButton> getButtons();

    /**
     * Adds a button to the dialog header.
     * <p>
     * <br>
     * Example:
     * <pre>{@code
     * headerButtons.addButton(new HeaderButton("editBtn")
     *         .withCaption("Edit")
     *         .withIcon(CubaIcon.EDIT)
     *         .withClickHandler(clickEvent -> {
     *             // make fields editable
     *         }));
     * }</pre>
     *
     * @param headerButton button
     */
    void addButton(HeaderButton headerButton);

    /**
     * Adds a button to the dialog header at position index. 0 - index is a leftmost position in the header.
     * <p>
     * <br>
     * Example:
     * <pre>{@code
     * headerButtons.addButton(0, new HeaderButton("editBtn")
     *         .withCaption("Edit")
     *         .withIcon(CubaIcon.EDIT)
     *         .withClickHandler(clickEvent -> {
     *             // make fields editable
     *         }));
     * }</pre>
     *
     * @param position     button position
     * @param headerButton button
     */
    void addButton(int position, HeaderButton headerButton);

    /**
     * Removes button by its id
     *
     * @param id button's id
     */
    void removeButton(String id);
}
