package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.*;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.*;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerEnchanterBooks;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiEnchanterBooks;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.Keyboard;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityEnchanterBooks extends TileElectricMachine implements IUpgradableBlock, IUpdateTick,
        IHasRecipe, IUpdatableTileEvent {

    public final InvSlotUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final InvSlotRecipes inputSlotA;
    public final ComponentProcess componentProcess;
    public final ComponentBaseEnergy enchant;
    public MachineRecipe output;

    public TileEntityEnchanterBooks(BlockPos pos, BlockState state) {
        super(400, 1, 1,BlockBaseMachine3.enchanter_books,pos,state);
        Recipes.recipes.addInitRecipes(this);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 400
        ));
        this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.addComponent(new AirPollutionComponent(this, 0.1));
        this.inputSlotA = new InvSlotRecipes(this, "enchanter_books", this);
        this.componentProcess = this.addComponent(new ComponentProcess(this, 400, 1));
        this.componentProcess.setHasAudio(false);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);
        this.componentProcess.setExpSource();
        this.enchant = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.EXPERIENCE, this, 2000));
    }

    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.componentProcess.getEnergyConsume() + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.componentProcess.getOperationsPerTick());
        }
        super.addInformation(stack, tooltip);

    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.enchanter_books;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public ContainerEnchanterBooks getGuiContainer(final Player var1) {
        return new ContainerEnchanterBooks(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiEnchanterBooks((ContainerEnchanterBooks) menu);
    }

    @Override
    public void init() {
        addRecipe(new ItemStack(Blocks.LAPIS_BLOCK, 1), Enchantments.BLOCK_EFFICIENCY, 1, 200);
        addRecipe1(new ItemStack(Blocks.LAPIS_BLOCK, 3), Enchantments.BLOCK_EFFICIENCY, 2, 400);
        addRecipe1(new ItemStack(Blocks.LAPIS_BLOCK, 5), Enchantments.BLOCK_EFFICIENCY, 3, 600);
        addRecipe1(new ItemStack(Blocks.LAPIS_BLOCK, 8), Enchantments.BLOCK_EFFICIENCY, 4, 700);
        addRecipe1(new ItemStack(Blocks.LAPIS_BLOCK, 12), Enchantments.BLOCK_EFFICIENCY, 5, 800);
        addRecipe(new ItemStack(Blocks.REDSTONE_BLOCK, 2), Enchantments.BLOCK_FORTUNE, 1, 300);
        addRecipe1(new ItemStack(Blocks.REDSTONE_BLOCK, 4), Enchantments.BLOCK_FORTUNE, 2, 800);
        addRecipe1(new ItemStack(Blocks.REDSTONE_BLOCK, 8), Enchantments.BLOCK_FORTUNE, 3, 1200);
        addRecipe(new ItemStack(Blocks.OBSIDIAN, 12), Enchantments.UNBREAKING, 1, 300);
        addRecipe1(new ItemStack(Blocks.OBSIDIAN, 20), Enchantments.UNBREAKING, 2, 800);
        addRecipe1(new ItemStack(Blocks.OBSIDIAN, 32), Enchantments.UNBREAKING, 3, 1200);
        addRecipe(new ItemStack(Blocks.DIAMOND_BLOCK, 2), Enchantments.MOB_LOOTING, 1, 300);
        addRecipe1(new ItemStack(Blocks.DIAMOND_BLOCK, 4), Enchantments.MOB_LOOTING, 2, 800);
        addRecipe1(new ItemStack(Blocks.DIAMOND_BLOCK, 8), Enchantments.MOB_LOOTING, 3, 1200);
        addRecipe(new ItemStack(Blocks.EMERALD_BLOCK, 10), Enchantments.SILK_TOUCH, 1, 600);
        addRecipe(new ItemStack(IUItem.crafting_elements.getStack(282), 4), Enchantments.ALL_DAMAGE_PROTECTION, 1, 300);
        addRecipe1(new ItemStack(IUItem.crafting_elements.getStack(282), 16), Enchantments.ALL_DAMAGE_PROTECTION, 2, 500);
        addRecipe1(new ItemStack(IUItem.crafting_elements.getStack(282), 32), Enchantments.ALL_DAMAGE_PROTECTION, 3, 900);
        addRecipe1(new ItemStack(IUItem.crafting_elements.getStack(282), 64), Enchantments.ALL_DAMAGE_PROTECTION, 4, 1200);
        addRecipe(new ItemStack(Blocks.ICE, 4), Enchantments.FROST_WALKER, 1, 300);
        addRecipe1(new ItemStack(Blocks.ICE, 16), Enchantments.FROST_WALKER, 2, 500);
        addRecipe(new ItemStack(Blocks.SLIME_BLOCK, 4), Enchantments.THORNS, 1, 500);
        addRecipe1(new ItemStack(Blocks.SLIME_BLOCK, 10), Enchantments.THORNS, 2, 900);
        addRecipe1(new ItemStack(Blocks.SLIME_BLOCK, 24), Enchantments.THORNS, 3, 1400);
        addRecipe(new ItemStack(Items.GOLDEN_CARROT, 16), Enchantments.AQUA_AFFINITY, 1, 500);
        addRecipe(new ItemStack(Items.PUFFERFISH), Enchantments.RESPIRATION, 1, 200);
        addRecipe1(new ItemStack(Items.PUFFERFISH), Enchantments.RESPIRATION, 2, 400);
        addRecipe1(new ItemStack(Items.PUFFERFISH), Enchantments.RESPIRATION, 3, 600);
        addRecipe(new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 1), Enchantments.FIRE_PROTECTION, 1, 200);
        addRecipe1(new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 2), Enchantments.FIRE_PROTECTION, 2, 400);
        addRecipe1(new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 4), Enchantments.FIRE_PROTECTION, 3, 800);
        addRecipe1(new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 8), Enchantments.FIRE_PROTECTION, 4, 1200);
        addRecipe(new ItemStack(Items.GHAST_TEAR, 1), Enchantments.SHARPNESS, 1, 200);
        addRecipe1(new ItemStack(Items.GHAST_TEAR, 2), Enchantments.SHARPNESS, 2, 400);
        addRecipe1(new ItemStack(Items.GHAST_TEAR, 4), Enchantments.SHARPNESS, 3, 800);
        addRecipe1(new ItemStack(Items.GHAST_TEAR, 8), Enchantments.SHARPNESS, 4, 1200);
        addRecipe1(new ItemStack(Items.GHAST_TEAR, 16), Enchantments.SHARPNESS, 5, 1500);
        addRecipe(new ItemStack(Items.BLAZE_ROD, 8), Enchantments.BLAST_PROTECTION, 1, 200);
        addRecipe1(new ItemStack(Items.BLAZE_ROD, 16), Enchantments.BLAST_PROTECTION, 2, 400);
        addRecipe1(new ItemStack(Items.BLAZE_ROD, 32), Enchantments.BLAST_PROTECTION, 3, 800);
        addRecipe1(new ItemStack(Items.BLAZE_ROD, 64), Enchantments.BLAST_PROTECTION, 4, 1000);
        addRecipe(new ItemStack(Items.SHULKER_SHELL, 2), Enchantments.FALL_PROTECTION, 1, 200);
        addRecipe1(new ItemStack(Items.SHULKER_SHELL, 4), Enchantments.FALL_PROTECTION, 2, 400);
        addRecipe1(new ItemStack(Items.SHULKER_SHELL, 8), Enchantments.FALL_PROTECTION, 3, 800);
        addRecipe1(new ItemStack(Items.SHULKER_SHELL, 16), Enchantments.FALL_PROTECTION, 4, 1000);
        addRecipe(new ItemStack(Items.MAGMA_CREAM, 16), Enchantments.FIRE_ASPECT, 1, 400);
        addRecipe1(new ItemStack(Items.MAGMA_CREAM, 48), Enchantments.FIRE_ASPECT, 2, 700);
        addRecipe(new ItemStack(Items.NETHER_WART, 16), Enchantments.FIRE_PROTECTION, 1, 400);
        addRecipe1(new ItemStack(Items.NETHER_WART, 32), Enchantments.FIRE_PROTECTION, 2, 700);
        addRecipe1(new ItemStack(Items.NETHER_WART, 64), Enchantments.FIRE_PROTECTION, 3, 1000);
        addRecipe(new ItemStack(Blocks.MAGMA_BLOCK, 16), Enchantments.FLAMING_ARROWS, 1, 600);
        addRecipe(new ItemStack(Items.ENDER_PEARL, 8), Enchantments.KNOCKBACK, 1, 400);
        addRecipe1(new ItemStack(Items.ENDER_PEARL, 16), Enchantments.KNOCKBACK, 2, 800);
        addRecipe(new ItemStack(Items.NETHER_STAR), Enchantments.INFINITY_ARROWS, 1, 400);
        addRecipe(new ItemStack(Blocks.QUARTZ_BLOCK, 2), Enchantments.POWER_ARROWS, 1, 200);
        addRecipe1(new ItemStack(Blocks.QUARTZ_BLOCK, 4), Enchantments.POWER_ARROWS, 2, 400);
        addRecipe1(new ItemStack(Blocks.QUARTZ_BLOCK, 8), Enchantments.POWER_ARROWS, 3, 600);
        addRecipe1(new ItemStack(Blocks.QUARTZ_BLOCK, 16), Enchantments.POWER_ARROWS, 4, 800);
        addRecipe1(new ItemStack(Blocks.QUARTZ_BLOCK, 32), Enchantments.POWER_ARROWS, 5, 1200);
    }

    @Override
    public void onUpdate() {

    }

    public void addRecipe(ItemStack stack, Enchantment enchantments, int lvl, int exp) {
        final IInputHandler input = Recipes.inputFactory;
        final CompoundTag nbt = new CompoundTag();
        nbt.putInt("exp", exp);
        ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(Collections.singletonMap(enchantments, lvl), enchantedBook);
        Recipes.recipes.addRecipe(
                "enchanter_books",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(Items.BOOK), input.getInput(stack)),
                        new RecipeOutput(nbt, enchantedBook))

        );

    }

    public void addRecipe1(ItemStack stack, Enchantment enchantments, int lvl, int exp) {
        final IInputHandler input = Recipes.inputFactory;
        final CompoundTag nbt = new CompoundTag();
        nbt.putInt("exp", exp);
        ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(Collections.singletonMap(enchantments, lvl-1), enchantedBook);
        ItemStack enchantedBook1 = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(Collections.singletonMap(enchantments, lvl), enchantedBook1);
        Recipes.recipes.addRecipe(
                "enchanter_books",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(enchantedBook),
                                input.getInput(stack)
                        ),
                        new RecipeOutput(nbt, enchantedBook1)
                )
        );

    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
    }

    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        return this.output;
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Processing,
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemExtract,
                UpgradableProperty.ItemInput
        );
    }

    public void onLoaded() {
        super.onLoaded();
        if (!level.isClientSide) {

            inputSlotA.load();
            this.getOutput();


        }


    }

}
