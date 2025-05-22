package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.Redstone;
import com.denfop.componets.RedstoneHandler;
import com.denfop.container.ContainerAutoFuse;
import com.denfop.gui.GuiAutoFuse;
import com.denfop.invslot.InvSlot;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityAutoFuse extends TileEntityInventory {

    public final InvSlot slotBomb;
    public final ComponentBaseEnergy rad_energy;
    private final Redstone redstone;
    public boolean fuse = false;
    public int timer = 60;
    private boolean boom = false;

    public TileEntityAutoFuse() {
        this.slotBomb = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() == IUItem.nuclear_bomb.itemBlock;
            }
        };
        this.rad_energy = this.addComponent(ComponentBaseEnergy.asBasicSource(EnergyType.RADIATION, this, 100000));
        this.redstone = this.addComponent(new Redstone(this));
        this.redstone.subscribe(new RedstoneHandler() {
                                    @Override
                                    public void action(final int input) {
                                        fuse = !fuse;
                                        new PacketUpdateFieldTile(getParent(), "fuse", fuse);
                                    }

                                }
        );
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.autofuse;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        this.fuse = customPacketBuffer.readBoolean();
        this.timer = customPacketBuffer.readInt();
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeBoolean(this.fuse);
        customPacketBuffer.writeInt(this.timer);
        return customPacketBuffer;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiAutoFuse(getGuiContainer(var1));
    }

    @Override
    public ContainerAutoFuse getGuiContainer(final EntityPlayer var1) {
        return new ContainerAutoFuse(this, var1);
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("timer")) {
            is.readUnsignedByte();
            this.timer = is.readInt();
            if (this.timer == 0) {
                this.boom = true;
            }
        }
        if (name.equals("fuse")) {
            is.readUnsignedByte();
            this.fuse = is.readBoolean();

        }
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        NBTTagCompound nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.setBoolean("fuse", fuse);
        return nbtTagCompound;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        fuse = nbtTagCompound.getBoolean("fuse");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateEntityClient() {
        super.updateEntityClient();
        if (this.fuse && !this.slotBomb.isEmpty()) {
            if (timer > 0) {
                timer--;
                if (timer == 0) {
                    if (boom) {
                        boom = false;
                        this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.pos.getX(), this.pos.getY() + 1,
                                this.pos.getZ(), 0D, 0.0D, 0.0D
                        );
                    }
                    this.timer = 60;
                } else {
                    this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.pos.getX(), this.pos.getY() + 0.5D,
                            this.pos.getZ(),
                            0.0D, 0.0D,
                            0.0D
                    );
                    this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.pos.getX(), this.pos.getY() + 0.5D,
                            this.pos.getZ(),
                            0.0D, 0.0D,
                            0.0D
                    );
                }

            }
        }
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        this.setActive(this.fuse);
        if (this.fuse && !this.slotBomb.isEmpty()) {
            if (timer >= 0) {
                if (timer != 0) {
                    timer--;
                }
                if (timer % 10 == 0) {
                    new PacketUpdateFieldTile(this, "timer", timer);
                }
                if (timer == 0) {
                    this.rad_energy.addEnergy(300);
                    this.slotBomb.get().shrink(1);
                    timer = 60;
                }


            }
        }
    }

}
