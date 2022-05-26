package com.denfop.items.book.core;

import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddPages {

    public final String mainparent;
    public final String textbefore;
    public final ResourceLocation resource;
    public final int x;
    public final int x1;
    public final int y;
    public final int y1;
    public final String textafter;
    public final String description;
    public final boolean rendercenter;
    public final String centerdescription;
    public final String name;
    public final int index;
    public final String text;
    List<AddPages> lst = new ArrayList<>();

    public AddPages(
            String name, String mainparent, int index, String text,
            String description
    ) {
        this(name, mainparent, index, text, "", null, 0, 0, 0, 0, "", description, false, "");
    }

    public AddPages(
            String name, String mainparent, int index, String text,
            String textbefore, ResourceLocation resource, int x, int y, int x1,
            int y1,
            String textafter, String description, boolean rendercenter, String centerdescription
    ) {
        this.name = name;
        this.index = index;
        this.text = text;
        this.mainparent = mainparent;
        this.textbefore = textbefore;
        this.resource = resource;
        this.x = x;
        this.x1 = x1;
        this.y = y;
        this.y1 = y1;
        this.textafter = textafter;
        this.description = description;
        this.rendercenter = rendercenter;
        this.centerdescription = centerdescription;
        lst.add(this);
        addToMainPage(this);
    }

    public void addToMainPage(final AddPages pages) {
        for (Map.Entry<Pages, List<AddPages>> page : Pages.pages.entrySet()) {
            if (page.getKey().name.equals(pages.mainparent)) {
                if (!page.getValue().contains(pages)) {
                    page.getValue().add(pages);
                }
            }
        }
    }

}
