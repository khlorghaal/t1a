package com.typ1a.common.Robots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.typ1a.common.BlocksItems.T1AItem;

public class ItemSentry extends T1AItem{
    public ItemSentry()   {
        super("Sentry");
    }
    
    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer player, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {if(!par3World.isRemote){
        if (par7 == 0)        {            --par5;        }
        if (par7 == 1)        {            ++par5;        }
        if (par7 == 2)        {            --par6;        }
        if (par7 == 3)        {            ++par6;        }
        if (par7 == 4)        {            --par4;        }
        if (par7 == 5)        {            ++par4;        }

    //    if (false){ return false; }
        if (par3World.getBlock(par4, par5, par6) == Blocks.air)
            {
            	par3World.spawnEntityInWorld(new EntitySentry(player, (double)par4, (double)par5, (double)par6));
            }

            par1ItemStack.stackSize--;
            return true;
        }
     return true;
    }
    
}