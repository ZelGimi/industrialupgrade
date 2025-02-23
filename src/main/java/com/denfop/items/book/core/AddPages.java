package com.denfop.items.book.core;

import com.denfop.api.gui.GuiElement;
import com.denfop.gui.GuiCore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AddPages {

    public final String mainParent;
    public final int index;
    private final String name;
    public List<GuiElement> elements;
    public GuiCore guiCore;
    List<AddPages> lst = new ArrayList<>();


    public AddPages(
            String mainParent, int index, GuiElement element
    ) {
        this.index = index;
        this.mainParent = mainParent;
        this.elements = new ArrayList<>(Collections.singletonList(element));
        this.name = "";
        lst.add(this);
        addToMainPage(this);
    }

    public AddPages(
            String mainParent, int index, List<GuiElement> elements
    ) {
        this.index = index;
        this.mainParent = mainParent;
        this.elements = elements;
        this.name = "";
        lst.add(this);
        addToMainPage(this);
    }

    public AddPages(
            String mainParent, int index, GuiElement element, String name
    ) {
        this.index = index;
        this.mainParent = mainParent;
        this.elements = new ArrayList<>(Collections.singletonList(element));
        this.name = name;
        lst.add(this);
        addToMainPage(this);
    }

    public AddPages(
            String mainParent, int index, List<GuiElement> elements, String name
    ) {
        this.index = index;
        this.mainParent = mainParent;
        this.elements = elements;
        this.name = name;
        lst.add(this);
        addToMainPage(this);
    }

    public List<GuiElement> getElements() {
        return elements;
    }

    public String getName() {
        return name;
    }

    public String getMainParent() {
        return mainParent;
    }

    public GuiCore getGuiCore() {
        return guiCore;
    }

    public void setGuiCore(final GuiCore guiCore) {
        this.guiCore = guiCore;
    }

    public void drawBackground(int mouseX, int mouseY) {
        GuiElement preElement = null;
        for (GuiElement element : this.elements) {
            if (element != null) {
                int y = preElement == null ? 0 : preElement.getHeight();
                if (this.guiCore != null) {
                    if (element.getGui() != null) {
                        element.drawBackground(mouseX, mouseY);
                    } else {
                        element.setGui(this.guiCore);
                        element.drawBackground(mouseX, mouseY);

                    }
                }
                preElement = element;
            }
        }
    }

    public void drawForeground(int mouseX, int mouseY) {
        GuiElement preElement = null;
        for (GuiElement element : this.elements) {
            if (element != null) {
                if (this.guiCore != null) {
                    int y = preElement == null ? 0 : preElement.getHeight();
                    if (element.getGui() != null) {
                        element.drawForeground(mouseX, mouseY);
                    } else {
                        element.setGui(this.guiCore);
                        element.drawForeground(mouseX, mouseY);

                    }
                }
                preElement = element;
            }
        }
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
