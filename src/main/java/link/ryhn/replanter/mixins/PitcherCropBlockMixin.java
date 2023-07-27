package link.ryhn.replanter.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.block.*;

@Mixin(PitcherCropBlock.class)
public interface PitcherCropBlockMixin {
	@Invoker("isFullyGrown")
	public boolean invokeIsFullyGrown(BlockState state);

	@Invoker("isLowerHalf")
	public static boolean invokeIsLowerHalf(BlockState state) {
		throw new AssertionError();
	}
}
