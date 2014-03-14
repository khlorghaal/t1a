package com.typ1a.common;

import java.util.List;
import java.util.Vector;

public abstract class Ticker{

	private static List<Ticker> instances = new Vector<Ticker>();

	public int ticksDone=0;

	public void kill(){
		instances.remove(this);
	}

	public Ticker() {
		instances.add(this);
	}

	private void update(){
		this.onUpdate();
		this.ticksDone++;
	}
	
	protected abstract void onUpdate();

	public static void updateAll(){

		synchronized(instances){
			for(Ticker ticker : instances.toArray( new Ticker[instances.size()] ))
				ticker.update();
		}
	}
}
