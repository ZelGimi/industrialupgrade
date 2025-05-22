package com.denfop.tiles.mechanism.dual;

import com.denfop.ElectricItem;
import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.api.upgrade.IUpgradeWithBlackList;
import com.denfop.api.upgrade.UpgradeModificator;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.upgrade.event.EventItemBlackListLoad;
import com.denfop.api.upgrade.event.EventItemLoad;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockUpgradeBlock;
import com.denfop.componets.ComponentProcess;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.gui.GuiUpgradeBlock;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.modules.ItemQuarryModule;
import com.denfop.items.modules.ItemUpgradeModule;
import com.denfop.tiles.base.EnumDoubleElectricMachine;
import com.denfop.tiles.base.TileDoubleElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.denfop.events.IUEventHandler.getUpgradeItem;

public class TileUpgradeBlock extends TileDoubleElectricMachine implements IHasRecipe {

    public TileUpgradeBlock() {
        super(1, 300, 1, EnumDoubleElectricMachine.UPGRADE, false);
        Recipes.recipes.addInitRecipes(this);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot) {
            @Override
            public void onLoaded() {
                super.onLoaded();
                this.componentProcess = ((TileUpgradeBlock) this.getParent()).componentProcess;
            }
        });
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 300
        ));
        this.componentProcess = this.addComponent(new ComponentProcess(this, 300, 1) {

            @Override
            public void operateWithMax(final MachineRecipe output, final int size) {
                this.operateWithMax(output);
            }

            @Override
            public void operateWithMax(final MachineRecipe output) {
                final List<ItemStack> processResult = this.updateTick.getRecipeOutput().getRecipe().output.items;
                if (this.updateTick.getRecipeOutput().getRecipe().output.metadata.getString("type").isEmpty()) {

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
                    if (module.getItem() instanceof ItemUpgradeModule) {
                        if (UpgradeSystem.system.getRemaining(stack1) == 0) {
                            this.updateTick.setRecipeOutput(null);
                            return;
                        }
                        EnumInfoUpgradeModules type = ItemUpgradeModule.getType(module.getItemDamage());
                        boolean should = UpgradeSystem.system.shouldUpdate(type, stack1);
                        if (!should) {
                            this.updateTick.setRecipeOutput(null);
                            return;
                        }
                        int Damage = stack1.getItemDamage();
                        double newCharge = ElectricItem.manager.getCharge(stack1);
                        final Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(stack1);
                        this.invSlotRecipes.consume();
                        this.outputSlot.add(processResult);
                        ItemStack stack = this.outputSlot.get();
                        stack.setTagCompound(nbt1);
                        NBTTagCompound nbt = ModUtils.nbt(stack);
                        final List<UpgradeModificator> list = UpgradeSystem.system.getListModifications(stack);
                        NBTTagList modesTagList = nbt.getTagList("modes", 10);
                        NBTTagCompound upgrade = new NBTTagCompound();
                        upgrade.setInteger("index", module.getItemDamage());
                        modesTagList.appendTag(upgrade);
                        nbt.setTag("modes", modesTagList);
                        stack.setItemDamage(Damage);
                        ElectricItem.manager.charge(stack, 1, Integer.MAX_VALUE, true, false);
                        ElectricItem.manager.use(stack, 1, null);
                        EnchantmentHelper.setEnchantments(enchantmentMap, stack);
                        MinecraftForge.EVENT_BUS.post(new EventItemLoad(world, (IUpgradeItem) stack.getItem(), stack));
                    } else if (module.getItem() instanceof ItemQuarryModule && module.getItemDamage() == 12) {
                        int Damage = stack1.getItemDamage();
                        NBTTagCompound nbt2 = ModUtils.nbt(module);
                        double newCharge = ElectricItem.manager.getCharge(stack1);
                        final Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(stack1);
                        this.invSlotRecipes.consume();
                        this.outputSlot.add(processResult);
                        ItemStack stack = this.outputSlot.get();
                        stack.setTagCompound(nbt1);
                        NBTTagCompound nbt = ModUtils.nbt(stack);
                        NBTTagList tagList = nbt.getTagList("blacklist", 8);
                        int size = nbt2.getInteger("size");
                        for (int j = 0; j < size; j++) {
                            String l = "number_" + j;
                            String temp = nbt2.getString(l);
                            NBTTagString nbtTagString = new NBTTagString(temp);
                            tagList.appendTag(nbtTagString);
                        }
                        nbt.setTag("blacklist", tagList);
                        stack.setItemDamage(Damage);
                        ElectricItem.manager.charge(stack, 1, Integer.MAX_VALUE, true, false);
                        ElectricItem.manager.use(stack, 1, null);
                        EnchantmentHelper.setEnchantments(enchantmentMap, stack);

                        MinecraftForge.EVENT_BUS.post(new EventItemBlackListLoad(
                                world,
                                (IUpgradeWithBlackList) stack.getItem(),
                                stack,
                                nbt2
                        ));

                    }
                } else {
                    ItemStack stack1 = getUpgradeItem(this.invSlotRecipes.get(0))
                            ? this.invSlotRecipes.get(0)
                            : this.invSlotRecipes.get(1);
                    ItemStack module = getUpgradeItem(this.invSlotRecipes.get(0))
                            ? this.invSlotRecipes.get(1).copy()
                            : this.invSlotRecipes.get(0).copy();
                    boolean need = UpgradeSystem.system.needModificate(stack1, module);
                    if (need) {
                        NBTTagCompound nbt1 = ModUtils.nbt(stack1);
                        int Damage = stack1.getItemDamage();
                        double newCharge = ElectricItem.manager.getCharge(stack1);
                        final Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(stack1);

                        this.invSlotRecipes.consume();
                        this.outputSlot.add(processResult);
                        ItemStack stack = this.outputSlot.get();
                        stack.setTagCompound(nbt1);
                        UpgradeSystem.system.addModificate(
                                stack,
                                this.updateTick.getRecipeOutput().getRecipe().output.metadata.getString("type")
                        );
                        stack.setItemDamage(Damage);
                        ElectricItem.manager.charge(stack, 1, Integer.MAX_VALUE, true, false);
                        ElectricItem.manager.use(stack, 1, null);
                        EnchantmentHelper.setEnchantments(enchantmentMap, stack);

                        MinecraftForge.EVENT_BUS.post(new EventItemLoad(world, (IUpgradeItem) stack.getItem(), stack));

                    }
                }
            }


        });
        this.componentProcess.setHasAudio(true);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);
    }

    public void init() {

    }

    public IMultiTileBlock getTeBlock() {
        return BlockUpgradeBlock.upgrade_block;
    }

    public BlockTileEntity getBlock() {
        return IUItem.upgradeblock;
    }

    @Override
    public ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.upgradeblock);
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.upgrade_block.getSoundEvent();
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    public boolean isNormalCube() {
        return false;
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    public boolean isSideSolid(EnumFacing side) {
        return false;
    }

    public boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiUpgradeBlock(new ContainerDoubleElectricMachine(entityPlayer, this, type));
    }

    @Override
    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        if (this.output == null) {
            return null;
        }

        if (this.output.getRecipe().output.metadata.getString("type").isEmpty()) {
            ItemStack stack1 = getUpgradeItem(this.inputSlotA.get(0)) ? this.inputSlotA.get(0) : this.inputSlotA.get(1);
            ItemStack module = getUpgradeItem(this.inputSlotA.get(0)) ? this.inputSlotA.get(1) : this.inputSlotA.get(0);
            if (module.getItem() instanceof ItemUpgradeModule) {
                if (UpgradeSystem.system.getRemaining(stack1) == 0) {
                    this.output = null;
                    this.energy.addEnergy(this.componentProcess.getDefaultEnergyConsume() * this.componentProcess.getDefaultOperationLength());
                    return null;
                }
                EnumInfoUpgradeModules type = ItemUpgradeModule.getType(module.getItemDamage());
                boolean should = UpgradeSystem.system.shouldUpdate(type, stack1);
                if (!should) {
                    this.energy.addEnergy(this.componentProcess.getDefaultEnergyConsume() * this.componentProcess.getDefaultOperationLength());
                    this.output = null;
                    return null;
                }
            }
        } else {
            ItemStack stack1 = getUpgradeItem(this.inputSlotA.get(0)) ? this.inputSlotA.get(0) : this.inputSlotA.get(1);
            ItemStack module = getUpgradeItem(this.inputSlotA.get(0)) ? this.inputSlotA.get(1) : this.inputSlotA.get(0);
            boolean need = UpgradeSystem.system.needModificate(stack1, module);
            if (need) {
                return output;
            } else {
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
