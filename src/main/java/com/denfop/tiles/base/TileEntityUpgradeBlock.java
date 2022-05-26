package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.api.upgrade.IUpgradeWithBlackList;
import com.denfop.api.upgrade.UpgradeItemInform;
import com.denfop.api.upgrade.UpgradeModificator;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.upgrade.event.EventItemBlackListLoad;
import com.denfop.api.upgrade.event.EventItemLoad;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.gui.GUIUpgradeBlock;
import com.denfop.items.modules.QuarryModule;
import com.denfop.items.modules.UpgradeModule;
import com.denfop.utils.EnumInfoUpgradeModules;
import com.denfop.utils.ModUtils;
import ic2.api.item.ElectricItem;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.RecipeOutput;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.denfop.events.IUEventHandler.getUpgradeItem;

public class TileEntityUpgradeBlock extends TileEntityDoubleElectricMachine {

    public TileEntityUpgradeBlock() {
        super(1, 300, 1, Localization.translate("blockUpgrade.name"), EnumDoubleElectricMachine.UPGRADE);
    }

    public static void init() {
        addupgrade(IUItem.nanodrill, new ItemStack(IUItem.upgrademodule, 1, 13));
        addupgrade(IUItem.nanodrill, new ItemStack(IUItem.upgrademodule, 1, 3));
        addupgrade(IUItem.nanodrill, new ItemStack(IUItem.upgrademodule, 1, 16));
        addupgrade(IUItem.nanodrill, new ItemStack(IUItem.upgrademodule, 1, 6));
        addupgrade(IUItem.quantumdrill, new ItemStack(IUItem.upgrademodule, 1, 6));
        addupgrade(IUItem.quantumdrill, new ItemStack(IUItem.upgrademodule, 1, 13));
        addupgrade(IUItem.quantumdrill, new ItemStack(IUItem.upgrademodule, 1, 3));
        addupgrade(IUItem.quantumdrill, new ItemStack(IUItem.upgrademodule, 1, 16));
        addupgrade(IUItem.spectraldrill, new ItemStack(IUItem.upgrademodule, 1, 13));
        addupgrade(IUItem.spectraldrill, new ItemStack(IUItem.upgrademodule, 1, 3));
        addupgrade(IUItem.spectraldrill, new ItemStack(IUItem.upgrademodule, 1, 16));
        addupgrade(IUItem.spectraldrill, new ItemStack(IUItem.upgrademodule, 1, 6));
        addupgrade(IUItem.nanoaxe, new ItemStack(IUItem.upgrademodule, 1, 13));
        addupgrade(IUItem.nanoaxe, new ItemStack(IUItem.upgrademodule, 1, 3));
        addupgrade(IUItem.nanoaxe, new ItemStack(IUItem.upgrademodule, 1, 16));
        addupgrade(IUItem.nanoaxe, new ItemStack(IUItem.upgrademodule, 1, 6));
        addupgrade(IUItem.quantumaxe, new ItemStack(IUItem.upgrademodule, 1, 6));
        addupgrade(IUItem.quantumaxe, new ItemStack(IUItem.upgrademodule, 1, 13));
        addupgrade(IUItem.quantumaxe, new ItemStack(IUItem.upgrademodule, 1, 3));
        addupgrade(IUItem.quantumaxe, new ItemStack(IUItem.upgrademodule, 1, 16));
        addupgrade(IUItem.spectralaxe, new ItemStack(IUItem.upgrademodule, 1, 13));
        addupgrade(IUItem.spectralaxe, new ItemStack(IUItem.upgrademodule, 1, 3));
        addupgrade(IUItem.spectralaxe, new ItemStack(IUItem.upgrademodule, 1, 16));
        addupgrade(IUItem.spectralaxe, new ItemStack(IUItem.upgrademodule, 1, 6));
        addupgrade(IUItem.nanopickaxe, new ItemStack(IUItem.upgrademodule, 1, 13));
        addupgrade(IUItem.nanopickaxe, new ItemStack(IUItem.upgrademodule, 1, 3));
        addupgrade(IUItem.nanopickaxe, new ItemStack(IUItem.upgrademodule, 1, 16));
        addupgrade(IUItem.nanopickaxe, new ItemStack(IUItem.upgrademodule, 1, 6));
        addupgrade(IUItem.quantumpickaxe, new ItemStack(IUItem.upgrademodule, 1, 6));
        addupgrade(IUItem.quantumpickaxe, new ItemStack(IUItem.upgrademodule, 1, 13));
        addupgrade(IUItem.quantumpickaxe, new ItemStack(IUItem.upgrademodule, 1, 3));
        addupgrade(IUItem.quantumpickaxe, new ItemStack(IUItem.upgrademodule, 1, 16));
        addupgrade(IUItem.spectralpickaxe, new ItemStack(IUItem.upgrademodule, 1, 13));
        addupgrade(IUItem.spectralpickaxe, new ItemStack(IUItem.upgrademodule, 1, 3));
        addupgrade(IUItem.spectralpickaxe, new ItemStack(IUItem.upgrademodule, 1, 16));
        addupgrade(IUItem.spectralpickaxe, new ItemStack(IUItem.upgrademodule, 1, 6));
        addupgrade(IUItem.nanoshovel, new ItemStack(IUItem.upgrademodule, 1, 13));
        addupgrade(IUItem.nanoshovel, new ItemStack(IUItem.upgrademodule, 1, 3));
        addupgrade(IUItem.nanoshovel, new ItemStack(IUItem.upgrademodule, 1, 16));
        addupgrade(IUItem.nanoshovel, new ItemStack(IUItem.upgrademodule, 1, 6));
        addupgrade(IUItem.quantumshovel, new ItemStack(IUItem.upgrademodule, 1, 6));
        addupgrade(IUItem.quantumshovel, new ItemStack(IUItem.upgrademodule, 1, 13));
        addupgrade(IUItem.quantumshovel, new ItemStack(IUItem.upgrademodule, 1, 3));
        addupgrade(IUItem.quantumshovel, new ItemStack(IUItem.upgrademodule, 1, 16));
        addupgrade(IUItem.spectralshovel, new ItemStack(IUItem.upgrademodule, 1, 13));
        addupgrade(IUItem.spectralshovel, new ItemStack(IUItem.upgrademodule, 1, 3));
        addupgrade(IUItem.spectralshovel, new ItemStack(IUItem.upgrademodule, 1, 16));
        addupgrade(IUItem.spectralshovel, new ItemStack(IUItem.upgrademodule, 1, 6));
        addupgrade(IUItem.ultDDrill, new ItemStack(IUItem.upgrademodule, 1, 13));
        addupgrade(IUItem.ultDDrill, new ItemStack(IUItem.upgrademodule, 1, 3));
        addupgrade(IUItem.ultDDrill, new ItemStack(IUItem.upgrademodule, 1, 16));
        addupgrade(IUItem.ultDDrill, new ItemStack(IUItem.upgrademodule, 1, 6));
        addupgrade(IUItem.advancedSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 0));
        addupgrade(IUItem.advancedSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 1));
        addupgrade(IUItem.advancedSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 2));
        addupgrade(IUItem.advancedSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 15));
        addupgrade(IUItem.hybridSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 0));
        addupgrade(IUItem.hybridSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 1));
        addupgrade(IUItem.hybridSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 2));
        addupgrade(IUItem.hybridSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 15));
        addupgrade(IUItem.ultimateSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 0));
        addupgrade(IUItem.ultimateSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 1));
        addupgrade(IUItem.ultimateSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 2));
        addupgrade(IUItem.ultimateSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 15));
        addupgrade(IUItem.spectralSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 0));
        addupgrade(IUItem.spectralSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 1));
        addupgrade(IUItem.spectralSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 2));
        addupgrade(IUItem.spectralSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 15));
        addupgrade(IUItem.singularSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 0));
        addupgrade(IUItem.singularSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 1));
        addupgrade(IUItem.singularSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 2));
        addupgrade(IUItem.singularSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 15));
        addupgrade(IUItem.quantumBodyarmor, new ItemStack(IUItem.upgrademodule, 1, 2));
        addupgrade(IUItem.quantumBodyarmor, new ItemStack(IUItem.upgrademodule, 1, 7));
        addupgrade(IUItem.quantumBodyarmor, new ItemStack(IUItem.upgrademodule, 1, 14));
        addupgrade(IUItem.quantumHelmet, new ItemStack(IUItem.upgrademodule, 1, 2));
        addupgrade(IUItem.quantumHelmet, new ItemStack(IUItem.upgrademodule, 1, 8));
        addupgrade(IUItem.quantumLeggings, new ItemStack(IUItem.upgrademodule, 1, 2));
        addupgrade(IUItem.quantumLeggings, new ItemStack(IUItem.upgrademodule, 1, 9));
        addupgrade(IUItem.quantumBoots, new ItemStack(IUItem.upgrademodule, 1, 2));
        addupgrade(IUItem.quantumBoots, new ItemStack(IUItem.upgrademodule, 1, 10));
        addupgrade(IUItem.nano_bow, new ItemStack(IUItem.upgrademodule, 1, 4));
        addupgrade(IUItem.nano_bow, new ItemStack(IUItem.upgrademodule, 1, 11));
        addupgrade(IUItem.quantum_bow, new ItemStack(IUItem.upgrademodule, 1, 4));
        addupgrade(IUItem.quantum_bow, new ItemStack(IUItem.upgrademodule, 1, 11));
        addupgrade(IUItem.spectral_bow, new ItemStack(IUItem.upgrademodule, 1, 4));
        addupgrade(IUItem.spectral_bow, new ItemStack(IUItem.upgrademodule, 1, 11));
        addupgrade(IUItem.spectralSaber, new ItemStack(IUItem.upgrademodule, 1, 5));
        addupgrade(IUItem.spectralSaber, new ItemStack(IUItem.upgrademodule, 1, 12));
        addupgrade(IUItem.quantumSaber, new ItemStack(IUItem.upgrademodule, 1, 5));
        addupgrade(IUItem.quantumSaber, new ItemStack(IUItem.upgrademodule, 1, 12));
        addupgrade(IUItem.spectralSaber, new ItemStack(IUItem.upgrademodule, 1, 17));
        addupgrade(IUItem.spectralSaber, new ItemStack(IUItem.upgrademodule, 1, 19));
        addupgrade(IUItem.quantumSaber, new ItemStack(IUItem.upgrademodule, 1, 17));
        addupgrade(IUItem.quantumSaber, new ItemStack(IUItem.upgrademodule, 1, 19));
        addupgrade(IUItem.spectralSaber, new ItemStack(IUItem.upgrademodule, 1, 20));
        addupgrade(IUItem.spectralSaber, new ItemStack(IUItem.upgrademodule, 1, 23));
        addupgrade(IUItem.quantumSaber, new ItemStack(IUItem.upgrademodule, 1, 20));
        addupgrade(IUItem.quantumSaber, new ItemStack(IUItem.upgrademodule, 1, 23));
        addupgrade(IUItem.spectralSaber, new ItemStack(IUItem.upgrademodule, 1, 24));
        addupgrade(IUItem.quantumSaber, new ItemStack(IUItem.upgrademodule, 1, 24));
        addupgrade(IUItem.nanodrill, new ItemStack(IUItem.upgrademodule, 1, 21));
        addupgrade(IUItem.quantumdrill, new ItemStack(IUItem.upgrademodule, 1, 21));
        addupgrade(IUItem.spectraldrill, new ItemStack(IUItem.upgrademodule, 1, 21));
        addupgrade(IUItem.nanopickaxe, new ItemStack(IUItem.upgrademodule, 1, 21));
        addupgrade(IUItem.quantumpickaxe, new ItemStack(IUItem.upgrademodule, 1, 21));
        addupgrade(IUItem.spectralpickaxe, new ItemStack(IUItem.upgrademodule, 1, 21));
        addupgrade(IUItem.nanoshovel, new ItemStack(IUItem.upgrademodule, 1, 21));
        addupgrade(IUItem.quantumshovel, new ItemStack(IUItem.upgrademodule, 1, 21));
        addupgrade(IUItem.spectralshovel, new ItemStack(IUItem.upgrademodule, 1, 21));
        addupgrade(IUItem.ultDDrill, new ItemStack(IUItem.upgrademodule, 1, 21));
        addupgrade(IUItem.quantumBodyarmor, new ItemStack(IUItem.upgrademodule, 1, 18));
        addupgrade(IUItem.quantumHelmet, new ItemStack(IUItem.upgrademodule, 1, 18));
        addupgrade(IUItem.quantumLeggings, new ItemStack(IUItem.upgrademodule, 1, 18));
        addupgrade(IUItem.quantumBoots, new ItemStack(IUItem.upgrademodule, 1, 18));
        addupgrade(IUItem.quantumBodyarmor, new ItemStack(IUItem.upgrademodule, 1, 22));
        addupgrade(IUItem.quantumHelmet, new ItemStack(IUItem.upgrademodule, 1, 22));
        addupgrade(IUItem.quantumLeggings, new ItemStack(IUItem.upgrademodule, 1, 22));
        addupgrade(IUItem.quantumBoots, new ItemStack(IUItem.upgrademodule, 1, 22));
        addupgrade(IUItem.quantumBodyarmor, new ItemStack(IUItem.upgrademodule, 1, 25));
        addupgrade(IUItem.quantumHelmet, new ItemStack(IUItem.upgrademodule, 1, 25));
        addupgrade(IUItem.quantumLeggings, new ItemStack(IUItem.upgrademodule, 1, 25));
        addupgrade(IUItem.quantumBoots, new ItemStack(IUItem.upgrademodule, 1, 25));
        addupgrade(IUItem.advancedSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 18));
        addupgrade(IUItem.hybridSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 18));
        addupgrade(IUItem.ultimateSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 18));
        addupgrade(IUItem.spectralSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 18));
        addupgrade(IUItem.singularSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 18));
        addupgrade(IUItem.advancedSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 22));
        addupgrade(IUItem.hybridSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 22));
        addupgrade(IUItem.ultimateSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 22));
        addupgrade(IUItem.spectralSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 22));
        addupgrade(IUItem.singularSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 22));
        addupgrade(IUItem.advancedSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 25));
        addupgrade(IUItem.hybridSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 25));
        addupgrade(IUItem.ultimateSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 25));
        addupgrade(IUItem.spectralSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 25));
        addupgrade(IUItem.singularSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 25));
        addupgrade(IUItem.advancedSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 8));
        addupgrade(IUItem.hybridSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 8));
        addupgrade(IUItem.ultimateSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 8));
        addupgrade(IUItem.spectralSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 8));
        addupgrade(IUItem.singularSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 8));
        addupgrade(IUItem.NanoBodyarmor, new ItemStack(IUItem.upgrademodule, 1, 18));
        addupgrade(IUItem.NanoHelmet, new ItemStack(IUItem.upgrademodule, 1, 18));
        addupgrade(IUItem.NanoLeggings, new ItemStack(IUItem.upgrademodule, 1, 18));
        addupgrade(IUItem.NanoBoots, new ItemStack(IUItem.upgrademodule, 1, 18));
        addupgrade(IUItem.NanoBodyarmor, new ItemStack(IUItem.upgrademodule, 1, 22));
        addupgrade(IUItem.NanoHelmet, new ItemStack(IUItem.upgrademodule, 1, 22));
        addupgrade(IUItem.NanoLeggings, new ItemStack(IUItem.upgrademodule, 1, 22));
        addupgrade(IUItem.NanoBoots, new ItemStack(IUItem.upgrademodule, 1, 22));
        addupgrade(IUItem.NanoBodyarmor, new ItemStack(IUItem.upgrademodule, 1, 25));
        addupgrade(IUItem.NanoHelmet, new ItemStack(IUItem.upgrademodule, 1, 25));
        addupgrade(IUItem.NanoLeggings, new ItemStack(IUItem.upgrademodule, 1, 25));
        addupgrade(IUItem.NanoBoots, new ItemStack(IUItem.upgrademodule, 1, 25));
        addupgrade(IUItem.NanoBodyarmor, new ItemStack(IUItem.upgrademodule, 1, 2));
        addupgrade(IUItem.NanoBodyarmor, new ItemStack(IUItem.upgrademodule, 1, 7));
        addupgrade(IUItem.NanoBodyarmor, new ItemStack(IUItem.upgrademodule, 1, 14));
        addupgrade(IUItem.NanoHelmet, new ItemStack(IUItem.upgrademodule, 1, 2));
        addupgrade(IUItem.NanoHelmet, new ItemStack(IUItem.upgrademodule, 1, 8));
        addupgrade(IUItem.NanoLeggings, new ItemStack(IUItem.upgrademodule, 1, 2));
        addupgrade(IUItem.NanoLeggings, new ItemStack(IUItem.upgrademodule, 1, 9));
        addupgrade(IUItem.NanoBoots, new ItemStack(IUItem.upgrademodule, 1, 2));
        addupgrade(IUItem.NanoBoots, new ItemStack(IUItem.upgrademodule, 1, 10));
        addupgrade(IUItem.nanodrill, new ItemStack(IUItem.module9, 1, 12));
        addupgrade(IUItem.quantumdrill, new ItemStack(IUItem.module9, 1, 12));
        addupgrade(IUItem.spectraldrill, new ItemStack(IUItem.module9, 1, 12));
        addupgrade(IUItem.nanopickaxe, new ItemStack(IUItem.module9, 1, 12));
        addupgrade(IUItem.quantumpickaxe, new ItemStack(IUItem.module9, 1, 12));
        addupgrade(IUItem.spectralpickaxe, new ItemStack(IUItem.module9, 1, 12));
        addupgrade(IUItem.nanoshovel, new ItemStack(IUItem.module9, 1, 12));
        addupgrade(IUItem.quantumshovel, new ItemStack(IUItem.module9, 1, 12));
        addupgrade(IUItem.spectralshovel, new ItemStack(IUItem.module9, 1, 12));
        addupgrade(IUItem.nanoaxe, new ItemStack(IUItem.module9, 1, 12));
        addupgrade(IUItem.quantumaxe, new ItemStack(IUItem.module9, 1, 12));
        addupgrade(IUItem.spectralaxe, new ItemStack(IUItem.module9, 1, 12));
        addupgrade(IUItem.ultDDrill, new ItemStack(IUItem.module9, 1, 12));
        addupgrade(IUItem.nanodrill, new ItemStack(IUItem.upgrademodule, 1, 26));
        addupgrade(IUItem.quantumdrill, new ItemStack(IUItem.upgrademodule, 1, 26));
        addupgrade(IUItem.spectraldrill, new ItemStack(IUItem.upgrademodule, 1, 26));
        addupgrade(IUItem.nanopickaxe, new ItemStack(IUItem.upgrademodule, 1, 26));
        addupgrade(IUItem.quantumpickaxe, new ItemStack(IUItem.upgrademodule, 1, 26));
        addupgrade(IUItem.spectralpickaxe, new ItemStack(IUItem.upgrademodule, 1, 26));
        addupgrade(IUItem.nanoshovel, new ItemStack(IUItem.upgrademodule, 1, 26));
        addupgrade(IUItem.quantumshovel, new ItemStack(IUItem.upgrademodule, 1, 26));
        addupgrade(IUItem.spectralshovel, new ItemStack(IUItem.upgrademodule, 1, 26));
        addupgrade(IUItem.nanoaxe, new ItemStack(IUItem.upgrademodule, 1, 26));
        addupgrade(IUItem.quantumaxe, new ItemStack(IUItem.upgrademodule, 1, 26));
        addupgrade(IUItem.spectralaxe, new ItemStack(IUItem.upgrademodule, 1, 26));
        addupgrade(IUItem.ultDDrill, new ItemStack(IUItem.upgrademodule, 1, 26));
        addupgrade(IUItem.nanodrill, new ItemStack(IUItem.upgrademodule, 1, 27));
        addupgrade(IUItem.quantumdrill, new ItemStack(IUItem.upgrademodule, 1, 27));
        addupgrade(IUItem.spectraldrill, new ItemStack(IUItem.upgrademodule, 1, 27));
        addupgrade(IUItem.nanopickaxe, new ItemStack(IUItem.upgrademodule, 1, 27));
        addupgrade(IUItem.quantumpickaxe, new ItemStack(IUItem.upgrademodule, 1, 27));
        addupgrade(IUItem.spectralpickaxe, new ItemStack(IUItem.upgrademodule, 1, 27));
        addupgrade(IUItem.nanoshovel, new ItemStack(IUItem.upgrademodule, 1, 27));
        addupgrade(IUItem.quantumshovel, new ItemStack(IUItem.upgrademodule, 1, 27));
        addupgrade(IUItem.spectralshovel, new ItemStack(IUItem.upgrademodule, 1, 27));
        addupgrade(IUItem.nanoaxe, new ItemStack(IUItem.upgrademodule, 1, 27));
        addupgrade(IUItem.quantumaxe, new ItemStack(IUItem.upgrademodule, 1, 27));
        addupgrade(IUItem.spectralaxe, new ItemStack(IUItem.upgrademodule, 1, 27));
        addupgrade(IUItem.ultDDrill, new ItemStack(IUItem.upgrademodule, 1, 27));

        addupgrade(IUItem.nanopickaxe, new ItemStack(IUItem.upgrademodule, 1, 28));
        addupgrade(IUItem.quantumpickaxe, new ItemStack(IUItem.upgrademodule, 1, 28));
        addupgrade(IUItem.spectralpickaxe, new ItemStack(IUItem.upgrademodule, 1, 28));
        addupgrade(IUItem.nanopickaxe, new ItemStack(IUItem.upgrademodule, 1, 35));
        addupgrade(IUItem.quantumpickaxe, new ItemStack(IUItem.upgrademodule, 1, 35));
        addupgrade(IUItem.spectralpickaxe, new ItemStack(IUItem.upgrademodule, 1, 35));
        addupgrade(IUItem.nanopickaxe, new ItemStack(IUItem.upgrademodule, 1, 36));
        addupgrade(IUItem.quantumpickaxe, new ItemStack(IUItem.upgrademodule, 1, 36));
        addupgrade(IUItem.spectralpickaxe, new ItemStack(IUItem.upgrademodule, 1, 36));
        addupgrade(IUItem.nanoaxe, new ItemStack(IUItem.upgrademodule, 1, 28));
        addupgrade(IUItem.quantumaxe, new ItemStack(IUItem.upgrademodule, 1, 28));
        addupgrade(IUItem.spectralaxe, new ItemStack(IUItem.upgrademodule, 1, 28));
        addupgrade(IUItem.nanoaxe, new ItemStack(IUItem.upgrademodule, 1, 35));
        addupgrade(IUItem.quantumaxe, new ItemStack(IUItem.upgrademodule, 1, 35));
        addupgrade(IUItem.spectralaxe, new ItemStack(IUItem.upgrademodule, 1, 35));
        addupgrade(IUItem.nanoaxe, new ItemStack(IUItem.upgrademodule, 1, 36));
        addupgrade(IUItem.quantumaxe, new ItemStack(IUItem.upgrademodule, 1, 36));
        addupgrade(IUItem.spectralaxe, new ItemStack(IUItem.upgrademodule, 1, 36));
        addupgrade(IUItem.nanoshovel, new ItemStack(IUItem.upgrademodule, 1, 28));
        addupgrade(IUItem.quantumshovel, new ItemStack(IUItem.upgrademodule, 1, 28));
        addupgrade(IUItem.spectralshovel, new ItemStack(IUItem.upgrademodule, 1, 28));
        addupgrade(IUItem.nanoshovel, new ItemStack(IUItem.upgrademodule, 1, 35));
        addupgrade(IUItem.quantumshovel, new ItemStack(IUItem.upgrademodule, 1, 35));
        addupgrade(IUItem.spectralshovel, new ItemStack(IUItem.upgrademodule, 1, 35));
        addupgrade(IUItem.nanoshovel, new ItemStack(IUItem.upgrademodule, 1, 36));
        addupgrade(IUItem.quantumshovel, new ItemStack(IUItem.upgrademodule, 1, 36));
        addupgrade(IUItem.spectralshovel, new ItemStack(IUItem.upgrademodule, 1, 36));
        addupgrade(IUItem.ultDDrill, new ItemStack(IUItem.upgrademodule, 1, 28));
        addupgrade(IUItem.ultDDrill, new ItemStack(IUItem.upgrademodule, 1, 35));
        addupgrade(IUItem.ultDDrill, new ItemStack(IUItem.upgrademodule, 1, 36));
        addupgrade(IUItem.ultDDrill, new ItemStack(IUItem.upgrademodule, 1, 39));
        addupgrade(IUItem.ultDDrill, new ItemStack(IUItem.upgrademodule, 1, 31));
        addupgrade(IUItem.ultDDrill, new ItemStack(IUItem.upgrademodule, 1, 37));
        addupgrade(IUItem.nanopickaxe, new ItemStack(IUItem.upgrademodule, 1, 39));
        addupgrade(IUItem.quantumpickaxe, new ItemStack(IUItem.upgrademodule, 1, 39));
        addupgrade(IUItem.spectralpickaxe, new ItemStack(IUItem.upgrademodule, 1, 39));
        addupgrade(IUItem.nanopickaxe, new ItemStack(IUItem.upgrademodule, 1, 31));
        addupgrade(IUItem.quantumpickaxe, new ItemStack(IUItem.upgrademodule, 1, 31));
        addupgrade(IUItem.spectralpickaxe, new ItemStack(IUItem.upgrademodule, 1, 31));
        addupgrade(IUItem.nanopickaxe, new ItemStack(IUItem.upgrademodule, 1, 37));
        addupgrade(IUItem.quantumpickaxe, new ItemStack(IUItem.upgrademodule, 1, 37));
        addupgrade(IUItem.spectralpickaxe, new ItemStack(IUItem.upgrademodule, 1, 37));
        addupgrade(IUItem.nanoaxe, new ItemStack(IUItem.upgrademodule, 1, 39));
        addupgrade(IUItem.quantumaxe, new ItemStack(IUItem.upgrademodule, 1, 39));
        addupgrade(IUItem.spectralaxe, new ItemStack(IUItem.upgrademodule, 1, 39));
        addupgrade(IUItem.nanoaxe, new ItemStack(IUItem.upgrademodule, 1, 31));
        addupgrade(IUItem.quantumaxe, new ItemStack(IUItem.upgrademodule, 1, 31));
        addupgrade(IUItem.spectralaxe, new ItemStack(IUItem.upgrademodule, 1, 31));
        addupgrade(IUItem.nanoaxe, new ItemStack(IUItem.upgrademodule, 1, 37));
        addupgrade(IUItem.quantumaxe, new ItemStack(IUItem.upgrademodule, 1, 37));
        addupgrade(IUItem.spectralaxe, new ItemStack(IUItem.upgrademodule, 1, 37));
        addupgrade(IUItem.nanodrill, new ItemStack(IUItem.upgrademodule, 1, 39));
        addupgrade(IUItem.quantumdrill, new ItemStack(IUItem.upgrademodule, 1, 39));
        addupgrade(IUItem.spectraldrill, new ItemStack(IUItem.upgrademodule, 1, 39));
        addupgrade(IUItem.nanodrill, new ItemStack(IUItem.upgrademodule, 1, 31));
        addupgrade(IUItem.quantumdrill, new ItemStack(IUItem.upgrademodule, 1, 31));
        addupgrade(IUItem.spectraldrill, new ItemStack(IUItem.upgrademodule, 1, 31));
        addupgrade(IUItem.nanodrill, new ItemStack(IUItem.upgrademodule, 1, 37));
        addupgrade(IUItem.quantumdrill, new ItemStack(IUItem.upgrademodule, 1, 37));
        addupgrade(IUItem.spectraldrill, new ItemStack(IUItem.upgrademodule, 1, 37));

        addupgrade(IUItem.quantumBodyarmor, new ItemStack(IUItem.upgrademodule, 1, 30));
        addupgrade(IUItem.NanoBodyarmor, new ItemStack(IUItem.upgrademodule, 1, 30));
        addupgrade(IUItem.quantumBodyarmor, new ItemStack(IUItem.upgrademodule, 1, 33));
        addupgrade(IUItem.NanoBodyarmor, new ItemStack(IUItem.upgrademodule, 1, 33));
        addupgrade(IUItem.quantumBoots, new ItemStack(IUItem.upgrademodule, 1, 34));
        addupgrade(IUItem.NanoBoots, new ItemStack(IUItem.upgrademodule, 1, 34));

        addupgrade(IUItem.advancedSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 29));
        addupgrade(IUItem.hybridSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 29));
        addupgrade(IUItem.ultimateSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 29));
        addupgrade(IUItem.spectralSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 29));
        addupgrade(IUItem.singularSolarHelmet, new ItemStack(IUItem.upgrademodule, 1, 29));
        addupgrade(IUItem.quantumHelmet, new ItemStack(IUItem.upgrademodule, 1, 29));
        addupgrade(IUItem.NanoHelmet, new ItemStack(IUItem.upgrademodule, 1, 29));
        addupgrade(IUItem.quantumSaber, new ItemStack(IUItem.upgrademodule, 1, 38));
        addupgrade(IUItem.spectralSaber, new ItemStack(IUItem.upgrademodule, 1, 38));
        addupgrade(IUItem.nano_bow, new ItemStack(IUItem.upgrademodule, 1, 32));
        addupgrade(IUItem.quantum_bow, new ItemStack(IUItem.upgrademodule, 1, 32));
        addupgrade(IUItem.spectral_bow, new ItemStack(IUItem.upgrademodule, 1, 32));

    }

    public static void addupgrade(Item container, ItemStack fill) {
        NBTTagCompound nbt = ModUtils.nbt();
        nbt.setString(
                "mode_module",
                fill.getItem() instanceof UpgradeModule ? UpgradeModule.getType(fill.getItemDamage()).name : "blacklist"
        );
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        Recipes.upgrade.addRecipe(input.forStack(new ItemStack(container, 1, OreDictionary.WILDCARD_VALUE)),
                input.forStack(fill), nbt, new ItemStack(container, 1, OreDictionary.WILDCARD_VALUE)
        );

    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    @Override
    protected ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.upgradeblock);
    }

    protected boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    protected boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    @Override
    protected boolean isNormalCube() {
        return false;
    }

    @Override
    public void onNetworkUpdate(String field) {

    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GUIUpgradeBlock(new ContainerDoubleElectricMachine(entityPlayer, this, type));
    }

    @Override
    public RecipeOutput getOutput() {
        if (this.inputSlotA.isEmpty()) {
            return null;
        }

        RecipeOutput output = this.inputSlotA.process(0);

        ItemStack stack1 = getUpgradeItem(this.inputSlotA.get(0)) ? this.inputSlotA.get(0) : this.inputSlotA.get(1);
        ItemStack module = getUpgradeItem(this.inputSlotA.get(0)) ? this.inputSlotA.get(1) : this.inputSlotA.get(0);


        NBTTagCompound nbt1 = ModUtils.nbt(stack1);


        if (module.getItem() instanceof UpgradeModule) {
            if (!nbt1.getBoolean("canupgrade")) {
                this.energy.addEnergy(energyConsume * operationLength);
                return null;
            }
            EnumInfoUpgradeModules type = UpgradeModule.getType(module.getItemDamage());
            int min = 0;
             for (int i = 0; i < 4; i++) {
                if (nbt1.getString("mode_module" + i).equals(type.name)) {
                    min++;
                }
            }
            if (min >= type.max) {
                this.energy.addEnergy(energyConsume * operationLength);
                return null;
            }
            List<UpgradeItemInform> upgrade = UpgradeSystem.system.getInformation(stack1);
            List<Integer> lst = type.list;
            for (UpgradeItemInform upgrade1 : upgrade) {
                if (lst.contains(upgrade1.upgrade.ordinal())) {
                    return null;
                }
            }
        }
        if (output == null) {
            return null;
        }
        if (this.outputSlot.canAdd(output.items)) {
            return output;
        }
        return null;
    }

    public void operateOnce(RecipeOutput output, List<ItemStack> processResult) {

        ItemStack stack1 = getUpgradeItem(this.inputSlotA.get(0)) ? this.inputSlotA.get(0) : this.inputSlotA.get(1);
        ItemStack module = getUpgradeItem(this.inputSlotA.get(0)) ? this.inputSlotA.get(1) : this.inputSlotA.get(0);


        NBTTagCompound nbt1 = ModUtils.nbt(stack1);
        if (module.getItem() instanceof UpgradeModule) {
            int Damage = stack1.getItemDamage();
            double newCharge = ElectricItem.manager.getCharge(stack1);
            final Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(stack1);
            this.inputSlotA.consume(0);
            this.outputSlot.add(processResult);
            ItemStack stack = this.outputSlot.get();
            stack.setTagCompound(nbt1);
            NBTTagCompound nbt = ModUtils.nbt(stack);
            String mode = output.metadata.getString("mode_module");

            int k = 0;
            for (int i = 0; i < 4; i++) {
                if (nbt.getString("mode_module" + i).isEmpty()) {
                    k = i;
                    break;
                }
            }
            nbt.setString("mode_module" + k, mode);
            ElectricItem.manager.charge(stack, newCharge, Integer.MAX_VALUE, true, false);
            EnchantmentHelper.setEnchantments(enchantmentMap, stack);
            MinecraftForge.EVENT_BUS.post(new EventItemLoad(world, (IUpgradeItem) stack.getItem(), stack));
            stack.setItemDamage(Damage);
        }

        if (module.getItem() instanceof QuarryModule && module.getItemDamage() == 12) {
            int Damage = stack1.getItemDamage();
            NBTTagCompound nbt2 = ModUtils.nbt(module);
            double newCharge = ElectricItem.manager.getCharge(stack1);
            final Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(stack1);

            this.inputSlotA.consume(0);
            this.outputSlot.add(processResult);
            ItemStack stack = this.outputSlot.get();
            stack.setTagCompound(nbt1);
            NBTTagCompound nbt = ModUtils.nbt(stack);
            for (int j = 0; j < 18; j++) {
                String l = "number_" + j;
                String temp = nbt2.getString(l);
                nbt.setString(l, temp);
            }
            nbt.setBoolean("list", true);
            ElectricItem.manager.charge(stack, newCharge, Integer.MAX_VALUE, true, false);
            EnchantmentHelper.setEnchantments(enchantmentMap, stack);
            stack.setItemDamage(Damage);
            MinecraftForge.EVENT_BUS.post(new EventItemBlackListLoad(
                    world,
                    (IUpgradeWithBlackList) stack.getItem(),
                    stack,
                    nbt2
            ));

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
