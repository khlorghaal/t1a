package com.typ1a.common;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.copySign;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import net.minecraft.util.MathHelper;
public class Maths {
	public static final double TAU= PI*2;

	/**Linear interpolation*/
	public static float lerp(float a, float b, float w){
	    return a + w*(b-a);
	}

	public static float clamp(float n, float min, float max){
		if(n<min) return min;
		if(n>max) return max;
		return n;
	}
	public static int clamp(int n, int min, int max){
		if(n<min) return min;
		if(n>max) return max;
		return n;
	}
	
	/**Correct so that origin is +x, orient z properly.<br/>
	 * theta 0 is +x*/
	public static double defuckVanillaAngle(double a){
		return Math.toRadians(-a-90);
	}
	/**de-correct so that +y is down >:C <br/> 
	 * This is so that theta 0 is +z, being forward, probably.
	 * That makes harder math though, its easier to think of +x as forward.*/
	public static double refuckVanillaAngle(double a){
		return Math.toDegrees(-90+a);
	}
	/**Takes any number and modulates it to [-pi, pi]*/
	public static double constrainStandardRadianAngle(double angle){
		angle%=TAU;
		if(angle<0) angle+=TAU;
		if(angle>PI) angle= angle-TAU;
		return angle;
	}
	/**Use radians you plebian*/
	public static double constrainStandardDegreeAngle(double angle){
		angle%=360;
		if(angle<0) angle+=360;
		if(angle>180) angle= angle-360;
		return angle;
	}

	/**Limits the return by the increment. Will not overshoot.*/
	public static double angleShortestPathIncremented(double angle, double target, double delta){
		double ret= angleShortestPath(angle, target);
		if(abs(ret)>abs(delta)){
			ret= copySign(1, ret);
			ret*= delta;
		}
		//		if(constrainStandardRadianAngle(angle+ret) > target)
		return ret;
	}
	/**@param angle angle in radians with 0 on +x from [pi, -pi]
	 * @param target angle
	 * @return the value to be added to angle that results in the shortest path to target*/
	public static double angleShortestPath(double angle, double target){
		//make all things positive to make lower bound 0
		//saves ops as no need to add/sub 0
		angle+= PI;
		target+= PI;

		//____ upper bound = tau
		//  -  target value
		//-    angle value
		//____ lower bound = 0
		//
		//delta can be up or down through three paths
		//one is direct, two are modulated/looped
		//one of the modulated ones will always be the largest path
		final double direct= target-angle;
		final double loopup= (TAU - angle) + (target);
		final double loopdown= -(angle + TAU-target);
		final double absdirect= abs(direct);
		final double absloopup= abs(loopup);
		final double absloopdown= abs(loopdown);

		switch( leastval(new double[]{absdirect,absloopup,absloopdown})){
		case 0:
			return direct;
		case 1:
			return loopup;
		case 2:
			return loopdown;

		default://will never happen
			return 0;
		}
	}
	/**@return the index of the value in the array*/
	public static int leastval(double[] vals){
		double lowest= Double.MAX_VALUE;
		int index=0;
		for(int i=0; i!=vals.length; i++){
			if(vals[i]<lowest){
				lowest= vals[i];
				index= i;
			}
		}
		return index;
	}
	public static int mostval(double[] vals){
		double biggest= Double.MIN_VALUE;
		int index=0;
		for(int i=0; i!=vals.length; i++){
			if(vals[i]>biggest){
				biggest= vals[i];
				index= i;
			}
		}
		return index;
	}
	
	public Vector3f getLocalizedVector(Vector3f global, Matrix4f rot){
		Vector4f globl= new Vector4f();
		Vector4f ret4= new Vector4f();
		globl.x= global.x; globl.y= global.y; globl.z= global.z; globl.w= 0;
		
		Matrix4f invrot= new Matrix4f();
		Matrix4f.invert(rot, invrot);
		
		Matrix4f.transform(invrot, globl, ret4);
		
		Vector3f ret= new Vector3f();
		ret.x= ret4.x; ret.y= ret4.y; ret.z= ret4.z;
		return ret;
	}
	
	/**From http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToMatrix/*/
	public static Matrix4f quatToMatrix(Quaternion q){
		final Matrix4f ret= new Matrix4f();
	    float sqw = q.w*q.w;
	    float sqx = q.x*q.x;
	    float sqy = q.y*q.y;
	    float sqz = q.z*q.z;

	    // invs (inverse square length) is only required if quaternion is not already norret.malised
	    float invs = 1 / (sqx + sqy + sqz + sqw);
	    ret.m00 = ( sqx - sqy - sqz + sqw)*invs ; // since sqw + sqx + sqy + sqz =1/invs*invs
	    ret.m11 = (-sqx + sqy - sqz + sqw)*invs ;
	    ret.m22 = (-sqx - sqy + sqz + sqw)*invs ;
	    
	    float temp1 = q.x*q.y;
	    float temp2 = q.z*q.w;
	    ret.m10 = 2* (temp1 + temp2)*invs ;
	    ret.m01 = 2 * (temp1 - temp2)*invs ;
	    
	    temp1 = q.x*q.z;
	    temp2 = q.y*q.w;
	    ret.m20 = 2* (temp1 - temp2)*invs ;
	    ret.m02 = 2* (temp1 + temp2)*invs ;
	    temp1 = q.y*q.z;
	    temp2 = q.x*q.w;
	    ret.m21 = 2* (temp1 + temp2)*invs ;
	    ret.m12 = 2* (temp1 - temp2)*invs ;
	    return ret;
	}
	
	/**From http://xith.org/archive/JavaCoolDude/showsrc
	 * /showsrc.php?src=../JWS/Lwjgl/MagicRoom/source/MathUtils/Quaternion.java*/
	public static Quaternion slerp(Quaternion  q1, Quaternion  q2, float t){
	    Quaternion ret= new Quaternion();
	    	// From "Advanced Animation and Rendering Techniques"
	    	// by Watt and Watt pg. 364, function as implemented appeared to be 
	    	// incorrect.  Fails to choose the same quaternion for the float
	    	// covering. Resulting in change of direction for rotations.
	    	// Fixed function to negate the first quaternion in the case that the
	    	// dot product of q1 and this is negative. Second case was not needed. 

	         float dot,s1,s2,om,sinom;

	         dot = q2.x*q1.x + q2.y*q1.y + q2.z*q1.z + q2.w*q1.w;
	         
	         if ( dot < 0 ) {
	            // negate quaternion
	           q1.x = -q1.x;  q1.y = -q1.y;  q1.z = -q1.z;  q1.w = -q1.w;
	           dot = -dot;
	         }

	         if ( (1.0 - dot) > 0.000001f ) {
	           om = (float) Math.acos(dot);
	           sinom = (float) Math.sin(om);
	           s1 = (float) (Math.sin((1.0-t)*om)/sinom);
	           s2 = (float) (Math.sin( t*om)/sinom);
	         } else{
	           s1 = 1 - t;
	           s2 = t;
	         }
	         ret.w = (float)(s1*q1.w + s2*q2.w);
	         ret.x = (float)(s1*q1.x + s2*q2.x);
	         ret.y = (float)(s1*q1.y + s2*q2.y);
	         ret.z = (float)(s1*q1.z + s2*q2.z);
	      
	    return ret;
	  }
}
