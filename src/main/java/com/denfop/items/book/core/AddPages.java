package com.denfop.items.book.core;

import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddPages {

    public final String mainParent;
    public final String textBefore;
    public final ResourceLocation resource;
    public final int x;
    public final int x1;
    public final int y;
    public final int y1;
    public final String textAfter;
    public final String description;
    public final boolean renderCenter;
    public final String centerDescription;
    public final String name;
    public final int index;
    public final String text;
    List<AddPages> lst = new ArrayList<>();

    public AddPages(
            String name, String mainParent, int index, String text,
            String description
    ) {
        this(name, mainParent, index, text, "", null, 0, 0, 0, 0, "", description, false, "");
    }

    public AddPages(
            String name, String mainParent, int index, String text,
            String textBefore, ResourceLocation resource, int x, int y, int x1,
            int y1,
            String textAfter, String description, boolean renderCenter, String centerDescription
    ) {
        this.name = name;
        this.index = index;
        this.text = text;
        this.mainParent = mainParent;
        this.textBefore = textBefore;
        this.resource = resource;
        this.x = x;
        this.x1 = x1;
        this.y = y;
        this.y1 = y1;
        this.textAfter = textAfter;
        this.description = description;
        this.renderCenter = renderCenter;
        this.centerDescription = centerDescription;
        lst.add(this);
        addToMainPage(this);
    }

    public void addToMainPage(final AddPages pages) {
        for (Map.Entry<Pages, List<AddPages>> page : Pages.pages.entrySet()) {
            if (page.getKey().name.equals(pages.mainParent)) {
                if (!page.getValue().contains(pages)) {
                    page.getValue().add(pages);
                }
            }
        }
    }

}
