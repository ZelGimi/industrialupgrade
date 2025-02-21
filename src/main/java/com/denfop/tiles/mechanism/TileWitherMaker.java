package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.container.ContainerBaseWitherMaker;
import com.denfop.gui.GuiWitherMaker;
import com.denfop.network.packet.PacketStopSound;
import com.denfop.recipe.IInputHandler;
import com.denfop.recipe.IInputItemStack;
import com.denfop.tiles.base.TileBaseWitherMaker;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.Set;

public class TileWitherMaker extends TileBaseWitherMaker implements IHasRecipe {

    public static SoundEvent[] soundEvents = new SoundEvent[]{EnumSound.WitherIdle1.getSoundEvent(), EnumSound.WitherHurt3.getSoundEvent()};

    public TileWitherMaker() {
        super(1, 1500, 1);
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
    public void initiate(final int soundEvent) {
        if (soundEvent == 3) {
            this.getWorld().playSound(
                    null,
                    this.pos,
                    soundEvents[this.world.rand.nextInt(soundEvents.length)],
                    SoundCategory.BLOCKS,
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
                this.getWorld().playSound(null, this.pos, getSound(), SoundCategory.BLOCKS, 1F, 1);
            } else if (soundEvent == 1) {
                new PacketStopSound(getWorld(), this.pos);
                this.getWorld().playSound(null, this.pos, EnumSound.InterruptOne.getSoundEvent(), SoundCategory.BLOCKS, 1F, 1);
            } else {
                new PacketStopSound(getWorld(), this.pos);
                this.getWorld().playSound(null, this.pos, EnumSound.WitherDeath1.getSoundEvent(), SoundCategory.BLOCKS, 1F, 1);

            }
        }
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine1.gen_wither;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine;
    }

    public void init() {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        GenerationMicrochip(
                input.getInput(new ItemStack(Items.SKULL, 1, 1), 1),
                input.getInput(new ItemStack(Blocks.SOUL_SAND), 1),
                new ItemStack(Items.NETHER_STAR, 1)
        );

    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiWitherMaker(new ContainerBaseWitherMaker(entityPlayer, this));
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
