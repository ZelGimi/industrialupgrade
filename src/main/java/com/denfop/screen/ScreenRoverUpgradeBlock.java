package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.space.rovers.enums.EnumTypeUpgrade;
import com.denfop.api.space.upgrades.SpaceUpgradeSystem;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.containermenu.ContainerMenuDoubleElectricMachine;
import com.denfop.items.space.ItemSpaceUpgradeModule;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static com.denfop.blockentity.mechanism.BlockEntityUpgradeRover.getUpgradeItem;


public class ScreenRoverUpgradeBlock<T extends ContainerMenuDoubleElectricMachine> extends ScreenMain<ContainerMenuDoubleElectricMachine> {

    public final ContainerMenuDoubleElectricMachine container;

    public ScreenRoverUpgradeBlock(ContainerMenuDoubleElectricMachine container1) {
        super(container1);
        this.container = container1;
        this.componentList.clear();
        this.addComponent(new ScreenWidget(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new ScreenWidget(this, 7, 65, EnumTypeComponent.ENERGY,
                new WidgetDefault<>(this.container.base.energy)
        ));
    }


    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture(getTexture());
        int progress = (int) (27 * this.container.base.getProgress());
        int progress1 = (int) (27 * this.container.base.getProgress());
        final int xoffset = guiLeft;
        final int yoffset = guiTop;

        ItemStack module = getUpgradeItem(this.container.base.inputSlotA.get(0))
                ? this.container.base.inputSlotA.get(1)
                : this.container.base.inputSlotA.get(0);
        ItemStack stack1 = getUpgradeItem(this.container.base.inputSlotA.get(0))
                ? this.container.base.inputSlotA.get(0)
                : this.container.base.inputSlotA.get(1);

        boolean allow;
        BaseMachineRecipe output = Recipes.recipes.getRecipeOutput("roverupgradeblock",
                false, this.container.base.inputSlotA.get(0),
                this.container.base.inputSlotA.get(1)


        );

        if (output != null) {
            if (SpaceUpgradeSystem.system.getRemaining(stack1) == 0) {
                allow = false;
                drawTexturedModalRect(poseStack, xoffset + 36, yoffset + 38, 180, 32, 27, 16);
                drawTexturedModalRect(poseStack, xoffset + 81, yoffset + 38, 180, 48, 27, 16);
            } else if (module.getItem() instanceof ItemSpaceUpgradeModule) {
                final EnumTypeUpgrade type = ItemSpaceUpgradeModule.getType(((ItemSpaceUpgradeModule<?>) module.getItem()).getElement().getId());
                allow = SpaceUpgradeSystem.system.shouldUpdate(type, stack1);

                if (allow) {
                    if (progress > 0) {
                        drawTexturedModalRect(poseStack, xoffset + 36, yoffset + 38, 180, 7, progress, 16);
                    }
                    if (progress1 > 0) {
                        drawTexturedModalRect(poseStack, xoffset + 81, yoffset + 38, 225, 7, progress1, 16);
                    }
                } else {
                    drawTexturedModalRect(poseStack, xoffset + 36, yoffset + 38, 180, 32, 27, 16);
                    drawTexturedModalRect(poseStack, xoffset + 81, yoffset + 38, 180, 48, 27, 16);


                }
            }
        }
    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/GuiUpgradeBlock.png".toLowerCase());
    }

}
