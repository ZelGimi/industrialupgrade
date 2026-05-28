package com.denfop.blockentity.storage;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.storage.EnumTypeSlots;
import com.denfop.api.storage.IWorkbenchInterface;
import com.denfop.api.storage.PatternItem;
import com.denfop.api.storage.StorageNetwork;
import com.denfop.api.storage.autocrafting.PatternStack;
import com.denfop.api.storage.autocrafting.SameStack;
import com.denfop.api.storage.autocrafting.TypeRecipe;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockStorageSystemEntity;
import com.denfop.componets.ComponentStorageEnergy;
import com.denfop.containermenu.ContainerInterfaceWorkbench;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.inventory.Inventory;
import com.denfop.mixin.access.RecipeManagerAccessor;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputItemStack;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenInterfaceWorkbench;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.ChatFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BlockEntityInterfaceWorkbench extends BlockEntityInventory implements IWorkbenchInterface {

    private static final String PREFERRED_MODID = Constants.MOD_ID;
    private final Inventory slotSlots;
    protected ComponentStorageEnergy energy;
    Map<Integer, PatternStack> patternItemHashMap = new HashMap<>();
    List<PatternStack> patternStacks = new ArrayList<>();
    private StorageNetwork network;

    public BlockEntityInterfaceWorkbench(BlockPos pos, BlockState state) {
        super(BlockStorageSystemEntity.interface_workbench, pos, state);

        this.energy = this.addComponent(ComponentStorageEnergy.asBasicSink(EnergyType.STORAGE, this, 0));
        this.slotSlots = new Inventory(this, Inventory.TypeItemSlot.INPUT, 63) {
            @Override
            public boolean canPlaceItem(int index, ItemStack stack) {
                return stack.getItem() instanceof PatternItem && ((PatternItem) stack.getItem()).hasPattern(stack) && ((PatternItem) stack.getItem()).getPattern(stack).typeRecipe() == TypeRecipe.WORKBENCH;
            }

            @Override
            public ItemStack set(int i, ItemStack empty) {
                ItemStack stack = super.set(i, empty);
                reload();
                if (network != null) {
                    StorageNetwork storageNetwork = network;
                    storageNetwork.reBuildPatterns = true;
                }
                return stack;
            }
        };
        slotSlots.setStackSizeLimit(1);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction facing) {
        if (ForgeCapabilities.ITEM_HANDLER == cap)
            return LazyOptional.empty();
        return super.getCapability(cap, facing);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockStorageSystemEntity.interface_workbench;
    }

    public BlockTileEntity getBlock() {
        return IUItem.storageSystem.getBlock(getTeBlock());
    }

    public Inventory getSlots() {
        return slotSlots;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);


    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();

        return packet;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        addStorageSystemConsumptionInformation(tooltip);
    }

    private void addStorageSystemConsumptionInformation(final List<String> tooltip) {
        tooltip.add(ChatFormatting.GOLD + Localization.translate("iu.storage_system.tooltip.header"));
        tooltip.add(ChatFormatting.GRAY + Localization.translate("iu.storage_system.tooltip.consumption")
                + ": " + ChatFormatting.YELLOW + formatStorageSystemPower(getRequiredPower()) + " "
                + Localization.translate("iu.storage_system.tooltip.unit"));
        tooltip.add(ChatFormatting.DARK_GRAY + Localization.translate("iu.storage_system.tooltip.consumption.description"));
    }

    private static String formatStorageSystemPower(final double value) {
        if (Math.abs(value - Math.rint(value)) < 0.000001D) {
            return String.valueOf((long) Math.rint(value));
        }
        return String.format(Locale.ROOT, "%.2f", value);
    }

    @Override
    public void onPlaced(final ItemStack stack, final LivingEntity placer, final Direction facing) {
        super.onPlaced(stack, placer, facing);

    }

    @Override
    public List<ItemStack> getWrenchDrops(final Player player, final int fortune) {
        List<ItemStack> itemStackList = super.getWrenchDrops(player, fortune);


        return itemStackList;
    }

    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();

        return packet;
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);

    }

    @Override
    public void onNeighborChange(BlockState neighbor, BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);

    }

    public void updateEntityServer() {
        super.updateEntityServer();

    }

    public void updateField(String name, CustomPacketBuffer is) {

        super.updateField(name, is);
    }

    @Override
    public ContainerInterfaceWorkbench getGuiContainer(final Player var1) {
        return new ContainerInterfaceWorkbench(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<?>> getGui(final Player var1, final ContainerMenuBase<?> var2) {
        return new ScreenInterfaceWorkbench((ContainerInterfaceWorkbench) var2);
    }

    public void onLoaded() {
        super.onLoaded();
        reload();
    }

    public void reload() {
        if (!(level instanceof ServerLevel))
            return;
        ;
        RecipeManager recipeManager = level.getRecipeManager();

        patternItemHashMap.clear();
        patternStacks.clear();
        for (int i = 0; i < slotSlots.size(); i++) {
            ItemStack stack = slotSlots.getItem(i);
            if (!stack.isEmpty()) {
                PatternItem cellItem = (PatternItem) stack.getItem();
                PatternStack cell = cellItem.getPattern(stack);
                Recipe<?> recipe = ((RecipeManagerAccessor) recipeManager).getRecipes().get(RecipeType.CRAFTING).get(cell.location());
                if (recipe != null) {
                    List<IInputItemStack> list = new ArrayList<>();
                    for (Ingredient ingredient : recipe.getIngredients()) {
                        if (!ingredient.isEmpty()) {


                            boolean find = false;
                            for (IInputItemStack iInputItemStack : list) {
                                if (iInputItemStack.matches(ingredient.getItems()[0])) {
                                    find = true;
                                    iInputItemStack.growAmount(ingredient.getItems()[0].getCount());
                                }
                            }
                            if (!find) {
                                final IInputItemStack input = Recipes.inputFactory.getInput(ingredient);
                                list.add(input);
                            }

                        }
                    }
                    List<SameStack> stacks = new ArrayList<>();
                    for (IInputItemStack iInputItemStack : list) {
                        stacks.add(new SameStack(pickPreferred(iInputItemStack.getInputs())));
                    }
                    if (!cell.matches(stacks))
                        continue;
                    boolean find = false;
                    for (SameStack sameStack : cell.output()) {
                        if (ModUtils.checkItemEquality(recipe.getResultItem(level.registryAccess()), sameStack.getStack())) {
                            find = true;
                            break;
                        }
                    }
                    if (!find)
                        continue;
                    patternItemHashMap.put(i, cell);
                    patternStacks.add(cell);
                }
            }
        }
    }

    private ItemStack pickPreferred(List<ItemStack> list) {
        if (list == null || list.isEmpty()) return ItemStack.EMPTY;


        for (ItemStack s : list) {
            if (s == null || s.isEmpty()) continue;
            var key = BuiltInRegistries.ITEM.getKey(s.getItem());
            if (key != null && PREFERRED_MODID.equals(key.getNamespace())) {
                return s;
            }
        }


        for (ItemStack s : list) {
            if (s != null && !s.isEmpty()) return s;
        }

        return list.get(0);
    }

    @Override
    public double getRequiredPower() {
        return 1;
    }

    @Override
    public void setStorageNetwork(StorageNetwork network) {
        this.network = network;
        if (network != null)
            network.reBuildPatterns = true;
    }


    @Override
    public List<PatternStack> getPatterns() {
        return patternStacks;
    }

    @Override
    public List<ItemStack> getStacks() {
        return Collections.emptyList();
    }

    @Override
    public List<FluidStack> getFluidStacks() {
        return Collections.emptyList();
    }

    @Override
    public BlockEntity getBlockEntityNeighbor() {
        return null;
    }

    @Override
    public Direction getDirection() {
        return Direction.UP;
    }

    @Override
    public EnumTypeSlots getTypeSlots() {
        return EnumTypeSlots.WHITELIST;
    }
}
