package com.denfop.tiles.mechanism;

import com.denfop.ElectricItem;
import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.space.rovers.api.IRoversItem;
import com.denfop.api.space.rovers.enums.EnumTypeUpgrade;
import com.denfop.api.space.upgrades.SpaceUpgradeSystem;
import com.denfop.api.space.upgrades.event.EventItemLoad;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentProcess;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.gui.GuiRoverUpgradeBlock;
import com.denfop.items.modules.ItemSpaceUpgradeModule;
import com.denfop.tiles.base.EnumDoubleElectricMachine;
import com.denfop.tiles.base.TileDoubleElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TileEntityUpgradeRover extends TileDoubleElectricMachine implements IHasRecipe {

    public TileEntityUpgradeRover() {
        super(1, 300, 1, EnumDoubleElectricMachine.UPGRADE_ROVER, false);
        Recipes.recipes.addInitRecipes(this);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot) {
            @Override
            public void onLoaded() {
                super.onLoaded();
                this.componentProcess = ((TileEntityUpgradeRover) this.getParent()).componentProcess;
            }
        });
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 300
        ));

        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
        this.componentProcess = this.addComponent(new ComponentProcess(this, 300, 1) {

            @Override
            public void operateWithMax(final MachineRecipe output, final int size) {
                this.operateWithMax(output);
            }

            @Override
            public void operateWithMax(final MachineRecipe output) {
                final List<ItemStack> processResult = this.updateTick.getRecipeOutput().getRecipe().output.items;


                ItemStack stack1 = getUpgradeItem(this.invSlotRecipes.get(0))
                        ? this.invSlotRecipes.get(0)
                        : this.invSlotRecipes.get(1);
                ItemStack module = getUpgradeItem(this.invSlotRecipes.get(0))
                        ? this.invSlotRecipes.get(1)
                        : this.invSlotRecipes.get(0);
                if (module.isEmpty()) {
                    return;
                }
                NBTTagCompound nbt1 = ModUtils.nbt(stack1);
                if (module.getItem() instanceof ItemSpaceUpgradeModule) {
                    if (SpaceUpgradeSystem.system.getRemaining(stack1) == 0) {
                        this.updateTick.setRecipeOutput(null);
                        return;
                    }
                    EnumTypeUpgrade type = ItemSpaceUpgradeModule.getType(module.getItemDamage());
                    boolean should = SpaceUpgradeSystem.system.shouldUpdate(type, stack1);
                    if (!should) {
                        this.updateTick.setRecipeOutput(null);
                        return;
                    }
                    int Damage = stack1.getItemDamage();
                    final Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(stack1);
                    this.invSlotRecipes.consume();
                    this.outputSlot.add(processResult);
                    ItemStack stack = this.outputSlot.get();
                    stack.setTagCompound(nbt1);
                    NBTTagCompound nbt = ModUtils.nbt(stack);
                    NBTTagList modesTagList = nbt.getTagList("modes", 10);
                    NBTTagCompound upgrade = new NBTTagCompound();
                    upgrade.setInteger("index", module.getItemDamage());
                    modesTagList.appendTag(upgrade);
                    nbt.setTag("modes", modesTagList);
                    stack.setItemDamage(Damage);
                    ElectricItem.manager.charge(stack, 1, Integer.MAX_VALUE, true, false);
                    ElectricItem.manager.use(stack, 1, null);
                    EnchantmentHelper.setEnchantments(enchantmentMap, stack);
                    MinecraftForge.EVENT_BUS.post(new EventItemLoad(world, (IRoversItem) stack.getItem(), stack));
                }

            }


        });
        this.componentProcess.setHasAudio(true);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);
    }

    public static boolean getUpgradeItem(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof IRoversItem;

    }

    public void init() {

    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.upgrade_rover;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.upgrade_block.getSoundEvent();
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiRoverUpgradeBlock(new ContainerDoubleElectricMachine(entityPlayer, this, type));
    }

    @Override
    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        if (this.output == null) {
            return null;
        }
        ItemStack stack1 = getUpgradeItem(this.inputSlotA.get(0)) ? this.inputSlotA.get(0) : this.inputSlotA.get(1);
        ItemStack module = getUpgradeItem(this.inputSlotA.get(0)) ? this.inputSlotA.get(1) : this.inputSlotA.get(0);
        if (module.getItem() instanceof ItemSpaceUpgradeModule) {
            if (SpaceUpgradeSystem.system.getRemaining(stack1) == 0) {
                this.output = null;
                this.energy.addEnergy(this.componentProcess.getDefaultEnergyConsume() * this.componentProcess.getDefaultOperationLength());
                return null;
            }
            EnumTypeUpgrade type = ItemSpaceUpgradeModule.getType(module.getItemDamage());
            boolean should = SpaceUpgradeSystem.system.shouldUpdate(type, stack1);
            if (!should) {
                this.energy.addEnergy(this.componentProcess.getDefaultEnergyConsume() * this.componentProcess.getDefaultOperationLength());
                this.output = null;
                return null;
            }
        }

        return output;

    }

    public String getStartSoundFile() {
        return "Machines/upgrade_block.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.ItemExtract, UpgradableProperty.ItemInput
        );
    }

}
