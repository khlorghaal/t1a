package com.typ1a.client.gui;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;

import com.typ1a.common.Containers.ContainerSilo;
import com.typ1a.common.Missiles.EntitySilo;
import com.typ1a.common.Network.PacketLaunchCode;


public class GUISilo extends ProperGUI {
	public EntityClientPlayerMP player;

	public Clickable LaunchButton;
	public ToggleAbsRel absRel;
	public TextField Xcoordbox,Zcoordbox;

	public GUISilo (InventoryPlayer inventoryPlayer, final EntitySilo silo) {
		super(new ContainerSilo(inventoryPlayer, silo), "Silo");

		this.player = (EntityClientPlayerMP)inventoryPlayer.player;

		LaunchButton = new Clickable(129,19,11,11,137,0);
		LaunchButton.clickAction= new Runnable() {
			@Override
			public void run() {
				if(Xcoordbox.str.length()!=0 && Zcoordbox.str.length()!=0){
					if(absRel.state==0)//absolute
						PacketLaunchCode.instance.send(silo, Integer.parseInt( Xcoordbox.str.toString() ) , Integer.parseInt( Zcoordbox.str.toString() ) );
					if(absRel.state==1)//relative
						PacketLaunchCode.instance.send(silo, (int)silo.posX + Integer.parseInt( Xcoordbox.str.toString() ) , (int)silo.posZ + Integer.parseInt( Zcoordbox.str.toString() ) );
				}
			}
		};

		absRel= new ToggleAbsRel(62, 5);
		Xcoordbox= new TextField(0x00e800, 64,18, 8);
		Zcoordbox = new TextField(0x00e800, 64,33, 8);
		addClickable(LaunchButton);
		addClickable(absRel);
		addClickable(Xcoordbox);
		addClickable(Zcoordbox);

		this.activePanel=Xcoordbox;
		activePanel.state=1;
	}

	@Override
	public void tabPressed(){
		if(activePanel!=null)
			activePanel.deselect();

		if(activePanel==null || activePanel== Zcoordbox)
			activePanel=Xcoordbox;
		else
			activePanel=Zcoordbox;

		activePanel.click(0,0);
	}
}