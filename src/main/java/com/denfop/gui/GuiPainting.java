package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.items.ItemPaints;
import com.denfop.utils.ModUtils;
import ic2.core.IC2;
import ic2.core.init.Localization;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiPainting extends GuiIU<ContainerDoubleElectricMachine> {

    public final ContainerDoubleElectricMachine container;

    public GuiPainting(ContainerDoubleElectricMachine container1) {
        super(container1);
        this.container = container1;
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
    }

    private static List<String> getInformation() {
        List<String> ret = new ArrayList<>();
        ret.add(Localization.translate("iu.paintinginformation1"));
        ret.add(Localization.translate("iu.paintinginformation2"));
        ret.add(Localization.translate("iu.paintinginformation3"));
        ret.add(Localization.translate("iu.paintinginformation4"));
        ret.add(Localization.translate("iu.paintinginformation5"));


        return ret;
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);

        handleUpgradeTooltip1(par1, par2);
        String tooltip2 =
                ModUtils.getString(Math.min(
                        this.container.base.energy.getEnergy(),
                        this.container.base.energy.getCapacity()
                )) + "/" + ModUtils.getString(this.container.base.energy.getCapacity()) + " " +
                        "EU";
        new AdvArea(this, 26, 56, 37, 71)
                .withTooltip(tooltip2)
                .drawForeground(par1, par2);
        if (!this.container.base.inputSlotA.get(1).isEmpty() && !this.container.base.inputSlotA
                .get(0)
                .isEmpty() && this.container.base.output != null) {
            this.fontRenderer.drawString(ModUtils.mode(this.container.base.output.getRecipe().output.metadata), 41, 70, 4210752);
        }
    }

    private void handleUpgradeTooltip1(int mouseX, int mouseY) {
        if (mouseX >= 165 && mouseX <= 175 && mouseY >= 0 && mouseY <= 12) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.paintinginformation"));
            List<String> compatibleUpgrades = getInformation();
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);

        this.mc.getTextureManager().bindTexture(getTexture());

        this.mc.getTextureManager().bindTexture(new ResourceLocation(IC2.RESOURCE_DOMAIN, "textures/gui/infobutton.png"));
        this.drawTexturedModalRect(this.guiLeft + 165, this.guiTop, 0, 0, 10, 10);
        this.mc.getTextureManager().bindTexture(this.getTexture());

        int chargeLevel = (int) (14.0F * this.container.base.getChargeLevel());
        int progress = (int) (14 * this.container.base.componentProgress.getBar());

        if (chargeLevel > 0) {
            drawTexturedModalRect(this.guiLeft + 25, this.guiTop + 57 + 14 - chargeLevel, 176, 14 - chargeLevel,
                    14, chargeLevel
            );
        }
        int down;
        ItemStack stack = ItemStack.EMPTY;
        if (!this.container.base.inputSlotA.get(0).isEmpty() && this.container.base.output != null) {
            stack = this.container.base.inputSlotA.get(0).getItem() instanceof ItemPaints
                    ? this.container.base.inputSlotA.get(0) : this.container.base.inputSlotA.get(1);
        }
        if (stack.isEmpty()) {
            down = 0;
        } else {
            down = 14 * (stack.getItemDamage() - 1);
        }

        if (progress > 0 && down >= 0 && !stack.isEmpty()) {
            drawTexturedModalRect(this.guiLeft + 75, this.guiTop + 35, 178, 33 + down, progress + 1, 13);
        }
        if (this.container.base.output != null) {
            stack = this.container.base.inputSlotA.get(0).getItem() instanceof ItemPaints
                    ? this.container.base.inputSlotA.get(1) : this.container.base.inputSlotA.get(0);

            RenderHelper.enableGUIStandardItemLighting();
            GL11.glPushMatrix();
            GL11.glColor4f(0.1F, 1, 0.1F, 1);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GlStateManager.disableLighting();
            GlStateManager.enableDepth();
            this.zLevel = 100.0F;
            mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            itemRender.renderItemAndEffectIntoGUI(
                    ModUtils.mode(this.container.base.output.getRecipe().output.metadata, stack),
                    this.guiLeft + 106,
                    this.guiTop + 52
            );
            GL11.glEnable(GL11.GL_LIGHTING);
            GlStateManager.enableLighting();

            RenderHelper.enableStandardItemLighting();
            GL11.glColor4f(0.1F, 1, 0.1F, 1);
            GL11.glPopMatrix();
        }
        RenderHelper.disableStandardItemLighting();
        RenderHelper.enableGUIStandardItemLighting();
    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIPainter.png");
    }

}
