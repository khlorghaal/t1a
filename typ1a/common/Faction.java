package com.typ1a.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**String are used as identifiers, so Allies/Enemies may be a faction or player*/
public class Faction {

	public static Vector<Faction> factionList=new Vector(20);//TODO get number from savefile

	private static Map<String, Faction> factionOfPlayer= new Hashtable<String, Faction>();
	public static Faction getFactionOfPlayer(String player){return factionOfPlayer.get(player);}

	//Player-* relations
	private static Map<String, List<String> >
	enemiesOfPlayer= new Hashtable<String, List<String> >(),
	alliesOfPlayer= new Hashtable<String, List<String> >();

	public static List<String> getEnemiesOfPlayer(String player){
		return enemiesOfPlayer.get(player);
	}
	public static List<String> getAlliesOfPlayer(String player){
		return alliesOfPlayer.get(player);
	}

	private static List<List<String>> 
	playerAllyList=new Vector(),
	playerEnemyList=new Vector();
	/////////////////////


	public String name, tag="", description="";

	public List<String>
	members=new Vector<String>(),
	allies=new Vector<String>(),
	enemies=new Vector<String>();


	public Faction(String name, String tag, 
			List<String> members, List<String> allies, List<String> enemies){
		this.name=name;
		this.tag=tag;
		this.members=members;
		this.allies=allies;
		this.enemies=enemies;
		factionList.add(this);

		for(String member : members){
			factionOfPlayer.put(member, this);
		}
	}

	/**Is for player-player relations, automatically factors in
	 * factions.
	 * @param nsbi not blue? shoot it
	 * @return -1 is hostile, 0 is neutral, 1 is friendly
	 *///TODO
	public static int FoF(String player, String target, boolean nbsi){
		int ret;

		//check for target player relation before target faction relation
		final Faction faction= getFactionOfPlayer(player);
		final Faction targetFaction= getFactionOfPlayer(player);
		if(faction!=null){
			if(targetFaction!=null){
				ret= FoFFactionToFaction(faction, targetFaction);
			}
			else{
				ret= FoFFactionToPlayer(faction, target);
			}
		}
		else{
			if(targetFaction!=null){
				ret= FoFPlayerToFaction(player, targetFaction);
			}
			else{
				ret= FoFPlayerPlayer(player, target);
			}
		}


		if(ret==0)
			ret= nbsi? -1:0;

		return ret;
	}
	/**if asker and target both have factions*/
	private static int FoFFactionToFaction(Faction faction, Faction target){
		if(faction.allies.contains("*"+target.name))
			return 1;
		if(faction.enemies.contains("*"+target.name))
			return -1;		
		return 0;
	}
	/**if target doesnt have a facton*/
	private static int FoFFactionToPlayer(Faction faction, String target){
		if(faction.allies.contains(target))
			return 1;
		if(faction.enemies.contains(target))
			return -1;		
		return 0;
	}
	/**if asker doesnt have a faction but target does*/
	private static int FoFPlayerToFaction(String player, Faction target){
		if(getAlliesOfPlayer(player).contains("*"+target.name))
			return 1;
		if(getEnemiesOfPlayer(player).contains("*"+target.name))
			return -1;
		return 0;
	}
	/**if asker nor target have a faction*/
	private static int FoFPlayerPlayer(String player, String target){
		return 0;
	}








	public static void memberJoin(String player, Faction faction){
		faction.members.add(player);
		factionOfPlayer.put(player, faction);
	}
	public static void memberLeave(String player){
		factionOfPlayer.get(player).members.remove(player);
		factionOfPlayer.remove(player);
	}

	/*Files are saved in the format
	 * Faction
	 * member[]
	 * ally[]
	 * enemy[]
	 * 
	 * elements separated by spaces, arrays by \n
	 * */
	private void write(){
	}
	private void read(){

	}

	private static BufferedWriter writer;
	private static BufferedReader reader;

	public static void saveFactions(File file) throws FileNotFoundException, IOException{
		writer= new BufferedWriter(new FileWriter(file));

		writer.write("This file may be edited manually but use extreme caution "+
				"as it is whitespace and case sensitive.\n\n"+
				"Allies/Enemies can be players or factions.\n"+
				"Faction names are prefixed with *\n\n"+
				"*FactionName\nmember[]\n\n"+
				"PlayerName\nally[]\nenemy[]\n"+
				"________________________________\n");
		for(Faction faction : factionList)
			faction.write();

		writer.flush();
		writer.close();
	}
	public static void loadFactions(File file) throws ClassNotFoundException, IOException{
		reader= new BufferedReader(new FileReader(file));

	}

}
