package andrews.pandoras_creatures.entities;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;

import andrews.pandoras_creatures.entities.bases.AnimatedCreatureEntity;
import andrews.pandoras_creatures.registry.PCEntities;
import andrews.pandoras_creatures.registry.PCItems;
import andrews.pandoras_creatures.util.animation.Animation;
import andrews.pandoras_creatures.util.network.NetworkUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.GameRules;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.registries.ForgeRegistries;

public class BufflonEntity extends AnimatedCreatureEntity
{
	private static Biome[] biomes = new Biome[] {Biomes.TAIGA, Biomes.TAIGA_HILLS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA_HILLS};
	
	//Stores the Bufflon type
	private static final DataParameter<Integer> BUFFLON_TYPE = EntityDataManager.createKey(BufflonEntity.class, DataSerializers.VARINT);
	//Stores whether or not the Bufflon is tamed and the UUID of the owner
	private static final DataParameter<Byte> TAMED = EntityDataManager.createKey(BufflonEntity.class, DataSerializers.BYTE);
	private static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.createKey(BufflonEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	
	private int thinkTime;
	private int feedingCooldown;
	
	public static final Animation THROW_ANIMATION = new Animation(12);
	public static final Animation TEST_ANIMATION = new Animation(100);
	
    public BufflonEntity(EntityType<? extends BufflonEntity> type, World worldIn)
    {
        super(type, worldIn);
    }

    public BufflonEntity(World world, double posX, double posY, double posZ)
    {
        this(PCEntities.BUFFLON, world);
        this.setPosition(posX, posY, posZ);
    }
    
    @Override
    protected void registerGoals()
    {
    	
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0D);
    }
    
    @Override
	protected void registerData()
    {
		super.registerData();
		this.dataManager.register(BUFFLON_TYPE, 0);
		this.dataManager.register(TAMED, (byte)0);
		this.dataManager.register(OWNER_UNIQUE_ID, Optional.empty());
    }
    
    @Override
	public void writeAdditional(CompoundNBT compound)
    {
		super.writeAdditional(compound);
		//The Bufflon Variant
		compound.putInt("BufflonType", this.getBufflonType());
		//The Owner of the Bufflon
		if(this.getOwnerId() == null)
		{
			compound.putString("OwnerUUID", "");
	   	}
		else
		{
			compound.putString("OwnerUUID", this.getOwnerId().toString());
	   	}
	}
	
	@Override
	public void readAdditional(CompoundNBT compound)
	{
		super.readAdditional(compound);
		//The Bufflon Variant
		this.setBufflonType(compound.getInt("BufflonType"));
		//The Owner of the Bufflon
		String s;
	    if(compound.contains("OwnerUUID", NBT.TAG_STRING))
	    {
	    	s = compound.getString("OwnerUUID");
	  	}
	    else
	    {
	    	String s1 = compound.getString("Owner");
	    	s = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), s1);
		}
	    //Attempts to set the Bufflon as tamed 
		if(!s.isEmpty())
		{
			try
			{
	        	this.setOwnerId(UUID.fromString(s));
	            this.setTamed(true);
	        }
			catch(Throwable var4)
			{
				this.setTamed(false);
	        }
		}
	}
	
	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficulty, SpawnReason reason, ILivingEntityData spawnData, CompoundNBT dataTag)
	{
		spawnData = super.onInitialSpawn(world, difficulty, reason, spawnData, dataTag);
		Random rand = new Random();
		int type = rand.nextInt(2) + 1;
		if(dataTag != null && dataTag.contains("BufflonType", 3))
		{
			this.setBufflonType(dataTag.getInt("BufflonType"));
			return spawnData;
		}
		this.setBufflonType(type);
		return spawnData;
	}
	
	/**
	 * Makes sure to only allow leashing to the owner of the Bufflon
	 */
	@Override
	public boolean canBeLeashedTo(PlayerEntity player)
	{
		return this.isTamed() && this.isOwner(player);
	}
    
    @Override
    protected boolean processInteract(PlayerEntity player, Hand hand)
    {
    	ItemStack itemstack = player.getHeldItem(hand);
        if(itemstack.getItem() == Items.STICK)
        {
        	if(this.isAnimationPlaying(BLANK_ANIMATION) && !this.getEntityWorld().isRemote())
        	{
        		System.out.println("Interacted");
        		NetworkUtil.setPlayingAnimationMessage(this, THROW_ANIMATION);
        	}
        	return true;
        }
        else if(itemstack.getItem() == Items.BLAZE_ROD)
        {
        	if(world.isRemote)
        	{
        		player.sendMessage(new StringTextComponent("Is entity tamed: " + this.isTamed()));
        		if(this.isTamed())
        		{
        			player.sendMessage(new StringTextComponent("Entity owner is: " + this.getOwner().getName().getString()));
        		}
        	}
        	return true;
        }
        else if(itemstack.getItem() == PCItems.HERB_BUNDLE)
        {
        	if(!this.world.isRemote && !this.isTamed())
        	{
        		if(feedingCooldown <= 0)
        		{
        			feedingCooldown = 10;
		    		if(!player.abilities.isCreativeMode)
		    		{
		                itemstack.shrink(1);
		            }
		    		
		    		if(this.rand.nextInt(3) == 0)
		    		{
		    			mountTo(player);
		    		}
		    		else
		    		{
		    			this.playTameEffect(false);
		    			this.world.setEntityState(this, (byte)6);
		            }
        		}
        	}
        	return true;
        }
        else
        {
        	return super.processInteract(player, hand);
        }
    }
    
    @Override
    public void livingTick()
    {
    	super.livingTick();
    	if(feedingCooldown > 0)
    	{
    		feedingCooldown--;
    	}
    	
    	if(!world.isRemote && !this.isTamed() && this.isBeingRidden())
    	{
    		if(thinkTime > 0)
    		{
    			thinkTime--;
    		}
    		else
    		{
    			if(this.rand.nextInt(4) == 0)
        		{
        			this.setTamedBy((PlayerEntity) this.getPassengers().get(0));
        			this.navigator.clearPath();
        			this.setAttackTarget((LivingEntity)null);
        			this.playTameEffect(true);
        			this.world.setEntityState(this, (byte)7);
        		}
        		else
        		{
        			//Removes the Passengers
        			for(int i = this.getPassengers().size() - 1; i >= 0; --i)
        			{
        				Entity entity = this.getPassengers().get(i);
        				entity.stopRiding();
        				entity.setMotion(entity.getMotion().add((rand.nextInt(3) - 1) * 0.3D, (double)1.2F, (rand.nextInt(3) - 1) * 0.3D));
        				entity.velocityChanged = true;
        				NetworkUtil.setPlayingAnimationMessage(this, THROW_ANIMATION);
        		   	}
        			this.playTameEffect(false);
        			this.world.setEntityState(this, (byte)6);
                }
    		}
    	}
    }
    
    @Override
	public Animation[] getAnimations()
    {
		return new Animation[] {TEST_ANIMATION, THROW_ANIMATION};
	}
    
    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn)
    {
        return sizeIn.height * 0.6F;
    }
    
    /**
     * Gets called when passengers get updated
     */
    @Override
    public void updatePassenger(Entity passenger)
    {
    	super.updatePassenger(passenger);
    	if(this.isPassenger(passenger))//Makes sure the passenger entity is actually an passenger
    	{
    		float offsetX = 0F;
    		float offsetY = 0F;
//    		float f1 = (float)((this.isAlive() ? (double)0.01F : this.getMountedYOffset()) + passenger.getYOffset());
            if(this.getPassengers().size() > 0)
            {
            	int i = this.getPassengers().indexOf(passenger);
            	if(i == 0)
            	{
            		offsetX = 1.1F;
            		offsetY = 2.3F;
            	}
            	else if(i == 1)
            	{
            		//TODO Change values to make fit for other people
            		offsetX = 0.5F;
            		offsetY = 1.4F;
            	}
            	else
            	{
            		//TODO Change values to make fit for other people
            		offsetX = 0.5F;
            		offsetY = 1.4F;
            	}
            }

            Vec3d vec3d = (new Vec3d((double)offsetX, 0.0D, 0.0D)).rotateYaw(-this.rotationYaw * ((float)Math.PI / 180F) - ((float)Math.PI / 2F));
            passenger.setPosition(this.posX + vec3d.x, this.posY + (double)offsetY, this.posZ + vec3d.z);
         }
    }
    
    /**
     * Checks whether or not this entity can despawn
     * @param distanceToClosestPlayer - The distance to the closest player
     * @return - Returns if this entity can despawn
     */
    @Override
    public boolean canDespawn(double distanceToClosestPlayer)
    {
    	return !this.isTamed() && !this.hasCustomName();
    }

    /**
     * Prevents this entity from despawning
     * @return - True if this entity is prevented from despawning
     */
    @Override
    public boolean preventDespawn()
    {
    	return this.isTamed();
    } 
    
    /**
     * Play the taming effect, will either be hearts or smoke depending on status
     */
    protected void playTameEffect(boolean play)
    {
    	IParticleData iparticledata = ParticleTypes.HEART;
    	if(!play)
    	{
    		iparticledata = ParticleTypes.SMOKE;
    	}

    	for(int i = 0; i < 7; ++i)
    	{
    		double d0 = this.rand.nextGaussian() * 0.02D;
    		double d1 = this.rand.nextGaussian() * 0.02D;
    		double d2 = this.rand.nextGaussian() * 0.02D;
    		this.world.addParticle(iparticledata, this.posX + (double)(this.rand.nextFloat() * this.getWidth() * 2.0F) - (double)this.getWidth(), this.posY + 0.5D + (double)(this.rand.nextFloat() * this.getHeight()), this.posZ + (double)(this.rand.nextFloat() * this.getWidth() * 2.0F) - (double)this.getWidth(), d0, d1, d2);
    	}
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id)
    {
    	if(id == 7)
    	{
    		this.playTameEffect(true);
    	}
    	else if(id == 6)
    	{
    		this.playTameEffect(false);
    	}
    	else
    	{
    		super.handleStatusUpdate(id);
    	}
	}
    
    private void mountTo(PlayerEntity player)
    {
    	if(!this.world.isRemote)
        {
        	player.rotationYaw = this.rotationYaw;
        	player.rotationPitch = this.rotationPitch;
        	player.startRiding(this);
        	this.thinkTime = 40;
        }
    }
    
    /**
     * @return - Wether or not the Bufflon it is tamed
     */
    public boolean isTamed()
    {
    	return (this.dataManager.get(TAMED) & 4) != 0;
	}

    /**
     * Set wether or not this Bufflon entity should be tamed
     * @param tamed - The tame status the entity should be in afterwards
     */
 	public void setTamed(boolean tamed)
 	{
        byte b0 = this.dataManager.get(TAMED);
        if(tamed)
        {
        	this.dataManager.set(TAMED, (byte)(b0 | 4));
        }
        else
        {
        	this.dataManager.set(TAMED, (byte)(b0 & -5));
        }
//      this.setupTamedAI();
 	}
 	
 	/**
 	 * @return - The UUID of the owning entity, if entity is null this will return null
 	 */
 	@Nullable
    public UUID getOwnerId()
 	{
       return this.dataManager.get(OWNER_UNIQUE_ID).orElse((UUID)null);
    }

 	/**
 	 * Used to set the owner UUID of the entity
 	 * @param uuid - The UUID the owning entity has
 	 */
    public void setOwnerId(@Nullable UUID uuid)
    {
       this.dataManager.set(OWNER_UNIQUE_ID, Optional.ofNullable(uuid));
    }

    /**
     * Handles the taming and sets the given Player as the owner of the Entity
     * @param player - The Player that will be the owner of the tamed Entity
     */
    public void setTamedBy(PlayerEntity player)
    {
    	this.setTamed(true);
    	this.setOwnerId(player.getUniqueID());
//    	if(player instanceof ServerPlayerEntity)
//    	{
//    		CriteriaTriggers.TAME_ANIMAL.trigger((ServerPlayerEntity)player, this);
//    	}
    }

    /**
     * @return - The Entity that owns the Bufflon
     */
    @Nullable
    public LivingEntity getOwner()
    {
    	try
    	{
    		UUID uuid = this.getOwnerId();
    		return uuid == null ? null : this.world.getPlayerByUuid(uuid);
    	}
    	catch(IllegalArgumentException var2)
    	{
    		return null;
    	}
    }
    
    /**
     * Wether or not this entity can attack
     */
    public boolean canAttack(LivingEntity target)
    {
    	return this.isOwner(target) ? false : super.canAttack(target);
    }

    /**
     * @param entityIn - The entity that will be checked as a possible owner
     * @return - Wether or not the given entity is the owner of this Entity
     */
   	public boolean isOwner(LivingEntity entityIn)
   	{
   		return entityIn == this.getOwner();
   	}
   	
   	/**
   	 * @return - The team of the Entity
   	 */
   	public Team getTeam()
   	{
   		if(this.isTamed())
   		{
   			LivingEntity livingentity = this.getOwner();
   			if(livingentity != null)
   			{
   				return livingentity.getTeam();
   			}
   		}
        return super.getTeam();
   	}

   	/**
   	 * Returns whether this Entity is on the same team as the given Entity.
   	 */
   	public boolean isOnSameTeam(Entity entityIn)
   	{
   		if(this.isTamed())
   		{
   			LivingEntity livingentity = this.getOwner();
           if(entityIn == livingentity)
           {
        	   return true;
           }

           if(livingentity != null)
           {
        	   return livingentity.isOnSameTeam(entityIn);
           }
   		}
        return super.isOnSameTeam(entityIn);
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	public void onDeath(DamageSource cause)
	{
		if(!this.world.isRemote && this.world.getGameRules().getBoolean(GameRules.SHOW_DEATH_MESSAGES) && this.getOwner() instanceof ServerPlayerEntity)
		{
			this.getOwner().sendMessage(this.getCombatTracker().getDeathMessage());
        }
        super.onDeath(cause);
	}
    
    /**
     * Used to get the current Bufflon type, if there is none a random type will be generated
     * @return - The Bufflon type Id
     */
    public int getBufflonType()
    {
    	if(this.dataManager.get(BUFFLON_TYPE) == 0)
    	{
    		Random rand = new Random();
    		this.dataManager.set(BUFFLON_TYPE, rand.nextInt(2) + 1);
    		return this.dataManager.get(BUFFLON_TYPE);
    	}
    	else
    	{
    		return this.dataManager.get(BUFFLON_TYPE);
    	}
	}
	
    /**
     * Used to set the Bufflon type, different bufflon types use different textures
     * @param typeId - the Bufflon type the entity should become
     */
	public void setBufflonType(int typeId)
	{
		this.dataManager.set(BUFFLON_TYPE, typeId);
	}
    
    public static void addSpawn()
    {
		ForgeRegistries.BIOMES.getValues().stream().forEach(BufflonEntity::processSpawning);
	}
	
    private static void processSpawning(Biome biome)
    {
		if(Arrays.asList(biomes).contains(biome))
		{
			biome.addSpawn(EntityClassification.AMBIENT, new Biome.SpawnListEntry(PCEntities.BUFFLON, 40, 2, 5));
		}
    }
}
