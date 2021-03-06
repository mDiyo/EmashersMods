package emasher.sockets.modules;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.core.EmasherCore;
import emasher.sockets.SocketsMod;

public class ModEnergyInput extends SocketModule
{

	public ModEnergyInput(int id)
	{
		super(id, "sockets:energyInput");
	}

	@Override
	public String getLocalizedName()
	{
		return "Energy Input";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Accepts both MJ and EU");
		l.add("from adjacent cables/generators/etc.");
	}
	
	@Override
	public void addRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "g g", " b ", Character.valueOf('g'), Item.goldNugget, Character.valueOf('p'), EmasherCore.psu,
				Character.valueOf('b'), SocketsMod.blankSide);
		
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "g g", " b ", Character.valueOf('g'), "ingotCopper", Character.valueOf('p'), EmasherCore.psu,
				Character.valueOf('b'), SocketsMod.blankSide));
	}
	
	@Override
	public boolean acceptsEnergy(SideConfig config) { return true; }
	
	/*@Override
	public boolean hasRSIndicator() { return true; }
	
	@Override
	public boolean hasLatchIndicator() { return true; }*/
	
	@Override
	public boolean isEnergyInterface(SideConfig config) { return true; }
	
	@Override
	public int getPowerRequested(SideConfig config, SocketTileAccess ts)
	{
		boolean canIntake = true;
		
		for(int i = 0; i < 3; i++)
		{
			if(config.rsControl[i] && ts.getRSControl(i)) canIntake = false;
			if(config.rsLatch[i] && ts.getRSControl(i)) canIntake = false;
		}
		
		if(canIntake)
		{
			return 100;
		}
		
		return 0;
	}

}
