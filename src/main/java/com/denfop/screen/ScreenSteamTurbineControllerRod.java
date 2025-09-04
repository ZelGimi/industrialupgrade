package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.TooltipWidget;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blockentity.mechanism.steamturbine.IRod;
import com.denfop.blocks.mechanism.BlockSteamTurbineEntity;
import com.denfop.containermenu.ContainerMenuSteamTurbineControllerRod;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.recipes.BaseRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ScreenSteamTurbineControllerRod<T extends ContainerMenuSteamTurbineControllerRod> extends ScreenMain<ContainerMenuSteamTurbineControllerRod> {


    private final ItemStack stack;

    public ScreenSteamTurbineControllerRod(ContainerMenuSteamTurbineControllerRod guiContainer) {
        super(guiContainer);
        this.stack = BaseRecipes.getBlockStack(BlockSteamTurbineEntity.steam_turbine_rod);
        this.componentList.clear();
        this.imageWidth = 198;
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) {
        super.mouseClicked(i, j, k);
        int xMin = guiLeft;
        int yMin = guiTop;
        int x = i - guiLeft;
        int y = j - guiTop;
        for (int index = 0; index < this.container.base.getRods().size(); index++) {
            if (x >= 6 + (index) * 21 && x <= 23 + (index) * 21 && y >= 34 && y <= 51) {
                BlockEntityMultiBlockElement tileMultiBlockBase =
                        (BlockEntityMultiBlockElement) container.base
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
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        for (int i = 0; i < this.container.base.getRods().size(); i++) {
            BlockPos pos = container.base.getRods().get(i).getBlockPos();
            new TooltipWidget(this, 6 + (i) * 21, 34, 18, 18).withTooltip(stack.getDisplayName().getString() + "\n" + "x" +
                    ": " + pos.getX() + " y: " + pos.getY() + " z: " + pos.getZ()).drawForeground(poseStack, par1, par2);

        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        this.bindTexture();
        for (int i = 0; i < this.container.base.getRods().size(); i++) {
            IRod tileMultiBlockBase =
                    container.base
                            .getRods()
                            .get(i);
            if (tileMultiBlockBase.getRods().size() == 4) {
                drawTexturedModalRect(poseStack, this.guiLeft + 5 + (i) * 21, this.guiTop + 33, 236, 21, 20, 20);

            }


        }
    }

    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guisteamturbine_controllerblade.png");
    }

}
