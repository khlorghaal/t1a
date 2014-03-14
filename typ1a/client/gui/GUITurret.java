package com.typ1a.client.gui;

import net.minecraft.entity.player.InventoryPlayer;

import com.typ1a.common.Robots.EntitySentry;


public class GUITurret extends ProperGUI {
		
		ButtonMultistate aggro= ButtonMultistate.newAggroSetter(6, 6);
		ButtonMultistate sizep= ButtonMultistate.newTargetSizeSetter(6, 19);
			
		TextField retention= new TextField(139, 6, 2);
		TextField attention= new TextField(139, 19, 2);
		ButtonMultistate focus= ButtonMultistate.newBool(139, 30);	
		
		ButtonOpenEquipment eq;
		ClickableDial restYaw= new ClickableDial(26,34, 33,33, 74,26, 16,16);
		ClickableDial restPitch= new ClickableDial(61,34, 19,33, 108,26, 3,16);
		
        public GUITurret (InventoryPlayer inventoryPlayer, EntitySentry sentry) {
                super(null,"Turret");
                this.xSize=166;
                this.ySize=76;
                
            	
            	addClickable(aggro);
            	addClickable(sizep);
            	addClickable(focus);
            	
            	addClickable(retention);
            	addClickable(attention);
            	
            	addClickable(restPitch);
            	addClickable(restYaw);
            	
            	eq= new ButtonOpenEquipment(6, 54, sentry.hashCode());
            	addClickable(eq);
            	addClickable(new Clickable(84, 6, 54,27, 128,23));//plaintext
        }
}