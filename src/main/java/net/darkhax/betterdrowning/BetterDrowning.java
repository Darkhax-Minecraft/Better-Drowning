package net.darkhax.betterdrowning;

import net.darkhax.bookshelf.util.WorldUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;

@Mod("betterdrowning")
public class BetterDrowning {
    
    private final Configuration config = new Configuration();
    
    public BetterDrowning() {
        
        ModLoadingContext.get().registerConfig(Type.COMMON, this.config.getSpec());
        
        MinecraftForge.EVENT_BUS.addListener(this::onEntityTick);
        MinecraftForge.EVENT_BUS.addListener(this::onLivingHurt);
        
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.addListener(this::calculateFogDensity));
    }
    
    private void calculateFogDensity (FogDensity event) {
        
        // Allows water fog to be reduced or disabled.
        if (this.config.waterFog.isEnabled() && event.getInfo().getFluidState().isTagged(FluidTags.WATER)) {
            
            event.setDensity((float) (event.getDensity() * this.config.waterFog.getModifier()));
            event.setCanceled(true);
        }
    }
    
    private void onEntityTick (LivingUpdateEvent event) {
        
        final LivingEntity living = event.getEntityLiving();
        final int maxAir = living.getMaxAir();
        
        // Fixes air being above the player's max air value.
        if (this.config.fixMaxAir.isEnabled() && living.getAir() > maxAir) {
            
            living.setAir(maxAir);
        }
        
        // Refills air instantly when entities are out of the water.
        if (this.config.airRefill.isEnabled() && living.getAir() < maxAir && !living.isInWaterOrBubbleColumn()) {
            
            living.setAir(maxAir);
        }
    }
    
    private void onLivingHurt (LivingHurtEvent event) {
        
        // Increases drowning damage the lower the player is.
        if (this.config.strongerDrowning.isEnabled()) {
            
            final LivingEntity living = event.getEntityLiving();
            
            if (event.getSource() == DamageSource.DROWN && living.isInWaterOrBubbleColumn() && living.getAir() <= 0) {
                
                final int depth = Math.max(WorldUtils.getWaterDepth(living, false) - this.config.strongerDrowning.getSafeDepth(), 0);
                
                if (depth > 0) {
                    
                    event.setAmount((float) (event.getAmount() + depth * this.config.strongerDrowning.getDamagePerBlock()));
                }
            }
        }
    }
}