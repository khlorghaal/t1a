package com.typ1a.client;

import static org.lwjgl.opengl.GL11.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Stack;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;

import net.minecraftforge.common.DimensionManager;

public class CubeRenderer {

	// drawArray 8*3*3=72 vs drawElements 8*3 + 3*2*6=60
	// but drawArray requires 72 additions while drawElements 24

	private static final FloatBuffer cubeVertsBufr= BufferUtils.createFloatBuffer(8*3);
	private static final ByteBuffer cubeIndexOrderBufr= BufferUtils.createByteBuffer(3*2*6);

	static{
		cubeVertsBufr.put(new float[]{
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				-0.5f, 0.5f,-0.5f,
				0.5f, 0.5f,-0.5f,
				-0.5f,-0.5f, 0.5f,
				0.5f,-0.5f, 0.5f,
				-0.5f, 0.5f, 0.5f,
				0.5f, 0.5f, 0.5f});
		//			0,0,0, 1,0,0, 0,1,0, 1,1,0,
		//			0,0,1, 1,0,1, 0,1,1, 1,1,1});

		cubeIndexOrderBufr.put(new byte[]{
				//front
				//2 3
				//0 1
				//back
				//6 7
				//4 5
				0,3,1, 0,2,3,//front
				5,7,4, 6,4,7,//back
				3,2,7, 6,7,2,//top
				0,1,4, 5,4,1,//bottom
				2,0,4, 4,6,2,//left
				1,3,7, 7,5,1,//right
		});
	}
	
	public static void drawCubes(int dataRectTexID){
		
	}
	/**@param positions xn,yn,zn...*/
	public static void drawCubes(float[] positions, int color){
		glBindTexture(GL_TEXTURE_2D, 0);
		final int r,g,b,a;
		r= (color>>24) &0xff;//be very mindful of the sign bit :c
		g= (color&0x00ff0000)/0x10000;
		b= (color&0x0000ff00)/0x100;
		a= (color&0x000000ff);

		final boolean blend= a<0xf8;
		glPushAttrib(GL_LIGHTING);
		glDisable(GL_LIGHTING);

		glPushAttrib(GL_BLEND);
		if(blend){
			//			glDepthMask(false);
			//			glDisable(GL_ALPHA_TEST);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		}
		else
			glDisable(GL_BLEND);

		glColor4ub((byte)r, (byte)g, (byte)b, (byte)a);

		pushProgram();
		for(int i=0; i<positions.length;){
			glPushMatrix();
			glTranslatef(positions[i++], positions[i++], positions[i++]);
			//TODO PURGE THIS FILTH
			glScalef(2, 2, 2);
			drawCube();
			glPopMatrix();
		}
		popProgram();
		glPopAttrib();
		glPopAttrib();

	}

	private static Stack<Integer> progstack= new Stack<Integer>();
	private static IntBuffer ibuf= BufferUtils.createIntBuffer(16);//min size of 16 for some reason?
	private static void pushProgram(){
		ibuf.position(0);
		GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM, ibuf);
		ibuf.position(0);
		progstack.push(ibuf.get());
	}
	private static void popProgram(){
		GL20.glUseProgram(progstack.pop());
	}


	private static int drawCubeSingle, cubeVBO, cubeIBO;
	public static void drawCube(){
		glCallList(drawCubeSingle);
	}
	static{
		//make cube VBO
		cubeVertsBufr.position(0);
		cubeIndexOrderBufr.position(0);
		cubeVBO= GL15.glGenBuffers();
		cubeIBO= GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, cubeVBO);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, cubeVertsBufr, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, cubeIBO);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, cubeIndexOrderBufr, GL15.GL_STATIC_DRAW);


		//compile draw cube
		drawCubeSingle= glGenLists(1);
		glNewList(drawCubeSingle, GL_COMPILE);

		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, cubeVBO);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, cubeIBO);
		GL11.glDrawElements(GL11.GL_TRIANGLES, 3*2*6, GL11.GL_UNSIGNED_BYTE, 0);

		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		glEndList();
	}

	//Shaders for instancing
	public static int progInstancedLit= GL20.glCreateProgram();
	private static int vsh_IL= GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
	private static int fsh_IL= GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
	static {
		GL20.glShaderSource(vsh_IL, 
				"#version 140\n" +
						"#extension GL_ARB_draw_instanced : enable\n" +
						"uniform mat4 mvpm;" +
						"uniform float pT;"+
						"uniform sampler2DRect particledata;" +
						"flat out vec4 color;" +
						"void main(){" +
						"vec4 pos= texelFetch(particledata, ivec2(0, gl_InstanceID));" +
						"pos+= pT * texelFetch(particledata, ivec2(1, gl_InstanceID));" +
						"gl_Position= mvpm * (pos);" +
						"color= texelFetch(particledata, ivec2(3, gl_InstanceID));" +
				"}");
		GL20.glCompileShader(vsh_IL);
		GL20.glAttachShader(vsh_IL, progInstancedLit);

		GL20.glShaderSource(fsh_IL, 
				"#version 140\n" +
						"uniform sampler2DRect particledata;" +
						"in vec4 color;" +
						"void main(){" +
						"vec4 lifedata= texelFetch(particledata, ivec2(4, gl_InstanceID));" +
						"float timealive= lifedata.x;" +
						"float maxlife= lifedata.y;" +
						"float fadeaftertime= lifedata.z;" +
						"float a;" +
						"if(timealive<fadeaftertime)" +
						"{a=1;}" +
						"else" +
						"{a= 1- ((timealive-fadeaftertime)/maxlife);}" +
						"gl_FragColor= (color.xyz, 1);//a);" +
				"}");
		GL20.glCompileShader(fsh_IL);
		System.out.println("====VSH====");
		System.out.println(GL20.glGetShaderInfoLog(vsh_IL, 100));
		System.out.println("====FSH====");
		System.out.println(GL20.glGetShaderInfoLog(fsh_IL, 100));
		GL20.glAttachShader(fsh_IL, progInstancedLit);

		GL20.glLinkProgram(progInstancedLit);
	}
}
