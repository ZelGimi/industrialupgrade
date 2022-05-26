package com.denfop.items.book.core;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pages {

    public static List<Pages> lst = new ArrayList<>();
    public static Map<Pages, List<AddPages>> pages = new HashMap<>();
    public final String mainparent;
    public final String name;
    public final int index;
    public final String text;
    public final ItemStack stack;

    public Pages(
            String name, String mainparent, int index, String text,
            ItemStack stack
    ) {
        this.name = name;
        this.index = index;
        this.text = text;
        this.mainparent = mainparent;
        this.stack = stack;
        lst.add(this);
        addToMainPage(this);
        pages.put(this, new ArrayList<>());
    }

    public void addToMainPage(final Pages pages) {
        for (Map.Entry<MainPage, List<Pages>> page : MainPage.mainpages.entrySet()) {
            if (page.getKey().name.equals(pages.mainparent)) {
                if (!page.getValue().contains(pages)) {
                    page.getValue().add(pages);
                }
            }
        }
    }

}
