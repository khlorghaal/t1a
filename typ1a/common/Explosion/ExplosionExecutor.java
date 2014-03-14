package com.typ1a.common.Explosion;

import java.util.ArrayList;
import java.util.List;

public class ExplosionExecutor{
	private static List<Runnable> queue= new ArrayList<Runnable>();

	private static Thread thread= new Thread(new Runnable() {
		@Override
		public void run() {
			while(true){
				Runnable[] queuedump;
				synchronized (queue) {
					if(queue.size()==0){
						try {
							queue.wait();
						} catch (InterruptedException e){e.printStackTrace();}
					}
					
					queuedump= queue.toArray(new Runnable[0]);
					queue.clear();	
				}
				for(Runnable r : queuedump)
					r.run();

			}
		}

	}, "Nuke Explosions");
	static{
		thread.start();
	}

	public static void execute(Runnable command) {
		synchronized (queue) {
			queue.add(command);
			queue.notify();
		}
	}
}
