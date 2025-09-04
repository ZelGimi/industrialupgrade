package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.blockentity.base.BlockEntityDoubleElectricMachine;
import com.denfop.blockentity.base.EnumDoubleElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSunnariumMakerEntity;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuDoubleElectricMachine;
import com.denfop.inventory.Inventory;
import com.denfop.recipe.IInputHandler;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenSunnariumPanelMaker;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntitySunnariumPanelMaker extends BlockEntityDoubleElectricMachine implements IHasRecipe {

    public final ComponentBaseEnergy sunenergy;
    public final Inventory input_slot;

    public BlockEntitySunnariumPanelMaker(BlockPos pos, BlockState state) {
        super(1, 300, 1, EnumDoubleElectricMachine.SUNNARIUM_PANEL, BlockSunnariumMakerEntity.gen_sunnarium_plate, pos, state);
        this.sunenergy = this.addComponent(ComponentBaseEnergy
                .asBasicSink(EnergyType.SOLARIUM, this, 10000, 1));
        this.componentProcess.setHasAudio(false);
        Recipes.recipes.addInitRecipes(this);
        this.input_slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (this.get(0).isEmpty()) {
                    ((BlockEntitySunnariumPanelMaker) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((BlockEntitySunnariumPanelMaker) this.base).inputSlotA.changeAccepts(this.get(0));
                }
                return content;
            }

            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() == IUItem.recipe_schedule.getItem();
            }

            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.RECIPE_SCHEDULE;
            }
        };
    }

    public static void addsunnuriumpanel(ItemStack container, ItemStack fill, ItemStack output) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        List<TagKey<Item>> tags = fill.getTags().filter(itemTagKey -> itemTagKey.location().getPath().split("/").length > 1).toList();
        TagKey<Item> tag = null;
        if (!tags.isEmpty())
            tag = tags.get(0);
        List<ItemStack> list = input.getInput(tag == null ? "" : tag).getInputs();
        if (list != null && list.isEmpty() && tag == null) {
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
                                    input.getInput(tag)
                            ),
                            new RecipeOutput(null, output)
                    )
            );
        }
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

    public MultiBlockEntity getTeBlock() {
        return BlockSunnariumMakerEntity.gen_sunnarium_plate;
    }

    public BlockTileEntity getBlock() {
        return IUItem.sunnariummaker.getBlock();
    }

    public void init() {

        addsunnuriumpanel(
                new ItemStack(IUItem.sunnarium.getStack(2)),
                new ItemStack(IUItem.plate.getStack(9)),
                new ItemStack(IUItem.sunnariumpanel.getStack(0))
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel.getStack(0)),
                new ItemStack(IUItem.plate.getStack(0)),
                new ItemStack(IUItem.sunnariumpanel.getStack(1))
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel.getStack(1)),
                new ItemStack(IUItem.plate.getStack(11)),
                new ItemStack(IUItem.sunnariumpanel.getStack(2))
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel.getStack(2)),
                new ItemStack(IUItem.plate.getStack(13)),
                new ItemStack(IUItem.sunnariumpanel.getStack(3))
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel.getStack(3)),
                new ItemStack(IUItem.plate.getStack(7)),
                new ItemStack(IUItem.sunnariumpanel.getStack(4))
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel.getStack(4)),
                new ItemStack(IUItem.plate.getStack(15)),
                new ItemStack(IUItem.sunnariumpanel.getStack(5))
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel.getStack(5)),
                new ItemStack(IUItem.plate.getStack(16)),
                new ItemStack(IUItem.sunnariumpanel.getStack(6))
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel.getStack(6)),
                new ItemStack(IUItem.plate.getStack(6)),
                new ItemStack(IUItem.sunnariumpanel.getStack(7))
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel.getStack(7)),
                new ItemStack(IUItem.plate.getStack(8)),
                new ItemStack(IUItem.sunnariumpanel.getStack(8))
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel.getStack(8)),
                new ItemStack(IUItem.plate.getStack(14)),
                new ItemStack(IUItem.sunnariumpanel.getStack(9))
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel.getStack(9)),
                new ItemStack(IUItem.plate.getStack(2)),
                new ItemStack(IUItem.sunnariumpanel.getStack(10))
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnarium.getStack(0)),
                new ItemStack(IUItem.plate.getStack(1)),
                new ItemStack(IUItem.sunnarium.getStack(1))
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel.getStack(10)),
                new ItemStack(IUItem.alloysplate.getStack(7)),
                new ItemStack(IUItem.sunnariumpanel.getStack(11))
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel.getStack(11)),
                new ItemStack(IUItem.plate.getStack(5)),
                new ItemStack(IUItem.sunnariumpanel.getStack(12))
        );

    }


    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<?>> getGui(Player entityPlayer, ContainerMenuBase<?> isAdmin) {
        return new ScreenSunnariumPanelMaker((ContainerMenuDoubleElectricMachine) isAdmin);

    }


    public String getStartSoundFile() {
        return null;
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
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
