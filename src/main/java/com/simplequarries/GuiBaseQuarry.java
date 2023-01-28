package com.simplequarries;

import com.denfop.IUItem;
import com.denfop.gui.AdvArea;
import com.denfop.gui.GuiIU;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import ic2.core.IC2;
import ic2.core.init.Localization;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class GuiBaseQuarry extends GuiIU<ContainerBaseQuarry> {

    public final ContainerBaseQuarry container;

    public GuiBaseQuarry(ContainerBaseQuarry container1) {
        super(container1, container1.base.getStyle());
        this.container = container1;
        this.xSize = 229;
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 147 && mouseX <= 158 && mouseY >= 27 && mouseY <= 76) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("gui.SuperSolarPanel.storage") + ": " + ModUtils.getString(this.container.base.energy.getEnergy()) + "/" + ModUtils.getString(
                    this.container.base.energy.getCapacity()));
            List<String> compatibleUpgrades = getStringList();
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        } else if (mouseX >= 189 && mouseX <= 206 && mouseY >= 83 && mouseY <= 100) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("button.rf"));
            List<String> compatibleUpgrades = Collections.singletonList(Localization.translate(this.container.base.vein_need
                    ? "iu.simplyquarries.info5"
                    : "iu.simplyquarries.info4"));
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }
            this.drawTooltip(mouseX, mouseY, text);
        } else if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.simplyquarries_info"));
            List<String> compatibleUpgrades = ListInformationUtils.quarry;
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    protected void mouseClicked(int i, int j, int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
        if (x >= 179 && x <= 189 && y >= 29 && y <= 39) {
            IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 0 + (shift ? 1 : 0));
        }
        if (x >= 206 && x <= 216 && y >= 29 && y <= 39) {
            IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 10 + (shift ? 1 : 0));
        }
        if (x >= 179 && x <= 189 && y >= 64 && y <= 74) {
            IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 20 + (shift ? 1 : 0));
        }
        if (x >= 206 && x <= 216 && y >= 64 && y <= 74) {
            IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 30 + (shift ? 1 : 0));
        }
        if (x >= 146 && x <= 160 && y >= 5 && y <= 23) {
            IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 40 + (shift ? 1 : 0));
        }
        if (x >= 189 && x <= 206 && y >= 83 && y <= 100) {
            IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 50);
        }
        if (x >= 209 && x <= 224 && y >= 86 && y <= 99) {
            IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 60);
        }
    }

    private List<String> getStringList() {
        List<String> lst = new ArrayList<>();
        if (this.container.base != null) {
            lst.add(Localization.translate("iu.machines_work_energy") + this.container.base.energyconsume + "EU/t");
            if (this.container.base.blockpos != null) {
                lst.add("X: " + this.container.base.blockpos.getX());
                lst.add("Y: " + this.container.base.blockpos.getY());
                lst.add("Z: " + this.container.base.blockpos.getZ());
            } else {
                lst.add("X: " + this.container.base.default_pos.getX());
                lst.add("Y: " + this.container.base.default_pos.getY());
                lst.add("Z: " + this.container.base.default_pos.getZ());

            }
        }
        return lst;
    }

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        handleUpgradeTooltip(mouseX, mouseY);
        handleUpgradeTooltip1(mouseX, mouseY);
        this.fontRenderer.drawString(TextFormatting.GREEN + "" + this.container.base.min_y, 190
                , 12, ModUtils.convertRGBcolorToInt(217, 217, 217));
        this.fontRenderer.drawString(TextFormatting.GREEN + "" + this.container.base.max_y, 190
                , 48, ModUtils.convertRGBcolorToInt(217, 217, 217));
        new AdvArea(this, 172, 27, 175, 75)
                .withTooltip("EXP: " + ModUtils.getString(this.container.base.exp.getEnergy()) + "/" + ModUtils.getString(this.container.base.exp.getCapacity()))
                .drawForeground(mouseX
                        , mouseY);
        new AdvArea(this, 163, 27, 167, 75)
                .withTooltip("QE: " + ModUtils.getString(this.container.base.energy1.getEnergy()) + "/" + ModUtils.getString(this
                        .container.base.energy1.getCapacity()))
                .drawForeground(mouseX
                        , mouseY);
        String tooltip =
                ModUtils.getString(this.container.base
                        .cold
                        .getEnergy()) + "°C" + "/" + ModUtils.getString(this.container.base.cold.getCapacity()) + "°C";
        new AdvArea(this, 140, 62, 144, 76)
                .withTooltip(tooltip)
                .drawForeground(mouseX
                        , mouseY);
        new AdvArea(this, 209, 86, 224, 99).withTooltip(this.container.base.need_work ? Localization.translate("turn_off") :
                Localization.translate("turn_on")).drawForeground(mouseX, mouseY);
        new AdvArea(this, 146, 5, 160, 23).withTooltip(Localization.translate("sq.add_experience")).drawForeground(mouseX
                , mouseY);
        boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
        if (!shift) {
            new AdvArea(this, 179, 29, 189, 39).withTooltip("+1").drawForeground(mouseX
                    , mouseY);
            new AdvArea(this, 179, 64, 189, 74).withTooltip("+1").drawForeground(mouseX
                    , mouseY);
            new AdvArea(this, 206, 29, 216, 39).withTooltip("-1").drawForeground(mouseX
                    , mouseY);
            new AdvArea(this, 206, 64, 216, 74).withTooltip("-1").drawForeground(mouseX
                    , mouseY);
        } else {
            new AdvArea(this, 179, 29, 189, 39).withTooltip("+10").drawForeground(mouseX
                    , mouseY);
            new AdvArea(this, 179, 64, 189, 74).withTooltip("+10").drawForeground(mouseX
                    , mouseY);
            new AdvArea(this, 206, 29, 216, 39).withTooltip("-10").drawForeground(mouseX
                    , mouseY);
            new AdvArea(this, 206, 64, 216, 74).withTooltip("-10").drawForeground(mouseX
                    , mouseY);
        }
    }

    private void handleUpgradeTooltip1(int mouseX, int mouseY) {
        if (this.container.base.blockpos != null) {
            if (mouseX >= 186 && mouseX <= 217 && mouseY >= 118 && mouseY < 149) {
                int x = mouseX - 186;
                int z = mouseY - 118;
                final Chunk chunk = this.container.base.getWorld().getChunkFromBlockCoords(this.container.base.blockpos);
                final int chunkx = chunk.x * 16;
                final int chunkz = chunk.z * 16;
                BlockPos pos1 = new BlockPos(chunkx + (x / 2), this.container.base.blockpos.getY(), chunkz + (z / 2));
                IBlockState state = this.container.base.getWorld().getBlockState(pos1);
                Block block = state.getBlock();

                List<String> text = new ArrayList<>();
                if (state.getMaterial() == Material.AIR) {
                    text.add(new ItemStack(Items.AIR).getDisplayName());
                } else if (new ItemStack(block, 1,
                        block.getMetaFromState(state)
                ).getItem() == Items.AIR) {
                    text.add(block.getLocalizedName());
                } else {
                    text.add(new ItemStack(block, 1,
                            block.getMetaFromState(state)
                    ).getDisplayName());
                }
                List<String> compatibleUpgrades = new ArrayList<>();
                compatibleUpgrades.add("X: " + pos1.getX());
                compatibleUpgrades.add("Y: " + pos1.getY());
                compatibleUpgrades.add("Z: " + pos1.getZ());
                Iterator<String> var5 = compatibleUpgrades.iterator();
                String itemstack;
                while (var5.hasNext()) {
                    itemstack = var5.next();
                    text.add(itemstack);
                }

                this.drawTooltip(mouseX, mouseY, text);


            }
        }
    }

    private int getColor(IBlockState state, World world, BlockPos pos) {
        if (state.getMaterial() == Material.AIR) {
            return (int) this.container.base.getWorld().provider.getCloudColor(0).x;
        }
        MapColor color = state.getBlock().getMapColor(state, world, pos);
        return color.colorValue | -16777216;
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        int h = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        this.mc.getTextureManager().bindTexture(getTexture());
        drawTexturedModalRect(h, k, 0, 0, this.xSize, this.ySize);
        // drawTexturedModalRect(h, k + 112, 0, 112, 176, this.ySize - 112);
        this.drawBackground();
        this.mc.getTextureManager().bindTexture(getTexture());
        int chargeLevel = (int) (48.0F * this.container.base.energy.getEnergy()
                / this.container.base.energy.getCapacity());
        int exp = (int) (48.0F * this.container.base.exp.getEnergy()
                / this.container.base.exp.getCapacity());
        int chargeLevel1 = (int) (48.0F * this.container.base.energy1.getEnergy()
                / this.container.base.energy1.getCapacity());
        int heat = (int) (14.0F * this.container.base.cold.getFillRatio());
        if (this.container.base.blockpos != null) {
            final double[][] colors = new double[16][16];
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    final Chunk chunk = this.container.base.getWorld().getChunkFromBlockCoords(this.container.base.blockpos);
                    final int chunkx = chunk.x * 16;
                    final int chunkz = chunk.z * 16;
                    BlockPos pos1 = new BlockPos(chunkx + i, this.container.base.blockpos.getY(), chunkz + j);

                    IBlockState state = this.container.base.getWorld().getBlockState(pos1);
                    colors[i][j] = this.getColor(state, this.container.base.getWorld(), pos1);
                }
            }
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            for (int i = 0; i < 32; i++) {
                for (int j = 0; j < 32; j++) {
                    this.drawColoredRect(186 + i, 117 + j, 1, 1, (int) colors[i / 2][j / 2]);
                }
            }
        }
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (heat >= 0) {
            drawTexturedModalRect(
                    h + 140, k + 62 + 14 - heat, 194, 219 + 14 - heat, 4,
                    heat
            );

        }
        if (this.container.base.need_work) {
            this.drawTexturedModalRect(h + 209, k + 86, 233, 53, 239 - 224, 84 - 70);

        }
        if (exp > 0) {
            drawTexturedModalRect(h + 172, k + 28 + 48 - exp, 194,
                    171 + 48 - exp, 3, exp
            );
        }
        if (chargeLevel1 > 0) {
            drawTexturedModalRect(h + 164, k + 28 + 48 - chargeLevel1, 200,
                    171 + 48 - chargeLevel1, 12, chargeLevel1
            );
        }
        if (chargeLevel > 0) {
            drawTexturedModalRect(h + 140 + 1 + 5 + 1, k + 28 + 48 - chargeLevel, 176,
                    171 + 48 - chargeLevel, 12, chargeLevel
            );
        }
        int y1 = 6;
        for (int i = 0; i < this.container.base.index; i++) {
            drawTexturedModalRect(h + 6, k + y1, 174,
                    220, 16, 16
            );
            y1 += 18;
        }
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation(IC2.RESOURCE_DOMAIN, "textures/gui/infobutton.png"));
        drawTexturedModalRect(h + 3, k + 3, 0, 0, 10, 10);
        this.mc.getTextureManager().bindTexture(getTexture());
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glPushMatrix();
        GL11.glColor4f(0.1F, 1, 0.1F, 1);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        this.zLevel = 100.0F;
        mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        ItemStack stack;
        stack = new ItemStack(IUItem.heavyore, 1, 8);
        itemRender.renderItemAndEffectIntoGUI(
                stack,
                h + 190,
                k + 84
        );
        GL11.glEnable(GL11.GL_LIGHTING);
        GlStateManager.enableLighting();

        RenderHelper.enableStandardItemLighting();
        GL11.glColor4f(0.1F, 1, 0.1F, 1);
        GL11.glPopMatrix();

    }


    public String getName() {
        return container.base.getInventoryName();
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(SQConstants.MOD_ID, "textures/gui/GUIQuantumQuerry.png");
    }

}
