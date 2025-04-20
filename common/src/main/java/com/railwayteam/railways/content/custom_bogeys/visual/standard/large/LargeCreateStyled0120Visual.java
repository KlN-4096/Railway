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

public class LargeCreateStyled0120Visual implements BogeyVisual {
    private final TransformedInstance[] shafts; // 横向轴
    private final TransformedInstance[] middleShafts; // 纵向轴
    private final TransformedInstance frame;
    private final TransformedInstance piston;
    private final TransformedInstance[] fullBlindWheels;
    private final TransformedInstance[] semiBlindWheels;
    private final TransformedInstance[] wheels;
    private final TransformedInstance[] pins;

    public LargeCreateStyled0120Visual(VisualizationContext ctx, float partialTick, boolean inContraption) {
        // 创建横向轴实例
        var shaftInstancer = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.block(AllBlocks.SHAFT.getDefaultState()
                        .setValue(ShaftBlock.AXIS, Direction.Axis.X)));
        shafts = new TransformedInstance[2];
        for (int i = 0; i < 2; i++) {
            shafts[i] = shaftInstancer.createInstance();
        }

        // 创建纵向轴实例
        var middleShaftInstancer = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.block(AllBlocks.SHAFT.getDefaultState()
                        .setValue(ShaftBlock.AXIS, Direction.Axis.Z)));
        middleShafts = new TransformedInstance[6]; // -3, -2, -1, 1, 2, 3
        for (int i = 0; i < 6; i++) {
            middleShafts[i] = middleShaftInstancer.createInstance();
        }

        // 创建框架和活塞实例
        frame = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(LARGE_CREATE_STYLED_0_12_0_FRAME))
                .createInstance();
        piston = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(LARGE_CREATE_STYLED_0_12_0_PISTON))
                .createInstance();

        // 创建全遮蔽轮实例
        var fullBlindWheelInstancer = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(LC_STYLE_FULL_BLIND_WHEELS));
        fullBlindWheels = new TransformedInstance[2];
        for (int i = 0; i < 2; i++) {
            fullBlindWheels[i] = fullBlindWheelInstancer.createInstance();
        }

        // 创建半遮蔽轮实例
        var semiBlindWheelInstancer = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(LC_STYLE_SEMI_BLIND_WHEELS));
        semiBlindWheels = new TransformedInstance[2];
        for (int i = 0; i < 2; i++) {
            semiBlindWheels[i] = semiBlindWheelInstancer.createInstance();
        }

        // 创建车轮实例
        var wheelInstancer = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(AllPartialModels.LARGE_BOGEY_WHEELS));
        wheels = new TransformedInstance[2];
        for (int i = 0; i < 2; i++) {
            wheels[i] = wheelInstancer.createInstance();
        }

        // 创建销钉实例
        var pinInstancer = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(AllPartialModels.BOGEY_PIN));
        pins = new TransformedInstance[6]; // -3, -2, -1, 0, 1, 2
        for (int i = 0; i < 6; i++) {
            pins[i] = pinInstancer.createInstance();
        }
    }

    @Override
    public void update(CompoundTag bogeyData, float wheelAngle, PoseStack poseStack) {
        // 更新横向轴
        for (int i = 0; i < 2; i++) {
            int side = i == 0 ? 1 : -1;
            shafts[i].setTransform(poseStack)
                    .translate(-.5, .25, -.5f + side * 5.364)
                    .center()
                    .rotateXDegrees(wheelAngle)
                    .uncenter()
                    .setChanged();
        }

        // 更新纵向轴
        int index = 0;
        for (int side = -3; side < 4; side++) {
            if (side == 0) continue;
            middleShafts[index].setTransform(poseStack)
                    .translate(-.5f, .25f, -.5f + side * -1.7)
                    .center()
                    .rotateZDegrees(wheelAngle)
                    .uncenter()
                    .setChanged();
            index++;
        }

        // 更新框架
        frame.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .setChanged();

        // 更新活塞
        piston.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .translate(0, 0, 1 / 4f * Math.sin(AngleHelper.rad(wheelAngle)))
                .setChanged();

        // 更新全遮蔽轮、半遮蔽轮和正常轮
        for (int i = 0; i < 2; i++) {
            int side = i == 0 ? 1 : -1;

            // 全遮蔽轮
            fullBlindWheels[i].setTransform(poseStack)
                    .scale(1 - 1 / 512f)
                    .translate(0, 1, side * .8733)
                    .rotateXDegrees(wheelAngle)
                    .translate(0, -1, 0)
                    .setChanged();

            // 半遮蔽轮
            semiBlindWheels[i].setTransform(poseStack)
                    .scale(1 - 1 / 512f)
                    .translate(0, 1, side * 2.62)
                    .rotateXDegrees(wheelAngle)
                    .translate(0, -1, 0)
                    .setChanged();

            // 正常轮
            wheels[i].setTransform(poseStack)
                    .scale(1 - 1 / 512f)
                    .translate(0, 1, side * 4.3665)
                    .rotateXDegrees(wheelAngle)
                    .setChanged();
        }

        // 更新销钉
        for (int i = 0; i < 6; i++) {
            int side = i - 3; // -3, -2, -1, 0, 1, 2
            pins[i].setTransform(poseStack)
                    .scale(1 - 1 / 512f)
                    .translate(0, 1, .8733f + side * 1.74657)
                    .rotateXDegrees(wheelAngle)
                    .translate(0, 1 / 4f, 0)
                    .rotateXDegrees(-wheelAngle)
                    .setChanged();
        }
    }

    @Override
    public void hide() {
        for (TransformedInstance shaft : shafts) {
            shaft.setZeroTransform().setChanged();
        }
        for (TransformedInstance middleShaft : middleShafts) {
            middleShaft.setZeroTransform().setChanged();
        }
        frame.setZeroTransform().setChanged();
        piston.setZeroTransform().setChanged();
        for (TransformedInstance fullBlindWheel : fullBlindWheels) {
            fullBlindWheel.setZeroTransform().setChanged();
        }
        for (TransformedInstance semiBlindWheel : semiBlindWheels) {
            semiBlindWheel.setZeroTransform().setChanged();
        }
        for (TransformedInstance wheel : wheels) {
            wheel.setZeroTransform().setChanged();
        }
        for (TransformedInstance pin : pins) {
            pin.setZeroTransform().setChanged();
        }
    }

    @Override
    public void updateLight(int packedLight) {
        for (TransformedInstance shaft : shafts) {
            shaft.light(packedLight).setChanged();
        }
        for (TransformedInstance middleShaft : middleShafts) {
            middleShaft.light(packedLight).setChanged();
        }
        frame.light(packedLight).setChanged();
        piston.light(packedLight).setChanged();
        for (TransformedInstance fullBlindWheel : fullBlindWheels) {
            fullBlindWheel.light(packedLight).setChanged();
        }
        for (TransformedInstance semiBlindWheel : semiBlindWheels) {
            semiBlindWheel.light(packedLight).setChanged();
        }
        for (TransformedInstance wheel : wheels) {
            wheel.light(packedLight).setChanged();
        }
        for (TransformedInstance pin : pins) {
            pin.light(packedLight).setChanged();
        }
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        for (TransformedInstance shaft : shafts) {
            consumer.accept(shaft);
        }
        for (TransformedInstance middleShaft : middleShafts) {
            consumer.accept(middleShaft);
        }
        consumer.accept(frame);
        consumer.accept(piston);
        for (TransformedInstance fullBlindWheel : fullBlindWheels) {
            consumer.accept(fullBlindWheel);
        }
        for (TransformedInstance semiBlindWheel : semiBlindWheels) {
            consumer.accept(semiBlindWheel);
        }
        for (TransformedInstance wheel : wheels) {
            consumer.accept(wheel);
        }
        for (TransformedInstance pin : pins) {
            consumer.accept(pin);
        }
    }

    @Override
    public void delete() {
        for (TransformedInstance shaft : shafts) {
            shaft.delete();
        }
        for (TransformedInstance middleShaft : middleShafts) {
            middleShaft.delete();
        }
        frame.delete();
        piston.delete();
        for (TransformedInstance fullBlindWheel : fullBlindWheels) {
            fullBlindWheel.delete();
        }
        for (TransformedInstance semiBlindWheel : semiBlindWheels) {
            semiBlindWheel.delete();
        }
        for (TransformedInstance wheel : wheels) {
            wheel.delete();
        }
        for (TransformedInstance pin : pins) {
            pin.delete();
        }
    }
}