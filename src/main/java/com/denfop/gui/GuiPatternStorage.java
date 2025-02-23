package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ImageScreen;
import com.denfop.api.gui.ItemImage;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerPatternStorage;
import com.denfop.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiPatternStorage extends GuiIU<ContainerPatternStorage> {

    private static final ResourceLocation background = new ResourceLocation(
            Constants.MOD_ID,
            "textures/gui/GUIPatternStorage.png"
    );

    public GuiPatternStorage(final ContainerPatternStorage container) {
        super(container);
        this.addElement(new ItemImage(this, 151, 28, () -> container.base.pattern != null ? container.base.pattern.getStack()
                : null));
        this.addComponent(new GuiComponent(this, 7, 19, EnumTypeComponent.LEFT,
                new Component<>(new ComponentButton(container.base, 0) {
                    @Override
                    public String getText() {
                        return Localization.translate("PatternStorage.gui.info.last");
                    }

                    @Override
                    public void ClickEvent() {
                        super.ClickEvent();
                        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(
                                SoundEvents.UI_BUTTON_CLICK,
                                1.0F
                        ));

                    }
                })
        ));
        this.addComponent(new GuiComponent(this, 36, 19, EnumTypeComponent.RIGHT,
                new Component<>(new ComponentButton(container.base, 1) {
                    @Override
                    public String getText() {
                        return Localization.translate("PatternStorage.gui.info.next");
                    }

                    @Override
                    public void ClickEvent() {
                        super.ClickEvent();
                        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(
                                SoundEvents.UI_BUTTON_CLICK,
                                1.0F
                        ));

                    }
                })
        ));
        this.addComponent(new GuiComponent(this, 10, 37, EnumTypeComponent.TOP,
                new Component<>(new ComponentButton(container.base, 2) {
                    @Override
                    public String getText() {
                        return Localization.translate("PatternStorage.gui.info.export");
                    }

                    @Override
                    public void ClickEvent() {
                        super.ClickEvent();
                        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(
                                SoundEvents.UI_BUTTON_CLICK,
                                1.0F
                        ));

                    }
                })
        ));
        this.addComponent(new GuiComponent(this, 26, 37, EnumTypeComponent.DOWN,
                new Component<>(new ComponentButton(container.base, 3) {
                    @Override
                    public String getText() {
                        return Localization.translate("PatternStorage.gui.info.import");
                    }

                    @Override
                    public void ClickEvent() {
                        super.ClickEvent();
                        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(
                                SoundEvents.UI_BUTTON_CLICK,
                                1.0F
                        ));

                    }
                })
        ));
        this.addElement(new ImageScreen(this, 7, 46, 167 - 6, 80 - 45));
        this.addElement(new ImageScreen(this, 150, 27, 18, 18));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        mouseX -= this.guiLeft;
        mouseY -= this.guiTop;


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
        return new ResourceLocation(
                Constants.MOD_ID,
                "textures/gui/guimachine.png"
        );
    }

}
