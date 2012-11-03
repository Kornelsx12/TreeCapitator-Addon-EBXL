package bspkrs.treecapitator.ebxl;

import java.util.EnumSet;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.BlockLog;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.TickType;
import extrabiomes.Extrabiomes;
import extrabiomes.api.Stuff;
import extrabiomes.module.summa.block.BlockCustomLog;
import extrabiomes.module.summa.block.BlockManager;
import extrabiomes.module.summa.block.BlockQuarterLog;
import extrabiomes.module.summa.worldgen.WorldGenAcacia;
import extrabiomes.module.summa.worldgen.WorldGenFirTree;
import extrabiomes.module.summa.worldgen.WorldGenFirTreeHuge;
import extrabiomes.module.summa.worldgen.WorldGenRedwood;
import extrabiomes.proxy.CommonProxy;
import bspkrs.treecapitator.*;
import bspkrs.util.ModVersionChecker;

@Mod(name="TreeCapitator Addon EBXL", modid="TreeCapitator Addon EBXL", version="FML 1.4.2.r02", useMetadata=true)
@NetworkMod(clientSideRequired=false, serverSideRequired=false)
public class TreeCapitatorEBXLAddonMod
{
    private static ModVersionChecker versionChecker;
    private String versionURL = "https://dl.dropbox.com/u/20748481/Minecraft/1.4.2/treeCapitatorEBXL.version";
    private String mcfTopic = "http://www.minecraftforum.net/topic/1009577-";

    @SideOnly(Side.CLIENT)
    public static Minecraft mcClient;

    public ModMetadata metadata;

    @PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
        TreeCapitator.init();
        metadata = event.getModMetadata();
        versionChecker = new ModVersionChecker(metadata.name, metadata.version, versionURL, mcfTopic, FMLLog.getLogger());
        versionChecker.checkVersionWithLogging();
    }

    @Init
    public void init(FMLInitializationEvent event)
    {
        if(event.getSide().equals(Side.CLIENT))
        {
            TickRegistry.registerTickHandler(new TreeCapitatorEBXLAddonTicker(EnumSet.of(TickType.CLIENT)), Side.CLIENT);
            this.mcClient = FMLClientHandler.instance().getClient();
        }
    }
    
    @PostInit
    public void postInit(FMLPostInitializationEvent event)
    {        
        Block.blocksList[Stuff.log.get().blockID] = null;
        Block.blocksList[Stuff.log.get().blockID] = new BlockEBXLTree(Stuff.log.get().blockID);
        prepareCL(Block.blocksList[Stuff.log.get().blockID]);
        
        Block.blocksList[Stuff.quarterLogNW.get().blockID] = null;
        Block.blocksList[Stuff.quarterLogNW.get().blockID] = 
                new BlockEBXLQLTree(Stuff.quarterLogNW.get().blockID, BlockQuarterLog.BarkOn.NW);
        prepareQL(Block.blocksList[Stuff.quarterLogNW.get().blockID], "extrabiomes.quarterlog.NW");
        
        Block.blocksList[Stuff.quarterLogNE.get().blockID] = null;
        Block.blocksList[Stuff.quarterLogNE.get().blockID] = 
                new BlockEBXLQLTree(Stuff.quarterLogNE.get().blockID, BlockQuarterLog.BarkOn.NE);
        prepareQL(Block.blocksList[Stuff.quarterLogNE.get().blockID], "extrabiomes.quarterlog.NE");        
        
        Block.blocksList[Stuff.quarterLogSW.get().blockID] = null;
        Block.blocksList[Stuff.quarterLogSW.get().blockID] = 
                new BlockEBXLQLTree(Stuff.quarterLogSW.get().blockID, BlockQuarterLog.BarkOn.SW);
        prepareQL(Block.blocksList[Stuff.quarterLogSW.get().blockID], "extrabiomes.quarterlog.SW");        
        
        Block.blocksList[Stuff.quarterLogSE.get().blockID] = null;
        Block.blocksList[Stuff.quarterLogSE.get().blockID] = 
                new BlockEBXLQLTree(Stuff.quarterLogSE.get().blockID, BlockQuarterLog.BarkOn.SE);
        prepareQL(Block.blocksList[Stuff.quarterLogSE.get().blockID], "extrabiomes.quarterlog.SE");     
        BlockEBXLQLTree.setDropID(Stuff.quarterLogSE.get().blockID);

        TreeCapitator.logClasses.add(BlockEBXLTree.class);
        TreeCapitator.logClasses.add(BlockEBXLQLTree.class);
    }
    
    protected void prepareCL(Block block)
    {
        final CommonProxy proxy = Extrabiomes.proxy;

        block.setBlockName("extrabiomes.customlog");
        proxy.registerBlock(block, extrabiomes.utility.MultiItemBlock.class);
        proxy.setBlockHarvestLevel(block, "axe", 0);

        for (final BlockCustomLog.BlockType type : BlockCustomLog.BlockType.values())
        {
            final ItemStack itemstack = new ItemStack(block, 1, type.metadata());
            proxy.addName(itemstack, type.itemName());
            proxy.registerOre("logWood", itemstack);
        }
        
        proxy.registerEventHandler(block);
    }
    
    protected void prepareQL(Block block, String name) 
    {
        final CommonProxy proxy = Extrabiomes.proxy;

        block.setBlockName(name);
        proxy.registerBlock(block, extrabiomes.utility.MultiItemBlock.class);
        proxy.setBlockHarvestLevel(block, "axe", 0);

        for (final BlockQuarterLog.BlockType type : BlockQuarterLog.BlockType.values())
        {
            final ItemStack itemstack = new ItemStack(block, 1, type.metadata());
            proxy.addName(itemstack, type.itemName());
            proxy.registerOre("logWood", itemstack);
        }

        proxy.registerOre("logWood", block);

        Extrabiomes.proxy.registerEventHandler(block);
    }

    @SideOnly(Side.CLIENT)
    public static boolean onTick(TickType tick, boolean isStart)
    {
        if (isStart) {
            return true;
        }

        if (mcClient != null && mcClient.thePlayer != null)
        {
            if(TreeCapitator.allowUpdateCheck)
                if(!versionChecker.isCurrentVersion())
                    for(String msg : versionChecker.getInGameMessage())
                        mcClient.thePlayer.addChatMessage(msg);
            return false;
        }

        return true;
    }
}
