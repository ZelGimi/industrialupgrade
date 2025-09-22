package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSunnariumMaker;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.gui.GuiSunnariumPanelMaker;
import com.denfop.invslot.Inventory;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.EnumDoubleElectricMachine;
import com.denfop.tiles.base.TileDoubleElectricMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.EnumSet;
import java.util.Set;

public class TileSunnariumPanelMaker extends TileDoubleElectricMachine implements IHasRecipe {

    public final ComponentBaseEnergy sunenergy;
    public final Inventory input_slot;

    public TileSunnariumPanelMaker() {
        super(1, 300, 1, EnumDoubleElectricMachine.SUNNARIUM_PANEL);
        this.sunenergy = this.addComponent(ComponentBaseEnergy
                .asBasicSink(EnergyType.SOLARIUM, this, 10000, 1));
        this.componentProcess.setHasAudio(false);
        Recipes.recipes.addInitRecipes(this);
        this.input_slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if (this.get().isEmpty()) {
                    ((TileSunnariumPanelMaker) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((TileSunnariumPanelMaker) this.base).inputSlotA.changeAccepts(this.get());
                }
            }

            @Override
            public boolean isItemValidForSlot(final int index, final ItemStack stack) {
                return stack.getItem() == IUItem.recipe_schedule;
            }

            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.RECIPE_SCHEDULE;
            }
        };
    }

    public static void addsunnuriumpanel(ItemStack container, ItemStack fill, ItemStack output) {
        int id = OreDictionary.getOreIDs(fill)[0];
        String name = OreDictionary.getOreName(id);
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        if (name == null && fill.getItem() != IUItem.neutroniumingot) {
            Recipes.recipes.addRecipe(
                    "sunnuriumpanel",
                    new BaseMachineRecipe(
                            new Input(
                                    input.getInput(container),
                                    input.getInput(fill)
                            ),
                            new RecipeOutput(null, output)
                    )
            );
        } else {
            Recipes.recipes.addRecipe(
                    "sunnuriumpanel",
                    new BaseMachineRecipe(
                            new Input(
                                    input.getInput(container),
                                    input.getInput(name)
                            ),
                            new RecipeOutput(null, output)
                    )
            );
        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            if (this.input_slot.isEmpty()) {
                (this).inputSlotA.changeAccepts(ItemStack.EMPTY);
            } else {
                (this).inputSlotA.changeAccepts(this.input_slot.get());
            }
        }
    }

    public IMultiTileBlock getTeBlock() {
        return BlockSunnariumMaker.gen_sunnarium_plate;
    }

    public BlockTileEntity getBlock() {
        return IUItem.sunnariummaker;
    }

    public void init() {

        addsunnuriumpanel(
                new ItemStack(IUItem.sunnarium, 1, 2),
                new ItemStack(IUItem.plate, 1, 9),
                new ItemStack(IUItem.sunnariumpanel, 1, 0)
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel, 1, 0),
                new ItemStack(IUItem.plate, 1, 0),
                new ItemStack(IUItem.sunnariumpanel, 1, 1)
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel, 1, 1),
                new ItemStack(IUItem.plate, 1, 11),
                new ItemStack(IUItem.sunnariumpanel, 1, 2)
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel, 1, 2),
                new ItemStack(IUItem.plate, 1, 13),
                new ItemStack(IUItem.sunnariumpanel, 1, 3)
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel, 1, 3),
                new ItemStack(IUItem.plate, 1, 7),
                new ItemStack(IUItem.sunnariumpanel, 1, 4)
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel, 1, 4),
                new ItemStack(IUItem.plate, 1, 15),
                new ItemStack(IUItem.sunnariumpanel, 1, 5)
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel, 1, 5),
                new ItemStack(IUItem.plate, 1, 16),
                new ItemStack(IUItem.sunnariumpanel, 1, 6)
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel, 1, 6),
                new ItemStack(IUItem.plate, 1, 6),
                new ItemStack(IUItem.sunnariumpanel, 1, 7)
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel, 1, 7),
                new ItemStack(IUItem.plate, 1, 8),
                new ItemStack(IUItem.sunnariumpanel, 1, 8)
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel, 1, 8),
                new ItemStack(IUItem.plate, 1, 14),
                new ItemStack(IUItem.sunnariumpanel, 1, 9)
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel, 1, 9),
                new ItemStack(IUItem.plate, 1, 2),
                new ItemStack(IUItem.sunnariumpanel, 1, 10)
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnarium, 1, 0),
                new ItemStack(IUItem.plate, 1, 1),
                new ItemStack(IUItem.sunnarium, 1, 1)
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel, 1, 10),
                new ItemStack(IUItem.alloysplate, 1, 7),
                new ItemStack(IUItem.sunnariumpanel, 1, 11)
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel, 1, 11),
                new ItemStack(IUItem.plate, 1, 5),
                new ItemStack(IUItem.sunnariumpanel, 1, 12)
        );

    }


    @Override
    public ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.sunnariummaker);
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
        return new GuiSunnariumPanelMaker(new ContainerDoubleElectricMachine(entityPlayer, this, type));

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
                UpgradableProperty.EnergyStorage, UpgradableProperty.ItemExtract, UpgradableProperty.ItemInput
        );
    }

    @Override
    public SoundEvent getSound() {
        return null;
    }

}
