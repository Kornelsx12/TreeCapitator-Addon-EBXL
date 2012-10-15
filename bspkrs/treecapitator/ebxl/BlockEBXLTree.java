package bspkrs.treecapitator.ebxl;

import extrabiomes.module.summa.block.BlockCustomLog;
import bspkrs.treecapitator.TreeBlockBreaker;
import bspkrs.treecapitator.TreeCapitator;
import net.minecraft.src.Block;
import net.minecraft.src.BlockLeavesBase;
import net.minecraft.src.BlockVine;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;

public class BlockEBXLTree extends BlockCustomLog
{
	private TreeBlockBreaker breaker;

    public BlockEBXLTree(int i)
    {
        super(i);
        setHardness(TreeCapitator.logHardnessNormal);
        setRequiresSelfNotify();
    }

    /**
     * Called when the block is clicked by a player. Args: x, y, z, entityPlayer
     */
    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer entityPlayer)
    {
    	breaker = new TreeBlockBreaker(entityPlayer, this.blockID, this.getClass(), BlockLeavesBase.class, BlockVine.class);
    	breaker.onBlockClicked(world, x, y, z, entityPlayer);
    }

    /**
     * Called when the block is attempted to be harvested
     */
    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int md, EntityPlayer entityPlayer)
    {
    	if(breaker == null || !breaker.player.equals(entityPlayer))
    		breaker = new TreeBlockBreaker(entityPlayer, this.blockID, this.getClass(), BlockLeavesBase.class, BlockVine.class);
    	breaker.onBlockHarvested(world, x, y, z, md, entityPlayer);
    }

    /**
     * Returns the block hardness at a location. Args: world, x, y, z
     */
    @Override
    public float getBlockHardness(World par1World, int par2, int par3, int par4)
    {
        return breaker != null ? breaker.getBlockHardness() : TreeCapitator.logHardnessNormal;
    }
}
