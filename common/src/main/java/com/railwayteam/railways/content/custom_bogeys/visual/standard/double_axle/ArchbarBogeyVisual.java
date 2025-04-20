/*
 * Steam 'n' Rails
 * Copyright (c) 2025 The Railways Team
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

package com.railwayteam.railways.content.custom_bogeys.visual.standard.double_axle;

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

public class ArchbarBogeyVisual implements BogeyVisual {
    private final TransformedInstance secondaryShaft1;
    private final TransformedInstance secondaryShaft2;
    private final TransformedInstance frame;
    private final TransformedInstance wheel1;
    private final TransformedInstance wheel2;

    public ArchbarBogeyVisual(VisualizationContext ctx, float partialTick, boolean inContraption) {
        // 创建轴实例
        var secondaryShaftInstancer = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.block(AllBlocks.SHAFT.getDefaultState()
                        .setValue(ShaftBlock.AXIS, Direction.Axis.Z)));
        secondaryShaft1 = secondaryShaftInstancer.createInstance();
        secondaryShaft2 = secondaryShaftInstancer.createInstance();

        // 创建框架实例
        frame = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(ARCHBAR_FRAME))
                .createInstance();

        // 创建车轮实例
        var wheelInstancer = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(AllPartialModels.SMALL_BOGEY_WHEELS));
        wheel1 = wheelInstancer.createInstance();
        wheel2 = wheelInstancer.createInstance();
    }

    @Override
    public void update(CompoundTag bogeyData, float wheelAngle, PoseStack poseStack) {
        // 更新轴
        secondaryShaft1.setTransform(poseStack)
                .translate(-.5f, .25f, 0)
                .center()
                .rotateZDegrees(wheelAngle)
                .uncenter()
                .setChanged();

        secondaryShaft2.setTransform(poseStack)
                .translate(-.5f, .25f, -1)
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
                .translate(0, 11.975 / 16f, 0.998)
                .rotateXDegrees(wheelAngle)
                .setChanged();

        wheel2.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .translate(0, 11.975 / 16f, -0.998)
                .rotateXDegrees(wheelAngle)
                .setChanged();
    }

    @Override
    public void hide() {
        secondaryShaft1.setZeroTransform().setChanged();
        secondaryShaft2.setZeroTransform().setChanged();
        frame.setZeroTransform().setChanged();
        wheel1.setZeroTransform().setChanged();
        wheel2.setZeroTransform().setChanged();
    }

    @Override
    public void updateLight(int packedLight) {
        secondaryShaft1.light(packedLight).setChanged();
        secondaryShaft2.light(packedLight).setChanged();
        frame.light(packedLight).setChanged();
        wheel1.light(packedLight).setChanged();
        wheel2.light(packedLight).setChanged();
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        consumer.accept(secondaryShaft1);
        consumer.accept(secondaryShaft2);
        consumer.accept(frame);
        consumer.accept(wheel1);
        consumer.accept(wheel2);
    }

    @Override
    public void delete() {
        secondaryShaft1.delete();
        secondaryShaft2.delete();
        frame.delete();
        wheel1.delete();
        wheel2.delete();
    }
}