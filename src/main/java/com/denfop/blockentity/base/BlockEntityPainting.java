package com.denfop.blockentity.base;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.item.energy.EnergyItem;
import com.denfop.api.recipe.*;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine2Entity;
import com.denfop.componets.*;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuDoubleElectricMachine;
import com.denfop.recipe.IInputHandler;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenPainting;
import com.denfop.sound.EnumSound;
import com.denfop.utils.ElectricItem;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BlockEntityPainting extends BlockEntityDoubleElectricMachine implements IHasRecipe {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityPainting(BlockPos pos, BlockState state) {
        super(1, 300, 1, EnumDoubleElectricMachine.PAINTING, false, BlockBaseMachine2Entity.painter, pos, state);
        Recipes.recipes.addInitRecipes(this);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot) {
            @Override
            public void onLoaded() {
                super.onLoaded();
                this.componentProcess = ((BlockEntityPainting) this.getParent()).componentProcess;
            }
        });
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 300
        ));
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.05));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
        this.componentProcess = this.addComponent(new ComponentProcess(this, 300, 1) {
            @Override
            public void operateWithMax(final MachineRecipe output) {
                operate(output);

            }

            @Override
            public void operateWithMax(final MachineRecipe output, final int size) {
                operate(output);
            }


            public void operateOnce(List<ItemStack> processResult) {
                ItemStack stack1 = this.invSlotRecipes.get(1).getItem() instanceof EnergyItem
                        ? this.invSlotRecipes.get(1)
                        : this.invSlotRecipes.get(0);
                CompoundTag tNBT = ModUtils.nbt(stack1);
                int damage = stack1.getDamageValue();
                final Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(stack1);
                double newCharge = ElectricItem.manager.getCharge(stack1);
                this.invSlotRecipes.consume();
                this.outputSlot.add(processResult);
                ItemStack stack = this.outputSlot.get(0);
                stack.setTag(tNBT);
                CompoundTag nbt = ModUtils.nbt(stack);
                String mode = this.updateTick.getRecipeOutput().getRecipe().output.metadata.getString("mode");
                nbt.putString("mode", mode);
                ElectricItem.manager.use(stack, newCharge, null);
                ElectricItem.manager.charge(stack, newCharge, Integer.MAX_VALUE, true, false);
                EnchantmentHelper.setEnchantments(enchantmentMap, stack);
                stack.setDamageValue(damage);

            }
        });
        this.componentProcess.setHasAudio(true);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);
    }

    public static void addpainting(ItemStack container) {
        CompoundTag nbt = ModUtils.nbt();
        String name = "Zelen";
        nbt.putString("mode", name);
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;

        Recipes.recipes.addRecipe("painter", new BaseMachineRecipe(
                new Input(
                        input.getInput(container),
                        input.getInput(new ItemStack(IUItem.paints.getItemFromMeta(3), 4))
                ),
                new RecipeOutput(nbt, container)
        ));
        CompoundTag nbt1 = ModUtils.nbt();
        name = "Demon";
        nbt1.putString("mode", name);

        Recipes.recipes.addRecipe("painter", new BaseMachineRecipe(
                new Input(
                        input.getInput(container),
                        input.getInput(new ItemStack(IUItem.paints.getStack(4), 4))
                ),
                new RecipeOutput(nbt1, container)
        ));
        CompoundTag nbt2 = ModUtils.nbt();
        name = "Dark";
        nbt2.putString("mode", name);

        Recipes.recipes.addRecipe("painter", new BaseMachineRecipe(
                new Input(
                        input.getInput(container),
                        input.getInput(new ItemStack(IUItem.paints.getStack(6), 4))
                ),
                new RecipeOutput(nbt2, container)
        ));
        /*    CompoundTag nbt3 = ModUtils.nbt();
        name = "Cold";
        nbt3.putString("mode", name);

        Recipes.recipes.addRecipe("painter", new BaseMachineRecipe(
                new Input(
                        input.getInput(container),
                        input.getInput(new ItemStack(IUItem.paints.getStack(1), 4))
                ),
                new RecipeOutput(nbt3, container)
        ));*/
        CompoundTag nbt4 = ModUtils.nbt();
        name = "Ender";
        nbt4.putString("mode", name);

        Recipes.recipes.addRecipe("painter", new BaseMachineRecipe(
                new Input(
                        input.getInput(container),
                        input.getInput(new ItemStack(IUItem.paints.getStack(7), 4))
                ),
                new RecipeOutput(nbt4, container)
        ));
        CompoundTag nbt5 = ModUtils.nbt();
        name = "";
        nbt5.putString("mode", name);

        Recipes.recipes.addRecipe("painter", new BaseMachineRecipe(
                new Input(
                        input.getInput(container),
                        input.getInput(new ItemStack(IUItem.paints.getStack(0), 4))
                ),
                new RecipeOutput(nbt5, container)
        ));

        CompoundTag nbt6 = ModUtils.nbt();
        name = "Ukraine";
        nbt6.putString("mode", name);

        Recipes.recipes.addRecipe("painter", new BaseMachineRecipe(
                new Input(
                        input.getInput(container),
                        input.getInput(new ItemStack(IUItem.paints.getStack(2), 4))
                ),
                new RecipeOutput(nbt6, container)
        ));

        CompoundTag nbt7 = ModUtils.nbt();
        name = "Fire";
        nbt7.putString("mode", name);

        Recipes.recipes.addRecipe("painter", new BaseMachineRecipe(
                new Input(
                        input.getInput(container),
                        input.getInput(new ItemStack(IUItem.paints.getStack(5), 4))
                ),
                new RecipeOutput(nbt7, container)
        ));

        CompoundTag nbt8 = ModUtils.nbt();
        name = "Taiga";
        nbt8.putString("mode", name);

        Recipes.recipes.addRecipe("painter", new BaseMachineRecipe(
                new Input(
                        input.getInput(container),
                        input.getInput(new ItemStack(IUItem.paints.getStack(8), 4))
                ),
                new RecipeOutput(nbt8, container)
        ));


        CompoundTag nbt9 = ModUtils.nbt();
        name = "Snow";
        nbt9.putString("mode", name);

        Recipes.recipes.addRecipe("painter", new BaseMachineRecipe(
                new Input(
                        input.getInput(container),
                        input.getInput(new ItemStack(IUItem.paints.getStack(9), 4))
                ),
                new RecipeOutput(nbt9, container)
        ));
        CompoundTag nbt10 = ModUtils.nbt();
        name = "Desert";
        nbt10.putString("mode", name);

        Recipes.recipes.addRecipe("painter", new BaseMachineRecipe(
                new Input(
                        input.getInput(container),
                        input.getInput(new ItemStack(IUItem.paints.getStack(10), 4))
                ),
                new RecipeOutput(nbt10, container)
        ));
        CompoundTag nbt11 = ModUtils.nbt();
        name = "Emerald";
        nbt11.putString("mode", name);

        Recipes.recipes.addRecipe("painter", new BaseMachineRecipe(
                new Input(
                        input.getInput(container),
                        input.getInput(new ItemStack(IUItem.paints.getStack(11), 4))
                ),
                new RecipeOutput(nbt11, container)
        ));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine2Entity.painter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine1.getBlock(this.getTeBlock().getId());
    }

    public void init() {
        addpainting(new ItemStack(IUItem.nanodrill.getItem(), 1));
        addpainting(new ItemStack(IUItem.quantumdrill.getItem(), 1));
        addpainting(new ItemStack(IUItem.spectraldrill.getItem(), 1));

        addpainting(new ItemStack(IUItem.nanoaxe.getItem(), 1));
        addpainting(new ItemStack(IUItem.quantumaxe.getItem(), 1));
        addpainting(new ItemStack(IUItem.spectralaxe.getItem(), 1));

        addpainting(new ItemStack(IUItem.nanopickaxe.getItem(), 1));
        addpainting(new ItemStack(IUItem.diamond_drill.getItem(), 1));
        addpainting(new ItemStack(IUItem.drill.getItem(), 1));
        addpainting(new ItemStack(IUItem.vajra.getItem(), 1));
        addpainting(new ItemStack(IUItem.ult_vajra.getItem(), 1));
        addpainting(new ItemStack(IUItem.nano_bow.getItem(), 1));
        addpainting(new ItemStack(IUItem.quantum_bow.getItem(), 1));
        addpainting(new ItemStack(IUItem.spectral_bow.getItem(), 1));
        addpainting(new ItemStack(IUItem.quantumpickaxe.getItem(), 1));
        addpainting(new ItemStack(IUItem.spectralpickaxe.getItem(), 1));
        addpainting(new ItemStack(IUItem.nanoshovel.getItem(), 1));
        addpainting(new ItemStack(IUItem.quantumshovel.getItem(), 1));
        addpainting(new ItemStack(IUItem.spectralshovel.getItem(), 1));


        //    addpainting(new ItemStack(IUItem.spectral_helmet, 1, OreDictionary.WILDCARD_VALUE));
        //      addpainting(new ItemStack(IUItem.spectral_leggings, 1, OreDictionary.WILDCARD_VALUE));
        //      addpainting(new ItemStack(IUItem.spectral_chestplate, 1, OreDictionary.WILDCARD_VALUE));
        //      addpainting(new ItemStack(IUItem.spectral_boots, 1, OreDictionary.WILDCARD_VALUE));
        //      addpainting(new ItemStack(IUItem.adv_nano_chestplate, 1, OreDictionary.WILDCARD_VALUE));
        //       addpainting(new ItemStack(IUItem.adv_nano_boots, 1, OreDictionary.WILDCARD_VALUE));
        //       addpainting(new ItemStack(IUItem.adv_nano_helmet, 1, OreDictionary.WILDCARD_VALUE));
        //      addpainting(new ItemStack(IUItem.adv_nano_leggings, 1, OreDictionary.WILDCARD_VALUE));
        //      addpainting(new ItemStack(IUItem.nano_chestplate, 1, OreDictionary.WILDCARD_VALUE));
        //     addpainting(new ItemStack(IUItem.nano_boots, 1, OreDictionary.WILDCARD_VALUE));
        //      addpainting(new ItemStack(IUItem.nano_helmet, 1, OreDictionary.WILDCARD_VALUE));
        //      addpainting(new ItemStack(IUItem.nano_leggings, 1, OreDictionary.WILDCARD_VALUE));


    }


    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player entityPlayer, ContainerMenuBase<? extends CustomWorldContainer> isAdmin) {
        return new ScreenPainting((ContainerMenuDoubleElectricMachine) isAdmin);
    }


    public Set<EnumBlockEntityUpgrade> getUpgradableProperties() {
        return EnumSet.of(EnumBlockEntityUpgrade.Processing, EnumBlockEntityUpgrade.Transformer,
                EnumBlockEntityUpgrade.EnergyStorage, EnumBlockEntityUpgrade.ItemExtract, EnumBlockEntityUpgrade.ItemInput
        );
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.painting.getSoundEvent();
    }

    @Override
    public void onNetworkEvent(final int var1) {

    }

}
