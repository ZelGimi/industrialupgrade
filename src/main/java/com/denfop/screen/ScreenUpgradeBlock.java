package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.item.upgrade.UpgradeItemInform;
import com.denfop.api.item.upgrade.UpgradeSystem;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.containermenu.ContainerMenuDoubleElectricMachine;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.modules.ItemUpgradeModule;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

import static com.denfop.events.IUEventHandler.getUpgradeItem;

@OnlyIn(Dist.CLIENT)
public class ScreenUpgradeBlock<T extends ContainerMenuDoubleElectricMachine> extends ScreenMain<ContainerMenuDoubleElectricMachine> {

    public final ContainerMenuDoubleElectricMachine container;

    public ScreenUpgradeBlock(ContainerMenuDoubleElectricMachine container1) {
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

        boolean allow = true;
        BaseMachineRecipe output = Recipes.recipes.getRecipeOutput("upgradeblock",
                false, this.container.base.inputSlotA.get(0),
                this.container.base.inputSlotA.get(1)


        );

        if (output != null) {
            if (UpgradeSystem.system.getRemaining(stack1) == 0) {
                allow = false;
            }
            if (module.getItem() instanceof ItemUpgradeModule) {
                EnumInfoUpgradeModules type = ItemUpgradeModule.getType(IUItem.upgrademodule.getMeta((ItemUpgradeModule) module.getItem()));
                int min = 0;
                List<UpgradeItemInform> listInfo = UpgradeSystem.system.getInformation(stack1);
                for (UpgradeItemInform upgradeItemInform : listInfo) {
                    if (upgradeItemInform.upgrade.equals(type)) {
                        min = upgradeItemInform.number;
                    }
                }
                if (min >= type.max) {
                    allow = false;
                }
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
            } else {
                if (progress > 0) {
                    drawTexturedModalRect(poseStack, xoffset + 36, yoffset + 38, 180, 7, progress, 16);
                }
                if (progress1 > 0) {
                    drawTexturedModalRect(poseStack, xoffset + 81, yoffset + 38, 225, 7, progress1, 16);
                }
            }
        }
    }


    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.TEXTURES, "textures/gui/GuiUpgradeBlock.png".toLowerCase());
    }

}
