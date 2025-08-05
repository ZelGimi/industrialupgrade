package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.*;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerBaseWitherMaker;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiWitherMaker;
import com.denfop.network.packet.PacketStopSound;
import com.denfop.recipe.IInputHandler;
import com.denfop.recipe.IInputItemStack;
import com.denfop.tiles.base.TileBaseWitherMaker;
import com.denfop.utils.ParticleUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.Set;

public class TileWitherMaker extends TileBaseWitherMaker implements IHasRecipe {

    public static SoundEvent[] soundEvents = new SoundEvent[]{EnumSound.WitherIdle1.getSoundEvent(), EnumSound.WitherHurt3.getSoundEvent()};

    public TileWitherMaker(BlockPos pos, BlockState state) {
        super(1, 1500, 1, BlockBaseMachine1.gen_wither, pos, state);
        this.inputSlotA = new InvSlotRecipes(this, "wither", this);
        Recipes.recipes.addInitRecipes(this);
        this.componentProcess.setInvSlotRecipes(inputSlotA);
    }

    public static void GenerationMicrochip(
            IInputItemStack container,
            IInputItemStack fill2, ItemStack output
    ) {
        Recipes.recipes.addRecipe("wither", new BaseMachineRecipe(
                new Input(container, container, container, fill2, fill2, fill2, fill2),
                new RecipeOutput(
                        null,
                        output
                )
        ));

    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getActive()  && this.level.getGameTime() % 5 == 0){
            ParticleUtils.spawnWitherFabricatorParticles(level,pos,level.random);
        }
    }

    @Override
    public void initiate(final int soundEvent) {
        if (soundEvent == 3) {
            setType(valuesAudio[soundEvent % valuesAudio.length]);
            if (getSound() == null) {
                return;
            }
            if (!getEnable()) {
                return;
            }
            this.getWorld().playSound(
                    null,
                    this.getBlockPos(),
                    soundEvents[this.getWorld().random.nextInt(soundEvents.length)],
                    SoundSource.BLOCKS,
                    1F,
                    1
            );

        } else {


            setType(valuesAudio[soundEvent % valuesAudio.length]);
            if (getSound() == null) {
                return;
            }
            if (!getEnable()) {
                return;
            }
            if (soundEvent == 0) {
                this.getWorld().playSound(null, this.getBlockPos(), getSound(), SoundSource.BLOCKS, 1F, 1);
            } else if (soundEvent == 1) {
                new PacketStopSound(getWorld(), this.getBlockPos());
                this.getWorld().playSound(null, this.getBlockPos(), EnumSound.InterruptOne.getSoundEvent(), SoundSource.BLOCKS, 1F, 1);
            } else {
                new PacketStopSound(getWorld(), this.getBlockPos());
                this.getWorld().playSound(null, this.getBlockPos(), EnumSound.WitherDeath1.getSoundEvent(), SoundSource.BLOCKS, 1F, 1);

            }
        }
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine1.gen_wither;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine.getBlock(getTeBlock().getId());
    }

    public void init() {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        GenerationMicrochip(
                input.getInput(new ItemStack(Items.WITHER_SKELETON_SKULL), 1),
                input.getInput(new ItemStack(Blocks.SOUL_SAND), 1),
                new ItemStack(Items.NETHER_STAR, 1)
        );

    }


    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player entityPlayer, ContainerBase<? extends IAdvInventory> isAdmin) {
        return new GuiWitherMaker((ContainerBaseWitherMaker) isAdmin);
    }

    public ContainerBaseWitherMaker getGuiContainer(Player entityPlayer) {
        return new ContainerBaseWitherMaker(
                entityPlayer, this);
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
