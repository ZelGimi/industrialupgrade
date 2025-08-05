package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.ItemStackImage;
import com.denfop.blocks.mechanism.BlockSteamTurbine;
import com.denfop.container.ContainerSteamTurbineControllerRod;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.recipes.BaseRecipes;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.mechanism.steamturbine.IRod;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class GuiSteamTurbineControllerRod<T extends ContainerSteamTurbineControllerRod> extends GuiIU<ContainerSteamTurbineControllerRod> {


    private final ItemStack stack;

    public GuiSteamTurbineControllerRod(ContainerSteamTurbineControllerRod guiContainer) {
        super(guiContainer);
        this.stack = BaseRecipes.getBlockStack(BlockSteamTurbine.steam_turbine_rod);
        this.componentList.clear();
        this.imageWidth=198;
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) {
        super.mouseClicked(i, j, k);
        int xMin = guiLeft;
        int yMin = guiTop;
        int x = i - guiLeft;
        int y =j - guiTop;
        for (int index = 0; index < this.container.base.getRods().size(); index++) {
            if (x >= 6 + (index) * 21 && x <= 23 + (index ) * 21 && y >= 34 && y <= 51) {
                TileEntityMultiBlockElement tileMultiBlockBase =
                        (TileEntityMultiBlockElement) container.base
                                .getRods()
                                .get(index);
                if (tileMultiBlockBase != null && tileMultiBlockBase.getMain() != null && tileMultiBlockBase
                        .getMain()
                        .isFull() && !tileMultiBlockBase.isRemoved()) {
                    this.container.base.updateTileServer(Minecraft.getInstance().player, index);
                    new PacketUpdateServerTile(this.container.base, index);
                }
            }
        }

    }


    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack,par1, par2);
        for (int i = 0; i < this.container.base.getRods().size(); i++) {
            BlockPos pos = container.base.getRods().get(i).getBlockPos();
            new Area(this, 6 + (i) * 21, 34, 18, 18).withTooltip(stack.getDisplayName().getString() + "\n" + "x" +
                    ": " + pos.getX() + " y: " + pos.getY() + " z: " + pos.getZ()).drawForeground(poseStack,par1, par2);

        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack,partialTicks, mouseX, mouseY);
        this.bindTexture();
        for (int i = 0; i < this.container.base.getRods().size(); i++) {
            IRod tileMultiBlockBase =
                    container.base
                            .getRods()
                            .get(i);
            if (  tileMultiBlockBase.getRods().size() == 4){
                drawTexturedModalRect(poseStack, this.guiLeft + 5+ (i) * 21, this.guiTop + 33, 236, 21, 20, 20);

            }



        }
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guisteamturbine_controllerblade.png");
    }

}
