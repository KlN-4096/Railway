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

public class Medium606TrailingVisual implements BogeyVisual {
    private final TransformedInstance shaft1;
    private final TransformedInstance shaft2;
    private final TransformedInstance frame;
    private final TransformedInstance wheel1;
    private final TransformedInstance wheel2;
    private final TransformedInstance wheel3;

    public Medium606TrailingVisual(VisualizationContext ctx, float partialTick, boolean inContraption) {
        // 创建轴实例
        var shaftInstancer = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.block(AllBlocks.SHAFT.getDefaultState()
                        .setValue(ShaftBlock.AXIS, Direction.Axis.Z)));
        shaft1 = shaftInstancer.createInstance();
        shaft2 = shaftInstancer.createInstance();

        // 创建框架实例
        frame = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(MEDIUM_6_0_6_TRAILING_FRAME))
                .createInstance();

        // 创建车轮实例 - 需要3个
        var wheelInstancer = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(MEDIUM_SHARED_WHEELS));
        wheel1 = wheelInstancer.createInstance();
        wheel2 = wheelInstancer.createInstance();
        wheel3 = wheelInstancer.createInstance();
    }

    @Override
    public void update(CompoundTag bogeyData, float wheelAngle, PoseStack poseStack) {
        // 更新轴
        shaft1.setTransform(poseStack)
                .translate(-.5f, .31f, .5f)
                .center()
                .rotateZDegrees(wheelAngle)
                .uncenter()
                .setChanged();

        shaft2.setTransform(poseStack)
                .translate(-.5f, .31f, .5f - 2)
                .center()
                .rotateZDegrees(wheelAngle)
                .uncenter()
                .setChanged();

        // 更新框架
        frame.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .setChanged();

        // 更新车轮
        wheel1.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .translate(0, 13 / 16f, -1 * 1.5)
                .rotateXDegrees(wheelAngle)
                .translate(0, -13 / 16f, 0)
                .setChanged();

        wheel2.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .translate(0, 13 / 16f, 0)
                .rotateXDegrees(wheelAngle)
                .translate(0, -13 / 16f, 0)
                .setChanged();

        wheel3.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .translate(0, 13 / 16f, 1 * 1.5)
                .rotateXDegrees(wheelAngle)
                .translate(0, -13 / 16f, 0)
                .setChanged();
    }

    @Override
    public void hide() {
        shaft1.setZeroTransform().setChanged();
        shaft2.setZeroTransform().setChanged();
        frame.setZeroTransform().setChanged();
        wheel1.setZeroTransform().setChanged();
        wheel2.setZeroTransform().setChanged();
        wheel3.setZeroTransform().setChanged();
    }

    @Override
    public void updateLight(int packedLight) {
        shaft1.light(packedLight).setChanged();
        shaft2.light(packedLight).setChanged();
        frame.light(packedLight).setChanged();
        wheel1.light(packedLight).setChanged();
        wheel2.light(packedLight).setChanged();
        wheel3.light(packedLight).setChanged();
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        consumer.accept(shaft1);
        consumer.accept(shaft2);
        consumer.accept(frame);
        consumer.accept(wheel1);
        consumer.accept(wheel2);
        consumer.accept(wheel3);
    }

    @Override
    public void delete() {
        shaft1.delete();
        shaft2.delete();
        frame.delete();
        wheel1.delete();
        wheel2.delete();
        wheel3.delete();
    }
}