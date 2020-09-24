package andrews.pandoras_creatures.world.structures.end_prison;

public class EndPrisonStructure //extends Structure<NoFeatureConfig> //TODO
{
//	public EndPrisonStructure(Codec<NoFeatureConfig> codec)
//	{
//		super(codec);
//	}
//	
//	@Override
//	public String getStructureName()
//	{
//		return this.getRegistryName().toString();
//	}
//	
//	@Override
//	public Structure.IStartFactory<NoFeatureConfig> getStartFactory()
//	{
//		return EndPrisonStructure.Start::new;
//	}
//	
//	@Nullable
//    @Override
//    public BlockPos func_236388_a_(IWorldReader worldReader, StructureManager structureManager, BlockPos blockPos, int p_236388_4_, boolean p_236388_5_, long p_236388_6_, StructureSeparationSettings separationSettings)
//	{
//        return super.func_236388_a_(worldReader, structureManager, blockPos, p_236388_4_, p_236388_5_, p_236388_6_, new StructureSeparationSettings(32, getBiomeFeatureSeparation(), getSeedModifier()));//TODO make sure the rarity is ok
//    }
//	
//	@Override
//    protected boolean func_230363_a_(ChunkGenerator chunkGen, BiomeProvider biomeProvider, long p_230363_3_, SharedSeedRandom sharedSeedRandom, int chunkPosX, int chunkPosZ, Biome biome, ChunkPos chunkPos, NoFeatureConfig p_230363_10_)
//	{
//		return true; //TODO
//    }
//
////	@Override TODO got removed?
////	public int getSize()
////	{
////		return 8;
////	}
//
//	private int getSeedModifier()
//	{
//		return 0;
//	}
//	
////	@Override
////	public boolean func_225558_a_(BiomeManager biomeManager, ChunkGenerator<?> chunkGen, Random rand, int chunkPosX, int chunkPosZ, Biome biome)
////	{
////		ChunkPos chunkpos = this.getStartPositionForPosition(chunkGen, rand, chunkPosX, chunkPosZ, 0, 0);
////		if(chunkPosX == chunkpos.x && chunkPosZ == chunkpos.z)
////		{
////			if(chunkGen.hasStructure(biome, this))
////			{
////				return true;
////			}
////		}
////		return false;
////	}
//	
////	@Override
////	protected int getBiomeFeatureDistance(ChunkGenerator<?> generator)
////	{
////		return 32;
////	}
//
//	private int getBiomeFeatureSeparation()
//	{
//		return 16;
//	}
//
//	public static class Start extends StructureStart<NoFeatureConfig>
//	{
//		public Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ, MutableBoundingBox bounds, int references, long seed)
//		{
//			super(structure, chunkX, chunkZ, bounds, references, seed);
//		}
//
//		@Override
//		public void func_230364_a_(ChunkGenerator generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config)
//		{
//			int x = chunkX * 16;
//			int z = chunkZ * 16;
//			BlockPos blockpos1 = new BlockPos(x, 120, z);
//			Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];
//			this.components.add(new EndPrisonPieces.Piece(templateManagerIn, blockpos1, rotation, EndPrisonPiece.MAIN));
//			this.components.add(new EndPrisonPieces.Piece(templateManagerIn, getFrontBlockPos(x, z, rotation), rotation, EndPrisonPiece.FRONT));
//			this.components.add(new EndPrisonPieces.Piece(templateManagerIn, getBackBlockPos(x, z, rotation), rotation, EndPrisonPiece.BACK));
//			this.components.add(new EndPrisonPieces.Piece(templateManagerIn, getRightBlockPos(x, z, rotation), rotation, EndPrisonPiece.RIGHT));
//			this.components.add(new EndPrisonPieces.Piece(templateManagerIn, getLeftBlockPos(x, z, rotation), rotation, EndPrisonPiece.LEFT));
//			if(rand.nextInt(3) == 0)
//			{
//				this.components.add(new EndPrisonPieces.Piece(templateManagerIn, getShipBlockPos(x, z, rotation), getShipRotation(rotation), EndPrisonPiece.SHIP));
//			}
//			this.recalculateStructureSize();
//		}
//
////		@Override TODO make sure the above method is the replacement
////		public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn)
////		{
////			int x = chunkX * 16;
////			int z = chunkZ * 16;
////			BlockPos blockpos1 = new BlockPos(x, 120, z);
////			Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];
////			this.components.add(new EndPrisonPieces.Piece(templateManagerIn, blockpos1, rotation, EndPrisonPiece.MAIN));
////			this.components.add(new EndPrisonPieces.Piece(templateManagerIn, getFrontBlockPos(x, z, rotation), rotation, EndPrisonPiece.FRONT));
////			this.components.add(new EndPrisonPieces.Piece(templateManagerIn, getBackBlockPos(x, z, rotation), rotation, EndPrisonPiece.BACK));
////			this.components.add(new EndPrisonPieces.Piece(templateManagerIn, getRightBlockPos(x, z, rotation), rotation, EndPrisonPiece.RIGHT));
////			this.components.add(new EndPrisonPieces.Piece(templateManagerIn, getLeftBlockPos(x, z, rotation), rotation, EndPrisonPiece.LEFT));
////			if(rand.nextInt(3) == 0)
////			{
////				this.components.add(new EndPrisonPieces.Piece(templateManagerIn, getShipBlockPos(x, z, rotation), getShipRotation(rotation), EndPrisonPiece.SHIP));
////			}
////			this.recalculateStructureSize();
////		}
//	}
//	
//	/**
//	 * Used to get the correct position for the front piece of the End Prison.
//	 */
//	private static BlockPos getFrontBlockPos(int x, int z, Rotation rot)
//	{
//		switch(rot)
//		{
//		default:
//		case NONE:
//			return new BlockPos(x, 120, z + 31);
//		case CLOCKWISE_90:
//			return new BlockPos(x - 31, 120, z);
//		case COUNTERCLOCKWISE_90:
//			return new BlockPos(x + 31, 120, z);
//		case CLOCKWISE_180:
//			return new BlockPos(x, 120, z - 31);
//		}
//	}
//	
//	/**
//	 * Used to get the correct position for the back piece of the End Prison.
//	 */
//	private static BlockPos getBackBlockPos(int x, int z, Rotation rot)
//	{
//		switch(rot)
//		{
//		default:
//		case NONE:
//			return new BlockPos(x, 120, z - 3);
//		case CLOCKWISE_90:
//			return new BlockPos(x + 3, 120, z);
//		case COUNTERCLOCKWISE_90:
//			return new BlockPos(x - 3, 120, z);
//		case CLOCKWISE_180:
//			return new BlockPos(x, 120, z + 3);
//		}
//	}
//	
//	/**
//	 * Used to get the correct position for the right piece of the End Prison.
//	 */
//	private static BlockPos getRightBlockPos(int x, int z, Rotation rot)
//	{
//		switch(rot)
//		{
//		default:
//		case NONE:
//			return new BlockPos(x + 31, 120, z);
//		case CLOCKWISE_90:
//			return new BlockPos(x, 120, z + 31);
//		case COUNTERCLOCKWISE_90:
//			return new BlockPos(x, 120, z - 31);
//		case CLOCKWISE_180:
//			return new BlockPos(x - 31, 120, z);
//		}
//	}
//	
//	/**
//	 * Used to get the correct position for the left piece of the End Prison.
//	 */
//	private static BlockPos getLeftBlockPos(int x, int z, Rotation rot)
//	{
//		switch(rot)
//		{
//		default:
//		case NONE:
//			return new BlockPos(x - 3, 120, z);
//		case CLOCKWISE_90:
//			return new BlockPos(x, 120, z - 3);
//		case COUNTERCLOCKWISE_90:
//			return new BlockPos(x, 120, z + 3);
//		case CLOCKWISE_180:
//			return new BlockPos(x + 3, 120, z);
//		}
//	}
//	
//	/**
//	 * Used to get the correct position for the ship piece of the End Prison.
//	 */
//	private static BlockPos getShipBlockPos(int x, int z, Rotation rot)
//	{
//		switch(rot)
//		{
//		default:
//		case NONE:
//			return new BlockPos(x + 1, 120, z + 57);
//		case CLOCKWISE_90:
//			return new BlockPos(x - 45, 120, z + 29);
//		case COUNTERCLOCKWISE_90:
//			return new BlockPos(x + 45, 120, z - 29);
//		case CLOCKWISE_180:
//			return new BlockPos(x - 1, 120, z - 57);
//		}
//	}
//	
//	/**
//	 * Used to adjust the rotation of the ship.
//	 * @param rot - The Rotation of the End Prison
//	 */
//	private static Rotation getShipRotation(Rotation rot)
//	{
//		switch(rot)
//		{
//		default:
//		case NONE:
//			return Rotation.COUNTERCLOCKWISE_90;
//		case CLOCKWISE_90:
//			return Rotation.CLOCKWISE_180;
//		case COUNTERCLOCKWISE_90:
//			return Rotation.NONE;
//		case CLOCKWISE_180:
//			return Rotation.CLOCKWISE_90;
//		}
//	}
//	
//	public static enum EndPrisonPiece
//	{
//		MAIN, BACK, FRONT, LEFT, RIGHT, SHIP;
//	}
}