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

package com.railwayteam.railways.content.custom_bogeys.visual.standard.medium;


import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.content.trains.bogey.BogeyVisual;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import net.createmod.catnip.math.AngleHelper;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import static com.railwayteam.railways.registry.CRBlockPartials.*;


import static com.railwayteam.railways.registry.CRBlockPartials.*;

public class MediumQuintupleWheelVisual implements BogeyVisual {
    private final TransformedInstance[] shafts;
    private final TransformedInstance frame;
    private final TransformedInstance[] wheels;

    public MediumQuintupleWheelVisual(VisualizationContext ctx, float partialTick, boolean inContraption) {
        // 创建轴实例 - 需要4个
        var shaftInstancer = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.block(AllBlocks.SHAFT.getDefaultState()
                        .setValue(ShaftBlock.AXIS, Direction.Axis.Z)));
        shafts = new TransformedInstance[4];
        for (int i = 0; i < 4; i++) {
            shafts[i] = shaftInstancer.createInstance();
        }

        // 创建框架实例
        frame = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(MEDIUM_QUINTUPLE_WHEEL_FRAME))
                .createInstance();

        // 创建车轮实例 - 需要5个
        var wheelInstancer = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(MEDIUM_SHARED_WHEELS));
        wheels = new TransformedInstance[5];
        for (int i = 0; i < 5; i++) {
            wheels[i] = wheelInstancer.createInstance();
        }
    }

    @Override
    public void update(CompoundTag bogeyData, float wheelAngle, PoseStack poseStack) {
        // 更新轴
        for (int side = 0; side < 4; side++) {
            shafts[side].setTransform(poseStack)
                    .translate(-.5f, .31f, 1.8f + side * -1.5)
                    .center()
                    .rotateZDegrees(wheelAngle)
                    .uncenter()
                    .setChanged();
        }

        // 更新框架
        frame.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .setChanged();

        // 更新车轮
        for (int side = -1; side < 4; side++) {
            int index = side + 1; // 转换为0-4范围
            wheels[index].setTransform(poseStack)
                    .scale(1 - 1 / 512f)
                    .translate(0, 13 / 16f, -1.5f + side * 1.5)
                    .rotateXDegrees(wheelAngle)
                    .translate(0, -13 / 16f, 0)
                    .setChanged();
        }
    }

    @Override
    public void hide() {
        for (TransformedInstance shaft : shafts) {
            shaft.setZeroTransform().setChanged();
        }
        frame.setZeroTransform().setChanged();
        for (TransformedInstance wheel : wheels) {
            wheel.setZeroTransform().setChanged();
        }
    }

    @Override
    public void updateLight(int packedLight) {
        for (TransformedInstance shaft : shafts) {
            shaft.light(packedLight).setChanged();
        }
        frame.light(packedLight).setChanged();
        for (TransformedInstance wheel : wheels) {
            wheel.light(packedLight).setChanged();
        }
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        for (TransformedInstance shaft : shafts) {
            consumer.accept(shaft);
        }
        consumer.accept(frame);
        for (TransformedInstance wheel : wheels) {
            consumer.accept(wheel);
        }
    }

    @Override
    public void delete() {
        for (TransformedInstance shaft : shafts) {
            shaft.delete();
        }
        frame.delete();
        for (TransformedInstance wheel : wheels) {
            wheel.delete();
        }
    }
}