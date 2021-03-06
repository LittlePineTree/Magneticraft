@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.cout970.magneticraft.tilerenderer.multiblock

import com.cout970.magneticraft.multiblock.core.MultiblockManager
import com.cout970.magneticraft.tileentity.multiblock.TileMultiblock
import com.cout970.magneticraft.tilerenderer.core.BaseTileRenderer
import com.cout970.magneticraft.tilerenderer.core.Utilities
import com.cout970.magneticraft.util.vector.vec3Of
import net.minecraft.client.Minecraft

/**
 * Created by cout970 on 2017/08/10.
 */
private val BREAK_LINES_REGEX = """(\\n)|(\n)""".toRegex()

abstract class TileRendererMultiblock<T : TileMultiblock> : BaseTileRenderer<T>() {

    override fun renderTileEntityAt(te: T, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int) {
        stackMatrix {
            translate(x, y, z)

            if (!te.active) {
                Utilities.multiblockPreview(te.multiblockContext())

                if (te.world.totalWorldTime % 20L == 0L) {
                    val ctx = te.multiblockContext().copy(player = Minecraft.getMinecraft().player)
                    te.clientErrors = MultiblockManager.checkMultiblockStructure(ctx)
                }

                if (te.clientErrors.isNotEmpty()) {
                    val txt = te.clientErrors.first().formattedText

                    txt.split(BREAK_LINES_REGEX).forEachIndexed { index, s ->
                        Utilities.renderFloatingLabel(s, vec3Of(0.5f, 2f - index * 4 / 16f, 0.5f))
                    }
                }
            } else {
                ticks = partialTicks
                time = (te.world.totalWorldTime and 0xFF_FFFF).toDouble() + partialTicks
                render(te)
            }
        }
    }

    override fun isGlobalRenderer(te: T): Boolean = true
}



