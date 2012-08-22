package reuze;


import reuze.ga_Triangle2D;

import java.util.List;


public interface ga_i_PolygonTesselator {

    /**
     * Tesselates the given polygon into a set of triangles.
     * 
     * @param poly
     *            polygon
     * @return list of triangles
     */
    public List<ga_Triangle2D> tesselatePolygon(ga_Polygon poly);

}