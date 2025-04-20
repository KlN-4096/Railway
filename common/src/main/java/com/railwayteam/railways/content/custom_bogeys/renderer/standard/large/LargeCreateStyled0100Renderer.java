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

package com.railwayteam.railways.content.custom_bogeys.renderer.standard.large;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.content.trains.bogey.BogeyRenderer;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;


import static com.railwayteam.railways.registry.CRBlockPartials.*;

public class LargeCreateStyled0100Renderer implements BogeyRenderer {

    @Override
    public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {
        // 初始化渲染缓冲区
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.cutoutMipped());

        // 渲染横向轴
        for (int side : Iterate.positiveAndNegative) {
            SuperByteBuffer shaft = CachedBuffers.block(AllBlocks.SHAFT.getDefaultState()
                    .setValue(ShaftBlock.AXIS, Direction.Axis.X));
            shaft.translate(-.5, .25, -.5f + side * 4.3675)
                    .center()
                    .rotateXDegrees(wheelAngle)
                    .uncenter()
                    .light(light)
                    .overlay(overlay)
                    .renderInto(poseStack, buffer);
        }

        // 渲染纵向轴
        for (int side = -3; side < 3; side++) {
            SuperByteBuffer shaft = CachedBuffers.block(AllBlocks.SHAFT.getDefaultState()
                    .setValue(ShaftBlock.AXIS, Direction.Axis.Z));
            shaft.translate(-.5f, .25f, -1.3f + side * -1.6)
                    .center()
                    .rotateZDegrees(wheelAngle)
                    .uncenter()
                    .light(light)
                    .overlay(overlay)
                    .renderInto(poseStack, buffer);
        }

        // 渲染框架
        CachedBuffers.partial(LARGE_CREATE_STYLED_0_10_0_FRAME, Blocks.AIR.defaultBlockState())
                .scale(1 - 1 / 512f)
                .light(light)
                .overlay(overlay)
                .renderInto(poseStack, buffer);

        // 渲染活塞
        CachedBuffers.partial(LARGE_CREATE_STYLED_0_10_0_PISTON, Blocks.AIR.defaultBlockState())
                .translate(0, 0, 1 / 4f * Math.sin(AngleHelper.rad(wheelAngle)))
                .light(light)
                .overlay(overlay)
                .renderInto(poseStack, buffer);

        // 渲染全遮蔽轮
        SuperByteBuffer fullBlindWheel = CachedBuffers.partial(LC_STYLE_FULL_BLIND_WHEELS, Blocks.AIR.defaultBlockState());
        fullBlindWheel.translate(0, 1, 0)
                .rotateXDegrees(wheelAngle)
                .translate(0, -1, 0)
                .light(light)
                .overlay(overlay)
                .renderInto(poseStack, buffer);

        // 渲染半遮蔽轮和正常轮
        for (int side : Iterate.positiveAndNegative) {
            // 半遮蔽轮
            SuperByteBuffer semiBlindWheel = CachedBuffers.partial(LC_STYLE_SEMI_BLIND_WHEELS, Blocks.AIR.defaultBlockState());
            semiBlindWheel.translate(0, 1, side * 1.684)
                    .rotateXDegrees(wheelAngle)
                    .translate(0, -1, 0)
                    .light(light)
                    .overlay(overlay)
                    .renderInto(poseStack, buffer);

            // 正常轮
            SuperByteBuffer wheel = CachedBuffers.partial(AllPartialModels.LARGE_BOGEY_WHEELS, Blocks.AIR.defaultBlockState());
            wheel.translate(0, 1, side * 3.3684)
                    .rotateXDegrees(wheelAngle)
                    .light(light)
                    .overlay(overlay)
                    .renderInto(poseStack, buffer);
        }

        // 渲染销钉
        for (int side = -2; side < 3; side++) {
            SuperByteBuffer pin = CachedBuffers.partial(AllPartialModels.BOGEY_PIN, Blocks.AIR.defaultBlockState());
            pin.translate(0, 1, side * 1.6842)
                    .rotateXDegrees(wheelAngle)
                    .translate(0, 1 / 4f, 0)
                    .rotateXDegrees(-wheelAngle)
                    .light(light)
                    .overlay(overlay)
                    .renderInto(poseStack, buffer);
        }
    }
}