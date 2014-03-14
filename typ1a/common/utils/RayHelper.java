//author khlorghaal
package com.typ1a.common.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class RayHelper {
	/**@param xyz position of start
	 * @param dxdydz ray or motion/t*/
	public static Entity getEntityNearestFromRay(World world, Entity[] exclusions,
			double x, double y, double z,
			double dx, double dy, double dz){
		AxisAlignedBB aabb= AxisAlignedBB.getBoundingBox
				(x,y,z, x+dx, y+dy, z+dz).expand(20,20,20);
		//TODO hone aabb expansion
		List<Entity> list= world.getEntitiesWithinAABB(Entity.class, aabb);

		for(Entity e: exclusions){
			list.remove(e);
		}
		List<Entity> filteredList= new ArrayList<Entity>();
		for(Entity e: list){//multithread if this gets laggy
			if(e.boundingBox!=null){
				if(isLineInsideBox(e.getBoundingBox(), x, y, z, dx, dy, dz))
					filteredList.add(e);
			}
			else if(isLineInsideBox(
					AxisAlignedBB.getBoundingBox(
							-20 +e.posX,
							-20 +e.posY,
							-20 +e.posZ,
							20 +e.posX,
							20 +e.posY,
							20 +e.posZ),
					x, y, z, dx, dy, dz))
				filteredList.add(e);
		}
		return getClosestEntity(filteredList, x,y,z);
	}
	/**@param xyz origin of the line
	 * @param d(xyz) vector of any length describing line's slope*/
	public static boolean isLineInsideBox(AxisAlignedBB box, double x,double y,double z, double dx,double dy,double dz){
		//line will enter box on a side whose polarity (minimum or maximum) is opposide the polarity of the lines component,
		//meaning its necessary to check if the line intersects any of 3 faces

		//if component A is 0 then line cannot enter from an BC face, as the BC face is orthogonal to components BC
		//thus do not do the either of the two respective face checks when da==0
		//(technically if they are on the exact same place that is not true,
		//but it could be argued in that case its only touching the box and not actually inside it)

		final double //take bounds relative to origin of vector, not the box
		minX=box.minX-x,
		maxX=box.maxX-x,
		minY=box.minY-y,
		maxY=box.maxY-y,
		minZ=box.minZ-z,
		maxZ=box.maxZ-z;

		//do azimuthal intersection checks (*y planes) first because they are more likely [confirm?]
		try{
			//zy check
			if(isLineInsideFace(dz/dx, dy/dx, 
					( (dx>0) ? minX : maxX ) -x, //T //if line component is positive check the negative (minimal) face
					minZ, maxZ, minY, maxY) ) //A //B
				return true;
			//xy check
			if(isLineInsideFace(dx/dz, dy/dz, 
					( (dz>0) ? minZ : maxZ ) -x,
					minX, maxX, minY, maxY) )
				return true;
			//xz check
			if(isLineInsideFace(dx/dy, dz/dy, 
					( (dy>0) ? minY : maxY ) -x,
					minX, maxX, minZ, maxZ) )
				return true;
		}
		catch(ArithmeticException e){}
		return false;
	}
	/**takes a line which intersects the origin as a 2-vector and checks if it is inside a rectangle orthogonal to that 2-vector 
	 * when multiplied by the scalar distance between the orthogonal planes
	 * @param T dimension between the planes
	 * @param bound of the rectangle*/
	private static boolean isLineInsideFace(double dAdT, double dBdT, double distT,  
			double boundAmin, double boundAmax, double boundBmin, double boundBmax){

		//A,B is the intersection of the line with the rectangles plane
		final double A= dAdT*distT;

		//do false checks instead of true checks to allow early breaking
		if(A<boundAmin)
			return false;
		if(A>boundAmax)
			return false;

		final double B= dBdT*distT;
		if(B<boundBmin)
			return false;
		if(B>boundBmax)
			return false;

		return true;
	}


	public static Entity getClosestEntity(List<Entity> list, double x, double y, double z){
		if(list.size()==0)
			return null;

		Entity ret=null;
		double clostestDist=0xfffffff;
		for(Entity e: list){
			final double dist= getDistance(x, y, z, e.posX, e.posY, e.posZ);
			if(dist<clostestDist){
				clostestDist=dist;
				ret=e;
			}				
		}
		return ret;
	}

	public static double getDistance(double x,double y,double z, double u,double v,double w){
		final double dist=
				Math.sqrt( (x-u)*(x-u) + (y-v)*(y-v) + (z-w)*(z-w) );
		return dist;
	}
}
