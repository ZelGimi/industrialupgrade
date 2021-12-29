package com.denfop.audio;

import com.denfop.IUCore;
import ic2.core.IC2;
import ic2.core.util.LogCategory;
import ic2.core.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import paulscode.sound.SoundSystem;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.URL;

@SideOnly(Side.CLIENT)
final class AudioSourceClient extends AudioSource implements Comparable<AudioSourceClient> {

    private final SoundSystem soundSystem;
    private final String sourceName;
    private boolean valid = false;
    private boolean culled = false;
    private final String initialSoundFile;
    private final boolean loop;
    private final boolean prioritized;
    private final Reference<Object> obj;
    private AudioPosition position;
    private final PositionSpec positionSpec;
    private float configuredVolume;
    private float realVolume;
    private boolean isPlaying = false;

    AudioSourceClient(
            SoundSystem soundSystem,
            String sourceName,
            Object obj,
            PositionSpec positionSpec,
            String initialSoundFile,
            boolean loop,
            boolean prioritized,
            float volume
    ) {

        this.sourceName = sourceName;
        this.initialSoundFile = initialSoundFile;
        this.loop = loop;
        this.prioritized = prioritized;
        this.obj = new WeakReference(obj);
        this.position = AudioPosition.getFrom(obj, positionSpec);
        this.positionSpec = positionSpec;
        this.configuredVolume = volume;
        this.soundSystem = soundSystem;
        URL url = AudioSource.class.getClassLoader().getResource("assets/industrialupgrade/sounds/" + initialSoundFile);
        if (url == null) {
            url = AudioSource.class.getClassLoader().getResource("ic2/sounds/" + initialSoundFile);
        }

        if (url == null) {
            IC2.log.warn(LogCategory.Audio, "Invalid sound file: %s.", this.initialSoundFile);
        } else {

            soundSystem.newSource(this.prioritized, this.sourceName, url, this.initialSoundFile, this.loop,
                    this.position.x, this.position.y, this.position.z, 0,
                    ((AudioManagerClient) IUCore.audioManager).fadingDistance * Math.max(this.configuredVolume, 1.0F)
            );
            this.valid = true;
            this.setVolume(volume);
        }
    }

    void setup() {
        if (this.valid) {
            throw new IllegalStateException("already initialized");
        } else {
            URL url = AudioSource.class.getClassLoader().getResource("assets/industrialupgrade/sounds/" + initialSoundFile);
            if (url == null) {
                url = AudioSource.class.getClassLoader().getResource("ic2/sounds/" + initialSoundFile);
            }

            if (url == null) {
                IC2.log.warn(LogCategory.Audio, "Invalid sound file: %s.", this.initialSoundFile);
            } else {
                this.soundSystem.newSource(this.prioritized, this.sourceName, url, this.initialSoundFile, this.loop,
                        this.position.x, this.position.y, this.position.z, 0,
                        ((AudioManagerClient) IUCore.audioManager).fadingDistance * Math.max(this.configuredVolume, 1.0F)
                );
                this.valid = true;
                this.setVolume(this.configuredVolume);
            }
        }
    }

    public int compareTo(AudioSourceClient x) {
        return this.culled
                ? (int) ((this.realVolume * 0.9F - x.realVolume) * 128.0F)
                : (int) ((this.realVolume - x.realVolume) * 128.0F);
    }

    public void remove() {
        if (this.check()) {
            this.stop();
            this.soundSystem.removeSource(this.sourceName);
            this.setInvalid();
        }
    }

    boolean isValid() {
        return this.valid;
    }

    void setInvalid() {
        this.valid = false;
    }

    public void play() {
        if (this.check()) {
            if (!this.isPlaying) {
                this.isPlaying = true;
                if (!this.culled) {
                    this.soundSystem.play(this.sourceName);
                }
            }
        }
    }

    public void pause() {
        if (this.check()) {
            if (this.isPlaying && !this.culled) {
                this.isPlaying = false;
                this.soundSystem.pause(this.sourceName);
            }
        }
    }

    public void stop() {
        if (this.check() && this.isPlaying) {
            this.isPlaying = false;
            if (!this.culled) {
                this.soundSystem.stop(this.sourceName);
            }
        }
    }

    public void flush() {
        if (this.check()) {
            if (this.isPlaying && !this.culled) {
                this.soundSystem.flush(this.sourceName);
            }
        }
    }

    public void cull() {
        if (this.check() && !this.culled) {
            this.soundSystem.cull(this.sourceName);
            this.culled = true;
        }
    }

    public void activate() {
        if (this.check() && this.culled) {
            this.soundSystem.activate(this.sourceName);
            this.culled = false;
            if (this.isPlaying) {
                this.isPlaying = false;
                this.play();
            }

        }
    }

    public float getVolume() {
        return !this.check() ? 0.0F : this.soundSystem.getVolume(this.sourceName);
    }

    public float getRealVolume() {
        return this.realVolume;
    }

    public void setVolume(float volume) {
        if (this.check()) {
            this.configuredVolume = volume;
            this.soundSystem.setVolume(this.sourceName, 0.001F);
        }
    }

    public void setPitch(float pitch) {
        if (this.check()) {
            this.soundSystem.setPitch(this.sourceName, pitch);
        }
    }

    public void updatePosition() {
        if (this.check()) {
            this.position = AudioPosition.getFrom(this.obj.get(), this.positionSpec);
            if (this.position != null) {
                this.soundSystem.setPosition(this.sourceName, this.position.x, this.position.y, this.position.z);
            }
        }
    }

    public void updateVolume(EntityPlayer player) {
        if (this.check() && this.isPlaying) {
            float maxDistance = ((AudioManagerClient) IUCore.audioManager).fadingDistance * Math.max(this.configuredVolume, 1.0F);
            float rolloffFactor = 1.0F;
            float referenceDistance = 1.0F;
            World world = player.getEntityWorld();
            float x = (float) player.posX;
            float y = (float) player.posY;
            float z = (float) player.posZ;
            float distance;
            float gain;
            float newRealVolume;
            float dx;
            if (this.position != null && this.position.getWorld() == world) {
                gain = this.position.x - x;
                newRealVolume = this.position.y - y;
                dx = this.position.z - z;
                distance = (float) Math.sqrt(gain * gain + newRealVolume * newRealVolume + dx * dx);
            } else {
                distance = (float) (1.0F / 0.0);
            }

            if (distance > maxDistance) {
                this.realVolume = 0.0F;
                this.cull();
            } else {
                if (distance < referenceDistance) {
                    distance = referenceDistance;
                }

                gain = 1.0F - rolloffFactor * (distance - referenceDistance) / (maxDistance - referenceDistance);
                newRealVolume = gain * this.configuredVolume * IUCore.audioManager.getMasterVolume();
                dx = (this.position.x - x) / distance;
                float dy = (this.position.y - y) / distance;
                float dz = (this.position.z - z) / distance;
                if ((double) newRealVolume > 0.1D) {
                    for (int i = 0; (float) i < distance; ++i) {
                        BlockPos pos = new BlockPos(Util.roundToNegInf(x), Util.roundToNegInf(y), Util.roundToNegInf(z));
                        IBlockState state = world.getBlockState(pos);
                        Block block = state.getBlock();
                        if (!block.isAir(state, world, pos)) {
                            if (block.isNormalCube(state, world, pos)) {
                                newRealVolume *= 0.6F;
                            } else {
                                newRealVolume *= 0.8F;
                            }
                        }

                        x += dx;
                        y += dy;
                        z += dz;
                    }
                }

                if ((double) Math.abs(this.realVolume / newRealVolume - 1.0F) > 0.06D) {
                    this.soundSystem.setVolume(
                            this.sourceName,
                            IUCore.audioManager.getMasterVolume() * Math.min(newRealVolume, 1.0F)
                    );
                }

                this.realVolume = newRealVolume;
            }
        } else {
            this.realVolume = 0.0F;
        }
    }

    private boolean check() {
        return (!this.valid || IUCore.audioManager.valid()) && (!(this.valid = false));
    }

}
