package reuze;

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

public class ga_PolygonsThatCollinearOverlap{
	public ga_Polygon polygon;
	public int pointIndex;
	public int nextPointIndex;
	public ga_Polygon polygon2;
	public int point2Index;
	public ga_PolygonsThatCollinearOverlap(ga_Polygon polygon, int pointIndex, int nextPointIndex, ga_Polygon polygon2, int point2Index){
		this.polygon = polygon;
		this.pointIndex = pointIndex;
		this.nextPointIndex = nextPointIndex;
		this.polygon2 = polygon2;
		this.point2Index = point2Index;
	}

	public ga_Polygon getPolygon() {
		return polygon;
	}

	public int getPointIndex() {
		return pointIndex;
	}
	public int getNextPointIndex() {
		return nextPointIndex;
	}
	public ga_Polygon getPolygon2() {
		return polygon2;
	}
	public int getPoint2Index() {
		return point2Index;
	}

	
	

}