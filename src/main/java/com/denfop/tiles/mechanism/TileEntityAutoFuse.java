package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.Redstone;
import com.denfop.componets.RedstoneHandler;
import com.denfop.container.ContainerAutoFuse;
import com.denfop.container.ContainerBase;
import com.denfop.gui.GuiAutoFuse;
import com.denfop.gui.GuiCore;
import com.denfop.invslot.InvSlot;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class TileEntityAutoFuse extends TileEntityInventory {

    public final InvSlot slotBomb;
    public final ComponentBaseEnergy rad_energy;
    private final Redstone redstone;
    public boolean fuse = false;
    public int timer = 60;
    private boolean boom = false;

    public TileEntityAutoFuse(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.autofuse,pos,state);
        this.slotBomb = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() == IUItem.nuclear_bomb.getItem();
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
    public void addInformation(ItemStack stack, List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.autofuse.info"));
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.autofuse;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
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
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiAutoFuse((ContainerAutoFuse) menu);
    }

    @Override
    public ContainerAutoFuse getGuiContainer(final Player var1) {
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
    public CompoundTag writeToNBT(final CompoundTag nbt) {
        CompoundTag nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.putBoolean("fuse", fuse);
        return nbtTagCompound;
    }

    @Override
    public void readFromNBT(final CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        fuse = nbtTagCompound.getBoolean("fuse");
    }

    @Override
    public void updateEntityClient() {
        super.updateEntityClient();
        if (this.fuse && !this.slotBomb.isEmpty()) {
            if (timer > 0) {
                timer--;
                if (timer == 0) {
                    if (boom) {
                        boom = false;
                        this.level.addParticle(ParticleTypes.EXPLOSION,
                                this.worldPosition.getX() + 0.5D,
                                this.worldPosition.getY() + 1.0D,
                                this.worldPosition.getZ() + 0.5D,
                                0.0D, 0.0D, 0.0D);
                    }
                    this.timer = 60;
                } else {
                    this.level.addParticle(ParticleTypes.SMOKE,
                            this.worldPosition.getX() + 0.5D,
                            this.worldPosition.getY() + 0.5D,
                            this.worldPosition.getZ() + 0.5D,
                            0.0D, 0.0D, 0.0D);
                    this.level.addParticle(ParticleTypes.LARGE_SMOKE,
                            this.worldPosition.getX() + 0.5D,
                            this.worldPosition.getY() + 0.5D,
                            this.worldPosition.getZ() + 0.5D,
                            0.0D, 0.0D, 0.0D);
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
                    this.slotBomb.get(0).shrink(1);
                    timer = 60;
                }


            }
        }
    }

}
