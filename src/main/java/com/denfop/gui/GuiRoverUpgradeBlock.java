package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.Recipes;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.space.rovers.enums.EnumTypeUpgrade;
import com.denfop.api.space.upgrades.SpaceUpgradeSystem;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.items.space.ItemSpaceUpgradeModule;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static com.denfop.tiles.mechanism.TileEntityUpgradeRover.getUpgradeItem;


public class GuiRoverUpgradeBlock<T extends ContainerDoubleElectricMachine> extends GuiIU<ContainerDoubleElectricMachine> {

    public final ContainerDoubleElectricMachine container;

    public GuiRoverUpgradeBlock(ContainerDoubleElectricMachine container1) {
        super(container1);
        this.container = container1;
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new GuiComponent(this, 7, 65, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
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
        return ResourceLocation.tryBuild(Constants.TEXTURES, "textures/gui/GuiUpgradeBlock.png".toLowerCase());
    }

}
