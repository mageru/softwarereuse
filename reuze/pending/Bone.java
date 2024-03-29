package reuze.pending;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import javax.media.j3d.Node;
import javax.media.j3d.SceneGraphPath;
import javax.media.j3d.Switch;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;

import reuze.gb_Vector3;

/**
 * Bone representation tailored to Acclaim format. Transform groups in this order (first is base):
 * 
 * 1) base translation 2) translation (only used for root) 2) base rotation (joint geom attached
 * here) 3) current rotation 4) inverse base rotation (bone geom attached here) 3) final
 * translation, using direction + length from ASF definition (the translation could also be
 * performed at the root of the child node -> then you could omit the first base translation)
 * 
 * Note: Euler rotations performed with fixed axes.
 * 
 * @author Michael Kipp
 */
public class Bone
{

    public static final int FIXED_AXES = 0, MOVING_AXES = 1;
    public static final int RX = 0, RY = 1, RZ = 2; // for specifying rotation order
    public static final int TX = 3, TY = 4, TZ = 5;
    public static final int NO_NAME = -1, SMALL_NAME = 0, BIG_NAME = 1;
    private String _name;
    private int _index;
    private Bone _parent;
    private Bone[] _children = new Bone[0];
    /*private BoneGeom _boneGeom; // the displayed geometry of the bone/joint
    private JointGeom _jointGeom;
    private Switch _textSwitch;
    private TransformGroup _baseTransTG, _transTG, _baseRotTG, _rotTG,
            _invBaseRotTG;
    private Transform3D _t1 = new Transform3D();
    private Transform3D _t2 = new Transform3D();
    private Transform3D _transTF = new Transform3D();
    private Transform3D _tworld = new Transform3D();*/
    private gb_Vector3 _trans = new gb_Vector3();
    private int[] _dof = new int[0]; // rotation order
    private int _numDOF = 0;
    private boolean _translationEnabled = true; // translation can be switched off
    private boolean _rotationEnabled = true; // rotation can be switched off
    private int _rotationType = FIXED_AXES; // Euler rotation type
    private double _scale = 1d; // scale is applied to all translational components
    public BoneJ3d j3d;
    public Bone()
    {
        j3d=new BoneJ3d();
        /*_baseTransTG = new TransformGroup();
        _baseTransTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        _baseTransTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        _transTG = new TransformGroup();
        _transTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        _baseRotTG = new TransformGroup();
        _baseRotTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        _baseRotTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        //        _baseRotTG.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
        _baseRotTG.setCapability(TransformGroup.ALLOW_LOCAL_TO_VWORLD_READ);
        _rotTG = new TransformGroup();
        _rotTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        _invBaseRotTG = new TransformGroup();
        _invBaseRotTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        // wire up TG's
        _baseTransTG.addChild(_transTG);
        _transTG.addChild(_baseRotTG);
        _baseRotTG.addChild(_rotTG);
        _rotTG.addChild(_invBaseRotTG);*/
    }

    public Bone(String name, int index, int[] dof)
    {
        this();
        _name = name;
        _index = index;
        _dof = dof;
        _numDOF = _dof.length;
    }

    public void scale(double factor)
    {
        _scale = factor;
        /*_baseTransTG.getTransform(_t1);
        _t1.get(_trans);
        _trans.scale(factor);
        _t1.set(_trans);
        _baseTransTG.setTransform(_t1);*/
        j3d.scale(factor, _trans);
        for (Bone b : _children) {
            b.scale(factor);
        }
        //        _boneGeom.detach();
        //        _jointGeom.detach();
    }

    public double getScale()
    {
        return _scale;
    }

    public void getWorldPosition(gb_Vector3 p)
    {
        /*p.set(0, 0, 0);
        Util.getFullTransform(_baseRotTG, _tworld);
        //        getWorldTransform(_tworld);
        _tworld.transform(p);*/
    	j3d.getWorldPosition(p);
    }

    /**
     * Ignores rotations and assumes that coord system is only translated from joint to joint.
     *
     * @param ref
     * @param p
     */
    public void getRelativeTransPosition(Bone ref, gb_Vector3 p)
    {
        getWorldPosition(p);
        gb_Vector3 p2 = new gb_Vector3();
        ref.getWorldPosition(p2);
        p.sub(p2);
    }

    /**
     * Returns position of this bone in frame of reference of given bone.
     *
     * Note that rotations in the kinematic chain can render this information useless.
     *
     * @param ref
     * @param p
     */
    public void getRelativePosition(Bone ref, gb_Vector3 p)
    {
        j3d.getRelativePosition(ref, p);

        //        Transform3D t1 = new Transform3D();
        //        Transform3D t2 = new Transform3D();
        //        Transform3D t3 = new Transform3D();
        //        getWorldTransform(t1);
        //        ref.getWorldTransform(t2);
        //        t2.invert();
        //        t3.mul(t2, t1);
        ////        t1.mul(t1, t2);
        //        t3.transform(p);
    }

    //    public void getWorldTransform(Transform3D tf) {
    //        _rotTG.getLocalToVworld(tf);
    //    }

    /**
     * Determines whether to rotate in fixed or moving axes manner (Euler rotation).
     *
     * @param type
     *            Either FIXED_AXES or MOVING_AXES.
     */
    public void setRotationType(int type)
    {
        _rotationType = type;
    }

    public void setSelected(boolean val)
    {
        j3d.setSelected(val);
    }

    public String getDOFString()
    {
        String res = "";
        for (int i = 0; i < _dof.length; i++) {
            switch (_dof[i]) {
                case RX:
                    res += "RX ";
                    break;
                case RY:
                    res += "RY ";
                    break;
                case RZ:
                    res += "RZ ";
                    break;
                case TX:
                    res += "TX ";
                    break;
                case TY:
                    res += "TY ";
                    break;
                case TZ:
                    res += "TZ ";
                    break;
            }
        }
        return res;
    }

    /**
     * Sets bone and children to zero rotation.
     */
    public void reset()
    {
        j3d.reset();
        for (Bone b : _children) {
            b.reset();
        }
    }

    /**
     * Switches on/off translation.
     */
    public void setTranslationEnabled(boolean val)
    {
        _translationEnabled = val;
    }

    /**
     * Switches on/off orientation/rotation.
     */
    public void setRotationEnabled(boolean val)
    {
        _rotationEnabled = val;
    }

    public double getLength()
    {
        return j3d.getLength();
    }

    public void selectGeom(int style)
    {
        j3d.selectGeom(style);
        for (int i = 0; i < _children.length; i++) {
            _children[i].selectGeom(style);
        }
    }

    public void selectJointGeom(int style)
    {
        selectJointGeom(style, true);
    }

    public void selectJointGeom(int style, boolean recursiv)
    {
        j3d.selectJointGeom(style);

        if (recursiv) {
            for (int i = 0; i < _children.length; i++) {
                _children[i].selectJointGeom(style);
            }
        }
    }

    public double getMaxDistance()
    {
//		System.out.println(">>>>>> Branch >>>>>");
        double dDistance = 0;
        if (_children.length > 0) {
            gb_Vector3 p3dCurrentBone = new gb_Vector3();
            getWorldPosition(p3dCurrentBone);
            double[] arrayDistances = new double[_children.length];
            int i = 0;

            for (Bone bone : _children) {
                double dTmp = 0;
                if (bone.getChildren().length == 0) {
//					System.out.println(">>>>>> leaf >>>>>\n\n");
                    gb_Vector3 p3dChild = new gb_Vector3();
                    bone.getWorldPosition(p3dChild);
                    dTmp = p3dChild.dst2(p3dCurrentBone);
//					System.out.println("Distance to leaf (" + _name + ", "
//							+ bone.getName() + ") : " + dTmp);
//					


                } else {

                    gb_Vector3 p3dChild = new gb_Vector3();
                    bone.getWorldPosition(p3dChild);
                    dTmp = p3dChild.dst2(p3dCurrentBone);
//					System.out.println("Distance to node (" + _name + ", "
//							+ bone.getName() + ") : " + dTmp);
//					double dRest = bone.getMaxDistance();
//					System.out.println("Distance of rest (" + _name + ", "
//							+ bone.getName() + ") :: " + dRest);
                    dTmp += bone.getMaxDistance();
                    ;
//					System.out.println("Distance of node (" + _name + ", "
//							+ bone.getName() + ") + rest:: " + dTmp  +"\n\n");
                }
                arrayDistances[i++] = dTmp;
            }

            for (double distance : arrayDistances) {
                dDistance = Math.max(distance, dDistance);
            }

        }

        return dDistance;

    }

    public Bone getParent()
    {
        return _parent;
    }

    public void setParent(Bone parent)
    {
        _parent = parent;
    }

    public Bone[] getChildren()
    {
        return _children;
    }

    public String getName()
    {
        return _name;
    }

    public void setName(String name)
    {
        _name = name;
    }

    public void setChildren(Bone[] children)
    {
        _children = children;
        for (int i = 0; i < children.length; i++) {
            Bone bone = children[i];
            j3d.getEndTG().addChild(bone.j3d.getBaseTG());
        }
    }

    /**
     * This does not work for some reason.
     */
    public void setRot(float x, float y, float z)
    {
        System.out.println("setRot " + x + " " + " " + y + " " + z);
        j3d._t1.setIdentity();
        j3d._t2.setIdentity();
        for (int i = 0; i < _numDOF; i++) {
            switch (_dof[i]) {
                case RX:
                    j3d._t2.rotX(x);
                    break;
                case RY:
                    j3d._t2.rotY(x);
                    break;
                case RZ:
                    j3d._t2.rotZ(x);
                    break;
            }
            // multiply rotations
            // fixed axes: multiply from left
            if (_rotationType == FIXED_AXES) {
                if (i > 0) {
                    j3d._t2.mul(j3d._t1);
                }
                j3d.set(j3d._t1, j3d._t2);
            } else {
                // moving axes: multiply from right
                j3d._t1.mul(j3d._t2);
                j3d._t2.setIdentity();
            }
            // set rotation
            if (_rotationEnabled) {
                j3d.setRotation(j3d._t1);
            }
        }
    }

    public void setFrame(int frame, float[] data)
    {
        setPose(frame, data, null);
    }

    /**
     * Sets this bone/joint to the translation/rotation of the given frame. 
     * Called in the animation loop.
     *
     * @param frame
     *            Frame number
     */
    public void setPose(int frame, float[] data, gb_Vector3 offsetTrans)
    {
        //        System.out.println("setPose " + frame);
        //        _lastFrame = frame;
        // do something if joint has at least one DOF
        if (_numDOF > 0) {
            int offset = frame * _numDOF;
            if (offset + _numDOF - 1 < data.length) {
                boolean hasTranslation = false;
                if (_parent == null) {
                    _trans.set(0, 0, 0);
                }
                j3d._t1.setIdentity();

                // go through DOF in the specified order
                for (int i = 0; i < _numDOF; i++) {
                    boolean isRotation = false;
                    switch (_dof[i]) {
                        case RX:
                            j3d._t2.rotX(data[offset + i]);
                            isRotation = true;
                            break;
                        case RY:
                            j3d._t2.rotY(data[offset + i]);
                            isRotation = true;
                            break;
                        case RZ:
                            j3d._t2.rotZ(data[offset + i]);
                            isRotation = true;
                            break;
                        case TX:
                            _trans.x += _scale * data[offset + i];
                            hasTranslation = true;
                            isRotation = false;
                            break;
                        case TY:
                            _trans.y += _scale * data[offset + i];
                            hasTranslation = true;
                            isRotation = false;
                            break;
                        case TZ:
                            _trans.z += _scale * data[offset + i];
                            hasTranslation = true;
                            isRotation = false;
                            break;
                    }

                    // multiply rotations
                    if (isRotation) {
                        // fixed axes: multiply from left
                        if (_rotationType == FIXED_AXES) {
                            if (i > 0) {
                                j3d._t2.mul(j3d._t1);
                            }
                            j3d.set(j3d._t1, j3d._t2);
                        } else {
                            // moving axes: multiply from right
                            j3d._t1.mul(j3d._t2);
                            j3d._t2.setIdentity();
                        }
                    }
                }

                // set rotation
                if (_rotationEnabled) {
                    j3d.setRotation(j3d._t1);
                }

                // add offset to translation of root
                if (offsetTrans != null && _parent == null) {
                    _trans.add(offsetTrans);
                    hasTranslation = true;
                }

                // set translation
                // (currently only possible for root bone! see mocapdata.com data)
                if (_parent == null) {
                    if (hasTranslation && _translationEnabled) {
                        j3d.setTranslation(_trans);
                    }
                }
            }
        }
    }

    public int getIndex()
    {
        return _index;
    }

    public int[] getDOF()
    {
        return _dof;
    }

    /**
     * Attaches bone geometry (e.g. line) which has orientation and length of the given vector, also
     * attaches joint geometry to the beginning of this bone.
     */
    public void attachGeom(gb_Vector3 direction, double maxRadius)
    {
        j3d.attachGeom(_parent, direction, maxRadius);
    }

    public Bone findBone(String name)
    {
        if (_name.equals(name)) {
            return this;
        }
        for (int i = 0; i < _children.length; i++) {
            Bone bone = _children[i].findBone(name);
            if (bone != null) {
                return bone;
            }
        }
        return null;
    }

    public void collectBones(HashMap<String, Bone> map)
    {
        map.put(_name, this);
        for (int i = 0; i < _children.length; i++) {
            Bone bone = _children[i];
            bone.collectBones(map);
        }
    }

    public void collectBones(List<Bone> list)
    {
        list.add(this);
        for (int i = 0; i < _children.length; i++) {
            Bone bone = _children[i];
            bone.collectBones(list);
        }
    }

    public void setBaseTranslation(gb_Vector3 vec)
    {
        j3d.setBaseTranslation(vec);
    }

    public gb_Vector3 getUpVector()
    {
        return j3d.getUpVector();

    }

    /**
     * Sets the base rotation of the joint (specified as ":axis" in ASF). Note that this operation
     * is reversed in _invBaseRotTG so that the position of the child limb is not affected by the
     * base rotation.
     */
    public void setBaseRotDeg(gb_Vector3 eulerDegrees)
    {
        j3d.setBaseRotDeg(eulerDegrees);
    }

    /**
     * Select display style of bone's name.
     */
    public void displayName(int type)
    {
        j3d.displayName(type);
        for (int i = 0; i < _children.length; i++) {
            _children[i].displayName(type);
        }
    }

    /**
     * Adds geometry to the joints.
     */
    //    public void init() {
    //        _jointGeom = new JointGeom(_baseRotTG);
    //        for (Bone bone : _children) {
    //            bone.init();
    //        }
    //    }
    public String toString()
    {
        return "<Bone: " + _name + ">";
    }

    public void print()
    {
        System.out.println("  bone: " + _name + " dof=" + _numDOF + " -> "
                + (_parent == null ? "VOID" : _parent.getName()));
        for (int i = 0; i < _children.length; i++) {
            Bone bone = _children[i];
            bone.print();
        }
    }
}
