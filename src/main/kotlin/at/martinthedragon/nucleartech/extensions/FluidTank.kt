package at.martinthedragon.nucleartech.extensions

import at.martinthedragon.nucleartech.fluid.FluidUnit
import at.martinthedragon.nucleartech.math.formatStorageFilling
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component.literal
import net.minecraftforge.fluids.capability.templates.FluidTank

fun FluidTank.getTooltip() = listOf(
    fluid.rawFluid.attributes.getDisplayName(fluid),
    Component.literal(FluidUnit.UnitType.MINECRAFT.formatStorageFilling(fluidAmount, capacity))
)

fun FluidTank.writeToNBTRaw(nbt: CompoundTag) = nbt.apply {
    fluid.writeToNBTRaw(nbt)
}
