package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.ItemStackImage;
import com.denfop.blocks.mechanism.BlockSteamTurbine;
import com.denfop.container.ContainerSteamTurbineControllerRod;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.recipes.BaseRecipes;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import java.io.IOException;

public class GuiSteamTurbineControllerRod extends GuiIU<ContainerSteamTurbineControllerRod> {


    private final ItemStack stack;

    public GuiSteamTurbineControllerRod(ContainerSteamTurbineControllerRod guiContainer) {
        super(guiContainer);
        this.stack = BaseRecipes.getBlockStack(BlockSteamTurbine.steam_turbine_rod);
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        for (int index = 0; index < this.container.base.getRods().size(); index++) {
            if (x >= 28 + (index / 3) * 36 && x < 46 + (index / 3) * 36) {
                if (y >= 28 + (index % 3) * 18 && y < 46 + (index % 3) * 18) {
                    TileEntityMultiBlockElement tileMultiBlockBase =
                            (TileEntityMultiBlockElement) container.base.getWorld().getTileEntity(container.base
                                    .getRods()
                                    .get(index)
                                    .getBlockPos());
                    if (tileMultiBlockBase != null && tileMultiBlockBase.getMain() != null && tileMultiBlockBase.getMain().isFull() && !tileMultiBlockBase.isInvalid()) {
                        this.container.base.updateTileServer(Minecraft.getMinecraft().player, index);
                        new PacketUpdateServerTile(this.container.base, index);
                    }
                }
            }
        }
    }


    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        for (int i = 0; i < this.container.base.getRods().size(); i++) {
            BlockPos pos = container.base.getRods().get(i).getBlockPos();
            new Area(this, 28 + (i / 3) * 36, 28 + (i % 3) * 18, 18, 18).withTooltip(stack.getDisplayName() + "\n" + "x" +
                    ": " + pos.getX() + " y: " + pos.getY() + " z: " + pos.getZ()).drawForeground(par1, par2);

        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        this.bindTexture();
        for (int i = 0; i < this.container.base.getRods().size(); i++) {


            new ItemStackImage(this, 28 + (i / 3) * 36, 28 + (i % 3) * 18, () -> stack).drawBackground(
                    this.guiLeft,
                    this.guiTop
            );


        }
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
