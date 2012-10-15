package bspkrs.treecapitator.ebxl;

import java.util.EnumSet;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)

public class TreeCapitatorEBXLAddonTicker implements ITickHandler 
{
	private EnumSet<TickType> tickTypes = EnumSet.noneOf(TickType.class);

	public TreeCapitatorEBXLAddonTicker(EnumSet<TickType> tickTypes) {
		this.tickTypes = tickTypes;
	}

	@Override
	public void tickStart(EnumSet<TickType> tickTypes, Object... tickData) 
	{
		tick(tickTypes, true);
	}

	@Override
	public void tickEnd(EnumSet<TickType> tickTypes, Object... tickData) 
	{
		tick(tickTypes, false);
	}

	private void tick(EnumSet<TickType> tickTypes, boolean isStart) {
		for (TickType tickType : tickTypes) 
		{
			if (!TreeCapitatorEBXLAddonMod.onTick(tickType, isStart)) 
			{
				this.tickTypes.remove(tickType);
				this.tickTypes.removeAll(tickType.partnerTicks());
			}
		}
	}

	@Override
	public EnumSet<TickType> ticks() 
	{
		return this.tickTypes;
	}

	@Override
	public String getLabel() 
	{
		return "TreeCapitatorIC2AddonTicker";
	}

}
