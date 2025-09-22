package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.Recipes;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.upgrade.UpgradeModificator;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.modules.ItemUpgradeModule;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static com.denfop.events.IUEventHandler.getUpgradeItem;

@SideOnly(Side.CLIENT)
public class GuiUpgradeBlock extends GuiIU<ContainerDoubleElectricMachine> {

    public final ContainerDoubleElectricMachine container;

    public GuiUpgradeBlock(ContainerDoubleElectricMachine container1) {
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

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);

    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(getTexture());
        int progress = (int) (27 * this.container.base.getProgress());
        int progress1 = (int) (27 * this.container.base.getProgress());
        final int xoffset = (this.width - this.xSize) / 2;
        final int yoffset = (this.height - this.ySize) / 2;

        ItemStack module = getUpgradeItem(this.container.base.inputSlotA.get(0))
                ? this.container.base.inputSlotA.get(1)
                : this.container.base.inputSlotA.get(0);
        ItemStack stack1 = getUpgradeItem(this.container.base.inputSlotA.get(0))
                ? this.container.base.inputSlotA.get(0)
                : this.container.base.inputSlotA.get(1);

        boolean allow = true;
        NBTTagCompound nbt1 = ModUtils.nbt(stack1);
        BaseMachineRecipe output = Recipes.recipes.getRecipeOutput("upgradeblock",
                false, this.container.base.inputSlotA.get(0),
                this.container.base.inputSlotA.get(1)


        );

        if (output != null) {
            if (UpgradeSystem.system.getRemaining(stack1) == 0) {
                allow = false;
            }
            if (module.getItem() instanceof ItemUpgradeModule) {
                EnumInfoUpgradeModules type = ItemUpgradeModule.getType(module.getItemDamage());
                int min = 0;
                final List<UpgradeModificator> list = UpgradeSystem.system.getListModifications(stack1);
                for (int i = 0; i < 4 + list.size(); i++) {
                    if (nbt1.getString("mode_module" + i).equals(type.name)) {
                        min++;
                    }
                }
                if (min >= type.max) {
                    allow = false;
                }
                if (allow) {
                    if (progress > 0) {
                        drawTexturedModalRect(xoffset + 36, yoffset + 38, 180, 7, progress, 16);
                    }
                    if (progress1 > 0) {
                        drawTexturedModalRect(xoffset + 81, yoffset + 38, 225, 7, progress1, 16);
                    }
                } else {
                    drawTexturedModalRect(xoffset + 36, yoffset + 38, 180, 32, 27, 16);
                    drawTexturedModalRect(xoffset + 81, yoffset + 38, 180, 48, 27, 16);


                }
            } else {
                if (progress > 0) {
                    drawTexturedModalRect(xoffset + 36, yoffset + 38, 180, 7, progress, 16);
                }
                if (progress1 > 0) {
                    drawTexturedModalRect(xoffset + 81, yoffset + 38, 225, 7, progress1, 16);
                }
            }
        }
    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/GuiUpgradeBlock.png");
    }

}
