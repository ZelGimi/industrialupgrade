package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.fakebody.*;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.events.client.GlobalRenderManager;
import com.denfop.events.client.SolarSystemRenderer;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.utils.Localization;
import com.denfop.world.WorldBaseGen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;


public class TileEntityHologramSpace extends BlockEntityBase {

    public List<IFakeBody> fakeBodyList = new ArrayList<>();
    @OnlyIn(Dist.CLIENT)
    SolarSystemRenderer solarSystemRenderer;
    private UUID uuid = new UUID(WorldBaseGen.random.nextLong(), WorldBaseGen.random.nextLong());

    public TileEntityHologramSpace(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockBaseMachine3Entity.hologram_space, p_155229_, p_155230_);
    }

    @Override
    public void addInformation(ItemStack stack, List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.hologram.info"));
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (this.getWorld().isClientSide) {
            solarSystemRenderer = new SolarSystemRenderer();
            GlobalRenderManager.addRender(level, pos, createFunction(this));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public Function<RenderLevelStageEvent, Void> createFunction(TileEntityHologramSpace te) {
        Function<RenderLevelStageEvent, Void> function = o -> {
            solarSystemRenderer.render(te, o);
            return null;
        };
        return function;
    }


    @Override
    public void onPlaced(final ItemStack stack, final LivingEntity placer, final Direction facing) {
        super.onPlaced(stack, placer, facing);
        if (placer instanceof Player) {
            this.uuid = placer.getUUID();
        }
    }


    @Override
    public void onUnloaded() {
        super.onUnloaded();
        if (this.getWorld().isClientSide) {
            GlobalRenderManager.removeRender(level, pos);
        }
    }

    @Override
    public CustomPacketBuffer writeUpdatePacket() {
        this.fakeBodyList = SpaceNet.instance.getFakeSpaceSystem().getBodyMap().get(uuid);
        if (this.fakeBodyList != null) {
            CustomPacketBuffer packetBuffer = new CustomPacketBuffer();
            packetBuffer.writeInt(fakeBodyList.size());
            for (IFakeBody fakeBody : fakeBodyList) {
                if (fakeBody instanceof IFakePlanet) {
                    packetBuffer.writeByte(0);
                }
                if (fakeBody instanceof IFakeSatellite) {
                    packetBuffer.writeByte(1);
                }
                if (fakeBody instanceof IFakeAsteroid) {
                    packetBuffer.writeByte(2);
                }
                if (fakeBody != null) {
                    try {
                        EncoderHandler.encode(packetBuffer, fakeBody.writeNBTTagCompound(new CompoundTag()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return packetBuffer;
        } else {
            CustomPacketBuffer packetBuffer = new CustomPacketBuffer();
            packetBuffer.writeInt(0);
            return packetBuffer;
        }

    }

    @Override
    public void readFromNBT(final CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.uuid = nbtTagCompound.getUUID("player");
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbt) {
        CompoundTag nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.putUUID("player", uuid);
        return nbtTagCompound;
    }

    @Override
    public void readUpdatePacket(final CustomPacketBuffer is) {
        super.readUpdatePacket(is);
        int size = is.readInt();
        this.fakeBodyList.clear();
        for (int i = 0; i < size; i++) {
            byte id = is.readByte();
            try {
                if (id == 0) {
                    CompoundTag nbt = (CompoundTag) DecoderHandler.decode(is);
                    FakePlanet fakePlanet = new FakePlanet(nbt);
                    this.fakeBodyList.add(fakePlanet);
                }
                if (id == 1) {
                    CompoundTag nbt = (CompoundTag) DecoderHandler.decode(is);
                    FakeSatellite fakePlanet = new FakeSatellite(nbt);
                    this.fakeBodyList.add(fakePlanet);
                }
                if (id == 2) {
                    CompoundTag nbt = (CompoundTag) DecoderHandler.decode(is);
                    FakeAsteroid fakePlanet = new FakeAsteroid(nbt);
                    this.fakeBodyList.add(fakePlanet);
                }
            } catch (Exception e) {
            }
            ;
        }
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("fakebody")) {
            int size = is.readInt();
            this.fakeBodyList.clear();
            for (int i = 0; i < size; i++) {
                byte id = is.readByte();
                try {
                    if (id == 0) {
                        CompoundTag nbt = (CompoundTag) DecoderHandler.decode(is);
                        FakePlanet fakePlanet = new FakePlanet(nbt);
                        this.fakeBodyList.add(fakePlanet);
                    }
                    if (id == 1) {
                        CompoundTag nbt = (CompoundTag) DecoderHandler.decode(is);
                        FakeSatellite fakePlanet = new FakeSatellite(nbt);
                        this.fakeBodyList.add(fakePlanet);
                    }
                    if (id == 2) {
                        CompoundTag nbt = (CompoundTag) DecoderHandler.decode(is);
                        FakeAsteroid fakePlanet = new FakeAsteroid(nbt);
                        this.fakeBodyList.add(fakePlanet);
                    }
                } catch (Exception e) {
                }
                ;
            }
        }
    }

    @Override
    public boolean needUpdate() {
        return true;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();

    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.hologram_space;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
