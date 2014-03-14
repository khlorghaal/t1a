package com.typ1a.common;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.typ1a.common.Robots.Ballistic;
import com.typ1a.common.utils.Vector3;

public class Kinematics {

	public static final int ROLL=0, PITCH=2, YAW=1;
	public static final Vector3f
	Y= new Vector3f(0,1,0), X= new Vector3f(1,0,0), Z= new Vector3f(0,0,1);


	protected Vector4f Tl= new Vector4f(), 
			T= new Vector4f(),
			a= new Vector4f(),
			v= new Vector4f();
	/**The drag on each local axis. v+= (f - drag*v)/mass <br/>
	 * Max speed = force/dragconst
	 * proof: http://farm4.staticflickr.com/3783/10954808274_bb5009929e_o.jpg
	 * */
	private Vector4f drag= new Vector4f();
	private Vector4f rotdrag= new Vector4f();
	{
		Tl.w= T.w= a.w= v.w= drag.w= 0;
		rotdrag.w= 1;
	}

	private float mass, G= (float)Ballistic.G;

	protected Matrix4f rot= new Matrix4f();
	private Matrix4f w= new Matrix4f();
	private Matrix4f dw= new Matrix4f();
	private Quaternion prevrot= new Quaternion();

	public Kinematics(float mass, Vector3f drag, Vector3f angulardrag){
		this.mass= mass;
		this.drag.x= drag.x; this.drag.y= drag.y; this.drag.z= drag.z;
		rotdrag.x= angulardrag.x; rotdrag.y= angulardrag.y; rotdrag.z= angulardrag.z;
	}
	public Kinematics(float mass, Vector3f drag, Vector3f angulardrag, Vector3f v, Matrix4f rotation){
		this(mass, drag, angulardrag);
		this.v.x= v.x; this.v.y= v.y; this.v.z= v.z;
		rot= rotation;
	}

	public void tick(){
		rotate();
		mass=20;
		final Vector4f f= new Vector4f();

		Matrix4f.transform(rot, Tl, f);//+= rotated Tlocal
		Vector4f.add(f, T, f);//+= Tglobal

		//a = (f-drag(v))/mass
		a.x= ( f.x - drag.x * v.x )/mass;
		a.y= ( f.y - drag.y * v.y )/mass;
		a.z= ( f.z - drag.z * v.z )/mass;
		//v+=a
		Vector4f.add(a, v, v);
	}

	public void setThrustGlobal(Vector3f T){
		this.T.x= T.x;	this.T.y= T.y;	this.T.z= T.z;
	}
	public void addThrustGlobal(Vector3f T){
		this.T.x+= T.x;	this.T.y+= T.y;	this.T.z+= T.z;
	}

	public void setThrustLocal(Vector3f T){
		Tl.x= T.x; Tl.y= T.y; Tl.z= T.z;
	}
	public void setThrustLocal(float fwd, float right, float up){
		setThrustLocal(new Vector3f(fwd, up, right));
	}
	public void hardStop(){
		T.x= 0; T.y= 0; T.z= 0;
		Tl.x= 0; Tl.y= 0; Tl.z= 0;
		a.x= 0; a.y= 0; a.z= 0;
		v.x= 0; v.y= 0; v.z= 0;
	}



	private void rotate(){
		prevrot.setFromMatrix(rot);
		//		Matrix4f.mul(w, dw, w);//add accel to velocity
		Matrix4f.mul(rot, w, rot);//add velocity to rotation
		w= new Matrix4f();

//						rot= new Matrix4f();//.rotate(-0.01f, new Vector3f(0,1,0));
	}
	public void addTorqueLocal(int localaxis, float mag){
		Matrix4f.rotate(mag, getLocalAxis(localaxis), w, w);
	}
	
	public Vector3f getLocalAxis(int localaxis){
		final Vector3f ret= new Vector3f();
		switch(localaxis){
		case 0:
			ret.x= rot.m00;
			ret.y= rot.m10;
			ret.z= rot.m20;			
			break;
		case 1:
			ret.x= rot.m01;
			ret.y= rot.m11;
			ret.z= rot.m21;
			break;
		case 2:
			ret.x= rot.m02;
			ret.y= rot.m12;
			ret.z= rot.m22;
			break;
		}
		return ret;
	}

	//	public static Vector3f rotateVector(Vector3f vec, Vector3f axis, float angle){
	//		final Vector3f ret= new Vector3f();
	//		//vrot = v cos(a) + (k*v)sin(a) + k(k dot v)(1-cos(a))
	//		final float cosa= (float) Math.cos(angle);
	//		final float sina= (float) Math.sin(angle);
	//		final Vector3f kcrossv= new Vector3f();Vector3f.cross(axis, vec, kcrossv);
	//		final float kdotv= Vector3f.dot(axis, vec);
	//		ret.x= vec.x*cosa + kcrossv.x*sina + axis.x*kdotv*(1-cosa);
	//		ret.y= vec.y*cosa + kcrossv.y*sina + axis.y*kdotv*(1-cosa);
	//		ret.z= vec.z*cosa + kcrossv.z*sina + axis.z*kdotv*(1-cosa);
	//		return ret;
	//	}
	/**@param normal of the plane collided with
	 * @param elasticity 0-1*/
	public void collide(Vector3 normal, float elasticity){
		//TODO
	}


	public void setGravity(float G){this.G= G;}
	public Vector3f getVelocity(){
		Vector3f ret= new Vector3f();
		ret.x= v.x; ret.y= v.y; ret.z= v.z;
		return ret;
	}

	public Matrix4f getRotation(){return rot;}
	public Matrix4f getRotationInterpolated(float partiality){
		partiality=1/partiality;
		final Quaternion rotq= Quaternion.setFromMatrix(rot, new Quaternion());		
		final Quaternion intplq= Maths.slerp(prevrot, rotq, partiality);

		final Matrix4f ret= new Matrix4f();
		return Maths.quatToMatrix(intplq);
		//		return rot;
	}

	//Use a vec as the point at the end of an arc
	//the projection of the rotating vector onto the primary rotation plane is an ellipse
	//with no other rotation than the measured one, is a perfect circle
	//in these equations C is the axis of rotation, thus AB is the plane of rotation
	//A^2 + B^2 = 1
	//apply the warpage from the nonprimary rotations
	//A= (1-B^2)^1/2 /cos(Brot)
	//cos(Brot)*A^2= 1-B^2
	//therefore
	//cos(Brot)*A^2 + cos(Arot)*B^2 = 1
	public float getYaw(){
		//[xx yx zx] [1]
		//[xy yy zy]*[0]
		//[xz yz zz] [0]
		//=
		//[xx]
		//[0]
		//[xz]
		return (float) -Math.atan2(rot.m02, rot.m00);
	}
	public float getPitch(){
		//[xx yx zx] [1]
		//[xy yy zy]*[0]
		//[xz yz zz] [0]
		//=
		//[0]
		//[xy]
		//[xz]
		return (float) Math.atan2(rot.m01, rot.m02);
	}
	public float getRoll(){
		//		return (float) Math.acos( getRotation().m11 );
		return 0;
	}
}
