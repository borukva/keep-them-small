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
        if (ModConfig.shouldStayBaby(self)) {
            self.setBreedingAge(-24000);
        }
    }
}