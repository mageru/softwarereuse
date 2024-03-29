package reuze.pending;

/*
 * Copyright (c) 2008, Keith Woodward
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of Keith Woodward nor the names
 *    of its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */


//import straightedge.geom.*;
//import straightedge.geom.path.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;

import reuze.ga_Polygon;
import reuze.ga_Vector2;


/**
 *
 * @author Phillip
 */
public class WorldObstacle extends GameWorld{
	protected ArrayList<ga_Polygon> makePolygons(){
		ArrayList<ga_Polygon> allPolys = new ArrayList<ga_Polygon>();
		for (int i = 0; i < 5; i++){
			for (int j = 0; j < 5; j++){
				ArrayList<ga_Vector2> pointList = new ArrayList<ga_Vector2>();
				pointList.add(new ga_Vector2(0, 0));
				pointList.add(new ga_Vector2(15, 5));
				pointList.add(new ga_Vector2(30, 0));
				pointList.add(new ga_Vector2(30, 30));
				pointList.add(new ga_Vector2(0, 30));
				ga_Polygon poly = new ga_Polygon(pointList);
				assert poly.isCounterClockWise();
				poly.translate(90 + 45*i, 50 + 45*j);
				//poly.rotate(i+j);
				allPolys.add(poly);
			}
		}

		for (int i = 0; i < 5; i++){
			for (int j = 0; j < 5; j++){
				ArrayList<ga_Vector2> pointList = new ArrayList<ga_Vector2>();
				int numPoints = 6;
				double angleIncrement = Math.PI*2f/(numPoints*2);
				float rBig = 22;
				float rSmall = 8;
				double currentAngle = 0;
				for (int k = 0; k < numPoints; k++){
					double x = rBig*Math.cos(currentAngle);
					double y = rBig*Math.sin(currentAngle);
					pointList.add(new ga_Vector2((float)x, (float)y));
					currentAngle += angleIncrement;
					x = rSmall*Math.cos(currentAngle);
					y = rSmall*Math.sin(currentAngle);
					pointList.add(new ga_Vector2((float)x, (float)y));
					currentAngle += angleIncrement;
				}
				ga_Polygon poly = new ga_Polygon(pointList);
				assert poly.isCounterClockWise();
				poly.rotate(i+j);
				poly.translate(330 + 45*i, 60 + 45*j);
				allPolys.add(poly);
			}
		}

		{
			ArrayList<ga_Vector2> pointList = new ArrayList<ga_Vector2>();
			pointList.add(new ga_Vector2(5, 25));
			pointList.add(new ga_Vector2(25, 25));
			pointList.add(new ga_Vector2(25, 5));
			pointList.add(new ga_Vector2(5, 0));
			pointList.add(new ga_Vector2(30, 0));
			pointList.add(new ga_Vector2(30, 30));
			pointList.add(new ga_Vector2(0, 30));
			pointList.add(new ga_Vector2(0, 0));
			ga_Polygon poly = new ga_Polygon(pointList);
			assert poly.isCounterClockWise();
			poly.translate(100, 350);
			poly.scale(3);
			allPolys.add(poly);
		}
		{
			ArrayList<ga_Vector2> pointList = new ArrayList<ga_Vector2>();

			pointList.add(new ga_Vector2(5, 25));
			pointList.add(new ga_Vector2(25, 25));
			pointList.add(new ga_Vector2(25, 5));
			pointList.add(new ga_Vector2(5, 0));
			pointList.add(new ga_Vector2(30, 0));
			pointList.add(new ga_Vector2(30, 30));
			pointList.add(new ga_Vector2(0, 30));
			pointList.add(new ga_Vector2(0, 25));
			pointList.add(new ga_Vector2(10, 5));
			ga_Polygon poly = new ga_Polygon(pointList);
			assert poly.isCounterClockWise();
			poly.translate(100, 550);
			poly.scale(3);
			allPolys.add(poly);
		}
		{
			ArrayList<ga_Vector2> pointList = new ArrayList<ga_Vector2>();
			pointList.add(new ga_Vector2(0, 0));
			pointList.add(new ga_Vector2(15, 5));
			pointList.add(new ga_Vector2(30, 0));
			pointList.add(new ga_Vector2(30, 30));
			pointList.add(new ga_Vector2(0, 30));
			ga_Polygon poly = new ga_Polygon(pointList);
			assert poly.isCounterClockWise();
			poly.scale(3);
			poly.translate(200, 350);
			allPolys.add(poly);
		}
		{
			ArrayList<ga_Vector2> pointList = new ArrayList<ga_Vector2>();
			pointList.add(new ga_Vector2(0, 0));
			pointList.add(new ga_Vector2(15, 5));
			pointList.add(new ga_Vector2(30, 0));
			pointList.add(new ga_Vector2(30, 30));
			pointList.add(new ga_Vector2(0, 30));
			ga_Polygon poly = new ga_Polygon(pointList);
			assert poly.isCounterClockWise();
			poly.scale(3);
			poly.rotate(1);
			poly.translate(270, 350);
			allPolys.add(poly);
		}
		{
			ArrayList<ga_Vector2> pointList = new ArrayList<ga_Vector2>();
			int numPoints = 6;
			double angleIncrement = Math.PI*2f/(numPoints*2);
			float rBig = 22;
			float rSmall = 8;
			double currentAngle = 0;
			for (int i = 0; i < numPoints; i++){
				double x = rBig*Math.cos(currentAngle);
				double y = rBig*Math.sin(currentAngle);
				pointList.add(new ga_Vector2((float)x, (float)y));
				currentAngle += angleIncrement;
				x = rSmall*Math.cos(currentAngle);
				y = rSmall*Math.sin(currentAngle);
				pointList.add(new ga_Vector2((float)x, (float)y));
				currentAngle += angleIncrement;
			}
			ga_Polygon poly = new ga_Polygon(pointList);
			assert poly.isCounterClockWise();
			poly.scale(3.5f);
			poly.rotate(2.5f);
			poly.translate(400, 350);
			allPolys.add(poly);
		}
		return allPolys;
	}
}
