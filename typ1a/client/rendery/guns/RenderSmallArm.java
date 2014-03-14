package com.typ1a.client.rendery.guns;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import com.typ1a.client.T1AClientProxy;
import com.typ1a.client.rendery.RenderLaser;
import com.typ1a.common.SmallArms.ItemAttachment;
import com.typ1a.common.utils.Vector3;


public abstract class RenderSmallArm extends ModelBase implements IItemRenderer{

	protected static final Minecraft mc= Minecraft.getMinecraft();
	
	protected ModelRenderer Mag;
	protected ModelRenderer Bolt;
	protected ModelRenderer Main;
	protected ModelRenderer Barrel;

	protected Object currentPlayer;
	
	public RenderSmallArm(){
	}
	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		if(type==ItemRenderType.EQUIPPED_FIRST_PERSON || type==ItemRenderType.EQUIPPED)
			return true;
		return false;
	}
	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{return false;}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		glPushMatrix();
		float recoil= getRecoil(data[1]);
		currentPlayer= data[1];
		this.bindTexture();

		if(data[1]!=null && data[1]==mc.thePlayer
				&&	mc.gameSettings.thirdPersonView==0){
			transform1stPerson();
			transformRecoil(recoil);
		}
		else
			transform3rdPerson();
		
		if(item.stackTagCompound.hasKey(ItemAttachment.subnames[ItemAttachment.LASSIGHT]))
			RenderLaser.render(new Vector3(.2,.1,1.5), new Vector3(100, -5.7, -5.6), 0xff0000);
		preRender(recoil);
		doRendering(recoil);
		
		glPopMatrix();
	}
	/**any special GL calls you want done do here*/
	protected void preRender(float r){}
	
	protected void doRendering(float r){
		if(Barrel!=null)
			Barrel.render(getScale());

		glScalef(getWidthSqrt(), 1f, 1f);
		if(this.Mag!=null)
			Mag.render(getScale());		
		glScalef(getWidthSqrt(), 1f, 1f);//called twice so mag is thicker than barrel
		Main.render(getScale());

		this.transformBolt(r);
		Bolt.render(getScale());
	}
	protected void transformRecoil(float r){
		glTranslatef(0, -r*.2f, -r*.4f);
		glRotatef(r*12, 1, 0, 0);
	}
	protected void transformBolt(float r){
		glTranslatef(0f,0f,-r*getScale()*3);
	}
	
	
	protected abstract float getScale();
	protected float getWidthSqrt(){
		return 1.00f;	}
	
	protected float getRecoil(Object player){
		float ret=0;
		if(player!=null)
			ret=T1AClientProxy.getRecoilForEntity( (Entity)player, getRecoilMultiplier());
		return ret;
	}
	public abstract float getRecoilMultiplier();
	
	private ResourceLocation rsl= new ResourceLocation("t1a:textures/"+getTexName()+".png");
	private void bindTexture(){
		Minecraft.getMinecraft().renderEngine.bindTexture(rsl);
	}
	
	protected abstract String getTexName();

	protected abstract void transform1stPerson();
	protected abstract void transform3rdPerson();
}
