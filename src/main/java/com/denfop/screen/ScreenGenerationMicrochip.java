package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.containermenu.ContainerMenuBaseGenerationChipMachine;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenGenerationMicrochip<T extends ContainerMenuBaseGenerationChipMachine> extends ScreenMain<ContainerMenuBaseGenerationChipMachine> {

    public final ContainerMenuBaseGenerationChipMachine container;

    public ScreenGenerationMicrochip(
            ContainerMenuBaseGenerationChipMachine container1
    ) {
        super(container1);
        this.container = container1;
        this.componentList.clear();
        this.addComponent(new ScreenWidget(this, 127, 52, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new ScreenWidget(this, 7, 62, EnumTypeComponent.ENERGY, new WidgetDefault<>(this.container.base.energy)));
        this.addComponent(new ScreenWidget(this, 68, 60, EnumTypeComponent.HEAT,
                new WidgetDefault<>(container1.base.heat)
        ));
    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);

    }


    protected void drawBackgroundAndTitle(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft(), this.guiTop(), 0, 0, this.imageWidth, this.imageHeight);
        this.drawTexturedModalRect(poseStack, this.guiLeft() - 22, this.guiTop() + 82, 8, 7, 20, 20);
    }

    protected void renderBg(PoseStack poseStack, float f, int x, int y) {
        super.renderBg(poseStack, f, x, y);
        int xoffset = (this.width - this.imageWidth) / 2;
        int yoffset = (this.height - this.imageHeight) / 2;
        this.bindTexture();
        int progress = (int) (15.0F * this.container.base.componentProgress.getBar());
        int progress1 = (int) (10.0F * this.container.base.componentProgress.getBar());
        int progress2 = (int) (20F * this.container.base.componentProgress.getBar());

        if (progress > 0) {
            drawTexturedModalRect(poseStack, xoffset + 28, yoffset + 12, 176, 34, progress + 1, 32);
        }
        if (progress1 > 0) {
            drawTexturedModalRect(poseStack, xoffset + 60, yoffset + 16, 176, 65, progress1 + 1, 21);
        }
        if (progress2 > 0) {
            drawTexturedModalRect(poseStack, xoffset + 89, yoffset + 22, 176, 86, progress2 + 1, 8);
        }


    }

    public String getName() {
        return "";
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/GUICirsuit.png".toLowerCase());
    }

}
