package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.ComponentProcess;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerEnchanterBooks;
import com.denfop.gui.GuiEnchanterBooks;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
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

    public TileEntityEnchanterBooks() {
        super(400, 1, 1);
        Recipes.recipes.addInitRecipes(this);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 400
        ));
        this.inputSlotA = new InvSlotRecipes(this, "enchanter_books", this);
        this.componentProcess = this.addComponent(new ComponentProcess(this, 400, 1));
        this.componentProcess.setHasAudio(false);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);
        this.componentProcess.setExpSource();
        this.enchant = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.EXPERIENCE, this, 2000));
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.enchanter_books;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public ContainerEnchanterBooks getGuiContainer(final EntityPlayer var1) {
        return new ContainerEnchanterBooks(this,var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiEnchanterBooks(getGuiContainer(var1));
    }

    @Override
    public void init() {
        addRecipe(new ItemStack(Blocks.LAPIS_BLOCK, 9), Enchantments.EFFICIENCY, 1, 200);
        addRecipe1(new ItemStack(Blocks.LAPIS_BLOCK, 15), Enchantments.EFFICIENCY, 2, 400);
        addRecipe1(new ItemStack(Blocks.LAPIS_BLOCK, 24), Enchantments.EFFICIENCY, 3, 600);
        addRecipe1(new ItemStack(Blocks.LAPIS_BLOCK, 32), Enchantments.EFFICIENCY, 4, 700);
        addRecipe1(new ItemStack(Blocks.LAPIS_BLOCK, 48), Enchantments.EFFICIENCY, 5, 800);
        addRecipe(new ItemStack(Blocks.REDSTONE_BLOCK, 9), Enchantments.FORTUNE, 1, 300);
        addRecipe1(new ItemStack(Blocks.REDSTONE_BLOCK, 24), Enchantments.FORTUNE, 2, 800);
        addRecipe1(new ItemStack(Blocks.REDSTONE_BLOCK, 48), Enchantments.FORTUNE, 3, 1200);
        addRecipe(new ItemStack(Blocks.OBSIDIAN, 12), Enchantments.UNBREAKING, 1, 300);
        addRecipe1(new ItemStack(Blocks.OBSIDIAN, 20), Enchantments.UNBREAKING, 2, 800);
        addRecipe1(new ItemStack(Blocks.OBSIDIAN, 32), Enchantments.UNBREAKING, 3, 1200);
        addRecipe(new ItemStack(Blocks.DIAMOND_BLOCK, 8), Enchantments.LOOTING, 1, 300);
        addRecipe1(new ItemStack(Blocks.DIAMOND_BLOCK, 20), Enchantments.LOOTING, 2, 800);
        addRecipe1(new ItemStack(Blocks.DIAMOND_BLOCK, 30), Enchantments.LOOTING, 3, 1200);
        addRecipe(new ItemStack(Blocks.EMERALD_BLOCK, 20), Enchantments.SILK_TOUCH, 1, 600);
        addRecipe(new ItemStack(IUItem.crafting_elements, 4, 282), Enchantments.PROTECTION, 1, 300);
        addRecipe1(new ItemStack(IUItem.crafting_elements, 16, 282), Enchantments.PROTECTION, 2, 500);
        addRecipe1(new ItemStack(IUItem.crafting_elements, 32, 282), Enchantments.PROTECTION, 3, 900);
        addRecipe1(new ItemStack(IUItem.crafting_elements, 64, 282), Enchantments.PROTECTION, 4, 1200);
        addRecipe(new ItemStack(Blocks.ICE, 4), Enchantments.FROST_WALKER, 1, 300);
        addRecipe1(new ItemStack(Blocks.ICE, 16), Enchantments.FROST_WALKER, 2, 500);
        addRecipe(new ItemStack(Blocks.SLIME_BLOCK, 8), Enchantments.THORNS, 1, 500);
        addRecipe1(new ItemStack(Blocks.SLIME_BLOCK, 20), Enchantments.THORNS, 2, 900);
        addRecipe1(new ItemStack(Blocks.SLIME_BLOCK, 48), Enchantments.THORNS, 3, 1400);
        addRecipe(new ItemStack(Items.GOLDEN_CARROT, 32), Enchantments.AQUA_AFFINITY, 1, 500);
        addRecipe(new ItemStack(Items.FISH, 1, 3), Enchantments.RESPIRATION, 1, 200);
        addRecipe1(new ItemStack(Items.FISH, 1, 3), Enchantments.RESPIRATION, 2, 400);
        addRecipe1(new ItemStack(Items.FISH, 1, 3), Enchantments.RESPIRATION, 3, 600);
        addRecipe(new ItemStack(Items.GOLDEN_APPLE, 1, 1), Enchantments.FIRE_PROTECTION, 1, 200);
        addRecipe1(new ItemStack(Items.GOLDEN_APPLE, 2, 1), Enchantments.FIRE_PROTECTION, 2, 400);
        addRecipe1(new ItemStack(Items.GOLDEN_APPLE, 4, 1), Enchantments.FIRE_PROTECTION, 3, 800);
        addRecipe1(new ItemStack(Items.GOLDEN_APPLE, 8, 1), Enchantments.FIRE_PROTECTION, 4, 1200);
        addRecipe(new ItemStack(Items.GHAST_TEAR, 1), Enchantments.SHARPNESS, 1, 200);
        addRecipe1(new ItemStack(Items.GHAST_TEAR, 2), Enchantments.SHARPNESS, 2, 400);
        addRecipe1(new ItemStack(Items.GHAST_TEAR, 4), Enchantments.SHARPNESS, 3, 800);
        addRecipe1(new ItemStack(Items.GHAST_TEAR, 8), Enchantments.SHARPNESS, 4, 1200);
        addRecipe1(new ItemStack(Items.GHAST_TEAR, 16), Enchantments.SHARPNESS, 5, 1500);
        addRecipe(new ItemStack(Items.BLAZE_ROD, 8), Enchantments.BLAST_PROTECTION, 1, 200);
        addRecipe1(new ItemStack(Items.BLAZE_ROD, 16), Enchantments.BLAST_PROTECTION, 2, 400);
        addRecipe1(new ItemStack(Items.BLAZE_ROD, 32), Enchantments.BLAST_PROTECTION, 3, 800);
        addRecipe1(new ItemStack(Items.BLAZE_ROD, 64), Enchantments.BLAST_PROTECTION, 4, 1000);
        addRecipe(new ItemStack(Items.SHULKER_SHELL, 2), Enchantments.FEATHER_FALLING, 1, 200);
        addRecipe1(new ItemStack(Items.SHULKER_SHELL, 6), Enchantments.FEATHER_FALLING, 2, 400);
        addRecipe1(new ItemStack(Items.SHULKER_SHELL, 12), Enchantments.FEATHER_FALLING, 3, 800);
        addRecipe1(new ItemStack(Items.SHULKER_SHELL, 32), Enchantments.FEATHER_FALLING, 4, 1000);
        addRecipe(new ItemStack(Items.MAGMA_CREAM, 16), Enchantments.FIRE_ASPECT, 1, 400);
        addRecipe1(new ItemStack(Items.MAGMA_CREAM, 48), Enchantments.FIRE_ASPECT, 2, 700);
        addRecipe(new ItemStack(Items.NETHER_WART, 16), Enchantments.FIRE_PROTECTION, 1, 400);
        addRecipe1(new ItemStack(Items.NETHER_WART, 32), Enchantments.FIRE_PROTECTION, 2, 700);
        addRecipe1(new ItemStack(Items.NETHER_WART, 64), Enchantments.FIRE_PROTECTION, 3, 1000);
        addRecipe(new ItemStack(Blocks.MAGMA, 16), Enchantments.FLAME, 1, 600);
        addRecipe(new ItemStack(Items.ENDER_PEARL, 8), Enchantments.KNOCKBACK, 1, 400);
        addRecipe1(new ItemStack(Items.ENDER_PEARL, 16), Enchantments.KNOCKBACK, 2, 800);
        addRecipe(new ItemStack(Items.NETHER_STAR), Enchantments.INFINITY, 1, 400);
        addRecipe(new ItemStack(Blocks.QUARTZ_BLOCK, 4), Enchantments.POWER, 1, 200);
        addRecipe1(new ItemStack(Blocks.QUARTZ_BLOCK, 8), Enchantments.POWER, 2, 400);
        addRecipe1(new ItemStack(Blocks.QUARTZ_BLOCK, 16), Enchantments.POWER, 3, 600);
        addRecipe1(new ItemStack(Blocks.QUARTZ_BLOCK, 32), Enchantments.POWER, 4, 800);
        addRecipe1(new ItemStack(Blocks.QUARTZ_BLOCK, 64), Enchantments.POWER, 5, 1200);
    }

    @Override
    public void onUpdate() {

    }

    public void addRecipe(ItemStack stack, Enchantment enchantments, int lvl, int exp) {
        final IInputHandler input = Recipes.inputFactory;
        final NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("exp", exp);
        Recipes.recipes.addRecipe(
                "enchanter_books",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(Items.BOOK), input.getInput(stack)),
                        new RecipeOutput(nbt, ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(enchantments, lvl)))
                )
        );

    }

    public void addRecipe1(ItemStack stack, Enchantment enchantments, int lvl, int exp) {
        final IInputHandler input = Recipes.inputFactory;
        final NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("exp", exp);
        Recipes.recipes.addRecipe(
                "enchanter_books",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(enchantments,
                                        lvl - 1))),
                                input.getInput(stack)
                        ),
                        new RecipeOutput(nbt, ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(enchantments, lvl)))
                )
        );

    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        return this.output;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Processing,
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemConsuming,
                UpgradableProperty.ItemProducing
        );
    }

    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {

            inputSlotA.load();
            this.getOutput();


        }


    }

}
