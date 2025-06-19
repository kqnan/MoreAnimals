package szuc.kqn.moreanimals.nms;


import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.targeting.PathfinderTargetCondition;
import net.minecraft.world.entity.animal.EntityAnimal;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeItemStack;
import net.minecraft.world.level.IMaterial;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftSheep;
import org.bukkit.entity.Sheep;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class SheepTemptAIImpl extends PathfinderGoal implements SheepTemptAI {
    private static final PathfinderTargetCondition TEMP_TARGETING = PathfinderTargetCondition.forNonCombat().range((double)10.0F).ignoreLineOfSight();
    private final PathfinderTargetCondition targetingConditions;
    protected final EntityCreature mob;
    private final double speedModifier;
    private double px;
    private double py;
    private double pz;
    private double pRotX;
    private double pRotY;

    protected EntityHuman player;
    private int calmDown;
    private boolean isRunning;
    private final RecipeItemStack items;
    private final boolean canScare;

    public SheepTemptAIImpl(EntityCreature var0, double var1, RecipeItemStack var3, boolean var4) {
        this.mob = var0;
        this.speedModifier = var1;
        this.items = var3;
        this.canScare = var4;
        this.setFlags(EnumSet.of(Type.MOVE, Type.LOOK));
        this.targetingConditions = TEMP_TARGETING.copy().selector(this::shouldFollow);


    }

    public boolean canUse() {
        if (this.calmDown > 0) {
            --this.calmDown;
            return false;
        } else {
            this.player = this.mob.level().getNearestPlayer(this.targetingConditions, this.mob);
            return this.player != null;
        }
    }

    private boolean shouldFollow(EntityLiving var0) {
        return this.items.test(var0.getMainHandItem()) || this.items.test(var0.getOffhandItem());
    }

    public boolean canContinueToUse() {
        if (this.canScare()) {
            if (this.mob.distanceToSqr(this.player) < (double)36.0F) {
                if (this.player.distanceToSqr(this.px, this.py, this.pz) > 0.010000000000000002) {
                    return false;
                }

                if (Math.abs((double)this.player.getXRot() - this.pRotX) > (double)5.0F || Math.abs((double)this.player.getYRot() - this.pRotY) > (double)5.0F) {
                    return false;
                }
            } else {
                this.px = this.player.getX();
                this.py = this.player.getY();
                this.pz = this.player.getZ();
            }

            this.pRotX = (double)this.player.getXRot();
            this.pRotY = (double)this.player.getYRot();
        }

        return this.canUse();
    }

    protected boolean canScare() {
        return this.canScare;
    }

    public void start() {
        this.px = this.player.getX();
        this.py = this.player.getY();
        this.pz = this.player.getZ();
        this.isRunning = true;
    }

    public void stop() {
        this.player = null;
        this.mob.getNavigation().stop();
        this.calmDown = reducedTickDelay(100);
        this.isRunning = false;
    }

    public void tick() {
        this.mob.getLookControl().setLookAt(this.player, (float)(this.mob.getMaxHeadYRot() + 20), (float)this.mob.getMaxHeadXRot());
        if (this.mob.distanceToSqr(this.player) < (double)6.25F) {
            this.mob.getNavigation().stop();
        } else {
            this.mob.getNavigation().moveTo(this.player, this.speedModifier);
        }

    }

    public boolean isRunning() {
        return this.isRunning;
    }

    @Override
    public @NotNull Object createPathfinderGoal(@NotNull Sheep sheep) {

        return new SheepTemptAIImpl(((CraftSheep)sheep).getHandle(),1.1,RecipeItemStack.of(new IMaterial[]{Items.WHEAT}), false);
    }
}
