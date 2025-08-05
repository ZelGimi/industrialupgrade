package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.recipe.*;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSunnariumPanelMaker;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerSunnariumMaker;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiSunnariumMaker;
import com.denfop.invslot.InvSlot;
import com.denfop.recipe.IInputHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.Set;

public class TileSunnariumMaker extends TileBaseSunnariumMaker implements IUpdateTick, IHasRecipe {


    public final InvSlot input_slot;

    public TileSunnariumMaker(BlockPos pos, BlockState state) {
        super(1, 300, 1, BlockSunnariumPanelMaker.gen_sunnarium, pos, state);
        this.inputSlotA = new InvSlotRecipes(this, "sunnurium", this);
        Recipes.recipes.addInitRecipes(this);
        this.componentProcess.setInvSlotRecipes(inputSlotA);
        this.input_slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (this.get(0).isEmpty()) {
                    ((TileSunnariumMaker) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((TileSunnariumMaker) this.base).inputSlotA.changeAccepts(this.get(0));
                }
                return content;
            }

            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.RECIPE_SCHEDULE;
            }

            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() == IUItem.recipe_schedule.getItem();
            }
        };
    }

    public static void addSunnariumMaker(
            ItemStack container,
            ItemStack container1,
            ItemStack container2,
            ItemStack container3,
            ItemStack output
    ) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        TagKey<Item> name1 = null;
        TagKey<Item> name2 = null;
        TagKey<Item> name3 = null;
        TagKey<Item> name4 = null;
        if (!container.getTags().filter(itemTagKey -> itemTagKey.location().getPath().split("/").length > 1).toList().isEmpty()) {
            name1 = container.getTags().filter(itemTagKey -> itemTagKey.location().getPath().split("/").length > 1).toList().get(0);
        }
        if (!container1.getTags().filter(itemTagKey -> itemTagKey.location().getPath().split("/").length > 1).toList().isEmpty()) {
            name2 = container1.getTags().filter(itemTagKey -> itemTagKey.location().getPath().split("/").length > 1).toList().get(0);
        }
        if (!container2.getTags().filter(itemTagKey -> itemTagKey.location().getPath().split("/").length > 1).toList().isEmpty()) {
            name3 = container2.getTags().filter(itemTagKey -> itemTagKey.location().getPath().split("/").length > 1).toList().get(0);
        }
        if (!container3.getTags().filter(itemTagKey -> itemTagKey.location().getPath().split("/").length > 1).toList().isEmpty()) {
            name4 = container3.getTags().filter(itemTagKey -> itemTagKey.location().getPath().split("/").length > 1).toList().get(0);
        }
        Recipes.recipes.addRecipe(
                "sunnurium",
                new BaseMachineRecipe(
                        new Input(
                                name1 == null ? input.getInput(container) : input.getInput(name1, container.getCount()),
                                name2 == null ? input.getInput(container1) : input.getInput(name2, container1.getCount()),
                                name3 == null ? input.getInput(container2) : input.getInput(name3, container2.getCount()),
                                name4 == null ? input.getInput(container3) : input.getInput(name4, container3.getCount())

                        ),
                        new RecipeOutput(null, output)
                )
        );


    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            if (this.input_slot.isEmpty()) {
                (this).inputSlotA.changeAccepts(ItemStack.EMPTY);
            } else {
                (this).inputSlotA.changeAccepts(this.input_slot.get(0));
            }
        }
    }

    public IMultiTileBlock getTeBlock() {
        return BlockSunnariumPanelMaker.gen_sunnarium;
    }

    public BlockTileEntity getBlock() {
        return IUItem.sunnariumpanelmaker.getBlock();
    }

    public void init() {
        addSunnariumMaker(
                new ItemStack(IUItem.sunnarium.getStack(4), 4),
                new ItemStack(Items.GLOWSTONE_DUST),
                new ItemStack(Items.QUARTZ),
                new ItemStack(IUItem.iuingot.getStack(3)),
                new ItemStack(IUItem.sunnarium.getStack(3))
        );
        addSunnariumMaker(
                new ItemStack(IUItem.core.getStack(0)),
                new ItemStack(Items.IRON_INGOT),
                new ItemStack(IUItem.iuingot.getStack(1)),
                new ItemStack(IUItem.sunnarium.getStack(0)),
                new ItemStack(IUItem.excitednucleus.getStack(0))
        );
        addSunnariumMaker(
                new ItemStack(IUItem.core.getStack(1)),
                IUItem.bronzeIngot,
                new ItemStack(IUItem.alloysingot.getStack(7)),
                new ItemStack(IUItem.sunnarium.getStack(0)),
                new ItemStack(IUItem.excitednucleus.getStack(1))
        );
        addSunnariumMaker(
                new ItemStack(IUItem.core.getStack(2)),
                new ItemStack(IUItem.iuingot.getStack(13)),
                new ItemStack(IUItem.alloysingot.getStack(3)),
                new ItemStack(IUItem.sunnarium.getStack(0)),
                new ItemStack(IUItem.excitednucleus.getStack(2))
        );
        addSunnariumMaker(
                new ItemStack(IUItem.core.getStack(3)),
                IUItem.leadIngot,
                new ItemStack(IUItem.iuingot.getStack(4)),
                new ItemStack(IUItem.sunnarium.getStack(0)),
                new ItemStack(IUItem.excitednucleus.getStack(3))
        );
        addSunnariumMaker(
                new ItemStack(IUItem.core.getStack(4)),
                new ItemStack(Items.PRISMARINE_SHARD),
                new ItemStack(Items.EMERALD),
                new ItemStack(IUItem.sunnarium.getStack(0)),
                new ItemStack(IUItem.excitednucleus.getStack(4))
        );
        addSunnariumMaker(
                new ItemStack(IUItem.core.getStack(5)),
                new ItemStack(Items.ENDER_PEARL),
                new ItemStack(Items.BLAZE_ROD),
                new ItemStack(IUItem.sunnarium.getStack(0)),
                new ItemStack(IUItem.excitednucleus.getStack(5))
        );
        addSunnariumMaker(
                new ItemStack(IUItem.core.getStack(6)),
                new ItemStack(Items.QUARTZ),
                new ItemStack(Items.ENDER_EYE),
                new ItemStack(IUItem.sunnarium.getStack(0)),
                new ItemStack(IUItem.excitednucleus.getStack(6))
        );
        addSunnariumMaker(
                new ItemStack(IUItem.core.getStack(7)),
                new ItemStack(IUItem.alloysingot.getStack(1)),
                new ItemStack(IUItem.alloysingot.getStack(8)),
                new ItemStack(IUItem.sunnarium.getStack(0)),
                new ItemStack(IUItem.excitednucleus.getStack(7))
        );
        addSunnariumMaker(
                new ItemStack(IUItem.core.getStack(8)),
                new ItemStack(IUItem.alloysingot.getStack(6)),
                new ItemStack(IUItem.alloysingot.getStack(2)),
                new ItemStack(IUItem.sunnarium.getStack(0)),
                new ItemStack(IUItem.excitednucleus.getStack(8))
        );
        addSunnariumMaker(
                new ItemStack(IUItem.core.getStack(9)),
                new ItemStack(IUItem.nuclear_res.getStack(9)),
                new ItemStack(IUItem.photoniy.getItem()),
                new ItemStack(IUItem.sunnarium.getStack(0)),
                new ItemStack(IUItem.excitednucleus.getStack(9))
        );
        addSunnariumMaker(
                new ItemStack(IUItem.core.getStack(10)),
                new ItemStack(IUItem.neutroniumingot.getItem()),
                new ItemStack(Items.CHORUS_FRUIT),
                new ItemStack(IUItem.sunnarium.getStack(0)),
                new ItemStack(IUItem.excitednucleus.getStack(10))
        );
        addSunnariumMaker(

                new ItemStack(IUItem.core.getStack(11)),
                new ItemStack(IUItem.alloysingot.getStack(5)),
                new ItemStack(IUItem.alloysingot.getStack(4)),
                new ItemStack(IUItem.sunnarium.getStack(0)),
                new ItemStack(IUItem.excitednucleus.getStack(11))
        );
        addSunnariumMaker(
                new ItemStack(IUItem.core.getStack(12)),
                new ItemStack(IUItem.alloysingot.getStack(0)),
                new ItemStack(Items.WITHER_SKELETON_SKULL),
                new ItemStack(IUItem.sunnarium.getStack(0)),
                new ItemStack(IUItem.excitednucleus.getStack(12))
        );
        addSunnariumMaker(
                new ItemStack(IUItem.core.getStack(13)),
                new ItemStack(IUItem.alloysingot.getStack(9)),
                new ItemStack(Items.NETHER_STAR),
                new ItemStack(IUItem.sunnarium.getStack(0)),
                new ItemStack(IUItem.excitednucleus.getStack(13))
        );
        addSunnariumMaker(
                new ItemStack(IUItem.crafting_elements.getStack(282), 4),
                new ItemStack(IUItem.crafting_elements.getStack(319)),
                new ItemStack(IUItem.crafting_elements.getStack(386), 2),
                new ItemStack(IUItem.crafting_elements.getStack(434)),
                new ItemStack(IUItem.crafting_elements.getStack(320))
        );


    }


    public String getInventoryName() {

        return Localization.translate("blockSunnariumMaker.name");
    }

    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<?>> getGui(Player entityPlayer, ContainerBase<?> isAdmin) {
        return new GuiSunnariumMaker((ContainerSunnariumMaker) isAdmin);
    }

    public ContainerSunnariumMaker getGuiContainer(Player entityPlayer) {
        return new ContainerSunnariumMaker(entityPlayer, this);
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
