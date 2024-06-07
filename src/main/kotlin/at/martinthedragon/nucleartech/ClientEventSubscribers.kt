package at.martinthedragon.nucleartech

import at.martinthedragon.nucleartech.config.NuclearConfig
import at.martinthedragon.nucleartech.extensions.*
import at.martinthedragon.nucleartech.fluid.trait.FluidTraitHandler
import at.martinthedragon.nucleartech.hazard.HazardSystem
import at.martinthedragon.nucleartech.rendering.NTechCapes
import at.martinthedragon.nucleartech.screen.AnvilScreen
import at.martinthedragon.nucleartech.screen.UseTemplateFolderScreen
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.*
import net.minecraft.world.entity.Entity
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.RecipesUpdatedEvent
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.client.event.RenderTooltipEvent
import net.minecraftforge.event.TagsUpdatedEvent
import net.minecraftforge.event.entity.EntityJoinLevelEvent
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.VersionChecker
import net.minecraftforge.fml.common.Mod

@Suppress("unused", "UNUSED_PARAMETER")
@Mod.EventBusSubscriber(Dist.CLIENT, modid = NuclearTech.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
object ClientEventSubscribers {
    @SubscribeEvent @JvmStatic
    fun onItemTooltip(event: ItemTooltipEvent) {
        HazardSystem.addHoverText(event.itemStack, event.entity, event.toolTip, event.flags)
    }

    @SubscribeEvent @JvmStatic
    fun onTooltip(event: RenderTooltipEvent.GatherComponents) {
        FluidTraitHandler.addHoverText(event)
    }

    @SubscribeEvent @JvmStatic
    fun onTagsUpdated(event: TagsUpdatedEvent) {
        UseTemplateFolderScreen.reloadSearchTree()
    }

    @SubscribeEvent @JvmStatic
    fun onRecipesUpdated(event: RecipesUpdatedEvent) {
        UseTemplateFolderScreen.reloadSearchTree()
        AnvilScreen.reloadSearchTree()
    }

    @SubscribeEvent @JvmStatic
    fun onRenderPlayer(event: RenderPlayerEvent.Pre) {
        NTechCapes.renderCape(event.entity, event.renderer)
    }

    @SubscribeEvent @JvmStatic
    fun onRenderPlayerPost(event: RenderPlayerEvent.Post) {
        NTechCapes.renderCapePost(event.renderer)
    }

    private var didVersionCheck = false

    @SubscribeEvent @JvmStatic
    fun clientVersionCheckChatMessage(event: EntityJoinLevelEvent) {
        val entity: Entity = event.entity
        if (!didVersionCheck && entity === Minecraft.getInstance().player) {
            val message = createVersionUpdateChatMessage()
            message?.let {
                entity.displayClientMessage(LangKeys.VERSION_CHECKER_ANNOUNCEMENT.get().gold(), false)
                entity.displayClientMessage(it, false)
            }
            didVersionCheck = true
        }
    }

    private fun createVersionUpdateChatMessage(): Component? {
        val currentVersion = NuclearTech.currentVersion ?: return null
        if (!NuclearConfig.client.displayUpdateMessage.get() || currentVersion == "0.0NONE") return null
        val versionCheckResult = NuclearTech.versionCheckResult ?: return null
        return when (versionCheckResult.status) {
            VersionChecker.Status.PENDING, VersionChecker.Status.FAILED, VersionChecker.Status.UP_TO_DATE, null -> null
            VersionChecker.Status.BETA, VersionChecker.Status.AHEAD -> {
                val cuttingEdgeMessage = if (NuclearTech.isSnapshot) LangKeys.VERSION_CHECKER_BLEEDING_EDGE.get().red() else LangKeys.VERSION_CHECKER_CUTTING_EDGE.get().gold()
                cuttingEdgeMessage.append(Component.literal(" ($currentVersion)").white())
            }
            VersionChecker.Status.OUTDATED, VersionChecker.Status.BETA_OUTDATED -> {
                LangKeys.VERSION_CHECKER_UPDATE.get().yellow()
                    .append(Component.literal(" ($currentVersion -> ").white())
                    .append(Component.literal("${versionCheckResult.target}").blue().underline().withStyle(Style.EMPTY
                        .withHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, LangKeys.VERSION_CHECKER_VIEW_RELEASES.get().gray()))
                        .withClickEvent(ClickEvent(ClickEvent.Action.OPEN_URL, versionCheckResult.url))))
                    .append(Component.literal(")").white())
                    .append("\n")
                    .run { if (versionCheckResult.changes.isNotEmpty()) append(LangKeys.VERSION_CHECKER_CHANGES_LIST.get().yellow()) else this }
                    .run { var next = this; for (change in versionCheckResult.changes.values.flatMap { it.split("\r\n", "\n", "\r") }) next = next.append(Component.literal('\n' + change.prependIndent()).white()); next }
            }
        }
    }
}
