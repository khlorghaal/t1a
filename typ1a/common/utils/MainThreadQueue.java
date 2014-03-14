package com.typ1a.common.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.typ1a.common.Ticker;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

/**For queueing methods needing execution onto the main thread 
 * upon the next tick as a simple concurrency prevention.*/
public class MainThreadQueue extends Ticker{

	private static MainThreadQueue instance= new MainThreadQueue();

	private static List<Runnable> queue= new ArrayList<Runnable>();
	
	public static void add(Runnable r){
		synchronized (queue) {
			queue.add(r);	
		}		
	}

	@Override
	public void onUpdate(){
		if(queue.size()==0 || FMLCommonHandler.instance().getEffectiveSide()==Side.CLIENT)
			return;
		//make an array so that other theads may add to queue while runnables run
		Runnable[] tasks;
		synchronized (queue) {
			tasks= queue.toArray(new Runnable[0]);
			queue.clear();
		}
		for(Runnable task : tasks){
			task.run();
		}
	}

	/**Make your own runnables, you lazy bastard*/
	@Deprecated
	public static class MethodCall implements Runnable{
		Method method;
		Object caller;
		Object[] params;

		/**@param caller the object which runs this method, null if static*/
		public MethodCall(Method method, Object caller, Object[] params){
			this.method= method;
			this.caller= caller;
			this.params= params;
		}
		@Override
		public void run(){
			try {
				method.invoke(caller, params);
			} catch (IllegalArgumentException e) {e.printStackTrace();}
			catch (IllegalAccessException e) {e.printStackTrace();}
			catch (InvocationTargetException e) {e.printStackTrace();}				
		}
	}
}
