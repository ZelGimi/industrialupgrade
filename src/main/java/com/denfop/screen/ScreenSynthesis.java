package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.blockentity.mechanism.dual.BlockEntitySynthesis;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.containermenu.ContainerMenuDoubleElectricMachine;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Collections;

@OnlyIn(Dist.CLIENT)
public class ScreenSynthesis<T extends ContainerMenuDoubleElectricMachine> extends ScreenMain<ContainerMenuDoubleElectricMachine> {

    public final ContainerMenuDoubleElectricMachine container;

    public ScreenSynthesis(ContainerMenuDoubleElectricMachine container1) {
        super(container1);
        this.container = container1;
        this.componentList.clear();
        this.addComponent(new ScreenWidget(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new ScreenWidget(this, 9, 63, EnumTypeComponent.RAD,
                new WidgetDefault<>(((BlockEntitySynthesis) this.container.base).rad_energy)
        ));
        this.addComponent(new ScreenWidget(this, 6, 34, EnumTypeComponent.ENERGY,
                new WidgetDefault<>(((BlockEntitySynthesis) this.container.base).energy)
        ));
        componentList.add(new ScreenWidget(this, 0, 0, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(
                        EnumTypeComponentSlot.SLOT,
                        Collections.singletonList(this.container.base.inputSlotA)
                ))
        ));
        componentList.add(new ScreenWidget(this, 0, 0, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(
                        EnumTypeComponentSlot.SLOT,
                        Collections.singletonList(((BlockEntitySynthesis) this.container.base).input_slot)
                ))
        ));
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics stack, final int mouseX, final int mouseY) {
        super.drawForegroundLayer(stack, mouseX, mouseY);

    }

    protected void renderBg(GuiGraphics stack, float f, int x, int y) {
        super.renderBg(stack, f, x, y);
        bindTexture(getTexture());
        int progress = (int) (13 * this.container.base.getProgress());
        int progress1 = (int) (24 * this.container.base.getProgress());
        int xoffset = this.guiLeft();
        int yoffset = guiTop();


        if (progress > 0) {
            drawTexturedModalRect(stack, xoffset + 42, yoffset + 35, 177, 33, progress + 1, 14);
        }
        if (progress1 > 0) {
            drawTexturedModalRect(stack, xoffset + 78, yoffset + 33, 177, 52, progress1 + 1, 23);
        }

        final MachineRecipe output = this.container.base.output;
        if (output != null) {
            draw(stack,
                    ChatFormatting.GREEN + Localization.translate("chance") + output.getRecipe().output.metadata.getInt(
                            "percent") + "%", xoffset + 66,
                    yoffset + 66, ModUtils.convertRGBcolorToInt(217, 217, 217)
            );
        }
    }


    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/GuiSynthesis.png".toLowerCase());
    }

}
