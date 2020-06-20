package com.flaurite.addon.headerbutton.web.toolkit.ui.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Timer;

public class LazyDescriptionManager {

    protected ButtonComposition targetComposition;

    protected static final String SHOW_DESCRIPTION_STYLE = "t-show-description";

    protected Timer lazyShowDescription = new Timer() {
        @Override
        public void run() {
            Element button = targetComposition.getButton();
            button.removeClassName(SHOW_DESCRIPTION_STYLE);
            button.addClassName(SHOW_DESCRIPTION_STYLE);
        }
    };

    public void setTargetComposition(ButtonComposition targetComposition) {
        this.targetComposition = targetComposition;
    }

    public void cancelLazyShowing() {
        if (targetComposition != null) {
            targetComposition.getButton().removeClassName(SHOW_DESCRIPTION_STYLE);
        }

        lazyShowDescription.cancel();
    }

    public void show() {
        if (targetComposition != null) {
            // schedule is already removes previous task
            lazyShowDescription.schedule(500);
        }
    }

    public void show(ButtonComposition composition) {
        lazyShowDescription.cancel();

        this.targetComposition = composition;

        if (targetComposition != null) {
            // schedule is already removes previous task
            lazyShowDescription.schedule(500);
        }
    }
}
