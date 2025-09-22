package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.ItemStackImage;
import com.denfop.container.ContainerWirelessControllerReactors;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import com.denfop.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiWirelessControllerReactors extends GuiIU<ContainerWirelessControllerReactors> {


    public GuiWirelessControllerReactors(ContainerWirelessControllerReactors guiContainer) {
        super(guiContainer);

    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        for (int index = 0; index < 12; index++) {
            if (x >= 28 + (index / 3) * 36 && x < 46 + (index / 3) * 36) {
                if (y >= 28 + (index % 3) * 18 && y < 46 + (index % 3) * 18) {
                    if (!this.container.base.itemStacks.get(index).isEmpty()) {
                        TileMultiBlockBase tileMultiBlockBase = this.container.base.tileMultiBlockBaseList.get((int) index);
                        if (tileMultiBlockBase != null && tileMultiBlockBase.isFull() && !tileMultiBlockBase.isInvalid()) {
                            this.container.base.updateTileServer(Minecraft.getMinecraft().player, index);
                            new PacketUpdateServerTile(this.container.base, index);
                        }
                    }
                }
            }
        }
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.wireless_reactor_controller.info"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 7; i++) {
                compatibleUpgrades.add(Localization.translate("iu.wireless_reactor_controller.info" + i));
            }
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX - 120, mouseY, text);
        }
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        handleUpgradeTooltip(par1, par2);
        for (int i = 0; i < 12; i++) {
            if (!this.container.base.invslot.get(i).isEmpty()) {
                ItemStack stack = this.container.base.itemStacks.get(i);
                if (!stack.isEmpty()) {
                    final NBTTagCompound nbt = ModUtils.nbt(this.container.base.invslot.get(i));
                    BlockPos pos = new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
                    new Area(this, 28 + (i / 3) * 36, 28 + (i % 3) * 18, 18, 18).withTooltip(stack.getDisplayName() + "\n" + "x" +
                            ": " + pos.getX() + " y: " + pos.getY() + " z: " + pos.getZ()).drawForeground(par1, par2);
                }
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        this.bindTexture();
        for (int i = 0; i < 12; i++) {
            if (!this.container.base.invslot.get(i).isEmpty()) {
                ItemStack stack = this.container.base.itemStacks.get(i);
                if (!stack.isEmpty()) {
                    new ItemStackImage(this, 28 + (i / 3) * 36, 28 + (i % 3) * 18, () -> stack).drawBackground(
                            this.guiLeft,
                            this.guiTop
                    );
                }
            }
        }
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(this.guiLeft + 3, guiTop + 3, 0, 0, 10, 10);
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
