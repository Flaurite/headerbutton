package com.flaurite.addon.headerbutton.ext;

import com.flaurite.addon.headerbutton.impl.HeaderButton;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ScreenHeaderTools {

    private ScreenHeaderTools() {
    }

    public static void checkPosition(int position) {
        if (position < 0) {
            throw new IllegalArgumentException("Position is less than 0");
        }
    }

    public static void checkUniqueId(List<HeaderButton> hButtons, String id) {
        if (hButtons == null) {
            return;
        }

        boolean exist = hButtons.stream()
                .anyMatch(hButton -> hButton.getId().equals(id));

        if (exist) {
            throw new IllegalStateException("HeaderButton with given id: '" + id + "' is already added");
        }
    }

    public static void checkUniqueIds(List<HeaderButton> hButtons) {
        Set<String> uniqueIds = new HashSet<>();
        for (HeaderButton hButton : hButtons) {
            String id = hButton.getId();
            if (!uniqueIds.contains(id)) {
                uniqueIds.add(id);
            } else {
                throw new IllegalStateException("HeaderButton collection contains duplicate id: '" + id + "'");
            }
        }
    }
}
