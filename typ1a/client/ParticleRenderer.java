package com.typ1a.client;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;

import org.lwjgl.BufferUtils;
import org.lwjgl.opencl.CL10;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

public class ParticleRenderer {
	public static int MAXPARTICLES= 100;
	//y axis = particleID
	//x0 position
	//x1 constantVel
	//x2 exponentialVel, multiplier
	//x3 color
	//x4 shoulddraw, timealive, maxlife, fadeaftertime
	private static class Particle{
		float[] pos;
		float[] constantVel;
		float[] expVel;
		float[] color;
		float[] lifedat;
		public Particle(float[] pos, float[] constantVel, float[] expVel, 
				float[] color, float timealive, float maxlife, float fadeaftertime){
			this.pos= pos;
			this.constantVel= constantVel;
			this.expVel= expVel;
			this.color= color;
			this.lifedat= new float[]{1, timealive, maxlife, fadeaftertime};
		}
	}
	
	//create and init particle sampler and fbo
	//only requires one sampler, as the only texel read from is the one about to be written to
	private final int particledata= GL11.glGenTextures();
	{
		GL11.glBindTexture(GL31.GL_TEXTURE_RECTANGLE, particledata);

		final FloatBuffer emptyTex= BufferUtils.createFloatBuffer(MAXPARTICLES*4*5);// * RGBA * attribs
		GL11.glTexImage2D(GL31.GL_TEXTURE_RECTANGLE, 0, GL30.GL_RGBA32F,   5, MAXPARTICLES,   0, GL11.GL_RGBA, GL11.GL_FLOAT, emptyTex);
		
	}
	
	////
	private List<Particle> newParticleBuffer= new ArrayList<Particle>();
	public void addParticle(Vector3f pos, Vector3f vconst, Vector3f vexpo, float expoMultiplier, float lifetime, byte r, byte g, byte b){
		
	}
	private void actuallyAddParticles(){
		
	}
	////
	public void render(float pT){
//		GL20.glUseProgram(render);
		GL20.glUniform1f(0, pT);
	}
	
	public void tick(){
		GL20.glUseProgram(compute);
	}
	////
	
	private int compute= GL20.glCreateProgram();
	private int cvid= GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
	private final String computeVert= 
			"#version 120\n" +
			"void main(){" +
			"gl_Position= gl_Vertex;" +
			"}";
	private int cfid= GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
	private final String computeFrag= 
			"#version 120\n" +
			"void main(){" +
			"int attrib= gl_FragCoord.y;" +
			"switch(attrib){" +
			"case 0://pos" +
			"" +
			"break;}" +
			"case 1://constvel" +
			"}" +
			"break;}" +
			"case 2://expvel" +
			"}" +
			"break;}" +
			"case 3://color" +
			"}" +
			"break;}" +
			"case 4://life" +
			"" +
			"break;}" +
			"gl_FragColor= col;" +
			"}";
	{
		GL20.glShaderSource(cvid, computeVert);
		GL20.glCompileShader(cvid);
		GL20.glAttachShader(cvid, compute);
		
		GL20.glShaderSource(cfid, computeFrag);
		GL20.glCompileShader(cfid);
		GL20.glAttachShader(cfid, compute);
		
		GL20.glLinkProgram(compute);
	}
}
