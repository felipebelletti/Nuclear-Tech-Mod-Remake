package at.martinthedragon.nucleartech.item

import at.martinthedragon.nucleartech.NuclearTech
import at.martinthedragon.nucleartech.extensions.gray
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Component.literal
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import kotlin.random.Random

class PolaroidItem(properties: Properties) : Item(properties), RandomModelItem {
    override var id = -1
        private set

    override fun chooseId(max: Int): Int {
        if (id == -1 || id >= max) do {
            id = Random.nextInt(max)
        } while (id == 3 || id == 8) // is 4 and 9
        return id
    }

    override fun inventoryTick(stack: ItemStack, level: Level, entity: Entity, slot: Int, selected: Boolean) {
        if (entity is ServerPlayer && entity.health < 10F) entity.addEffect(MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 30, 2, false, false, true))
    }

    override fun appendHoverText(stack: ItemStack, level: Level?, tooltip: MutableList<Component>, flag: TooltipFlag) {
        with(tooltip) {
            add(Component.literal("Fate chosen").gray())
            add(Component.literal.EMPTY)
            when (NuclearTech.polaroidID) {
                1 -> add(Component.literal("...").gray())
                2 -> add(Component.literal("Clear as glass.").gray())
                3 -> add(Component.literal("'M").gray())
                4 -> add(Component.literal("It's about time.").gray())
                5 -> add(Component.literal("If you stare long into the abyss, the abyss stares back.").gray())
                6 -> add(Component.literal("public Party celebration = new Party();").gray())
                7 -> add(Component.literal("V urnerq lbh yvxr EBG13!").gray())
                8 -> add(Component.literal("11011100").gray())
                9 -> add(Component.literal("Vg'f nobhg gvzr.").gray())
                10 -> add(Component.literal("Schrabidium dislikes the breeding reactor.").gray())
                11 -> add(Component.literal("yss stares back.6public Party cel").gray())
                12 -> add(Component.literal("Red streaks.").gray())
                13 -> add(Component.literal("Q1").gray())
                14 -> add(Component.literal("Q4").gray())
                15 -> add(Component.literal("Q3").gray())
                16 -> add(Component.literal("Q2").gray())
                17 -> add(Component.literal("Two friends before christmas.").gray())
                18 -> {
                    add(Component.literal("Duchess of the boxcars.").gray())
                    add(Component.literal.EMPTY)
                    add(Component.literal("\"P.S.: Thirty-one.\"").gray())
                    add(Component.literal("\"Huh, what does thirty-one mean?\"").gray())
                }
            }
            Unit
        }
    }
}
