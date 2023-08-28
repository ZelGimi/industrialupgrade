package com.denfop.items.book.core;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainPage {

    public static Map<MainPage, List<Pages>> mainpages = new HashMap<>();
    public static List<MainPage> lst = new ArrayList<>();
    public final String name;
    public final int index;
    public final String text;
    public final ItemStack stack;

    public MainPage(String name, int index, String text, ItemStack stack) {
        this.name = name;

        this.index = index;
        this.text = text;
        this.stack = stack;
        lst.add(this);
        mainpages.put(this, new ArrayList<>());
    }

}
