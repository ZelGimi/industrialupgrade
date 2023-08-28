package com.denfop.items.book.core;

import com.denfop.api.gui.GuiElement;
import com.denfop.api.gui.TextBook;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Pages {

    public static List<Pages> lst = new ArrayList<>();
    public static Map<Pages, List<AddPages>> pages = new HashMap<>();
    public final String mainParent;
    public final String name;
    public final int index;
    public final String text;
    public final ItemStack stack;
    public int width = 0;
    public int height = 0;

    public Pages(
            String name, String mainParent, int index, String text,
            ItemStack stack
    ) {
        this.name = name;
        this.index = index;
        this.text = text;
        this.mainParent = mainParent;
        this.stack = stack;
        lst.add(this);
        addToMainPage(this);
        pages.put(this, new ArrayList<>());
    }

    public void addPages(String name, GuiElement element, boolean ignoring) {
        if (element instanceof TextBook) {
            TextBook textBook = (TextBook) element;
            String text = textBook.getText();
            List<String> textList = TextBook.splitEqually(text);
            StringBuilder text2 = new StringBuilder();
            StringBuilder text3 = new StringBuilder();

            for (String text1 : textList) {
                if (this.height + 9 < 205) {
                    text2.append(text1);
                    this.height += 9;
                } else {
                    text3.append(text1);
                }
            }
            if (!text2.toString().isEmpty()) {
                if (!ignoring) {
                    new AddPages(this.name, pages.get(this).size(), element, name);
                } else {
                    final List<AddPages> addpages = pages.get(this);
                    final AddPages page = addpages.get(addpages.size() - 1);
                    final GuiElement preElement = page.elements.get(page.elements.size() - 1);
                    element.addY(preElement.getHeight());
                    page.elements.add(element);
                }
            } else if (!text3.toString().isEmpty()) {
                final TextBook guiElement = TextBook.create(
                        null,
                        textBook.getX(),
                        textBook.getY(),
                        text3.toString(),
                        textBook.getColor().get(),
                        textBook.isShadow()
                );
                this.addPages(name, guiElement, false);
            }

        } else {

            if (!ignoring) {
                new AddPages(this.name, pages.get(this).size(), element, name);
            } else {
                this.height += element.getHeight() + 2;
                this.width += element.getWidth();
                if (this.height < 205) {
                    final List<AddPages> addpages = pages.get(this);
                    final AddPages page = addpages.get(addpages.size() - 1);
                    final AtomicInteger height = new AtomicInteger();
                    page.elements.forEach(page1 -> height.addAndGet(page1.getHeight()));
                    element.addY(height.get() + 2);
                    page.elements.add(element);
                } else {
                    this.height = 0;
                    this.width = 0;
                    new AddPages(this.name, pages.get(this).size(), element, name);
                }
            }
        }

    }

    public void addPages(GuiElement element, boolean ignoring) {
        this.addPages("", element, ignoring);

    }

    public void addPages(String name, GuiElement element) {
        this.addPages(name, element, false);

    }

    public void addPages(GuiElement element) {
        this.addPages("", element, false);

    }

    public void addToMainPage(final Pages pages) {
        for (Map.Entry<MainPage, List<Pages>> page : MainPage.mainpages.entrySet()) {
            if (page.getKey().name.equals(pages.mainParent)) {
                if (!page.getValue().contains(pages)) {
                    page.getValue().add(pages);
                }
            }
        }
    }

}
