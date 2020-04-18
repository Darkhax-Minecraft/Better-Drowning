package net.darkhax.betterdrowning;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class Configuration {
    
    private final ForgeConfigSpec spec;
    
    public final FixMaxAir fixMaxAir;
    public final StrongerDrowning strongerDrowning;
    public final WaterFog waterFog;
    public final AirRefill airRefill;
    
    public Configuration() {
        
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        
        // General Configs
        builder.comment("General settings for the mod.");
        builder.push("general");
        
        this.fixMaxAir = new FixMaxAir(builder);
        this.strongerDrowning = new StrongerDrowning(builder);
        this.waterFog = new WaterFog(builder);
        this.airRefill = new AirRefill(builder);
        
        builder.pop();
        this.spec = builder.build();
    }
    
    public ForgeConfigSpec getSpec () {
        
        return this.spec;
    }
    
    static class FixMaxAir {
        
        private final BooleanValue enabled;
        
        public FixMaxAir(Builder builder) {
            
            builder.comment("Settings for the max air fix feature.");
            builder.push("max-air-fix");
            
            builder.comment("Should players have their air bubbles reset to the maximum if it is above the maximum?");
            this.enabled = builder.define("enabled", true);
            
            builder.pop();
        }
        
        public boolean isEnabled () {
            
            return this.enabled.get();
        }
    }
    
    static class StrongerDrowning {
        
        private final BooleanValue enabled;
        private final IntValue minDepth;
        private final DoubleValue damagePerBlock;
        
        public StrongerDrowning(Builder builder) {
            
            builder.comment("Settings for the stronger drowning feature.");
            builder.push("stronger-drowning");
            
            builder.comment("Should players take more drowning damage the deeper they are?");
            this.enabled = builder.define("enabled", true);
            
            builder.comment("The depth players can safely dive without this effect applying.");
            this.minDepth = builder.defineInRange("safe-depths", 10, 0, Integer.MAX_VALUE);
            
            builder.comment("The amount of damage to do per block below the safe depth.");
            this.damagePerBlock = builder.defineInRange("damage", 0.10d, 0d, Double.MAX_VALUE);
            
            builder.pop();
        }
        
        public boolean isEnabled () {
            
            return this.enabled.get();
        }
        
        public int getSafeDepth () {
            
            return this.minDepth.get();
        }
        
        public double getDamagePerBlock () {
            
            return this.damagePerBlock.get();
        }
    }
    
    static class WaterFog {
        
        private final BooleanValue enabled;
        private final DoubleValue modifier;
        
        public WaterFog(Builder builder) {
            
            builder.comment("Settings for the water fog feature.");
            builder.push("water-fog");
            
            builder.comment("Should water fog be modified?");
            this.enabled = builder.define("enabled", true);
            
            builder.comment("The amount to modify the water fog by. 0 is no fog, 1 is vanilla fog.");
            this.modifier = builder.defineInRange("damage", 0.1d, 0d, 1d);
            
            builder.pop();
        }
        
        public boolean isEnabled () {
            
            return this.enabled.get();
        }
        
        public double getModifier () {
            
            return this.modifier.get();
        }
    }
    
    static class AirRefill {
        
        private final BooleanValue enabled;
        
        public AirRefill(Builder builder) {
            
            builder.comment("Settings for the air refill feature.");
            builder.push("air-refill");
            
            builder.comment("Should air be instantly refilled when out of water like in 1.13 and below?");
            this.enabled = builder.define("enabled", false);
            
            builder.pop();
        }
        
        public boolean isEnabled () {
            
            return this.enabled.get();
        }
    }
}