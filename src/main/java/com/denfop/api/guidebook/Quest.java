package com.denfop.api.guidebook;

import com.denfop.IUCore;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Quest {

    private final boolean itemInform;
    public Shape prevShape;
    public ItemStack icon;
    public boolean hasPrev = false;
    public String unLocalizedName;
    public Shape shape;
    public TypeQuest typeQuest;
    public TypeObject typeObject;
    public List<ItemStack> itemStacks;
    public List<FluidStack> fluidStacks;
    public String prevName;
    public int prevX;
    public int prevY;
    public int x;
    public int y;
    private String localizedName;
    private String localizedDescription;

    private Quest(
            int x, int y, String unLocalizedName, String unLocalizedDescription, Shape shape, TypeQuest typeQuest,
            TypeObject typeObject, List<ItemStack> itemStacks, List<FluidStack> fluidStacks, GuideTab guideTab, String prev,
            final ItemStack icon,
            final boolean localizationItem,
            final boolean itemInform,
            final boolean noDescription
    ) {
        this.x = x;
        this.y = y;
        this.unLocalizedName = unLocalizedName;
        this.localizedName = unLocalizedName;
        this.localizedDescription = unLocalizedDescription;
        if (noDescription)
            this.localizedDescription = "";
        this.shape = shape;
        this.typeQuest = typeQuest;
        this.itemInform = itemInform;
        this.fluidStacks = Collections.unmodifiableList(fluidStacks);
        this.itemStacks = Collections.unmodifiableList(itemStacks);
        this.typeObject = typeObject;
        GuideBookCore.instance.addQuestToTab(this, guideTab);
        if (!prev.isEmpty()) {
            Quest prevQuest = GuideBookCore.instance.getPrev("iu.guide_quest_name." + prev, guideTab);
            this.prevX = prevQuest.x;
            this.prevY = prevQuest.y;
            this.hasPrev = true;
            this.prevName = prevQuest.unLocalizedName;
            this.prevShape = prevQuest.shape;
        }
        this.icon = icon;
        if (this.icon == null) {
            if (!this.itemStacks.isEmpty()) {
                this.icon = this.itemStacks.get(0);
            } else if (!this.fluidStacks.isEmpty()) {
                this.icon = ModUtils.getCellFromFluid(this.fluidStacks.get(0).getFluid());
            } else {
                this.icon = new ItemStack(Blocks.COBBLESTONE);
            }
        }
        if (localizationItem) {
            this.localizedName = this.icon.getDescriptionId();
            if (itemStacks.isEmpty() && !fluidStacks.isEmpty()) {
                this.localizedName = fluidStacks.get(0).getDescriptionId();
            }
        }

    }

    public String getLocalizedName() {
        return Localization.translate(localizedName);
    }

    public String getLocalizedDescription() {
        if (itemInform && IUCore.network.getClient() != null) {
            return getLocalization(this.icon);
        }
        return Localization.translate(localizedDescription);
    }

    @OnlyIn(Dist.CLIENT)
    private String getLocalization(ItemStack icon) {
        List<Component> information = new ArrayList<>();
        icon.getItem().appendHoverText(icon, new Item.TooltipContext() {
            @Nullable
            @Override
            public HolderLookup.Provider registries() {
                return Minecraft.getInstance().level.registryAccess();
            }

            @Override
            public float tickRate() {
                return Minecraft.getInstance().level.tickRateManager().tickrate();
            }

            @Nullable
            @Override
            public MapItemSavedData mapData(MapId mapId) {
                return Minecraft.getInstance().level.getMapData(mapId);
            }
        }, information, new TooltipFlag() {
            @Override
            public boolean isAdvanced() {
                return false;
            }

            @Override
            public boolean isCreative() {
                return true;
            }
        });
        AtomicReference<String> result = new AtomicReference<>("");
        information.forEach(informations -> {
            result.set(result.get() + informations.getString().replace("[", "").replace("]", "") + " ");
        });
        return result.get();

    }

    public final static class Builder {

        List<ItemStack> itemStacks = new ArrayList<>();
        List<FluidStack> fluidStacks = new ArrayList<>();
        private String unLocalizedName = "";
        private Shape shape = Shape.DEFAULT;
        private TypeQuest typeQuest = TypeQuest.DETECT;
        private TypeObject typeObject = TypeObject.FLUID_ITEM;
        private GuideTab guideTab = GuideBookCore.instance.guideTabs.get(0);
        private String unLocalizedDescription = "";
        private String prev = "";
        private int x;
        private int y;
        private ItemStack icon;
        private boolean localizationItem = false;
        private boolean itemInform;
        private boolean noDescription;

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(String name) {
            return new Builder().name(name);
        }

        public Builder name(String unLocalizedName) {
            this.unLocalizedName = "iu.guide_quest_name." + unLocalizedName;
            this.description(unLocalizedName);
            return this;
        }

        public Builder localizationItem() {
            this.localizationItem = true;
            return this;
        }

        public Builder noDescription() {
            this.noDescription = true;
            return this;
        }

        public Builder useItemInform() {
            this.itemInform = true;
            return this;
        }

        public Builder shape(Shape shape) {
            this.shape = shape;
            return this;
        }

        public Builder icon(ItemStack icon) {
            this.icon = icon;
            return this;
        }

        public Builder quest(TypeQuest typeQuest) {
            this.typeQuest = typeQuest;
            return this;
        }

        public Builder type(TypeObject typeObject) {
            this.typeObject = typeObject;
            return this;
        }

        public Builder itemStack(ItemStack itemStack) {
            if (this.typeObject.isItem()) {
                this.itemStacks.add(itemStack);
            }
            return this;
        }

        public Builder fluidStack(FluidStack fluidStack) {
            if (this.typeObject.isFluid()) {
                this.fluidStacks.add(fluidStack);
            }
            return this;
        }

        public Builder itemStack(ItemStack... itemStack) {
            if (this.typeObject.isItem()) {
                this.itemStacks.addAll(Arrays.asList(itemStack));
            }
            return this;
        }

        public Builder fluidStack(FluidStack... fluidStack) {
            if (this.typeObject.isFluid()) {
                this.fluidStacks.addAll(Arrays.asList(fluidStack));
            }
            return this;
        }

        public Builder itemStack(List<ItemStack> itemStack) {
            if (this.typeObject.isItem()) {
                this.itemStacks.addAll(itemStack);
            }
            return this;
        }

        public Builder fluidStack(List<FluidStack> fluidStack) {
            if (this.typeObject.isFluid()) {
                this.fluidStacks.addAll(fluidStack);
            }
            return this;
        }

        public Builder tab(GuideTab guideTab) {
            this.guideTab = guideTab;
            return this;
        }

        public Builder description(String unLocalizedDescription) {
            this.unLocalizedDescription = "iu.guide_quest_description." + unLocalizedDescription;
            return this;
        }

        public Builder prev(String prev) {
            this.prev = prev;
            return this;
        }

        public Builder position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public void build() {
            new Quest(
                    x,
                    y,
                    unLocalizedName,
                    unLocalizedDescription,
                    shape,
                    typeQuest,
                    typeObject,
                    itemStacks,
                    fluidStacks,
                    guideTab,
                    prev,
                    icon, localizationItem, itemInform, noDescription
            );
        }

    }

}
