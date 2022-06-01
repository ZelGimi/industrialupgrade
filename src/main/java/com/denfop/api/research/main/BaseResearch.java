package com.denfop.api.research.main;

import net.minecraft.item.ItemStack;

import java.util.List;

public class BaseResearch implements IResearch {

    public final String name;
    public final boolean unique;
    public final int minlevel;
    public final int points;
    public final IResearchPages page;
    public final boolean depends;
    public final IResearch dependencies;
    public final ItemStack itemstack;
    public final Icon icon;
    public final int id;
    public List<IResearchPart> parts;

    public BaseResearch(
            String name, IResearchPages page, boolean unique, ItemStack itemstack, Icon icon,
            List<IResearchPart> list, int id
    ) {
        this(name, page, 0, 0, unique, itemstack, icon, list, id);

    }

    public BaseResearch(
            String name, IResearchPages page, int minlevel, int points, boolean unique, ItemStack itemstack, Icon icon,
            List<IResearchPart> list, int id
    ) {
        this(name, page, minlevel, points, unique, null, itemstack, icon, list, id);
    }

    public BaseResearch(
            String name, IResearchPages page, int minlevel, int points, ItemStack itemstack, Icon icon,
            List<IResearchPart> list, int id
    ) {
        this(name, page, minlevel, points, false, null, itemstack, icon, list, id);
    }

    public BaseResearch(
            String name, IResearchPages page, int minlevel, int points, IResearch research, ItemStack itemstack, Icon icon,
            List<IResearchPart> list, int id
    ) {
        this(name, page, minlevel, points, false, research, itemstack, icon, list, id);
    }

    public BaseResearch(
            String name,
            IResearchPages page,
            int minlevel,
            int points,
            boolean unique,
            IResearch research,
            ItemStack itemstack,
            Icon icon,
            List<IResearchPart> list,
            int id
    ) {
        this.name = name;
        this.unique = unique;
        this.minlevel = minlevel;
        this.points = points;
        this.page = page;
        this.depends = research != null;
        this.dependencies = research;
        this.itemstack = itemstack;
        this.icon = icon;
        this.parts = list;
        this.id = id;
    }

    public BaseResearch(
            String name, IResearchPages page, ItemStack itemstack, Icon icon,
            List<IResearchPart> list, int id
    ) {
        this(name, page, 0, 0, false, itemstack, icon, list, id);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getMinLevel() {
        return this.minlevel;
    }

    @Override
    public int pointsPractise() {
        return this.points;
    }

    @Override
    public boolean IsUnique() {
        return this.unique;
    }

    @Override
    public boolean dependsOnOther() {
        return this.depends;
    }

    @Override
    public IResearch getDependencies() {
        return this.dependencies;
    }

    @Override
    public IResearchPages getResearchPage() {
        return this.page;
    }

    @Override
    public ItemStack getItemStack() {
        return this.itemstack;
    }

    @Override
    public Icon getIcon() {
        return this.icon;
    }

    @Override
    public List<IResearchPart> getPartsResearch() {
        return this.parts;
    }

    @Override
    public void setPartsResearch(final List<IResearchPart> list) {
        this.parts = list;
    }

    @Override
    public int getID() {
        return this.id;
    }

}
