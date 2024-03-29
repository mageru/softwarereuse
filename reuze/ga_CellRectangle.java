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


/**
 *
 * @author Keith
 */
public class ga_CellRectangle {
	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;
	int orientation;	// HORIZONTAL or VERTICAL
	ga_Cell cell;	// the top or left cell
	ga_Cell cell2;	// the bottom or right cell
	ga_CellLinks point;	// the left or bottom point
	ga_CellLinks point2;	// the right or top point

	public boolean isBorder(){
		boolean cellDiscovered = (cell == null ? true : cell.isDiscovered());
		boolean cell2Discovered = (cell2 == null ? true : cell2.isDiscovered());
		if (cellDiscovered == true && cell2Discovered == false ||
				cellDiscovered == false && cell2Discovered == true){
			return true;
		}
		return false;
	}
	
	public ga_Cell getCell() {
		return cell;
	}
	
	public ga_Cell getCell2() {
		return cell2;
	}

	public ga_CellLinks getPoint() {
		return point;
	}

	public ga_CellLinks getPoint2() {
		return point2;
	}

	public int getOrientation() {
		return orientation;
	}

	public void setCell(ga_Cell cell) {
		this.cell = cell;
	}

	public void setCell2(ga_Cell cell2) {
		this.cell2 = cell2;
	}

	public void setPoint(ga_CellLinks point) {
		this.point = point;
	}

	public void setPoint2(ga_CellLinks point2) {
		this.point2 = point2;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

}
