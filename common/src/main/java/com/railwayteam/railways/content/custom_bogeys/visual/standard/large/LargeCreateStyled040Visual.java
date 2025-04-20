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

package com.railwayteam.railways.content.custom_bogeys.visual.standard.large;

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

public class LargeCreateStyled040Visual implements BogeyVisual {
    private final TransformedInstance secondaryShaft1;
    private final TransformedInstance secondaryShaft2;
    private final TransformedInstance middleShaft1;
    private final TransformedInstance middleShaft2;
    private final TransformedInstance frame;
    private final TransformedInstance piston;
    private final TransformedInstance wheel1;
    private final TransformedInstance wheel2;
    private final TransformedInstance pin1;
    private final TransformedInstance pin2;

    public LargeCreateStyled040Visual(VisualizationContext ctx, float partialTick, boolean inContraption) {
        // 创建轴实例
        var secondaryShaftInstancer = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.block(AllBlocks.SHAFT.getDefaultState()
                        .setValue(ShaftBlock.AXIS, Direction.Axis.X)));
        secondaryShaft1 = secondaryShaftInstancer.createInstance();
        secondaryShaft2 = secondaryShaftInstancer.createInstance();

        var middleShaftInstancer = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.block(AllBlocks.SHAFT.getDefaultState()
                        .setValue(ShaftBlock.AXIS, Direction.Axis.Z)));
        middleShaft1 = middleShaftInstancer.createInstance();
        middleShaft2 = middleShaftInstancer.createInstance();

        // 创建框架和活塞实例
        frame = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(LARGE_CREATE_STYLED_0_4_0_FRAME))
                .createInstance();
        piston = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(LARGE_CREATE_STYLED_0_4_0_PISTON))
                .createInstance();

        // 创建车轮实例
        var wheelInstancer = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(AllPartialModels.LARGE_BOGEY_WHEELS));
        wheel1 = wheelInstancer.createInstance();
        wheel2 = wheelInstancer.createInstance();

        // 创建销钉实例
        var pinInstancer = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(AllPartialModels.BOGEY_PIN));
        pin1 = pinInstancer.createInstance();
        pin2 = pinInstancer.createInstance();
    }

    @Override
    public void update(CompoundTag bogeyData, float wheelAngle, PoseStack poseStack) {
        // 更新横向轴
        secondaryShaft1.setTransform(poseStack)
                .translate(-.5f, .25f, -.5f + 1.87)
                .center()
                .rotateXDegrees(wheelAngle)
                .uncenter()
                .setChanged();

        secondaryShaft2.setTransform(poseStack)
                .translate(-.5f, .25f, -.5f - 1.87)
                .center()
                .rotateXDegrees(wheelAngle)
                .uncenter()
                .setChanged();

        // 更新纵向轴
        middleShaft1.setTransform(poseStack)
                .translate(-.5f, .25f, -.5f + 1.2)
                .center()
                .rotateZDegrees(wheelAngle)
                .uncenter()
                .setChanged();

        middleShaft2.setTransform(poseStack)
                .translate(-.5f, .25f, -.5f - 1.2)
                .center()
                .rotateZDegrees(wheelAngle)
                .uncenter()
                .setChanged();

        // 更新框架
        frame.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .setChanged();

        // 更新活塞
        piston.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .translate(0, 0, 1 / 4f * Math.sin(AngleHelper.rad(wheelAngle)))
                .setChanged();

        // 更新车轮
        wheel1.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .translate(0, 1, .8732)
                .rotateXDegrees(wheelAngle)
                .setChanged();

        wheel2.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .translate(0, 1, -.8732)
                .rotateXDegrees(wheelAngle)
                .setChanged();

        // 更新销钉
        pin1.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .translate(0, 1, .8732)
                .rotateXDegrees(wheelAngle)
                .translate(0, 1 / 4f, 0)
                .rotateXDegrees(-wheelAngle)
                .setChanged();

        pin2.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .translate(0, 1, -.8732)
                .rotateXDegrees(wheelAngle)
                .translate(0, 1 / 4f, 0)
                .rotateXDegrees(-wheelAngle)
                .setChanged();
    }

    @Override
    public void hide() {
        secondaryShaft1.setZeroTransform().setChanged();
        secondaryShaft2.setZeroTransform().setChanged();
        middleShaft1.setZeroTransform().setChanged();
        middleShaft2.setZeroTransform().setChanged();
        frame.setZeroTransform().setChanged();
        piston.setZeroTransform().setChanged();
        wheel1.setZeroTransform().setChanged();
        wheel2.setZeroTransform().setChanged();
        pin1.setZeroTransform().setChanged();
        pin2.setZeroTransform().setChanged();
    }

    @Override
    public void updateLight(int packedLight) {
        secondaryShaft1.light(packedLight).setChanged();
        secondaryShaft2.light(packedLight).setChanged();
        middleShaft1.light(packedLight).setChanged();
        middleShaft2.light(packedLight).setChanged();
        frame.light(packedLight).setChanged();
        piston.light(packedLight).setChanged();
        wheel1.light(packedLight).setChanged();
        wheel2.light(packedLight).setChanged();
        pin1.light(packedLight).setChanged();
        pin2.light(packedLight).setChanged();
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        consumer.accept(secondaryShaft1);
        consumer.accept(secondaryShaft2);
        consumer.accept(middleShaft1);
        consumer.accept(middleShaft2);
        consumer.accept(frame);
        consumer.accept(piston);
        consumer.accept(wheel1);
        consumer.accept(wheel2);
        consumer.accept(pin1);
        consumer.accept(pin2);
    }

    @Override
    public void delete() {
        secondaryShaft1.delete();
        secondaryShaft2.delete();
        middleShaft1.delete();
        middleShaft2.delete();
        frame.delete();
        piston.delete();
        wheel1.delete();
        wheel2.delete();
        pin1.delete();
        pin2.delete();
    }
}