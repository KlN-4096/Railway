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

public class NarrowDoubleScotchYokeBogeyVisual implements BogeyVisual {
    private final TransformedInstance primaryShaft1;
    private final TransformedInstance primaryShaft2;
    private final TransformedInstance secondaryShaft1;
    private final TransformedInstance secondaryShaft2;
    private final TransformedInstance frame;
    private final TransformedInstance pistons;
    private final TransformedInstance wheel1;
    private final TransformedInstance wheel2;
    private final TransformedInstance pin1;
    private final TransformedInstance pin2;

    public NarrowDoubleScotchYokeBogeyVisual(VisualizationContext ctx, float partialTick, boolean inContraption) {
        // 创建主轴实例
        var primaryShaftInstancer = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.block(AllBlocks.SHAFT.getDefaultState()
                        .setValue(ShaftBlock.AXIS, Direction.Axis.Z)));
        primaryShaft1 = primaryShaftInstancer.createInstance();
        primaryShaft2 = primaryShaftInstancer.createInstance();

        // 创建次级轴实例
        var secondaryShaftInstancer = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.block(AllBlocks.SHAFT.getDefaultState()
                        .setValue(ShaftBlock.AXIS, Direction.Axis.X)));
        secondaryShaft1 = secondaryShaftInstancer.createInstance();
        secondaryShaft2 = secondaryShaftInstancer.createInstance();

        // 创建框架实例
        frame = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(NARROW_DOUBLE_SCOTCH_FRAME))
                .createInstance();

        // 创建活塞实例
        pistons = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(NARROW_DOUBLE_SCOTCH_PISTONS))
                .createInstance();

        // 创建车轮实例
        var wheelInstancer = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(NARROW_SCOTCH_WHEELS));
        wheel1 = wheelInstancer.createInstance();
        wheel2 = wheelInstancer.createInstance();

        // 创建车轮销钉实例
        var pinInstancer = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(NARROW_SCOTCH_WHEEL_PINS));
        pin1 = pinInstancer.createInstance();
        pin2 = pinInstancer.createInstance();
    }

    @Override
    public void update(CompoundTag bogeyData, float wheelAngle, PoseStack poseStack) {
        // 更新主轴
        primaryShaft1.setTransform(poseStack)
                .translate(-.5, 1 / 16., (7/16.))
                .center()
                .rotateZDegrees(wheelAngle)
                .uncenter()
                .setChanged();

        primaryShaft2.setTransform(poseStack)
                .translate(-.5, 1 / 16., (7/16.) - (30 / 16.))
                .center()
                .rotateZDegrees(wheelAngle)
                .uncenter()
                .setChanged();

        // 更新次级轴
        secondaryShaft1.setTransform(poseStack)
                .translate(-.5f, 6 / 16., (18 / 16.))
                .center()
                .rotateXDegrees(wheelAngle)
                .uncenter()
                .setChanged();

        secondaryShaft2.setTransform(poseStack)
                .translate(-.5f, 6 / 16., (18 / 16.) - (52 / 16.))
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
                .translate(0, 14 / 16f, 1 / 4f * Math.sin(AngleHelper.rad(wheelAngle)))
                .setChanged();

        // 更新两侧车轮
        wheel1.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .translate(0, 14 / 16., (12 / 16.)) // 正面车轮
                .rotateXDegrees(wheelAngle)
                .setChanged();

        wheel2.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .translate(0, 14 / 16., -(12 / 16.)) // 负面车轮
                .rotateXDegrees(wheelAngle)
                .setChanged();

        // 更新两侧销钉
        pin1.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .translate(0, 14 / 16., (12 / 16.)) // 正面销钉
                .rotateXDegrees(wheelAngle)
                .translate(0, 1 / 4f, 0)
                .rotateXDegrees(-wheelAngle)
                .setChanged();

        pin2.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .translate(0, 14 / 16., -(12 / 16.)) // 负面销钉
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
        wheel1.setZeroTransform().setChanged();
        wheel2.setZeroTransform().setChanged();
        pin1.setZeroTransform().setChanged();
        pin2.setZeroTransform().setChanged();
    }

    @Override
    public void updateLight(int packedLight) {
        primaryShaft1.light(packedLight).setChanged();
        primaryShaft2.light(packedLight).setChanged();
        secondaryShaft1.light(packedLight).setChanged();
        secondaryShaft2.light(packedLight).setChanged();
        frame.light(packedLight).setChanged();
        pistons.light(packedLight).setChanged();
        wheel1.light(packedLight).setChanged();
        wheel2.light(packedLight).setChanged();
        pin1.light(packedLight).setChanged();
        pin2.light(packedLight).setChanged();
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        consumer.accept(primaryShaft1);
        consumer.accept(primaryShaft2);
        consumer.accept(secondaryShaft1);
        consumer.accept(secondaryShaft2);
        consumer.accept(frame);
        consumer.accept(pistons);
        consumer.accept(wheel1);
        consumer.accept(wheel2);
        consumer.accept(pin1);
        consumer.accept(pin2);
    }

    @Override
    public void delete() {
        primaryShaft1.delete();
        primaryShaft2.delete();
        secondaryShaft1.delete();
        secondaryShaft2.delete();
        frame.delete();
        pistons.delete();
        wheel1.delete();
        wheel2.delete();
        pin1.delete();
        pin2.delete();
    }
}