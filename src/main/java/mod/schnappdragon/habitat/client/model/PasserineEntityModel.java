package mod.schnappdragon.habitat.client.model;

import mod.schnappdragon.habitat.common.entity.animal.PasserineEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

public class PasserineEntityModel<T extends PasserineEntity> extends SinglePartEntityModel<T> {
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart rightWing;
	private final ModelPart leftWing;
	private final ModelPart rightFoot;
	private final ModelPart leftFoot;
	private final ModelPart tail;

	private float preenAnim;

	public PasserineEntityModel(ModelPart part) {
		this.root = part;
		this.head = part.getChild("head");
		this.body = part.getChild("body");
		this.tail = this.body.getChild("tail");
		this.rightWing = part.getChild("right_wing");
		this.leftWing = part.getChild("left_wing");
		this.rightFoot = part.getChild("right_foot");
		this.leftFoot = part.getChild("left_foot");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -3.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, 20.0F, -2.0F));
		head.addChild("crest", ModelPartBuilder.create().uv(22, 2).cuboid(0.0F, -6.0F, -2.0F, 0.0F, 5.0F, 5.0F), ModelTransform.NONE);
		head.addChild("beak", ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, -1.0F, -3.0F, 1.0F, 1.0F, 1.0F), ModelTransform.NONE);
		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(16, 0).cuboid(-2.0F, -1.5F, -2.0F, 4.0F, 3.0F, 4.0F), ModelTransform.pivot(0.0F, 21.5F, 0.0F));
		body.addChild("tail", ModelPartBuilder.create().uv(5, 8).cuboid(-2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 5.0F), ModelTransform.pivot(0.0F, 0.5F, 2.0F));
		modelPartData.addChild("right_wing", ModelPartBuilder.create().uv(0, 8).cuboid(-1.0F, -1.0F, -1.0F, 1.0F, 3.0F, 4.0F), ModelTransform.pivot(-2.0F, 21.0F, -1.0F));
		modelPartData.addChild("left_wing", ModelPartBuilder.create().uv(0, 8).cuboid(0.0F, -1.0F, -1.0F, 1.0F, 3.0F, 4.0F), ModelTransform.pivot(2.0F, 21.0F, -1.0F));
		modelPartData.addChild("right_foot", ModelPartBuilder.create().uv(12, 0).cuboid(-2.0F, 0.0F, -2.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, 23.0F, 1.0F));
		modelPartData.addChild("left_foot", ModelPartBuilder.create().uv(12, 0).mirrored().cuboid(0.0F, 0.0F, -2.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, 23.0F, 1.0F));
		return TexturedModelData.of(modelData, 32, 16);
	}

	public ModelPart getPart() {
		return this.root;
	}

	@Override
	public void animateModel(PasserineEntity passerine, float limbSwing, float limbSwingAmount, float partialTick) {
		this.head.pivotY = 20.0F;
		this.head.pivotZ = -2.0F;
		this.body.pivotY = 21.5F;
		this.rightWing.pivotY = 21.0F;
		this.leftWing.pivotY = 21.0F;
		this.rightFoot.pivotY = 23.0F;
		this.leftFoot.pivotY = 23.0F;

		this.body.pitch = 0.0F;
		this.rightWing.pitch = 0.0F;
		this.leftWing.pitch = 0.0F;
		this.rightWing.roll = 0.0F;
		this.leftWing.roll = 0.0F;
		this.rightFoot.pitch = 0.0F;
		this.leftFoot.pitch = 0.0F;

		switch (getState(passerine)) {
			case SLEEPING:
				this.head.pivotY = 21.0F;
				this.head.pivotZ = -1.5F;
				this.body.pivotY = 22.5F;
				this.rightWing.pivotY = 22.0F;
				this.leftWing.pivotY = 22.0F;
				this.head.pitch = 0.3491F;
				this.head.yaw = 2.094F;
				this.tail.pitch = 0.1745F;
				break;
			case FLYING:
				this.body.pitch = -0.3927F;
				this.rightWing.pitch = -0.5236F;
				this.leftWing.pitch = -0.5236F;
				this.rightFoot.pitch = -0.5236F;
				this.leftFoot.pitch = -0.5236F;
				this.rightFoot.yaw = 0.1571F;
				this.leftFoot.yaw = -0.1571F;
				this.tail.pitch = 0.1309F;
				break;
			case PREENING:
				this.preenAnim = (float) passerine.getRemainingPreeningTicks() - partialTick;
			case STANDING:
			default:
				this.body.pitch = -0.0873F;
				this.rightWing.pitch = -0.1963F;
				this.leftWing.pitch = -0.1963F;
				this.rightFoot.yaw = 0.1745F;
				this.leftFoot.yaw = -0.1745F;
				this.tail.pitch = 0.3927F;
		}
	}

	public void setAngles(PasserineEntity passerine, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		if (getState(passerine) == PasserineEntityModel.State.PREENING) {
			int remainingPreeningTicks = passerine.getRemainingPreeningTicks();

			if (remainingPreeningTicks >= 4 && remainingPreeningTicks <= 36) {
				float f = (this.preenAnim - 4) / 32.0F;
				this.head.pivotZ = -1.0F;
				this.head.pitch = (float) (0.1745F + 0.1745F * Math.sin(f * 57.3F));
				this.head.yaw = (float) (1.833F + 0.2793F * Math.sin(f * 38.2F));
				this.rightWing.pitch = -0.5236F;
				this.rightWing.roll = 1.396F;
			} else {
				float f = (remainingPreeningTicks < 4 ? this.preenAnim : 40.0F - this.preenAnim) / 4.0F;
				this.head.pivotZ = MathHelper.lerp(f, -2.0F, -1.0F);
				this.head.pitch = MathHelper.lerp(f, 0.0F, 0.1745F);
				this.head.yaw = MathHelper.lerp(f, 0.0F, 1.833F);
				this.rightWing.pitch = MathHelper.lerp(f, -0.1963F, -0.5236F);
				this.rightWing.roll = MathHelper.lerp(f, 0.0F, 1.396F);
			}
		}
		else if (!(getState(passerine) == PasserineEntityModel.State.SLEEPING)) {
			this.head.pitch = headPitch * ((float) Math.PI / 180F);
			this.head.yaw = netHeadYaw * ((float) Math.PI / 180F);

			if (getState(passerine) == PasserineEntityModel.State.FLYING) {
				float f = ageInTicks * 0.2F;
				this.head.pivotY = 20.0F + f;
				this.body.pivotY = 21.5F + f;
				this.rightWing.pivotY = 21.0F + f;
				this.leftWing.pivotY = 21.0F + f;
				this.rightFoot.pivotY = 23.0F + f;
				this.leftFoot.pivotY = 23.0F + f;

				this.tail.pitch += Math.cos(limbSwing * 0.6662F) * 0.35F * limbSwingAmount;

				this.rightWing.roll = 0.2618F + ageInTicks;
				this.leftWing.roll = -0.2618F - ageInTicks;
			} else {
				this.rightFoot.pitch += Math.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
				this.leftFoot.pitch += Math.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
			}
		}
	}

	private static PasserineEntityModel.State getState(PasserineEntity passerine) {
		if (passerine.isPreening())
			return PasserineEntityModel.State.PREENING;
		else if (passerine.isAsleep())
			return PasserineEntityModel.State.SLEEPING;
		else
			return passerine.isInAir() ? PasserineEntityModel.State.FLYING : PasserineEntityModel.State.STANDING;
	}

	public enum State {
		FLYING,
		STANDING,
		SLEEPING,
		PREENING
	}
}