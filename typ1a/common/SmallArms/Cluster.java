package com.typ1a.common.SmallArms;

public class Cluster{
	/**number of rounds of the same type, /adjacent/ in this portion of the stack*/
	public int n;
	/**the dmgval corresponding to the bullet type*/
	public int typ;
	public Cluster(int n, int typ){
		this.n=n;
		this.typ=typ;
	}
	public static void push(int type, Cluster[] clusters){

	}
	public static int pop(Cluster[] clusters){
		return 0;
	}
	
	/**@return {}*/
	public static byte[] toByteArr(Cluster[] clusters){
		if(clusters.length==0)
			return new byte[0];
		final byte[] ret= new byte[clusters.length*2];
		for(int i=0; i!=ret.length; i+=2){
			ret[i]= (byte) clusters[i/2].n;
			ret[i+1]= (byte) clusters[i/2].typ;
		}
		return ret;
	}
	public static Cluster[] fromByteArr(byte[] dat){
		Cluster[] ret= new Cluster[dat.length/2];
		for(int i=0; i<dat.length;)
			ret[i/2]= new Cluster( dat[i++], dat[i++]);
		return ret;
	}
	/**@return ungrouped array of types*/
	public static byte[] getTypeArr(byte[] clu){			
		byte[] ret= new byte[getCount(clu)];
		int iterret=0;

		for(int c=0; c!=clu.length; c+=2){//iter cluster types
			final byte t= clu[c+1];			
			for(int n=clu[c]; n!=0; n--){//dump n amount of types into ret
				ret[iterret]= t;
				iterret++;
			}
		}
		return ret;
	}
	public static int getCount(byte[] clu){
		int ret=0;
		for(int i=0; i!=clu.length; i+=2)
			ret+= clu[i];
		return ret;
	}
	public static int getCount(Cluster[] clu){
		int ret=0;
		for(Cluster c : clu)
			ret+=c.n;
		return ret;
	}
}
