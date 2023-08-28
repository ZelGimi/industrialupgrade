package com.denfop.tiles.base;

import com.denfop.ElectricItem;
import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.item.IEnergyItem;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.componets.ComponentProcess;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.gui.GuiPainting;
import com.denfop.recipe.IInputHandler;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TilePainting extends TileDoubleElectricMachine implements IHasRecipe {

    public TilePainting() {
        super(1, 300, 1, EnumDoubleElectricMachine.PAINTING, false);
        Recipes.recipes.addInitRecipes(this);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot) {
            @Override
            public void onLoaded() {
                super.onLoaded();
                this.componentProcess = ((TilePainting) this.getParent()).componentProcess;
            }
        });
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 300
        ));
        this.componentProcess = this.addComponent(new ComponentProcess(this, 300, 1) {
            public void operateOnce(List<ItemStack> processResult) {
                ItemStack stack1 = this.invSlotRecipes.get(1).getItem() instanceof IEnergyItem
                        ? this.invSlotRecipes.get(1)
                        : this.invSlotRecipes.get(0);
                NBTTagCompound tNBT = ModUtils.nbt(stack1);
                int damage = stack1.getItemDamage();
                final Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(stack1);
                double newCharge = ElectricItem.manager.getCharge(stack1);
                this.invSlotRecipes.consume();
                this.outputSlot.add(processResult);
                ItemStack stack = this.outputSlot.get();
                stack.setTagCompound(tNBT);
                NBTTagCompound nbt = ModUtils.nbt(stack);
                String mode = this.updateTick.getRecipeOutput().getRecipe().output.metadata.getString("mode");
                nbt.setString("mode", mode);
                ElectricItem.manager.use(stack, newCharge, null);
                ElectricItem.manager.charge(stack, newCharge, Integer.MAX_VALUE, true, false);
                EnchantmentHelper.setEnchantments(enchantmentMap, stack);
                stack.setItemDamage(damage);

            }
        });
        this.componentProcess.setHasAudio(true);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);
    }

    public static void addpainting(ItemStack container) {
        NBTTagCompound nbt = ModUtils.nbt();
        String name = "Zelen";
        nbt.setString("mode", name);
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;

        Recipes.recipes.addRecipe("painter", new BaseMachineRecipe(
                new Input(
                        input.getInput(container),
                        input.getInput(new ItemStack(IUItem.paints, 4, 3))
                ),
                new RecipeOutput(nbt, container)
        ));
        NBTTagCompound nbt1 = ModUtils.nbt();
        name = "Demon";
        nbt1.setString("mode", name);

        Recipes.recipes.addRecipe("painter", new BaseMachineRecipe(
                new Input(
                        input.getInput(container),
                        input.getInput(new ItemStack(IUItem.paints, 4, 4))
                ),
                new RecipeOutput(nbt1, container)
        ));
        NBTTagCompound nbt2 = ModUtils.nbt();
        name = "Dark";
        nbt2.setString("mode", name);

        Recipes.recipes.addRecipe("painter", new BaseMachineRecipe(
                new Input(
                        input.getInput(container),
                        input.getInput(new ItemStack(IUItem.paints, 4, 6))
                ),
                new RecipeOutput(nbt2, container)
        ));
        NBTTagCompound nbt3 = ModUtils.nbt();
        name = "Cold";
        nbt3.setString("mode", name);

        Recipes.recipes.addRecipe("painter", new BaseMachineRecipe(
                new Input(
                        input.getInput(container),
                        input.getInput(new ItemStack(IUItem.paints, 4, 1))
                ),
                new RecipeOutput(nbt3, container)
        ));
        NBTTagCompound nbt4 = ModUtils.nbt();
        name = "Ender";
        nbt4.setString("mode", name);

        Recipes.recipes.addRecipe("painter", new BaseMachineRecipe(
                new Input(
                        input.getInput(container),
                        input.getInput(new ItemStack(IUItem.paints, 4, 7))
                ),
                new RecipeOutput(nbt4, container)
        ));
        NBTTagCompound nbt5 = ModUtils.nbt();
        name = "";
        nbt5.setString("mode", name);

        Recipes.recipes.addRecipe("painter", new BaseMachineRecipe(
                new Input(
                        input.getInput(container),
                        input.getInput(new ItemStack(IUItem.paints, 4, 0))
                ),
                new RecipeOutput(nbt5, container)
        ));

        NBTTagCompound nbt6 = ModUtils.nbt();
        name = "Ukraine";
        nbt6.setString("mode", name);

        Recipes.recipes.addRecipe("painter", new BaseMachineRecipe(
                new Input(
                        input.getInput(container),
                        input.getInput(new ItemStack(IUItem.paints, 4, 2))
                ),
                new RecipeOutput(nbt6, container)
        ));

        NBTTagCompound nbt7 = ModUtils.nbt();
        name = "Fire";
        nbt7.setString("mode", name);

        Recipes.recipes.addRecipe("painter", new BaseMachineRecipe(
                new Input(
                        input.getInput(container),
                        input.getInput(new ItemStack(IUItem.paints, 4, 5))
                ),
                new RecipeOutput(nbt7, container)
        ));

        NBTTagCompound nbt8 = ModUtils.nbt();
        name = "Taiga";
        nbt8.setString("mode", name);

        Recipes.recipes.addRecipe("painter", new BaseMachineRecipe(
                new Input(
                        input.getInput(container),
                        input.getInput(new ItemStack(IUItem.paints, 4, 8))
                ),
                new RecipeOutput(nbt8, container)
        ));


        NBTTagCompound nbt9 = ModUtils.nbt();
        name = "Snow";
        nbt9.setString("mode", name);

        Recipes.recipes.addRecipe("painter", new BaseMachineRecipe(
                new Input(
                        input.getInput(container),
                        input.getInput(new ItemStack(IUItem.paints, 4, 9))
                ),
                new RecipeOutput(nbt9, container)
        ));
        NBTTagCompound nbt10 = ModUtils.nbt();
        name = "Desert";
        nbt10.setString("mode", name);

        Recipes.recipes.addRecipe("painter", new BaseMachineRecipe(
                new Input(
                        input.getInput(container),
                        input.getInput(new ItemStack(IUItem.paints, 4, 10))
                ),
                new RecipeOutput(nbt10, container)
        ));
        NBTTagCompound nbt11 = ModUtils.nbt();
        name = "Emerald";
        nbt11.setString("mode", name);

        Recipes.recipes.addRecipe("painter", new BaseMachineRecipe(
                new Input(
                        input.getInput(container),
                        input.getInput(new ItemStack(IUItem.paints, 4, 11))
                ),
                new RecipeOutput(nbt11, container)
        ));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine2.painter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine1;
    }

    public void init() {
        addpainting(new ItemStack(IUItem.nanodrill, 1, OreDictionary.WILDCARD_VALUE));
        addpainting(new ItemStack(IUItem.quantumdrill, 1, OreDictionary.WILDCARD_VALUE));
        addpainting(new ItemStack(IUItem.spectraldrill, 1, OreDictionary.WILDCARD_VALUE));

        addpainting(new ItemStack(IUItem.nanoaxe, 1, OreDictionary.WILDCARD_VALUE));
        addpainting(new ItemStack(IUItem.quantumaxe, 1, OreDictionary.WILDCARD_VALUE));
        addpainting(new ItemStack(IUItem.spectralaxe, 1, OreDictionary.WILDCARD_VALUE));
        addpainting(new ItemStack(IUItem.nanopickaxe, 1, OreDictionary.WILDCARD_VALUE));
        addpainting(new ItemStack(IUItem.quantumpickaxe, 1, OreDictionary.WILDCARD_VALUE));
        addpainting(new ItemStack(IUItem.spectralpickaxe, 1, OreDictionary.WILDCARD_VALUE));
        addpainting(new ItemStack(IUItem.nanoshovel, 1, OreDictionary.WILDCARD_VALUE));
        addpainting(new ItemStack(IUItem.quantumshovel, 1, OreDictionary.WILDCARD_VALUE));
        addpainting(new ItemStack(IUItem.spectralshovel, 1, OreDictionary.WILDCARD_VALUE));


        addpainting(new ItemStack(IUItem.spectral_helmet, 1, OreDictionary.WILDCARD_VALUE));
        addpainting(new ItemStack(IUItem.spectral_leggings, 1, OreDictionary.WILDCARD_VALUE));
        addpainting(new ItemStack(IUItem.spectral_chestplate, 1, OreDictionary.WILDCARD_VALUE));
        addpainting(new ItemStack(IUItem.spectral_boots, 1, OreDictionary.WILDCARD_VALUE));
        addpainting(new ItemStack(IUItem.adv_nano_chestplate, 1, OreDictionary.WILDCARD_VALUE));
        addpainting(new ItemStack(IUItem.adv_nano_boots, 1, OreDictionary.WILDCARD_VALUE));
        addpainting(new ItemStack(IUItem.adv_nano_helmet, 1, OreDictionary.WILDCARD_VALUE));
        addpainting(new ItemStack(IUItem.adv_nano_leggings, 1, OreDictionary.WILDCARD_VALUE));

        addpainting(new ItemStack(IUItem.advancedSolarHelmet, 1, OreDictionary.WILDCARD_VALUE));
        addpainting(new ItemStack(IUItem.hybridSolarHelmet, 1, OreDictionary.WILDCARD_VALUE));
        addpainting(new ItemStack(IUItem.ultimateSolarHelmet, 1, OreDictionary.WILDCARD_VALUE));
        addpainting(new ItemStack(IUItem.spectralSolarHelmet, 1, OreDictionary.WILDCARD_VALUE));
        addpainting(new ItemStack(IUItem.singularSolarHelmet, 1, OreDictionary.WILDCARD_VALUE));

    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiPainting(new ContainerDoubleElectricMachine(entityPlayer, this, type));
    }

    public String getStartSoundFile() {
        return "Machines/painting.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.ItemConsuming, UpgradableProperty.ItemProducing
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
