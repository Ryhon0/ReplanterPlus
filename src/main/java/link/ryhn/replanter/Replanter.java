package link.ryhn.replanter;

import link.ryhn.replanter.mixins.PitcherCropBlockMixin;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class Replanter implements ModInitializer {
	private static final MinecraftClient mc = MinecraftClient.getInstance();

	@Override
	public void onInitialize() {
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			if (hitResult.getType() == HitResult.Type.BLOCK) {
				BlockPos pos = hitResult.getBlockPos();
				ItemStack heldStack = player.getMainHandStack();
				ItemStack offhandStack = player.getOffHandStack();

				if (hand == Hand.MAIN_HAND && !heldStack.isEmpty()) {
					this.replaceCrop(player, hand, heldStack, pos, world);
				} else if (hand == Hand.OFF_HAND && !offhandStack.isEmpty()) {
					this.replaceCrop(player, hand, offhandStack, pos, world);
				}
			}
			return ActionResult.PASS;
		});
	}

	private void replaceCrop(PlayerEntity player, Hand hand, ItemStack stack, BlockPos pos, World world) {
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		ClientPlayerInteractionManager im = mc.interactionManager;
		if (stack.getItem() instanceof BlockItem) {
			Block itemBlock = ((BlockItem) stack.getItem()).getBlock();

			if (block instanceof CropBlock) {

				if (itemBlock instanceof CropBlock) {
					if (((CropBlock) block).isMature(state)) {
						im.attackBlock(pos, Direction.DOWN);
					}
				}

				// Exception for nether warts
				// If nether wart block outline shape y value is max size then the block is
			} else if (block instanceof NetherWartBlock wartBlock && block == itemBlock) {
				// fully grown.
				if ((wartBlock.getOutlineShape(state, world, pos, ShapeContext.absent()).getBoundingBox().getYLength()
						* 16) == 14) {
					im.attackBlock(pos, Direction.DOWN);
				}
			} else if (block instanceof CocoaBlock cocoaBlock) {
				if ((cocoaBlock.getOutlineShape(state, world, pos, ShapeContext.absent()).getBoundingBox()
						.getYLength() == 0.5625)) {
					im.attackBlock(pos, world.getBlockState(pos).get(CocoaBlock.FACING));
				}
			} else if (block instanceof PitcherCropBlock pitcherCropBlock) {
				if (PitcherCropBlockMixin.invokeIsLowerHalf(state))
					if (((PitcherCropBlockMixin) pitcherCropBlock).invokeIsFullyGrown(state))
						im.attackBlock(pos, Direction.DOWN);
			} else if (block == Blocks.TORCHFLOWER) {
				im.attackBlock(pos, Direction.DOWN);
			}
		}
	}
}