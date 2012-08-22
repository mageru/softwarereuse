package reuze.demo;
import processing.core.PApplet;
import reuze.f_Handle;
import reuze.ied_ParticleEffect;
import reuze.dg_ParticleEmitter;
import reuze.vgu_SpritePS;
import reuze.vgu_e_ToxiclibsSupport;
import reuze.ga_Vector2;

public class demoToxiEffectParticles  extends PApplet {

	ied_ParticleEffect pe;
	dg_ParticleEmitter em;
	vgu_SpritePS awt;
	vgu_e_ToxiclibsSupport g;
	boolean stop=true;
	public void setup() {
	  size(640, 720, P2D);
	  pe=new ied_ParticleEffect();
	  g = new vgu_e_ToxiclibsSupport(this);
	  awt = new vgu_SpritePS();
	  vgu_SpritePS.g = g;
	  pe.load(new f_Handle("test.p"), new f_Handle("../data/"));
	  pe.setPosition(width/2, height/2);
	  stop=false;
	}

	public void draw() {
		background(0);
		if (stop || pe==null || awt==null) return;
		pe.draw(awt);
		pe.update(0.04f);
	}

	public void keyPressed() {

	}

	// Add a vertex!
	public void mousePressed() {
	  ga_Vector2 mouse = new ga_Vector2(mouseX, mouseY);
	}

}