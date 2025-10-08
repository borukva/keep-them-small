package com.tobzi.keepthemsmall.mixin;

import com.tobzi.keepthemsmall.config.ModConfig;
import net.minecraft.entity.passive.PassiveEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PassiveEntity.class)
public class PassiveEntityMixin {

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void onTickMovement(CallbackInfo ci) {
        PassiveEntity self = (PassiveEntity) (Object) this;

        // Check if this entity should be kept as a baby using our config logic.
        if (ModConfig.shouldStayBaby(self)) {
            // A baby's age is a negative number that counts up to 0.
            // By constantly setting it to its minimum value, we ensure it never reaches 0.
            self.setBreedingAge(-24000);
        }
    }
}