package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.agriculture.ICropItem;
import com.denfop.api.agriculture.genetics.GeneticTraits;
import com.denfop.api.agriculture.genetics.Genome;
import com.denfop.api.agriculture.genetics.IGenomeItem;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.*;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.*;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerInoculator;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiInoculator;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.items.bee.ItemJarBees;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.Set;

public class TileEntityInoculator extends TileElectricMachine implements IHasRecipe, IAudioFixer, IUpgradableBlock, IUpdateTick,
        IUpdatableTileEvent {

    public final InvSlotRecipes inputSlotA;
    public final InvSlotUpgrade upgradeSlot;
    public ComponentUpgradeSlots componentUpgrade;
    public ComponentProgress componentProgress;
    public ComponentProcess componentProcess;
    public MachineRecipe output;

    public TileEntityInoculator(BlockPos pos, BlockState state) {
        super(300, 1, 1,BlockBaseMachine3.inoculator,pos,state);
        Recipes.recipes.addInitRecipes(this);
        this.inputSlotA = new InvSlotRecipes(this, "inoculator", this) {
            @Override
            public boolean accepts(final ItemStack itemStack, final int index) {
                return itemStack.getItem() instanceof ICropItem || itemStack.getItem() instanceof com.denfop.api.bee.genetics.IGenomeItem || itemStack.getItem() instanceof IGenomeItem || itemStack.getItem() == IUItem.jarBees.getStack(0);
            }
        };
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);

        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot) {
            @Override
            public void onLoaded() {
                super.onLoaded();
                this.componentProcess = ((TileEntityInoculator) this.getParent()).componentProcess;
            }
        });

        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.25));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 300
        ));
        this.componentProcess = this.addComponent(new ComponentProcess(this, 300, 1) {

            @Override
            public void operateWithMax(final MachineRecipe output, final int size) {
                this.operateWithMax(output);
            }

            @Override
            public boolean checkRecipe() {
                if (output == null) {
                    return false;
                }
                ItemStack stack1 = (isCrop(this.invSlotRecipes.get(0)) || isBee(this.invSlotRecipes.get(0)))
                        ? this.invSlotRecipes.get(0)
                        : this.invSlotRecipes.get(1);
                ItemStack genome = (isCrop(this.invSlotRecipes.get(0)) || isBee(this.invSlotRecipes.get(0)))
                        ? this.invSlotRecipes.get(1)
                        : this.invSlotRecipes.get(0);
                if (isCrop(stack1)) {
                    IGenomeItem genomeItem = (IGenomeItem) genome.getItem();
                    Genome genome1 = new Genome(stack1);
                    final GeneticTraits genomeTraits = genomeItem.getGenomeTraits(genome);
                    return !genome1.hasGenome(genomeTraits.getGenetic());
                } else if (isBee(stack1)) {
                    com.denfop.api.bee.genetics.IGenomeItem genomeItem = (com.denfop.api.bee.genetics.IGenomeItem) genome.getItem();
                    com.denfop.api.bee.genetics.Genome genome1 = new com.denfop.api.bee.genetics.Genome(stack1);
                    final com.denfop.api.bee.genetics.GeneticTraits genomeTraits = genomeItem.getGenomeTraits(genome);
                    return !genome1.hasGenome(genomeTraits.getGenetic());
                }
                return false;
            }

            public boolean isCrop(ItemStack stack) {
                return stack.getItem() instanceof ICropItem;
            }

            public boolean isBee(ItemStack stack) {
                return stack.getItem() instanceof ItemJarBees;
            }

            @Override
            public void operateWithMax(final MachineRecipe output) {

                ItemStack stack1 = (isCrop(this.invSlotRecipes.get(0)) || isBee(this.invSlotRecipes.get(0)))
                        ? this.invSlotRecipes.get(0)
                        : this.invSlotRecipes.get(1);
                ItemStack genome = (isCrop(this.invSlotRecipes.get(0)) || isBee(this.invSlotRecipes.get(0)))
                        ? this.invSlotRecipes.get(1)
                        : this.invSlotRecipes.get(0);
                if (isCrop(stack1)) {
                    IGenomeItem genomeItem = (IGenomeItem) genome.getItem();
                    Genome genome1 = new Genome(stack1);
                    final GeneticTraits genomeTraits = genomeItem.getGenomeTraits(genome);
                    genome1.addGenome(genomeTraits, stack1);
                    ItemStack outputStack = genome1.getStack().copy();
                    this.invSlotRecipes.consume();
                    this.outputSlot.add(outputStack);
                } else if (isBee(stack1)) {
                    com.denfop.api.bee.genetics.IGenomeItem genomeItem = (com.denfop.api.bee.genetics.IGenomeItem) genome.getItem();
                    com.denfop.api.bee.genetics.Genome genome1 = new com.denfop.api.bee.genetics.Genome(stack1);
                    final com.denfop.api.bee.genetics.GeneticTraits genomeTraits = genomeItem.getGenomeTraits(genome);
                    genome1.addGenome(genomeTraits);
                    ItemStack outputStack = genome1.getStack().copy();
                    this.invSlotRecipes.consume();
                    this.outputSlot.add(outputStack);

                }


            }


        });
        this.componentProcess.setHasAudio(true);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);
    }

    public static void addRecipe(ItemStack container, ItemStack output) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "inoculator",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(container), input.getInput(output)),
                        new RecipeOutput(null, container)
                )
        );
    }

    public void init() {
        for (int i = 0; i < 82; i++) {
            if (i == 3) {
                continue;
            }
            ItemStack input = new ItemStack(IUItem.crops.getStack(0));

            final CompoundTag nbt = ModUtils.nbt(input);
            nbt.putInt("crop_id", i);
            for (GeneticTraits traits : GeneticTraits.values()) {
                addRecipe(input, new ItemStack(IUItem.genome_crop.getStack(traits.ordinal()), 1));
            }
        }
        ;
        for (int i = 1; i < 6; i++) {
            ItemStack input = IUItem.jarBees.getStack(0).getStackFromId(i);
            for (com.denfop.api.bee.genetics.GeneticTraits traits : com.denfop.api.bee.genetics.GeneticTraits.values()) {
                addRecipe(input, new ItemStack(IUItem.genome_bee.getStack(traits.ordinal()), 1));
            }
        }
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.inoculator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiInoculator((ContainerInoculator) menu);
    }

    @Override
    public ContainerInoculator getGuiContainer(final Player var1) {
        return new ContainerInoculator(var1, this);
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.ItemExtract, UpgradableProperty.ItemInput
        );
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
    }

}
