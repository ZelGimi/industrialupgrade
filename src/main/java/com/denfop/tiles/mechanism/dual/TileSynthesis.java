package com.denfop.tiles.mechanism.dual;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.*;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.ComponentProcess;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiSynthesis;
import com.denfop.invslot.InvSlot;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.EnumDoubleElectricMachine;
import com.denfop.tiles.base.TileDoubleElectricMachine;
import com.denfop.utils.ModUtils;
import com.denfop.utils.ParticleUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class TileSynthesis extends TileDoubleElectricMachine implements IHasRecipe {

    public final ComponentBaseEnergy rad_energy;
    public final InvSlot input_slot;

    public TileSynthesis(BlockPos pos, BlockState state) {
        super(1, 300, 1, EnumDoubleElectricMachine.SYNTHESIS, false, BlockBaseMachine1.synthesis, pos, state);
        Recipes.recipes.addInitRecipes(this);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot) {
            @Override
            public void onLoaded() {
                super.onLoaded();
                this.componentProcess = ((TileSynthesis) this.getParent()).componentProcess;
            }
        });
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 300
        ));
        this.componentProcess = this.addComponent(new ComponentProcess(this, 300, 1) {
            @Override
            public void operateWithMax(final MachineRecipe output) {
                consumeRadiation(1);
                operate(output);

            }

            public void operateWithMax(MachineRecipe output, int size) {

                if (output.getRecipe() == null) {
                    return;
                }
                size = Math.min(this.getSESize(size), this.getRadiationSize(size));
                consumeRadiation(size);
                for (int i = 0; i < size; i++) {
                    operate(output);
                }
            }

            protected int getRadiationSize(int size) {
                final int amount = this.updateTick.getRecipeOutput().getRecipe().output.metadata.getInt("rad_amount");
                return (int) Math.min(size, rad_energy.getEnergy() / amount);
            }

            public void operateOnce(List<ItemStack> processResult) {
                this.invSlotRecipes.consume();
                CompoundTag nbt = this.updateTick.getRecipeOutput().getRecipe().output.metadata;
                int procent = nbt.getInt("percent");
                RandomSource rand = this.getParent().getWorld().random;
                if ((rand.nextInt(100) + 1) > (100 - procent)) {
                    this.outputSlot.add(this.updateTick.getRecipeOutput().getRecipe().output.items.get(0));

                }

            }
        });
        this.rad_energy = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.RADIATION, this, 10000));
        this.componentProcess.setHasAudio(true);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);
        this.input_slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (this.get(0).isEmpty()) {
                    ((TileSynthesis) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((TileSynthesis) this.base).inputSlotA.changeAccepts(this.get(0));
                }
                return content;
            }

            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() == IUItem.recipe_schedule.getItem();
            }

            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.RECIPE_SCHEDULE;
            }
        };
    }
    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getActive()  && this.level.getGameTime() % 5 == 0){
            ParticleUtils.spawnFusionCoreParticles(level,pos,level.random);
        }
    }
    public static void addsynthesis(ItemStack container, ItemStack fill, int number, ItemStack output, int rad) {
        CompoundTag nbt = ModUtils.nbt();
        nbt.putInt("percent", number);
        nbt.putInt("rad_amount", rad);

        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe("synthesis", new BaseMachineRecipe(
                new Input(
                        input.getInput(container),
                        input.getInput(fill)
                ),
                new RecipeOutput(nbt, output)
        ));
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            if (this.input_slot.isEmpty()) {
                (this).inputSlotA.changeAccepts(ItemStack.EMPTY);
            } else {
                (this).inputSlotA.changeAccepts(this.input_slot.get(0));
            }
        }
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine1.synthesis;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine.getBlock(getTeBlock().getId());
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.synthesys.getSoundEvent();
    }

    public void init() {
        addsynthesis(
                new ItemStack(IUItem.radiationresources.getItemFromMeta(2), 1),
                new ItemStack(IUItem.crafting_elements.getStack(769)),
                32,
                new ItemStack(IUItem.radiationresources.getItemFromMeta(3), 1), 200
        );
        addsynthesis(
                new ItemStack(IUItem.radiationresources.getItemFromMeta(3), 1),
                new ItemStack(IUItem.crafting_elements.getStack(769)),
                27,
                new ItemStack(IUItem.radiationresources.getItemFromMeta(6), 1), 400
        );
        addsynthesis(
                new ItemStack(IUItem.radiationresources.getItemFromMeta(6), 1),
                new ItemStack(IUItem.crafting_elements.getStack(769)),
                22,
                new ItemStack(IUItem.radiationresources.getItemFromMeta(7), 1), 500
        );
        addsynthesis(
                new ItemStack(IUItem.radiationresources.getItemFromMeta(7), 1),
                new ItemStack(IUItem.crafting_elements.getStack(769)),
                19,
                new ItemStack(IUItem.radiationresources.getItemFromMeta(11), 1), 750
        );

        addsynthesis(IUItem.uraniumBlock, new ItemStack(IUItem.toriy.getItem()), 22, new ItemStack(IUItem.radiationresources.getItemFromMeta(8), 1), 150);
        addsynthesis(new ItemStack(IUItem.radiationresources.getItemFromMeta(1), 1), new ItemStack(IUItem.toriy.getItem()), 20, new ItemStack(IUItem.Plutonium), 100);

    }


    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player entityPlayer, ContainerBase<? extends IAdvInventory> isAdmin) {
        return new GuiSynthesis((ContainerDoubleElectricMachine) isAdmin);
    }


}
