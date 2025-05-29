package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.*;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerPatternStorage;
import com.denfop.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

public class GuiPatternStorage<T extends ContainerPatternStorage> extends GuiIU<ContainerPatternStorage> {

    private static final ResourceLocation background = new ResourceLocation(
            Constants.MOD_ID,
            "textures/gui/GUIPatternStorage.png".toLowerCase()
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
                        Minecraft.getInstance().getSoundManager().play(
                                SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F)
                        );

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
                        Minecraft.getInstance().getSoundManager().play(
                                SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F)
                        );

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
                        Minecraft.getInstance().getSoundManager().play(
                                SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F)
                        );

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
                        Minecraft.getInstance().getSoundManager().play(
                                SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F)
                        );

                    }
                })
        ));
        this.addElement(new ImageScreen(this, 7, 46, 167 - 6, 80 - 45));
        this.addElement(new ImageScreen(this, 150, 27, 18, 18));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        mouseX -= this.guiLeft;
        mouseY -= this.guiTop;


    }

    @Override
protected void drawForegroundLayer(GuiGraphics poseStack, final int mouseX, final int mouseY) {
        super.drawForegroundLayer( poseStack,mouseX, mouseY);
     draw( poseStack,Math.min(
                        container.base.index + 1,
                        container.base.maxIndex
                ) + " / " + container.base.maxIndex, this.imageWidth / 2, 30
                , 4210752
        );


        draw( poseStack,Localization.translate(Constants.ABBREVIATION + ".generic.text.Name"), 10, 48
                , 16777215
        );
        draw( poseStack,Localization.translate(Constants.ABBREVIATION + ".generic.text.UUMatte"), 10, 59
                , 16777215
        );
        draw( poseStack,Localization.translate(Constants.ABBREVIATION + ".generic.text.Energy"), 10, 70
                , 16777215
        );
        if (this.container.base.pattern != null) {
           draw( poseStack,this.container.base.pattern.getStack().getDisplayName(), 80, 49
                    , 16777215
            );

            draw( poseStack,ModUtils.getString(container.base.patternUu) + Localization.translate(
                            Constants.ABBREVIATION + ".generic.text.bucketUnit"), 105, 59, 16777215
            );
            draw( poseStack,ModUtils.getString(container.base.patternEu) + Localization.translate(
                            Constants.ABBREVIATION + ".generic.text.EF"), 80, 70, 16777215
            );
        }
    }



    protected ResourceLocation getTexture() {
        return new ResourceLocation(
                Constants.MOD_ID,
                "textures/gui/guimachine.png"
        );
    }

}
