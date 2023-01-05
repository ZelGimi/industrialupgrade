package com.denfop.tiles.mechanism.dual;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.api.upgrade.IUpgradeWithBlackList;
import com.denfop.api.upgrade.UpgradeModificator;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.upgrade.event.EventItemBlackListLoad;
import com.denfop.api.upgrade.event.EventItemLoad;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.gui.GuiUpgradeBlock;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.modules.ItemQuarryModule;
import com.denfop.items.modules.ItemUpgradeModule;
import com.denfop.tiles.base.EnumDoubleElectricMachine;
import com.denfop.tiles.base.TileEntityDoubleElectricMachine;
import com.denfop.utils.ModUtils;
import ic2.api.item.ElectricItem;
import ic2.api.upgrade.UpgradableProperty;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
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

public class TileEntityUpgradeBlock extends TileEntityDoubleElectricMachine implements IHasRecipe {

    public TileEntityUpgradeBlock() {
        super(1, 300, 1, EnumDoubleElectricMachine.UPGRADE);
        Recipes.recipes.addInitRecipes(this);
    }

    public void init() {

    }


    @Override
    protected ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.upgradeblock);
    }


    @SideOnly(Side.CLIENT)
    protected boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    protected boolean isNormalCube() {
        return false;
    }

    protected boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    protected boolean isSideSolid(EnumFacing side) {
        return false;
    }

    protected boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    @Override
    public void onNetworkUpdate(String field) {

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
                    this.energy.addEnergy(energyConsume * operationLength);
                    return null;
                }
                EnumInfoUpgradeModules type = ItemUpgradeModule.getType(module.getItemDamage());
                boolean should = UpgradeSystem.system.shouldUpdate(type, stack1);
                if (!should) {
                    this.energy.addEnergy(energyConsume * operationLength);
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

    public void operateOnce(MachineRecipe output, List<ItemStack> processResult) {
        if (this.output.getRecipe().output.metadata.getString("type").isEmpty()) {

            ItemStack stack1 = getUpgradeItem(this.inputSlotA.get(0)) ? this.inputSlotA.get(0) : this.inputSlotA.get(1);
            ItemStack module = getUpgradeItem(this.inputSlotA.get(0)) ? this.inputSlotA.get(1) : this.inputSlotA.get(0);


            NBTTagCompound nbt1 = ModUtils.nbt(stack1);
            if (module.getItem() instanceof ItemUpgradeModule) {
                if (UpgradeSystem.system.getRemaining(stack1) == 0) {
                    this.output = null;
                    return;
                }
                EnumInfoUpgradeModules type = ItemUpgradeModule.getType(module.getItemDamage());
                boolean should = UpgradeSystem.system.shouldUpdate(type, stack1);
                if (!should) {
                    this.output = null;
                    return;
                }
                int Damage = stack1.getItemDamage();
                double newCharge = ElectricItem.manager.getCharge(stack1);
                final Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(stack1);
                this.inputSlotA.consume();
                this.outputSlot.add(processResult);
                ItemStack stack = this.outputSlot.get();
                stack.setTagCompound(nbt1);
                NBTTagCompound nbt = ModUtils.nbt(stack);
                String mode = output.getRecipe().output.metadata.getString("mode_module");
                final List<UpgradeModificator> list = UpgradeSystem.system.getListModifications(stack);
                int k = 0;
                for (int i = 0; i < 4 + list.size(); i++) {
                    if (nbt.getString("mode_module" + i).isEmpty()) {
                        k = i;
                        break;
                    }
                }
                nbt.setString("mode_module" + k, mode);
                stack.setItemDamage(Damage);
                ElectricItem.manager.charge(stack, 1, Integer.MAX_VALUE, true, false);
                ElectricItem.manager.use(stack, 1, null);

                EnchantmentHelper.setEnchantments(enchantmentMap, stack);
                MinecraftForge.EVENT_BUS.post(new EventItemLoad(world, (IUpgradeItem) stack.getItem(), stack));


            }

            if (module.getItem() instanceof ItemQuarryModule && module.getItemDamage() == 12) {
                int Damage = stack1.getItemDamage();
                NBTTagCompound nbt2 = ModUtils.nbt(module);
                double newCharge = ElectricItem.manager.getCharge(stack1);
                final Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(stack1);

                this.inputSlotA.consume();
                this.outputSlot.add(processResult);
                ItemStack stack = this.outputSlot.get();
                stack.setTagCompound(nbt1);
                NBTTagCompound nbt = ModUtils.nbt(stack);
                int size = nbt2.getInteger("size");
                for (int j = 0; j < size; j++) {
                    String l = "number_" + j;
                    String temp = nbt2.getString(l);
                    nbt.setString(l, temp);
                }
                nbt.setBoolean("list", true);
                nbt.setInteger("size", size);
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
            ItemStack stack1 = getUpgradeItem(this.inputSlotA.get(0)) ? this.inputSlotA.get(0) : this.inputSlotA.get(1);
            ItemStack module = getUpgradeItem(this.inputSlotA.get(0))
                    ? this.inputSlotA.get(1).copy()
                    : this.inputSlotA.get(0).copy();
            boolean need = UpgradeSystem.system.needModificate(stack1, module);
            if (need) {
                NBTTagCompound nbt1 = ModUtils.nbt(stack1);
                int Damage = stack1.getItemDamage();
                double newCharge = ElectricItem.manager.getCharge(stack1);
                final Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(stack1);

                this.inputSlotA.consume();
                this.outputSlot.add(processResult);
                ItemStack stack = this.outputSlot.get();
                stack.setTagCompound(nbt1);
                UpgradeSystem.system.addModificate(stack, this.output.getRecipe().output.metadata.getString("type"));
                stack.setItemDamage(Damage);
                ElectricItem.manager.charge(stack, 1, Integer.MAX_VALUE, true, false);
                ElectricItem.manager.use(stack, 1, null);
                EnchantmentHelper.setEnchantments(enchantmentMap, stack);

                MinecraftForge.EVENT_BUS.post(new EventItemLoad(world, (IUpgradeItem) stack.getItem(), stack));

            }
        }
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
                UpgradableProperty.EnergyStorage, UpgradableProperty.ItemConsuming, UpgradableProperty.ItemProducing
        );
    }

}
