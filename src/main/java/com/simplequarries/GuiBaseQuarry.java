package com.simplequarries;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.ComponentEmpty;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ImageInterface;
import com.denfop.api.gui.ImageScreen;
import com.denfop.api.gui.ItemStackImage;
import com.denfop.componets.ComponentButton;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.gui.AdvArea;
import com.denfop.gui.GuiIU;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.tiles.base.TileMultiMatter;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class GuiBaseQuarry extends GuiIU<ContainerBaseQuarry> {

    public final ContainerBaseQuarry container;

    public GuiBaseQuarry(ContainerBaseQuarry container1) {
        super(container1, container1.base.getStyle());
        this.container = container1;
        this.xSize = 250;
        this.elements.add(new ImageInterface(this, 0, 0, this.xSize, this.ySize));
        this.addComponent(new GuiComponent(this, 175, 106, EnumTypeComponent.BIG_FRAME,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 145, 5, EnumTypeComponent.EXP_BUTTON,
                        new Component<>(new ComponentButton(container.base, 40, Localization.translate("sq.add_experience") +
                                "\n" + "EXP: " + ModUtils.getString(this.container.base.exp.getEnergy()) + "/" + ModUtils.getString(this.container.base.exp.getCapacity())))
                )
        );

        this.addComponent(new GuiComponent(this, 208, 31, EnumTypeComponent.FRAME,
                        new Component<>(new ComponentButton(container.base, 50, "") {
                            @Override
                            public String getText() {
                                return Localization.translate("button.rf") + "\n" + Localization.translate(container.base.vein_need
                                        ? "iu.simplyquarries.info5"
                                        : "iu.simplyquarries.info4");
                            }
                        })
                )
        );
        this.elements.add(new ItemStackImage(this, 212,
                35, () -> new ItemStack(IUItem.heavyore, 1, 8)
        ) {
            @Override
            public void drawForeground(final int mouseX, final int mouseY) {

            }
        });
        this.elements.add(new ImageScreen(this, 180, 9, 24, 12));
        this.addComponent(new GuiComponent(this, 179, 25, EnumTypeComponent.PLUS_BUTTON,
                        new Component<>(new ComponentButton(container.base, 0, "") {
                            @Override
                            public String getText() {
                                boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
                                return "+" + (shift ? 10 : 1);
                            }

                            @Override
                            public void ClickEvent() {
                                boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
                                int event = (shift ? 1 : 0);
                                new PacketUpdateServerTile(getEntityBlock(), event);
                            }
                        })
                )
        );
        this.addComponent(new GuiComponent(this, 193, 25, EnumTypeComponent.MINUS_BUTTON,
                        new Component<>(new ComponentButton(container.base, 0, "") {
                            @Override
                            public String getText() {
                                boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
                                return "-" + (shift ? 10 : 1);
                            }

                            @Override
                            public void ClickEvent() {
                                boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
                                int event = 10 + (shift ? 1 : 0);
                                new PacketUpdateServerTile(getEntityBlock(), event);
                            }
                        })
                )
        );

        this.elements.add(new ImageScreen(this, 180, 39, 24, 12));
        this.addComponent(new GuiComponent(this, 179, 55, EnumTypeComponent.PLUS_BUTTON,
                        new Component<>(new ComponentButton(container.base, 0, "") {
                            @Override
                            public String getText() {
                                boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
                                return "+" + (shift ? 10 : 1);
                            }

                            @Override
                            public void ClickEvent() {
                                boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
                                int event = 20 +(shift ? 1 : 0);
                                new PacketUpdateServerTile(getEntityBlock(), event);
                            }
                        })
                )
        );
        this.addComponent(new GuiComponent(this, 193, 55, EnumTypeComponent.MINUS_BUTTON,
                        new Component<>(new ComponentButton(container.base, 0, "") {
                            @Override
                            public String getText() {
                                boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
                                return "-" + (shift ? 10 : 1);
                            }

                            @Override
                            public void ClickEvent() {
                                boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
                                int event = 30 + (shift ? 1 : 0);
                                new PacketUpdateServerTile(getEntityBlock(), event);
                            }
                        })
                )
        );
        this.addComponent(new GuiComponent(this, 213, 11, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 70, this.container.base))
        ));
        this.elements.add(new ImageScreen(this, 170, 69, 62, 34));
        this.componentList.add(new GuiComponent(this, 143, 24, EnumTypeComponent.WORK_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 60, ""){
                    @Override
                    public String getText() {
                        return ((TileBaseQuarry)this.getEntityBlock()).need_work ? Localization.translate("turn_off") :
                                Localization.translate("turn_on");
                    }

                    @Override
                    public boolean active() {
                        return !((TileBaseQuarry)this.getEntityBlock()).need_work;
                    }
                })
        ));
        this.addComponent(new GuiComponent(this, 145, 50, EnumTypeComponent.BUTTON1,
                        new Component<>(new ComponentButton(container.base, 80,Localization.translate("iu.wind_change_side")))
                )
        );
        this.addComponent(new GuiComponent(this, 166, 50, EnumTypeComponent.COLD,
                        new Component<>(this.container.base.cold)
                )
        );
        this.addComponent(new GuiComponent(this, 231, 114, EnumTypeComponent.ENERGY_HEIGHT,
                        new Component<>(this.container.base.energy)
                )
        );
        this.addComponent(new GuiComponent(this, 235, 60, EnumTypeComponent.QUANTUM_HEIGHT1,
                        new Component<>(this.container.base.energy1)
                )
        );
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
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


    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        handleUpgradeTooltip(mouseX, mouseY);
        handleUpgradeTooltip1(mouseX, mouseY);
        this.fontRenderer.drawString(TextFormatting.GREEN + "" + this.container.base.min_y, 190
                , 12, ModUtils.convertRGBcolorToInt(217, 217, 217));
        if (this.container.base.blockpos != null) {
            this.fontRenderer.drawString(TextFormatting.GREEN + "X: " +  this.container.base.blockpos.getX(), 175
                    , 72, ModUtils.convertRGBcolorToInt(217, 217, 217));
            this.fontRenderer.drawString(TextFormatting.GREEN + "Y: " +  this.container.base.blockpos.getY(), 175
                    , 82, ModUtils.convertRGBcolorToInt(217, 217, 217));
            this.fontRenderer.drawString(TextFormatting.GREEN + "Z: " +  this.container.base.blockpos.getZ(), 175
                    , 92, ModUtils.convertRGBcolorToInt(217, 217, 217));
        } else {
            this.fontRenderer.drawString(TextFormatting.GREEN + "X: " +  this.container.base.default_pos.getX(), 175
                    , 72, ModUtils.convertRGBcolorToInt(217, 217, 217));
            this.fontRenderer.drawString(TextFormatting.GREEN + "Y: " +  this.container.base.default_pos.getY(), 175
                    , 82, ModUtils.convertRGBcolorToInt(217, 217, 217));
            this.fontRenderer.drawString(TextFormatting.GREEN + "Z: " +  this.container.base.default_pos.getZ(), 175
                    , 92, ModUtils.convertRGBcolorToInt(217, 217, 217));
        }
        this.fontRenderer.drawString(TextFormatting.GREEN + ""+ (int)this.container.base.energyconsume , 212
                , 77, ModUtils.convertRGBcolorToInt(217, 217, 217));
        this.fontRenderer.drawString(TextFormatting.GREEN + "EF/t" , 212
                , 87, ModUtils.convertRGBcolorToInt(217, 217, 217));
        this.fontRenderer.drawString(TextFormatting.GREEN + "" + this.container.base.max_y, 190
                , 42, ModUtils.convertRGBcolorToInt(217, 217, 217));





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
        super.drawGuiContainerBackgroundLayer(f, x, y);
        int h = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;


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
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(h + 3, k + 3, 0, 0, 10, 10);



    }


    public String getName() {
        return container.base.getInventoryName();
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guimachine.png");
    }

}
