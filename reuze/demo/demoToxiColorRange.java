package reuze.demo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import processing.core.PApplet;
import reuze.vc_a_CriteriaAccess;
import reuze.vc_ColorList;
import reuze.vc_ColorRange;
import reuze.vc_ColorTheoryRegistry;
import reuze.vc_i_ColorTheoryStrategy;
import reuze.vc_e_Colors;
import reuze.vc_ComparatorProximity;
import reuze.vc_DistanceRGB;

public class demoToxiColorRange  extends PApplet {
	float   SWATCH_HEIGHT = 24;
	float   SWATCH_WIDTH = 5;
	int     SWATCH_GAP = 1;

	float   MAX_SIZE = 150;
	int     NUM_DISCS = 300;

	boolean showDiscs=true;

	public void setup() {
	  size(1024, 768);
	  noLoop();
	  textFont(createFont("arial", 9));

	}

	public void draw() {
	  background(0);
	  vc_ColorList list = new vc_ColorList();
	  for (int i = 0; i < 100; i++) {
	    list.add(vc_e_Colors.newRandom());
	  }
	  int yoff = 10;
	  swatches(list, 10, yoff);
	  yoff += SWATCH_HEIGHT + 10;
	  vc_ColorList sorted = null;
	  sorted = list.clusterSort(vc_a_CriteriaAccess.HUE,
	  vc_a_CriteriaAccess.BRIGHTNESS, 12, true);
	  sorted = list.sortByComparator(new vc_ComparatorProximity(
	  vc_e_Colors.getColor("BLUE"), new vc_DistanceRGB()), false);
	  swatches(sorted, 10, yoff);
	  yoff += SWATCH_HEIGHT + 10;
	  sorted = list.sortByDistance(false);
	  swatches(sorted, 10, yoff);
	  yoff += SWATCH_HEIGHT + 10;
	  sorted = list.sortByDistance(new vc_DistanceRGB(), false);
	  swatches(sorted, 10, yoff);
	  yoff += SWATCH_HEIGHT + 10;
	  //sorted = list.sortByDistance(new CMYKDistanceProxy(), false);
	  //swatches(sorted, 10, yoff);
	  //yoff += SWATCH_HEIGHT + 10;

	  vc_e_Colors col = vc_e_Colors.newHSV(random(1), random(0.75f, 1), random(1));
	  int idx = 0;
	  yoff = 10;
	  ArrayList strategies=vc_ColorTheoryRegistry.getRegisteredStrategies();
	  for (Iterator i = strategies.iterator(); i.hasNext();) {
	    vc_i_ColorTheoryStrategy s = (vc_i_ColorTheoryStrategy) i.next();
	    sorted = vc_ColorList.createUsingStrategy(s, col);
	    sorted = sorted.sortByDistance(false);
	    swatches(sorted, 900, yoff);
	    yoff += SWATCH_HEIGHT + 10;
	    idx++;
	  }
	  yoff = 260;
	  col = vc_e_Colors.newRandom();
	  Collection ranges=vc_ColorRange.PRESETS.values();
	  for (Iterator i = ranges.iterator(); i.hasNext();) {
	    vc_ColorRange range = (vc_ColorRange) i.next();
	    sorted = range.getColors(col, 100, 0.1f);
	    sorted = sorted.sortByCriteria(vc_a_CriteriaAccess.BRIGHTNESS, false);
	    swatches(sorted, 10, yoff);
	    fill(255);
	    text(range.getName(), 15 + 100 * (SWATCH_WIDTH + SWATCH_GAP), yoff + SWATCH_HEIGHT);
	    yoff += SWATCH_HEIGHT + 10;
	  }
	  vc_ColorRange range = vc_ColorRange.FRESH.getSum(vc_ColorRange.BRIGHT).add(
	  vc_ColorRange.LIGHT).add(vc_e_Colors.getColor("WHITE"));
	  sorted = range.getColors(vc_e_Colors.getColor("MAGENTA"), 100, 0.35f);
	  sorted = sorted.sortByDistance(false);
	  swatches(sorted, 10, yoff);
	  yoff += SWATCH_HEIGHT + 10;
	  range = new vc_ColorRange(vc_ColorTheoryRegistry.SPLIT_COMPLEMENTARY
	    .createListFromColor(vc_e_Colors.getColor("YELLOW")));
	  sorted = range.getColors(100).sortByDistance(false);
	  swatches(sorted, 10, yoff);
	  yoff += SWATCH_HEIGHT + 10;
	}

	public void keyPressed() {
	  redraw();
	}


	void swatch(vc_e_Colors c, int x, int y) {
	  fill(c.toARGB());
	  rect(x, y, SWATCH_WIDTH, SWATCH_HEIGHT);
	}

	void swatches(vc_ColorList sorted, int x, int y) {
	  noStroke();
	  for (Iterator i = sorted.iterator(); i.hasNext();) {
	    vc_e_Colors c = (vc_e_Colors) i.next();
	    swatch(c, x, y);
	    x += SWATCH_WIDTH + SWATCH_GAP;
	  }
	}
}