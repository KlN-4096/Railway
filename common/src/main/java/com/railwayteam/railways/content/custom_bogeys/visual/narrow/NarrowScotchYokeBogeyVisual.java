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

package com.railwayteam.railways.content.custom_bogeys.visual.narrow;

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

public class NarrowScotchYokeBogeyVisual implements BogeyVisual {
    private final TransformedInstance primaryShaft1;
    private final TransformedInstance primaryShaft2;
    private final TransformedInstance secondaryShaft1;
    private final TransformedInstance secondaryShaft2;
    private final TransformedInstance frame;
    private final TransformedInstance pistons;
    private final TransformedInstance wheels;
    private final TransformedInstance pins;

    public NarrowScotchYokeBogeyVisual(VisualizationContext ctx, float partialTick, boolean inContraption) {
        // 创建主轴实例 (Z轴)
        var primaryShaftInstancer = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.block(AllBlocks.SHAFT.getDefaultState()
                        .setValue(ShaftBlock.AXIS, Direction.Axis.Z)));
        primaryShaft1 = primaryShaftInstancer.createInstance();
        primaryShaft2 = primaryShaftInstancer.createInstance();

        // 创建次级轴实例 (X轴)
        var secondaryShaftInstancer = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.block(AllBlocks.SHAFT.getDefaultState()
                        .setValue(ShaftBlock.AXIS, Direction.Axis.X)));
        secondaryShaft1 = secondaryShaftInstancer.createInstance();
        secondaryShaft2 = secondaryShaftInstancer.createInstance();

        // 创建框架实例
        frame = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(NARROW_SCOTCH_FRAME))
                .createInstance();

        // 创建活塞实例
        pistons = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(NARROW_SCOTCH_PISTONS))
                .createInstance();

        // 创建车轮实例
        wheels = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(NARROW_SCOTCH_WHEELS))
                .createInstance();

        // 创建销钉实例
        pins = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(NARROW_SCOTCH_WHEEL_PINS))
                .createInstance();
    }

    @Override
    public void update(CompoundTag bogeyData, float wheelAngle, PoseStack poseStack) {
        // 更新主轴
        primaryShaft1.setTransform(poseStack)
                .translate(-.5, 1 / 16., 0)
                .center()
                .rotateZDegrees(wheelAngle)
                .uncenter()
                .setChanged();

        primaryShaft2.setTransform(poseStack)
                .translate(-.5, 1 / 16., -1)
                .center()
                .rotateZDegrees(wheelAngle)
                .uncenter()
                .setChanged();

        // 更新次级轴
        secondaryShaft1.setTransform(poseStack)
                .translate(-.5f, 6 / 16., (6 / 16.))
                .center()
                .rotateXDegrees(wheelAngle)
                .uncenter()
                .setChanged();

        secondaryShaft2.setTransform(poseStack)
                .translate(-.5f, 6 / 16., (6 / 16.) - (28 / 16.))
                .center()
                .rotateXDegrees(wheelAngle)
                .uncenter()
                .setChanged();

        // 更新框架
        frame.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .translate(0, 5 / 16f, 0)
                .setChanged();

        // 更新活塞
        pistons.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .translate(0, 5 / 16f, 1 / 4f * Math.sin(AngleHelper.rad(wheelAngle)))
                .setChanged();

        // 更新车轮
        wheels.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .translate(0, 14 / 16., 0)
                .rotateXDegrees(wheelAngle)
                .setChanged();

        // 更新销钉
        pins.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .translate(0, 14 / 16., 0)
                .rotateXDegrees(wheelAngle)
                .translate(0, 1 / 4f, 0)
                .rotateXDegrees(-wheelAngle)
                .setChanged();
    }

    @Override
    public void hide() {
        primaryShaft1.setZeroTransform().setChanged();
        primaryShaft2.setZeroTransform().setChanged();
        secondaryShaft1.setZeroTransform().setChanged();
        secondaryShaft2.setZeroTransform().setChanged();
        frame.setZeroTransform().setChanged();
        pistons.setZeroTransform().setChanged();
        wheels.setZeroTransform().setChanged();
        pins.setZeroTransform().setChanged();
    }

    @Override
    public void updateLight(int packedLight) {
        primaryShaft1.light(packedLight).setChanged();
        primaryShaft2.light(packedLight).setChanged();
        secondaryShaft1.light(packedLight).setChanged();
        secondaryShaft2.light(packedLight).setChanged();
        frame.light(packedLight).setChanged();
        pistons.light(packedLight).setChanged();
        wheels.light(packedLight).setChanged();
        pins.light(packedLight).setChanged();
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        consumer.accept(primaryShaft1);
        consumer.accept(primaryShaft2);
        consumer.accept(secondaryShaft1);
        consumer.accept(secondaryShaft2);
        consumer.accept(frame);
        consumer.accept(pistons);
        consumer.accept(wheels);
        consumer.accept(pins);
    }

    @Override
    public void delete() {
        primaryShaft1.delete();
        primaryShaft2.delete();
        secondaryShaft1.delete();
        secondaryShaft2.delete();
        frame.delete();
        pistons.delete();
        wheels.delete();
        pins.delete();
    }
}