package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.agriculture.ICropItem;
import com.denfop.api.agriculture.genetics.GeneticTraits;
import com.denfop.api.agriculture.genetics.Genome;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerGenomeExtractor;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiGenomeExtractor;
import com.denfop.invslot.InvSlot;
import com.denfop.items.bee.ItemJarBees;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityGenomeExtractor extends TileEntityInventory implements IUpdatableTileEvent {

    public final InvSlotOutput slot;
    public final ComponentBaseEnergy energy;
    public final InvSlot input;
    public Genome genCrop;
    public com.denfop.api.bee.genetics.Genome genBee;
    int meta = -1;
    private boolean work;

    public TileEntityGenomeExtractor(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.genome_extractor,pos,state);
        this.slot = new InvSlotOutput(this, 1);
        this.input = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() instanceof ICropItem || stack.getItem() instanceof ItemJarBees;
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (content.isEmpty()) {
                    genBee = null;
                    genCrop = null;
                } else {
                    if (content.getItem() instanceof ICropItem) {
                        genCrop = new Genome(content);
                        genBee = null;
                    } else {
                        genBee = new com.denfop.api.bee.genetics.Genome(content);
                        genCrop = null;
                    }
                }
                return content;
            }
        };

        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.25));
        energy = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.QUANTUM, this, 1000));
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        final boolean hasGenCrop = customPacketBuffer.readBoolean();
        final boolean hasGenBee = customPacketBuffer.readBoolean();
        if (hasGenCrop) {
            ItemStack stack = customPacketBuffer.readItem();
            genCrop = new Genome(stack);

        } else {
            genCrop = null;
        }
        if (hasGenBee) {
            ItemStack stack = customPacketBuffer.readItem();
            genBee = new com.denfop.api.bee.genetics.Genome(stack);

        } else {
            genBee = null;
        }
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeBoolean(genCrop != null);
        customPacketBuffer.writeBoolean(genBee != null);
        if (genCrop != null) {
            customPacketBuffer.writeItemStack(genCrop.getStack(),false);
        }
        if (genBee != null) {
            customPacketBuffer.writeItemStack(genBee.getStack(),false);
        }
        return customPacketBuffer;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.genome_extractor;
    }


    @Override
    public ContainerGenomeExtractor getGuiContainer(final Player var1) {
        return new ContainerGenomeExtractor(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiGenomeExtractor((ContainerGenomeExtractor) menu);
    }


    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.work && !this.input.get(0).isEmpty() && this.slot.isEmpty() && meta != -1) {
            if (this.energy.getEnergy() >= 50) {
                this.energy.useEnergy(50);
                work = false;
                if (genCrop != null) {
                    genCrop.removeGenome(GeneticTraits.values()[meta], this.input.get(0));
                    this.slot.add(new ItemStack(IUItem.genome_crop.getStack(meta), 1));
                } else if (genBee != null) {
                    genBee.removeGenome(com.denfop.api.bee.genetics.GeneticTraits.values()[meta], this.input.get(0));
                    this.slot.add(new ItemStack(IUItem.genome_bee.getStack(meta), 1));
                }
            }
            meta = -1;
        } else {
            work = false;
            if (this.input.get(0).isEmpty()) {
                this.genCrop = null;
                this.genBee = null;
            }
        }
    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {
        if (var2 == -1) {
            work = true;
        } else {
            meta = (int) var2;
        }
    }

}
