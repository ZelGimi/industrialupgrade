package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.IType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.vein.IVein;
import com.denfop.api.vein.Type;
import com.denfop.api.vein.VeinSystem;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockQuarryVein;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerQuarryVein;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.datacomponent.VeinInfo;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiQuarryVein;
import com.denfop.items.ItemVeinSensor;
import com.denfop.items.upgradekit.ItemUpgradeMachinesKit;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.render.oilquarry.DataBlock;
import com.denfop.utils.Keyboard;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.List;

public class TileQuarryVein extends TileElectricMachine implements
        IUpdatableTileEvent, IType {

    @OnlyIn(Dist.CLIENT)
    public DataBlock dataBlock;
    public int levelMech;
    public int time;
    public int progress;
    public IVein vein;
    public boolean start = true;
    public int col;
    private int count;
    private boolean work;

    public TileQuarryVein(BlockPos pos, BlockState state) {
        super(400, 14, 1, BlockQuarryVein.quarry_vein, pos, state);
        this.progress = 0;
        this.time = 0;
        this.levelMech = 1;
        this.count = 0;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockQuarryVein.quarry_vein;
    }

    public BlockTileEntity getBlock() {
        return IUItem.oilquarry.getBlock();
    }


    @Override
    public ItemStack getPickBlock(final Player player, final HitResult target) {
        ItemStack stack = super.getPickBlock(player, target);
        final CompoundTag nbt = ModUtils.nbt(stack);
        nbt.putString("state", "active_" + this.levelMech);
        nbt.putInt("level", this.levelMech);
        return stack;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void updateEntityClient() {
        super.updateEntityClient();
        if (this.vein == null || !this.vein.get()) {
            if (this.getWorld().getGameTime() % 6 == 0) {
                if (this.work) {
                    final BlockPos pos1 = pos.below();
                    BlockState state = level.getBlockState(pos1);
                    if (state.isAir()) {
                        state = Blocks.STONE.defaultBlockState();
                    }
                    level.levelEvent(2001, pos, Block.getId(state));
                }
            }
        }
    }


    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (!player.getItemInHand(hand).isEmpty()) {
            if (player.getItemInHand(hand).getItem() instanceof ItemUpgradeMachinesKit) {
                ItemUpgradeMachinesKit<?> itemUpgradeMachinesKit = (ItemUpgradeMachinesKit) player.getItemInHand(hand).getItem();
                if (this.levelMech < 4 && itemUpgradeMachinesKit.getElement().getId() == 3) {
                    this.levelMech = 4;
                    this.setActive("active_" + levelMech);
                    player.getItemInHand(hand).setCount(player.getItemInHand(hand).getCount() - 1);
                    updateTileEntityField();
                    return true;
                } else if (this.levelMech < 4 && this.levelMech ==
                        (itemUpgradeMachinesKit.getElement().getId() + 1)) {
                    this.levelMech++;
                    player.getItemInHand(hand).setCount(player.getItemInHand(hand).getCount() - 1);
                    updateTileEntityField();
                    this.setActive("active_" + levelMech);
                    return true;
                }
            } else if (player.getItemInHand(hand).getItem() instanceof ItemVeinSensor) {
                if (this.vein != VeinSystem.system.getEMPTY() && this.vein.get() && this.vein.getType() != Type.EMPTY) {

                    String type;
                    if (this.vein.getType() == Type.VEIN) {
                        type = getType(this.vein.getMeta(), this.vein.isOldMineral());
                    } else {
                        if (this.vein.getType() == Type.OIL) {
                            type = "oil";
                        } else {
                            type = "gas";
                        }
                    }
                    player.getItemInHand(hand).set(DataComponentsInit.VEIN_INFO, new VeinInfo(type, this.pos.getX(), this.pos.getZ()));
                    return true;
                }
            }
        }
        return super.onActivated(player, hand, side, vec3);
    }


    private String getType(int meta, boolean oldMineral) {
        if (oldMineral) {
            String[] s = {"magnetite", "calaverite", "galena", "nickelite", "pyrite", "quartzite", "uranite", "azurite",
                    "rhodonite", "alfildit", "euxenite", "smithsonite", "ilmenite", "todorokite", "ferroaugite", "sheelite"};
            return s[meta % s.length];
        } else {
            String[] s = {"arsenopyrite", "braggite", "wolframite", "germanite", "coltan", "crocoite", "xenotime", "iridosmine",
                    "theoprastite", "tetrahedrite", "fergusonite", "celestine", "zircon", "crystal"};
            return s[meta % s.length];
        }
    }

    @Override
    public void onPlaced(ItemStack stack, LivingEntity placer, Direction facing) {
        super.onPlaced(stack, placer, facing);
        final CompoundTag nbt = ModUtils.nbt(stack);

        this.levelMech = nbt.getInt("level") != 0 ? nbt.getInt("level") : 1;
        if (levelMech != 1) {
            this.setActive("active_" + levelMech);
        }
        if (getWorld().dimension() != Level.OVERWORLD) {
            this.vein = VeinSystem.system.getEMPTY();
        } else {
            if (this.level.isClientSide) {
                return;
            }
            final ChunkAccess chunk = this.getWorld().getChunk(this.pos);
            final ChunkPos chunkpos = chunk.getPos();
            if (!VeinSystem.system.getChunkPos().contains(chunkpos)) {
                VeinSystem.system.addVein((LevelChunk) chunk);
            }
            this.vein = VeinSystem.system.getVein(chunkpos);
            if (this.vein.get()) {
                this.progress = 1200;
            }
            new PacketUpdateFieldTile(this, "vein", this.vein);

        }
    }


    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();
        try {
            EncoderHandler.encode(packet, levelMech);
            EncoderHandler.encode(packet, vein);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            levelMech = (int) DecoderHandler.decode(customPacketBuffer);
            vein = (IVein) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            progress = (int) DecoderHandler.decode(customPacketBuffer);
            levelMech = (int) DecoderHandler.decode(customPacketBuffer);
            vein = (IVein) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, progress);
            EncoderHandler.encode(packet, levelMech);
            EncoderHandler.encode(packet, vein);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (this.level.isClientSide) {
            return;
        }
        if (getWorld().dimension() != Level.OVERWORLD) {
            this.vein = VeinSystem.system.getEMPTY();
        } else {
            final LevelChunk chunk = (LevelChunk) this.getWorld().getChunk(this.pos);
            final ChunkPos chunkpos = chunk.getPos();
            if (!VeinSystem.system.getChunkPos().contains(chunkpos)) {
                VeinSystem.system.addVein(chunk);
            }
            this.vein = VeinSystem.system.getVein(chunkpos);


            if (this.vein.get()) {
                this.progress = 1200;
            }
            if (this.progress >= 1200) {
                this.vein.setFind(true);
            }
        }
        updateTileEntityField();
    }

    private void updateTileEntityField() {
        new PacketUpdateFieldTile(this, "level", this.levelMech);
        new PacketUpdateFieldTile(this, "vein", this.vein);

    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + 5 + Localization.translate("iu" +
                    ".machines_work_energy_type_eu"));
        }
        super.addInformation(stack, tooltip);

    }

    public void updateField(String name, CustomPacketBuffer is) {

        if (name.equals("count")) {
            try {
                this.count = (int) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("progress")) {
            try {
                this.progress = (int) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("work")) {
            try {
                this.work = (boolean) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("vein")) {
            try {
                this.vein = (IVein) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("level")) {
            try {
                this.levelMech = (int) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.updateField(name, is);
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.vein == null) {
            return;
        }
        if (this.vein.getCol() != this.count) {
            this.count = this.vein.getCol();
            if (this.getWorld().getGameTime() % 4 == 0) {
                new PacketUpdateFieldTile(this, "count", this.count);
            }
        }
        if (this.vein.get()) {
            if (this.getWorld().getGameTime() % 20 == 0) {
                if (this.col != vein.getCol()) {
                    new PacketUpdateFieldTile(this, "vein", this.vein);
                    col = vein.getCol();
                }
            }

            return;
        }

        if (this.progress < 1200 && this.energy.getEnergy() >= 5 && !this.vein.get()) {
            if (!work) {
                work = true;
                new PacketUpdateFieldTile(this, "work", this.work);
            }
            if (progress == 0) {
                initiate(2);
                time = 0;
                initiate(0);
            }
            if (time > 340) {
                initiate(2);
                time = 0;
            }
            if (time == 0) {
                initiate(0);
            }
            time++;
            progress += (int) Math.pow(2, this.levelMech - 1);
            this.energy.useEnergy(5);
            if (progress >= 1200) {
                initiate(2);
                this.progress = 1200;
                this.vein.setFind(true);
                updateTileEntityField();
            }


        } else {
            if (work) {
                work = false;
                new PacketUpdateFieldTile(this, "work", this.work);
            }
            if (this.time > 0) {
                initiate(2);
                this.time = 0;
            }
        }

    }


    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getInt("progress");
        this.levelMech = nbttagcompound.getInt("level");

    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putInt("progress", this.progress);
        nbttagcompound.putInt("level", this.levelMech);
        return nbttagcompound;
    }


    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<?>> getGui(Player entityPlayer, ContainerBase<?> isAdmin) {
        return new GuiQuarryVein((ContainerQuarryVein) isAdmin);
    }

    public ContainerQuarryVein getGuiContainer(Player entityPlayer) {
        return new ContainerQuarryVein(entityPlayer, this);
    }


    @Override
    public EnumTypeStyle getStyle() {
        switch (this.levelMech) {
            case 2:
                return EnumTypeStyle.ADVANCED;
            case 3:
                return EnumTypeStyle.IMPROVED;
            case 4:
                return EnumTypeStyle.PERFECT;
        }
        return EnumTypeStyle.DEFAULT;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.quarry.getSoundEvent();
    }

}
