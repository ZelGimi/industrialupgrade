package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSunnariumPanelMaker;
import com.denfop.container.ContainerSunnariumMaker;
import com.denfop.gui.GuiSunnariumMaker;
import com.denfop.recipe.IInputHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.EnumSet;
import java.util.Set;

public class TileSunnariumMaker extends TileBaseSunnariumMaker implements IUpdateTick, IHasRecipe {


    public TileSunnariumMaker() {
        super(1, 300, 1);
        this.inputSlotA = new InvSlotRecipes(this, "sunnurium", this);
        Recipes.recipes.addInitRecipes(this);
        this.componentProcess.setInvSlotRecipes(inputSlotA);

    }

    public static void addSunnariumMaker(
            ItemStack container,
            ItemStack container1,
            ItemStack container2,
            ItemStack container3,
            ItemStack output
    ) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        String name1 = "";
        String name2 = "";
        String name3 = "";
        String name4 = "";
        if (OreDictionary.getOreIDs(container).length > 0) {
            name1 = OreDictionary.getOreName(OreDictionary.getOreIDs(container)[0]);
        }
        if (OreDictionary.getOreIDs(container1).length > 0) {
            name2 = OreDictionary.getOreName(OreDictionary.getOreIDs(container1)[0]);
        }
        if (OreDictionary.getOreIDs(container2).length > 0) {
            name3 = OreDictionary.getOreName(OreDictionary.getOreIDs(container2)[0]);
        }
        if (OreDictionary.getOreIDs(container3).length > 0) {
            name4 = OreDictionary.getOreName(OreDictionary.getOreIDs(container3)[0]);
        }
        Recipes.recipes.addRecipe(
                "sunnurium",
                new BaseMachineRecipe(
                        new Input(
                                name1.isEmpty() ? input.getInput(container) : input.getInput(name1, container.getCount()),
                                name2.isEmpty() ? input.getInput(container1) : input.getInput(name2, container1.getCount()),
                                name3.isEmpty() ? input.getInput(container2) : input.getInput(name3, container2.getCount()),
                                name4.isEmpty() ? input.getInput(container3) : input.getInput(name4, container3.getCount())

                        ),
                        new RecipeOutput(null, output)
                )
        );


    }

    public IMultiTileBlock getTeBlock() {
        return BlockSunnariumPanelMaker.gen_sunnarium;
    }

    public BlockTileEntity getBlock() {
        return IUItem.sunnariumpanelmaker;
    }

    public void init() {
        addSunnariumMaker(
                new ItemStack(IUItem.sunnarium, 4, 4),
                new ItemStack(Items.GLOWSTONE_DUST),
                new ItemStack(Items.QUARTZ),
                new ItemStack(IUItem.iuingot, 1, 3),
                new ItemStack(IUItem.sunnarium, 1, 3)
        );
        addSunnariumMaker(
                new ItemStack(IUItem.core, 1, 0),
                new ItemStack(Items.IRON_INGOT),
                new ItemStack(IUItem.iuingot, 1, 1),
                new ItemStack(IUItem.sunnarium, 1, 0),
                new ItemStack(IUItem.excitednucleus, 1, 0)
        );
        addSunnariumMaker(
                new ItemStack(IUItem.core, 1, 1),
                IUItem.bronzeIngot,
                new ItemStack(IUItem.alloysingot, 1, 7),
                new ItemStack(IUItem.sunnarium, 1, 0),
                new ItemStack(IUItem.excitednucleus, 1, 1)
        );
        addSunnariumMaker(
                new ItemStack(IUItem.core, 1, 2),
                new ItemStack(IUItem.iuingot, 1, 13),
                new ItemStack(IUItem.alloysingot, 1, 3),
                new ItemStack(IUItem.sunnarium, 1, 0),
                new ItemStack(IUItem.excitednucleus, 1, 2)
        );
        addSunnariumMaker(
                new ItemStack(IUItem.core, 1, 3),
                IUItem.leadIngot,
                new ItemStack(IUItem.iuingot, 1, 4),
                new ItemStack(IUItem.sunnarium, 1, 0),
                new ItemStack(IUItem.excitednucleus, 1, 3)
        );
        addSunnariumMaker(
                new ItemStack(IUItem.core, 1, 4),
                new ItemStack(Items.PRISMARINE_SHARD),
                new ItemStack(Items.EMERALD),
                new ItemStack(IUItem.sunnarium, 1, 0),
                new ItemStack(IUItem.excitednucleus, 1, 4)
        );
        addSunnariumMaker(
                new ItemStack(IUItem.core, 1, 5),
                new ItemStack(Items.ENDER_PEARL),
                new ItemStack(Items.BLAZE_ROD),
                new ItemStack(IUItem.sunnarium, 1, 0),
                new ItemStack(IUItem.excitednucleus, 1, 5)
        );
        addSunnariumMaker(
                new ItemStack(IUItem.core, 1, 6),
                new ItemStack(Items.QUARTZ),
                new ItemStack(Items.ENDER_EYE),
                new ItemStack(IUItem.sunnarium, 1, 0),
                new ItemStack(IUItem.excitednucleus, 1, 6)
        );
        addSunnariumMaker(
                new ItemStack(IUItem.core, 1, 7),
                new ItemStack(IUItem.alloysingot, 1, 1),
                new ItemStack(IUItem.alloysingot, 1, 8),
                new ItemStack(IUItem.sunnarium, 1, 0),
                new ItemStack(IUItem.excitednucleus, 1, 7)
        );
        addSunnariumMaker(
                new ItemStack(IUItem.core, 1, 8),
                new ItemStack(IUItem.alloysingot, 1, 6),
                new ItemStack(IUItem.alloysingot, 1, 2),
                new ItemStack(IUItem.sunnarium, 1, 0),
                new ItemStack(IUItem.excitednucleus, 1, 8)
        );
        addSunnariumMaker(
                new ItemStack(IUItem.core, 1, 9),
                IUItem.RTGPellets,
                new ItemStack(IUItem.photoniy),
                new ItemStack(IUItem.sunnarium, 1, 0),
                new ItemStack(IUItem.excitednucleus, 1, 9)
        );
        addSunnariumMaker(
                new ItemStack(IUItem.core, 1, 10),
                new ItemStack(IUItem.neutroniumingot),
                new ItemStack(Items.CHORUS_FRUIT),
                new ItemStack(IUItem.sunnarium, 1, 0),
                new ItemStack(IUItem.excitednucleus, 1, 10)
        );
        addSunnariumMaker(

                new ItemStack(IUItem.core, 1, 11),
                new ItemStack(IUItem.alloysingot, 1, 5),
                new ItemStack(IUItem.alloysingot, 1, 4),
                new ItemStack(IUItem.sunnarium, 1, 0),
                new ItemStack(IUItem.excitednucleus, 1, 11)
        );
        addSunnariumMaker(
                new ItemStack(IUItem.core, 1, 12),
                new ItemStack(IUItem.alloysingot, 1, 0),
                new ItemStack(Items.SKULL, 1, 1),
                new ItemStack(IUItem.sunnarium, 1, 0),
                new ItemStack(IUItem.excitednucleus, 1, 12)
        );
        addSunnariumMaker(
                new ItemStack(IUItem.core, 1, 13),
                new ItemStack(IUItem.alloysingot, 1, 9),
                new ItemStack(Items.NETHER_STAR),
                new ItemStack(IUItem.sunnarium, 1, 0),
                new ItemStack(IUItem.excitednucleus, 1, 13)
        );
        addSunnariumMaker(
                new ItemStack(IUItem.crafting_elements, 4, 282),
                new ItemStack(IUItem.crafting_elements, 1, 319),
                new ItemStack(IUItem.crafting_elements, 2, 386),
                new ItemStack(IUItem.crafting_elements, 1, 434),
                new ItemStack(IUItem.crafting_elements, 1, 320)
        );
    }

    @Override
    public ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.sunnariumpanelmaker);
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

    public String getInventoryName() {

        return Localization.translate("blockSunnariumMaker.name");
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiSunnariumMaker(new ContainerSunnariumMaker(entityPlayer, this));
    }

    public String getStartSoundFile() {
        return null;
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
