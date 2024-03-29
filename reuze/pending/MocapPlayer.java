package reuze.pending;




import java.util.ArrayList;
import java.util.List;

import reuze.vam_BonesData;
import reuze.gb_Vector3;

/**
 * Plays a mocap animation, the "motor" that drives every skeleton.
 * Needs the skeleton and the animation data (usually mocap data).
 * The method update(fps) must be called periodically to make animation run.
 * 
 * @author Michael Kipp
 */
public class MocapPlayer
{

    private static final float DEFAULT_FPS = 90;
    private boolean _isPlaying = false;
    private Bone _root;
    private Bone[] _bones; // skeleton
    private double _time = 0;
    private float _fps;  // playback fps
    private int _frame = -1;  // current frame
    private vam_BonesData _animData;
    private gb_Vector3 _offset;
    private boolean _pinAtRoot = false;

    public MocapPlayer(Bone skeleton, vam_BonesData dat, gb_Vector3 offset)
    {
        _fps = DEFAULT_FPS;
        _animData = dat;
        _offset = offset;
        _root = skeleton;
        initSkeleton(skeleton);
    }

    private void initSkeleton(Bone skeleton)
    {
        List<Bone> ls = new ArrayList<Bone>();
        skeleton.collectBones(ls);
        _bones = new Bone[ls.size()];
        _bones = (Bone[]) ls.toArray(_bones);
    }

    private int mapTimeToFrame(double time)
    {
        return (int) Math.max(time * _fps, 0);
    }

    public Bone[] getBones()
    {
        return _bones;
    }

    /**
     * When set, figure's root will not move at all, the figure is "pinned"
     * at its root joint (usually the hip).
     * 
     * @param val whether to pin or not
     */
    public void setPinAtRoot(boolean val) {
        _pinAtRoot = val;
    }

    public void gotoTime(double sec)
    {
        int f = mapTimeToFrame(sec);
        gotoFrame(f < _animData.getNumFrames() ? f : _animData.getNumFrames() - 1);
    }

    private void gotoFrame(int frame)
    {
        if (_animData != null) {
            for (int i = 0; i < _bones.length; i++) {
                Bone bone = _bones[i];

                if (bone != _root || !_pinAtRoot) {
                    bone.setPose(frame, _animData.getBoneData(bone.getIndex()), _offset);
                }
            }
            _frame = frame;
            /*for (PlayerFrameListener li : _listeners) {
                li.frameUpdate(frame);
            }*/
        }
    }

    public int getNumFrames()
    {
        return _animData.getNumFrames();
    }

    public int getCurrentFrame()
    {
        return _frame;
    }

    /**
     * Intended playback speed of the motion capture fragment.
     * @param fps
     */
    public void setPlaybackFps(float fps)
    {
        _fps = fps;
        _time = _frame / _fps;
    }

    public float getPlaybackFps()
    {
        return _fps;
    }

    public void setIsPlaying(boolean val)
    {
        if (val && _animData != null) {
            _isPlaying = true;
        } else {
            _isPlaying = false;
        }
    }

    public boolean isPlaying()
    {
        return _isPlaying;
    }

    public void reset()
    {
        _isPlaying = false;
        _time = 0;
        _frame = -1;
        gotoFrame(0);
    }

    public void frameForward()
    {
        _frame++;
        if (_frame >= _animData.getNumFrames()) {
            _frame = 0;
        }
        _time = _frame / _fps;
        gotoFrame(_frame);
    }

    public void frameBackward()
    {
        _frame--;
        if (_frame < 0) {
            _frame = _animData.getNumFrames() - 1;
        }
        _time = _frame / _fps;
        gotoFrame(_frame);
    }

    /**
     * Converts the system's fps to the intended playback fps und changes
     * animation frames accordingly. Usually system speed is higher (and
     * is also an upper limit to the playback speed).
     * 
     * @param fps The fps speed of the system
     */
    public void update(float fps)
    {
        if (_isPlaying) {
            _time += 1 / fps;  // time in seconds since setIsPlaying
            int frame = (int) (_time * _fps); // frame in animation
            if (frame >= _animData.getNumFrames()) {
                _time = 0;
                _frame = -1;
            } else if (frame > _frame) {
                gotoFrame(frame);
                _frame = frame;
            }
        }
//        return _frame;
    }
}
