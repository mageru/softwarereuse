package reuze;

/*
 *   __               .__       .__  ._____.           
 * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
 * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
 *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
 *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
 *                   \/       \/             \/     \/ 
 *
 * Copyright (c) 2006-2011 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

public class gb_MeshVoxelizer {

    protected gb_a_VoxelSpace volume;
    protected int wallThickness = 0;

    public gb_MeshVoxelizer(int res) {
        this(res, res, res);
    }

    public gb_MeshVoxelizer(int resX, int resY, int resZ) {
        volume = new gb_VoxelSpaceHashMap(new gb_Vector3(1, 1, 1), resX, resY, resZ,
                0.1f);
    }

    public gb_MeshVoxelizer clear() {
        volume.clear();
        return this;
    }

    /**
     * @return the volume
     */
    public gb_a_VoxelSpace getVolume() {
        return volume;
    }

    /**
     * @return the wallThickness
     */
    public int getWallThickness() {
        return wallThickness;
    }

    protected void setVoxelAt(int x, int y, int z, float iso) {
        int mix = m_MathUtils.max(x - wallThickness, 0);
        int miy = m_MathUtils.max(y - wallThickness, 0);
        int miz = m_MathUtils.max(z - wallThickness, 0);
        int max = m_MathUtils.min(x + wallThickness, volume.resX1);
        int may = m_MathUtils.min(y + wallThickness, volume.resY1);
        int maz = m_MathUtils.min(z + wallThickness, volume.resZ1);
        for (z = miz; z <= maz; z++) {
            for (y = miy; y <= may; y++) {
                for (x = mix; x <= max; x++) {
                    volume.setVoxelAt(x, y, z, iso);
                }
            }
        }
    }

    /**
     * @param wallThickness
     *            the wallThickness to set
     */
    public gb_MeshVoxelizer setWallThickness(int wallThickness) {
        this.wallThickness = wallThickness;
        return this;
    }

    private gb_a_VoxelSpace solidifyVolume(gb_VoxelSpace volume) {
        for (int z = 0; z < volume.resZ; z++) {
            for (int y = 0; y < volume.resY; y++) {
                boolean isFilled = false;
                int startX = 0;
                for (int x = 0; x < volume.resX; x++) {
                    float val = volume.getVoxelAt(x, y, z);
                    if (val > 0) {
                        if (!isFilled) {
                            startX = x;
                            isFilled = true;
                        } else {
                            for (int i = startX; i <= x; i++) {
                                volume.setVoxelAt(i, y, z, 1);
                            }
                            isFilled = false;
                        }
                    }
                }
            }
        }
        return volume;
    }

    public gb_a_VoxelSpace voxelizeMesh(gb_i_Mesh mesh) {
        return voxelizeMesh(mesh, 1f);
    }

    public gb_a_VoxelSpace voxelizeMesh(gb_i_Mesh mesh, float iso) {
        gb_AABB3 box = mesh.getBoundingBox();
        gb_Vector3 bmin = box.getMin();
        gb_Vector3 bmax = box.getMax();
        m_ScaleMap wx = new m_ScaleMap(bmin.x, bmax.x, 1, volume.resX - 2);
        m_ScaleMap wy = new m_ScaleMap(bmin.y, bmax.y, 1, volume.resY - 2);
        m_ScaleMap wz = new m_ScaleMap(bmin.z, bmax.z, 1, volume.resZ - 2);
        m_ScaleMap gx = new m_ScaleMap(1, volume.resX - 2, bmin.x, bmax.x);
        m_ScaleMap gy = new m_ScaleMap(1, volume.resY - 2, bmin.y, bmax.y);
        m_ScaleMap gz = new m_ScaleMap(1, volume.resZ - 2, bmin.z, bmax.z);
        volume.setScale(box.getExtent().mul(2f));
        gb_Triangle tri = new gb_Triangle();
        gb_AABB3 voxel = new gb_AABB3(new gb_Vector3(), new gb_Vector3(volume.voxelSize).mul(0.5f));
        for (gb_TriangleFace f : mesh.getFaces()) {
            tri.a = f.a;
            tri.b = f.b;
            tri.c = f.c;
            gb_AABB3 bounds = tri.getBoundingBox();
            gb_Vector3 min = bounds.getMin();
            gb_Vector3 max = bounds.getMax();
            min = new gb_Vector3((int) wx.getClippedValueFor(min.x),
                    (int) wy.getClippedValueFor(min.y),
                    (int) wz.getClippedValueFor(min.z));
            max = new gb_Vector3((int) wx.getClippedValueFor(max.x),
                    (int) wy.getClippedValueFor(max.y),
                    (int) wz.getClippedValueFor(max.z));
            for (int z = (int) min.z; z <= max.z; z++) {
                for (int y = (int) min.y; y <= max.y; y++) {
                    for (int x = (int) min.x; x <= max.x; x++) {
                        if (x < volume.resX1 && y < volume.resY1
                                && z < volume.resZ1) {
                            voxel.set((float) gx.getClippedValueFor(x),
                                    (float) gy.getClippedValueFor(y),
                                    (float) gz.getClippedValueFor(z));
                            if (voxel.intersectsTriangle(tri)) {
                                setVoxelAt(x, y, z, iso);
                            }
                        }
                    }
                }
            }
        }
        return volume;
    }
}
