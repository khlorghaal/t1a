package com.typ1a.common;

/**States are variables to be synched. 
 * Though may be treated as a stream or whatever.*/
public interface IUpdateable{
	public int[] getStates();
	public void setStates(int[] states);
}
