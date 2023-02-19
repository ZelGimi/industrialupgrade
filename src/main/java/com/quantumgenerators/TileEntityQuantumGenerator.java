package com.quantumgenerators;

import com.denfop.IUItem;
import com.denfop.api.sytem.EnergyType;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.items.ItemCore;
import com.denfop.items.energy.ItemPurifier;
import com.denfop.tiles.base.TileEntityInventory;
import ic2.api.energy.EnergyNet;
import ic2.api.item.ElectricItem;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.IHasGui;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class TileEntityQuantumGenerator extends TileEntityInventory implements IHasGui, INetworkClientTileEntityEventListener {

    public final String texture;
    public final ComponentBaseEnergy energy;
    private final int meta;
    private final int tier;
    public double gen;
    public double genmax;
    private int upgrade;

    public TileEntityQuantumGenerator(int tier, String texture, int meta) {
        this.gen = 5 * Math.pow(4, (tier - 1)) / 16;
        this.genmax = 5 * Math.pow(4, (tier - 1)) / 16;
        this.energy = this.addComponent(ComponentBaseEnergy.asBasicSource(
                EnergyType.QUANTUM,this, gen * 32,
                        tier
                )
        );
        this.tier = tier;
        this.texture = texture;
        this.meta = meta;
        this.upgrade = 0;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.gen = nbttagcompound.getDouble("gen");
        this.genmax = nbttagcompound.getDouble("genmax");
        this.upgrade = nbttagcompound.getInteger("upgrade");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setDouble("genmax", this.genmax);
        nbttagcompound.setDouble("gen", this.gen);
        nbttagcompound.setInteger("upgrade", this.upgrade);
        return nbttagcompound;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip, final ITooltipFlag advanced) {
        super.addInformation(stack, tooltip, advanced);
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.quantumgenerator.info"));
            tooltip.add(Localization.translate("iu.quantumgenerator.info1"));
            tooltip.add(Localization.translate("gui.SuperSolarPanel.generating") + ": " + this.genmax + Localization.translate(
                    "iu.machines_work_energy_type_qe"));

        }
    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        this.energy.setSendingEnabled(true);
    }

    @SideOnly(Side.CLIENT)
    protected boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    protected boolean isNormalCube() {
        return false;
    }

    protected boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    protected boolean isSideSolid(EnumFacing side) {
        return false;
    }

    protected boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    protected void updateEntityServer() {
        super.updateEntityServer();

        this.energy.addEnergy(this.gen);
    }

    @Override
    protected boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        ItemStack stack = player.getHeldItem(hand);
        if (!stack.isEmpty()) {
            if (stack.getItem() instanceof ItemCore && stack.getItemDamage() == this.meta) {
                if (this.upgrade < 8) {
                    this.upgrade += 1;
                    this.gen = 5 * (1 + 0.25 * this.upgrade) * Math.pow(4, (this.tier - 1)) / 16;
                    this.genmax = 5 * (1 + 0.25 * this.upgrade) * Math.pow(4, (this.tier - 1)) / 16;
                    stack.setCount(stack.getCount() - 1);
                    return false;
                }
            } else if (stack.getItem() instanceof ItemPurifier) {
                if (!ElectricItem.manager.canUse(stack, 500)) {
                    return super.onActivated(player, hand, side, hitX, hitY, hitZ);
                }
                if (this.upgrade <= -4) {
                    return super.onActivated(player, hand, side, hitX, hitY, hitZ);
                }

                ElectricItem.manager.use(stack, 500, null);
                this.upgrade -= 1;
                this.gen = 5 * (1 + 0.25 * this.upgrade) * Math.pow(4, (this.tier - 1)) / 16;
                this.genmax = 5 * (1 + 0.25 * this.upgrade) * Math.pow(4, (this.tier - 1)) / 16;
                player.inventory.addItemStackToInventory(new ItemStack(IUItem.core, 1, this.meta));
            }
        }

        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    public void onNetworkEvent(final EntityPlayer entityPlayer, final int i) {
        if (i == 0) {
            if (this.gen + 1000 <= this.genmax) {
                this.gen += 1000;
            } else {
                this.gen = this.genmax;
            }
            this.energy.setCapacity(gen * 32);
        } else if (i == 1) {
            if (this.gen + 10000 <= this.genmax) {
                this.gen += 10000;
            } else {
                this.gen = this.genmax;
            }
            this.energy.setCapacity(gen * 32);
        } else if (i == 2) {
            if (this.gen + 100000 <= this.genmax) {
                this.gen += 100000;
            } else {
                this.gen = this.genmax;
            }
            this.energy.setCapacity(gen * 32);
        } else if (i == 3) {
            if (this.gen + 1000000 <= this.genmax) {
                this.gen += 1000000;
            } else {
                this.gen = this.genmax;
            }
            this.energy.setCapacity(gen * 32);
        }
        if (i == 4) {
            if (this.gen - 1000 >= 0) {
                this.gen -= 1000;
            } else {
                this.gen = 0;
            }
            this.energy.setCapacity(gen * 32);
        } else if (i == 5) {
            if (this.gen - 10000 >= 0) {
                this.gen -= 10000;
            } else {
                this.gen = 0;
            }
            this.energy.setCapacity(gen * 32);
        } else if (i == 6) {
            if (this.gen - 100000 >= 0) {
                this.gen -= 100000;
            } else {
                this.gen = 0;
            }
            this.energy.setCapacity(gen * 32);
        } else if (i == 7) {
            if (this.gen - 1000000 >= 0) {
                this.gen -= 1000000;
            } else {
                this.gen = 0;
            }
            this.energy.setCapacity(gen * 32);
        }

        this.energy.setSourceTier(EnergyNet.instance.getTierFromPower(this.gen));
    }

    @Override
    public ContainerQG getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerQG(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiQG(getGuiContainer(entityPlayer));
    }

    @Override
    public void onGuiClosed(final EntityPlayer entityPlayer) {

    }


}
