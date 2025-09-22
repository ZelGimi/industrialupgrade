package com.quantumgenerators;

import com.denfop.ElectricItem;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.sytem.EnergyType;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.items.ItemCore;
import com.denfop.items.energy.ItemPurifier;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.client.gui.GuiScreen;
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

import java.io.IOException;
import java.util.List;

public class TileQuantumGenerator extends TileEntityInventory implements IUpdatableTileEvent {

    public final String texture;
    public final ComponentBaseEnergy energy;
    private final int meta;
    private final int tier;
    public double gen;
    public double genmax;
    private int upgrade;

    public TileQuantumGenerator(int tier, String texture, int meta) {
        this.gen = 5 * Math.pow(4, (tier - 1)) / 16;
        this.genmax = 5 * Math.pow(4, (tier - 1)) / 16;
        this.energy = this.addComponent(ComponentBaseEnergy.asBasicSource(
                        EnergyType.QUANTUM, this, gen * 32,
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
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
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
    public boolean wrenchCanRemove(final EntityPlayer player) {
        return super.wrenchCanRemove(player) &&this.genmax >= 5 * Math.pow(4, (tier - 1)) / 16;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            gen = (double) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, gen);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        this.energy.setSendingEnabled(true);
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    public boolean isNormalCube() {
        return false;
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    public boolean isSideSolid(EnumFacing side) {
        return false;
    }

    public boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    public void updateEntityServer() {
        super.updateEntityServer();

        this.energy.addEnergy(this.gen);
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
    }

    @Override
    public boolean onActivated(
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
                    this.energy.setCapacity(genmax * 32);
                    this.energy.setSourceTier(EnergyNetGlobal.instance.getTierFromPower(this.gen));
                    stack.setCount(stack.getCount() - 1);
                    return true;
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
                this.energy.setCapacity(genmax * 32);
                this.energy.setSourceTier(EnergyNetGlobal.instance.getTierFromPower(this.gen));
                player.inventory.addItemStackToInventory(new ItemStack(IUItem.core, 1, this.meta));
                return true;
            }
        }

        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    public void updateTileServer(final EntityPlayer entityPlayer, final double i) {
        this.gen = i;
        if (this.gen  >= this.genmax) {
            this.gen = this.genmax;
        }
        this.energy.setCapacity(genmax * 32);
        this.energy.setSourceTier(EnergyNetGlobal.instance.getTierFromPower(this.gen));
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


}
