package reuze.demo;
import java.util.Iterator;

import processing.core.PApplet;
import reuze.pma_BehaviorAttraction;
import reuze.ga_i_CoordinateExtractor;
import reuze.pma_BehaviorForceGravity;
import reuze.ga_Rectangle;
import reuze.gb_SpatialBins;
import reuze.vgu_e_ToxiclibsSupport;
import reuze.ga_Vector2;
import reuze.pma_ParticleVerlet;
import reuze.pma_PhysicsVerlet;

public class demoToxiParticleAttractor extends PApplet {
	vgu_e_ToxiclibsSupport gfx;

    int NUM_PARTICLES = 1000;

    pma_PhysicsVerlet physics;

    private ga_Vector2 mousePos;

    private pma_BehaviorAttraction mouseAttractor;

    private void addParticle() {
        pma_ParticleVerlet p = new pma_ParticleVerlet(ga_Vector2.random().mul(5)
                .add(width * 0.5f, 0));
        physics.addParticle(p);
        // add a negative attraction force field around the new particle
        physics.addBehavior(new pma_BehaviorAttraction(p, 20, -1.2f, 0.01f));
    }

    public void draw() {
        background(0);
        noStroke();
        fill(255);
        if (physics.particles.size() < NUM_PARTICLES) {
            addParticle();
        }
        physics.update();
        for (Iterator<pma_ParticleVerlet> i = physics.particles.iterator(); i
                .hasNext();) {
            pma_ParticleVerlet p = i.next();
            ellipse(p.x, p.y, 5, 5);
        }
        //text("fps: " + frameRate, 20, 20);
    }

    public void mouseDragged() {
        mousePos.set(mouseX, mouseY);
    }

    public void mousePressed() {
        mousePos = new ga_Vector2(mouseX, mouseY);
        mouseAttractor = new pma_BehaviorAttraction(mousePos, 400, 1.2f);
        physics.addBehavior(mouseAttractor);
    }

    public void mouseReleased() {
        physics.removeBehavior(mouseAttractor);
    }

    public void setup() {
        size(1024, 640, OPENGL);
        frameRate(999);
        gfx = new vgu_e_ToxiclibsSupport(this);
        physics = new pma_PhysicsVerlet();
        physics.setDrag(0.1f);
        physics.setWorldBounds(new ga_Rectangle(0, 0, width, height));
        physics.addBehavior(new pma_BehaviorForceGravity(new ga_Vector2(0, 0.15f)));
        physics.setIndex(new gb_SpatialBins<ga_Vector2>(0, width, 80,
                new ga_i_CoordinateExtractor<ga_Vector2>() {

                    public final float coordinate(ga_Vector2 p) {
                        return p.x;
                    }
                }));
        textFont(createFont("SansSerif", 10));
    }
}