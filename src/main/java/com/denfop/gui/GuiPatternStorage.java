package com.denfop.gui;

import com.denfop.api.gui.*;
import com.denfop.api.recipe.RecipeInfo;
import com.denfop.container.ContainerPatternStorage;
import com.denfop.tiles.mechanism.TileEntityPatternStorage;
import ic2.core.init.Localization;
import ic2.core.util.Util;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPatternStorage extends GuiIC2<ContainerPatternStorage> {

    private static final ResourceLocation background = new ResourceLocation("ic2", "textures/gui/GUIPatternStorage.png");

    public GuiPatternStorage(final ContainerPatternStorage container) {
        super(container);
        this.addElement((new CustomButton(this, 7, 19, 9, 18, this.createEventSender(0))).withTooltip(
                "ic2.PatternStorage.gui.info.last"));
        this.addElement((new CustomButton(this, 36, 19, 9, 18, this.createEventSender(1))).withTooltip(
                "ic2.PatternStorage.gui.info.next"));
        this.addElement((new CustomButton(this, 10, 37, 16, 8, this.createEventSender(2))).withTooltip(
                "ic2.PatternStorage.gui.info.export"));
        this.addElement((new CustomButton(this, 26, 37, 16, 8, this.createEventSender(3))).withTooltip(
                "ic2.PatternStorage.gui.info.import"));
        this.addElement(Text.create(this, this.xSize / 2, 30, TextProvider.of(() -> {
            TileEntityPatternStorage te = container.base;
            return Math.min(te.index + 1, te.maxIndex) + " / " + te.maxIndex;
        }), 4210752, false, true, false));
        this.addElement(Text.create(this, 10, 48, TextProvider.ofTranslated("ic2.generic.text.Name"), 16777215, false));
        this.addElement(Text.create(this, 10, 59, TextProvider.ofTranslated("ic2.generic.text.UUMatte"), 16777215, false));
        this.addElement(Text.create(this, 10, 70, TextProvider.ofTranslated("ic2.generic.text.Energy"), 16777215, false));
        IEnableHandler patternInfoEnabler = () -> container.base.pattern != null;
        this.addElement(Text.create(this, 80, 48, TextProvider.of(() -> {
            final RecipeInfo pattern = container.base.pattern;
            return pattern != null ? pattern.getStack().getDisplayName() : null;
        }), 16777215, false).withEnableHandler(patternInfoEnabler));
        this.addElement(Text
                .create(
                        this,
                        80,
                        59,
                        TextProvider.of(() -> Util.toSiString(container.base.patternUu, 4) + Localization.translate(
                                "ic2.generic.text.bucketUnit")),
                        16777215,
                        false
                )
                .withEnableHandler(patternInfoEnabler));
        this.addElement(Text
                .create(
                        this,
                        80,
                        70,
                        TextProvider.of(() -> Util.toSiString(container.base.patternEu, 4) + Localization.translate(
                                "ic2.generic.text.EU")),
                        16777215,
                        false
                )
                .withEnableHandler(patternInfoEnabler));
        this.addElement(new ItemImage(this, 152, 29, () -> container.base.pattern != null ? container.base.pattern.getStack()
                : null));
    }

    protected ResourceLocation getTexture() {
        return background;
    }

}
