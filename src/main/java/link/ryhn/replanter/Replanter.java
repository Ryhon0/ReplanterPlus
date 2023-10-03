package link.ryhn.replanter;

import link.ryhn.replanter.mixins.PitcherCropBlockMixin;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
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
			return onUseBlock(player, world, hand, hitResult);
		});
	}

	ActionResult onUseBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
		boolean switchSeed = true;

		if (hitResult.getType() == HitResult.Type.BLOCK) {
			BlockPos pos = hitResult.getBlockPos();
			ItemStack stack = null;

			ItemStack mainStack = player.getMainHandStack();
			ItemStack offhandStack = player.getOffHandStack();

			if(mainStack.isEmpty())
				stack = offhandStack;
			
			

			if (hand == Hand.MAIN_HAND && !mainStack.isEmpty()) {
				stack = mainStack;
			} else if (hand == Hand.OFF_HAND && !offhandStack.isEmpty()) {
				stack = offhandStack;
			}

			if(stack == null)
				return ActionResult.PASS;

			if(switchSeed)
			{

			}
			else
			{

			}
		}

		return ActionResult.PASS;
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
				if ((wartBlock.getOutlineShape(state, world, pos, ShapeContext.absent()).getBoundingBox().getLengthY()
						* 16) == 14) {
					im.attackBlock(pos, Direction.DOWN);
				}
			} else if (block instanceof CocoaBlock cocoaBlock) {
				if ((cocoaBlock.getOutlineShape(state, world, pos, ShapeContext.absent()).getBoundingBox()
						.getLengthY() == 0.5625)) {
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

	Item getSeed(Block block) {
		if (block instanceof CropBlock cb) {
			return cb.asItem();
		} else if (block instanceof NetherWartBlock) {
			return Items.NETHER_WART;
		} else if (block instanceof PitcherCropBlock) {
			return Items.PITCHER_POD;
		} else if (block == Blocks.TORCHFLOWER) {
			return Items.TORCHFLOWER_SEEDS;
		}

		return null;
	}
}