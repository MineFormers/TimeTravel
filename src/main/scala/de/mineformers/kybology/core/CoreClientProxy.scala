/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 MineFormers
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package de.mineformers.kybology.core

import cpw.mods.fml.client.registry.RenderingRegistry
import cpw.mods.fml.relauncher.Side
import de.mineformers.core.mod.ClientProxy
import de.mineformers.kybology.Core
import de.mineformers.kybology.core.client.renderer.entity.RenderPulledBlock
import de.mineformers.kybology.core.client.renderer.misc.WorldWindowRenderer
import de.mineformers.kybology.core.entity.EntityPulledBlock
import net.minecraftforge.common.MinecraftForge

/**
 * CoreClientProxy
 *
 * @author PaleoCrafter
 */
class CoreClientProxy extends ClientProxy {
  override def init(): Unit = {
    RenderingRegistry.registerEntityRenderingHandler(classOf[EntityPulledBlock], new RenderPulledBlock)
    MinecraftForge.EVENT_BUS.register(new WorldWindowRenderer)
    Core.net.addHandler({
      case _ => null
    }, Side.CLIENT)
  }
}
