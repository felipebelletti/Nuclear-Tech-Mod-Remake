package at.martinthedragon.nucleartech.api.sound

import net.minecraft.client.Minecraft
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraftforge.client.ForgeHooksClient
import net.minecraft.util.RandomSource

public class MachineSoundInstance(
    sound: SoundEvent,
    source: SoundSource,
    pos: BlockPos,
    loop: Boolean,
    volume: Float = 1F,
    pitch: Float = 1F
) : AbstractTickableSoundInstance(sound, source, (Minecraft.getInstance().player?.position() ?: BlockPos.ZERO) as RandomSource) {
    init {
        x = pos.x + .5
        y = pos.y + .5
        z = pos.z + .5
        looping = loop
        delay = 0
        this.volume = volume
        this.pitch = pitch
    }

    private var interval = 20 + RandomSource.create().nextInt(20)

    override fun tick() {
        val level = Minecraft.getInstance().level ?: return
        if (level.gameTime % interval == 0L) {
            if (!isClientPlayerInRange()) {
                stop()
                return
            }

            val sound = ForgeHooksClient.playSound(Minecraft.getInstance().soundManager.soundEngine, this)
            if (sound == null) stop()
        }
    }

    override fun canStartSilent(): Boolean = true
}
