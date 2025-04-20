/*
 * Steam 'n' Rails
 * Copyright (c) 2022-2024 The Railways Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.railwayteam.railways.content.custom_bogeys.renderer.narrow;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.content.trains.bogey.BogeyRenderer;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.createmod.catnip.data.Iterate;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;

import static com.railwayteam.railways.registry.CRBlockPartials.*;
import static com.railwayteam.railways.registry.CRBlockPartials.NARROW_SCOTCH_WHEEL_PINS;

public class NarrowScotchYokeBogeyRenderer implements BogeyRenderer {
    @Override
    public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.cutoutMipped());

        for (int i : Iterate.zeroAndOne) {
            SuperByteBuffer primaryShaft = CachedBuffers.block(AllBlocks.SHAFT.getDefaultState()
                    .setValue(ShaftBlock.AXIS, Direction.Axis.Z));
            primaryShaft.translate(-.5, 1 / 16., i * -1)
                    .center()
                    .rotateZDegrees(wheelAngle)
                    .uncenter()
                    .light(light)
                    .overlay(overlay)
                    .renderInto(poseStack, buffer);
        }

        for (int i : Iterate.zeroAndOne) {
            SuperByteBuffer secondaryShaft = CachedBuffers.block(AllBlocks.SHAFT.getDefaultState()
                    .setValue(ShaftBlock.AXIS, Direction.Axis.X));
            secondaryShaft.translate(-.5f, 6 / 16., (6 / 16.) + i * -(28 / 16.))
                    .center()
                    .rotateXDegrees(wheelAngle)
                    .uncenter()
                    .light(light)
                    .overlay(overlay)
                    .renderInto(poseStack, buffer);
        }

        CachedBuffers.partial(NARROW_SCOTCH_FRAME, Blocks.AIR.defaultBlockState())
                .scale(1 - 1 / 512f)
                .translate(0, 5 / 16f, 0)
                .light(light)
                .overlay(overlay)
                .renderInto(poseStack, buffer);

        CachedBuffers.partial(NARROW_SCOTCH_PISTONS, Blocks.AIR.defaultBlockState())
                .scale(1 - 1 / 512f)
                .translate(0, 5 / 16f, 1 / 4f * Math.sin(AngleHelper.rad(wheelAngle)))
                .light(light)
                .overlay(overlay)
                .renderInto(poseStack, buffer);

        SuperByteBuffer wheels = CachedBuffers.partial(NARROW_SCOTCH_WHEELS, Blocks.AIR.defaultBlockState());
        wheels.scale(1 - 1 / 512f)
                .translate(0, 14 / 16., 0)
                .rotateXDegrees(wheelAngle)
                .translate(0, 0, 0)
                .light(light)
                .overlay(overlay)
                .renderInto(poseStack, buffer);

        SuperByteBuffer pins = CachedBuffers.partial(NARROW_SCOTCH_WHEEL_PINS, Blocks.AIR.defaultBlockState());
        pins.scale(1 - 1 / 512f)
                .translate(0, 14 / 16., 0)
                .rotateXDegrees(wheelAngle)
                .translate(0, 1 / 4f, 0)
                .rotateXDegrees(-wheelAngle)
                .light(light)
                .overlay(overlay)
                .renderInto(poseStack, buffer);
    }
}
