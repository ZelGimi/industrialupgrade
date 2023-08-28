package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.ItemImage;
import com.denfop.container.ContainerPatternStorage;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.ModUtils;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiPatternStorage extends GuiCore<ContainerPatternStorage> {

    private static final ResourceLocation background = new ResourceLocation(
            Constants.MOD_ID,
            "textures/gui/GUIPatternStorage.png"
    );

    public GuiPatternStorage(final ContainerPatternStorage container) {
        super(container);
        this.addElement(new ItemImage(this, 152, 29, () -> container.base.pattern != null ? container.base.pattern.getStack()
                : null));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        mouseX -= this.guiLeft;
        mouseY -= this.guiTop;
        if (mouseX >= 7 && mouseX <= 16 && mouseY >= 19 && mouseY <= 19 + 18) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            new PacketUpdateServerTile(this.container.base, 0);
        }
        if (mouseX >= 36 && mouseX <= 54 && mouseY >= 19 && mouseY <= 19 + 18) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            new PacketUpdateServerTile(this.container.base, 1);
        }
        if (mouseX >= 10 && mouseX <= 26 && mouseY >= 37 && mouseY <= 37 + 8) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            new PacketUpdateServerTile(this.container.base, 2);
        }
        if (mouseX >= 26 && mouseX <= 42 && mouseY >= 37 && mouseY <= 37 + 8) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            new PacketUpdateServerTile(this.container.base, 3);
        }
    }

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        this.fontRenderer.drawString(Math.min(
                        container.base.index + 1,
                        container.base.maxIndex
                ) + " / " + container.base.maxIndex, this.xSize / 2, 30
                , 4210752,
                false
        );
        new Area(this, 7, 19, 9, 18).withTooltip(Localization.translate("PatternStorage.gui.info.last")).drawForeground(
                mouseX,
                mouseY
        );
        new Area(this, 36, 19, 9, 18).withTooltip(Localization.translate("PatternStorage.gui.info.next")).drawForeground(
                mouseX,
                mouseY
        );
        new Area(this, 10, 37, 16, 8).withTooltip(Localization.translate("PatternStorage.gui.info.export")).drawForeground(
                mouseX,
                mouseY
        );
        new Area(this, 26, 37, 16, 8).withTooltip(Localization.translate("PatternStorage.gui.info.import")).drawForeground(
                mouseX,
                mouseY
        );


        this.fontRenderer.drawString(Localization.translate(Constants.ABBREVIATION + ".generic.text.Name"), 10, 48
                , 16777215,
                false
        );
        this.fontRenderer.drawString(Localization.translate(Constants.ABBREVIATION + ".generic.text.UUMatte"), 10, 59
                , 16777215,
                false
        );
        this.fontRenderer.drawString(Localization.translate(Constants.ABBREVIATION + ".generic.text.Energy"), 10, 70
                , 16777215,
                false
        );
        if (this.container.base.pattern != null) {
            this.fontRenderer.drawString(this.container.base.pattern.getStack().getDisplayName(), 80, 49
                    , 16777215,
                    false
            );

            this.fontRenderer.drawString(ModUtils.getString(container.base.patternUu) + Localization.translate(
                            Constants.ABBREVIATION + ".generic.text.bucketUnit"), 80, 59, 16777215,
                    false
            );
            this.fontRenderer.drawString(ModUtils.getString(container.base.patternEu) + Localization.translate(
                            Constants.ABBREVIATION + ".generic.text.EF"), 80, 70, 16777215,
                    false
            );
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }

    protected ResourceLocation getTexture() {
        return background;
    }

}
