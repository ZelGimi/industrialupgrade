package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.crop.CropItem;
import com.denfop.api.crop.genetics.GeneticTraits;
import com.denfop.api.crop.genetics.Genome;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuGenomeExtractor;
import com.denfop.inventory.Inventory;
import com.denfop.items.bee.ItemJarBees;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenGenomeExtractor;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockEntityGenomeExtractor extends BlockEntityInventory implements IUpdatableTileEvent {

    public final InventoryOutput slot;
    public final ComponentBaseEnergy energy;
    public final Inventory input;
    public Genome genCrop;
    public com.denfop.api.bee.genetics.Genome genBee;
    int meta = -1;
    private boolean work;

    public BlockEntityGenomeExtractor(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.genome_extractor, pos, state);
        this.slot = new InventoryOutput(this, 1);
        this.input = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() instanceof CropItem || stack.getItem() instanceof ItemJarBees;
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (content.isEmpty()) {
                    genBee = null;
                    genCrop = null;
                } else {
                    if (content.getItem() instanceof CropItem) {
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
            customPacketBuffer.writeItemStack(genCrop.getStack(), false);
        }
        if (genBee != null) {
            customPacketBuffer.writeItemStack(genBee.getStack(), false);
        }
        return customPacketBuffer;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.genome_extractor;
    }


    @Override
    public ContainerMenuGenomeExtractor getGuiContainer(final Player var1) {
        return new ContainerMenuGenomeExtractor(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenGenomeExtractor((ContainerMenuGenomeExtractor) menu);
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
