package reuze;
//***********************************************************************\
//* CS20a: Lab Assignment #3.                           October 27, 1996  *
//*                                                                       *
//*                     Traveling Salesman Problem                        *
//*                                                                       *
//* This Java program defines an applet for exploring solutions to the    *
//* well-known Traveling Salesman Problem.  The program generates problem *
//* instances containing 4 to 100 cities, and searches for optimal or     *
//* near-optimal tours (i.e. short paths that visit all cities) either by *
//* exhaustive search, or using a heuristic.                              *
//*                                                                       *
//***********************************************************************/

import java.awt.*;
import java.applet.*;
import java.util.*;

//***********************************************************************\
//*                                                                       *
//* This class creates the interface for the Traveling Salesman applet    *
//* and handles all the events generated by the buttons and slider.       *
//*                                                                       *
//***********************************************************************/
public class da_OptimizationTSPExhaustive extends Applet 
    {
    private int           num_cities;
    private boolean       use_heuristic;
    private Display       disp;
    private Panel         top, bottom;
    private Button        random, fixed, step, reset, optim;
    private TextField     dist_field, tours_field, cities_field;
    private CheckboxGroup group;
    private Checkbox      e_mode, h_mode;
    private Scrollbar     slider;

    private static final int
        Steps  = 1000,        // How many new tours to try on each step.
        MinPts = 4,           // Allow no newer than this many cities.
        MaxPts = 100,         // Allow no more than this many cities.
        align  = Label.RIGHT; // Controls alignment of labels.

    private void markOptimal( boolean flag )
        {
        optim.setBackground( flag ? Color.red   : Color.gray     ); 
        optim.setForeground( flag ? Color.white : Color.darkGray );
        }

    public void init() 
        {
        num_cities = 16;         // Default number of cities.
        use_heuristic = false;  // Use exhaustive search by default.

        // Create the four top-level widgets that define the interface.

        disp   = new Display( num_cities );
        top    = new Panel();
        bottom = new Panel();
        slider = new Scrollbar( Scrollbar.VERTICAL, Flip(num_cities), 5, MinPts, MaxPts );


        setFont( new Font( "Helvetica", Font.BOLD, 12 ) );

        // Create the numerical readouts and their labels along the top.

        cities_field = new TextField( 4 ); // Show number of cities.
        dist_field   = new TextField( 8 ); // Show current distance.
        tours_field  = new TextField( 9 ); // Show number of tours examined.

        // Add the readouts and labels to the top panel from left to right.
 
        top.setLayout( new FlowLayout() );
        top.add( new Label( "Distance", align ) ); top.add( dist_field   );
        top.add( new Label( "Tours"   , align ) ); top.add( tours_field  );
        top.add( new Label( "Cities"  , align ) ); top.add( cities_field );

        // Create the buttons and check boxes along the bottom row.

        random = new Button( "Random Problem" );
        fixed  = new Button( "Static Problem" );
        step   = new Button( "Step"           );
        reset  = new Button( "Reset"          );
        optim  = new Button( "Optimal"        );
        group  = new CheckboxGroup();
        e_mode = new Checkbox( "Exh" , group, true  ); 
        h_mode = new Checkbox( "Heur", group, false ); 

        // Add the buttons to the bottom panel from left to right.

        bottom.setLayout( new FlowLayout() );
        bottom.add( random );
        bottom.add( fixed  );
        bottom.add( e_mode );
        bottom.add( h_mode );
        bottom.add( step   );
        bottom.add( reset  );
        bottom.add( optim  );

        // Organize the button rows and the drawing area.

        setLayout( new BorderLayout() );
        add( "North" , top    );
        add( "Center", disp   );
        add( "South" , bottom );
        add( "East"  , slider );

        // Set the text fields.

        markOptimal( false );
        dist_field  .setText( String.valueOf( disp.distance() ) );
        tours_field .setText( "1" );
        cities_field.setText( String.valueOf( num_cities ) );
        }

    // Handle all the button-push events, and the slider events.
    // Note that the type of the event (i.e. the "id") is tested in each 
    // case, so that events generated by the mouse simply entering or
    // leaving the button or scrollbar are ignored.  Only "action"
    // events need be processed.
    public boolean handleEvent( Event e ) 
        {
        // Catch Scrollbar events to set problem size and update the readout.
        if( e.target == slider && ( e.id == Event.SCROLL_LINE_UP   ||
                                    e.id == Event.SCROLL_LINE_DOWN ||
                                    e.id == Event.SCROLL_ABSOLUTE  ||
                                    e.id == Event.SCROLL_PAGE_UP   ||
                                    e.id == Event.SCROLL_PAGE_DOWN ) )
            {
            num_cities = Flip( slider.getValue() );
            cities_field.setText( String.valueOf( num_cities ) );
            }
        else if( e.target == step && e.id == Event.ACTION_EVENT )
            {
            disp.improve( Steps, use_heuristic );
			}			
        else if( e.target == reset && e.id == Event.ACTION_EVENT )
            {
            disp.reset();
            }
        else if( e.target == random && e.id == Event.ACTION_EVENT )
            {
            // Generate a new random problem instance.
            disp.randomProb( num_cities ); 
            }
        else if( e.target == fixed && e.id == Event.ACTION_EVENT )
            {
            // Get a new static problem instance.
            disp.staticProb( num_cities ); 
            }
        else if( e.target == e_mode && e.id == Event.ACTION_EVENT )
            {
            disp.reset();
            use_heuristic = false;
            }
        else if( e.target == h_mode && e.id == Event.ACTION_EVENT )
            {
            disp.reset();
            use_heuristic = true;
            }
        else return false;

        // Update readouts and indicators.
        markOptimal( disp.isOptimal() );
        dist_field .setText( String.valueOf( disp.distance() ) );
        tours_field.setText( String.valueOf( disp.configs () ) );
        return true;
        }

    // This is used for flipping the value associated with the vertical
    // scrollbar (or "slider") so that it increases going up instead of
    // down.
    private int Flip( int n )
        {
        return MaxPts - ( n - MinPts );
        }

    }

//***********************************************************************\
//*                                                                       *
//* This class handles the graphical output.  It creates a panel that     *
//* is used for drawing the cities and tours.  This class is an extension *
//* of the Solver class, so it has access to all the public methods and   *
//* data of the Solver.                                                   *
//*                                                                       *
//***********************************************************************/
class Display extends Solver
    {
    private int         VertRad = 4;  // Radius of the vertices.
    private int         Border  = 10; // Min dist from edge of display area.
    private int         X_size, Y_size;
    private TSP_Problem Problem;
    private Graphics    G;

    private static Color
        Bcolor = Color.darkGray,  // Color of background.
        Vcolor = Color.orange,    // Color of vertices.
        Lcolor = Color.green;     // Color of lines.

    public Display( int size ) // Constructor.
        {
        setBackground( Bcolor );
        Problem = new TSP_Problem();
        randomProb( size );
        }

    public boolean improve( int steps, boolean use_heuristic )
        {
        boolean improved = use_heuristic ? heuristicImprove ( steps ) 
                                         : exhaustiveImprove( steps );
        if( improved ) repaint();
        return improved;
        }

    public void randomProb( int size )
        {
        cities = Problem.randomProb( size );
        startSolver();
        repaint();
        }

    public void staticProb( int size )
        {
        cities = Problem.staticProb( size );
        startSolver();
        repaint();
        }

    public void start() 
        {
        //Rectangle R = bounds();
        X_size = 400;//R.width  - 2 * Border;
        Y_size = 400;//R.height - 2 * Border;
        }

    private void DrawCity( ga_Point P )
        {
        int diam = 2 * VertRad;
        int x = (int)( X_size * P.x ) + Border;
        int y = (int)( Y_size * P.y ) + Border;
        G.fillOval( x - VertRad, y - VertRad, diam, diam );
        }

    public void DrawCities() 
        {
        G.setColor( Vcolor );
        for( int i = 0; i < cities.size(); i++ )
            DrawCity( (ga_Point)cities.elementAt(i) );
        }

    public void paint( Graphics g ) 
        {
        G = g;
        start();
        clear();
        showTour();
        DrawCities();
        }

    public void reset()
        {
        resetTour();
        repaint();
        }

    public void clear()
        {
        /*TODO Rectangle rec = bounds();
        G.setPaintMode();
        G.setColor( Color.darkGray );
        G.fillRect( 0, 0, rec.width, rec.height );*/
        }

    public void showTour()
        {
        int x0, x1, x2;
        int y0, y1, y2;
        if( cities.isEmpty() ) return;
        Permutation P = currentTour();
        G.setColor( Lcolor );
        ga_Point p = (ga_Point)cities.lastElement();
        x0 = x1 = (int)( X_size * p.x ) + Border;
        y0 = y1 = (int)( Y_size * p.y ) + Border;
        for( int i = 0; i < P.size(); i++ )
            { 
            p = (ga_Point)cities.elementAt( P.index(i) );
            x2 = (int)( X_size * p.x ) + Border;
            y2 = (int)( Y_size * p.y ) + Border;
            G.drawLine( x1, y1, x2, y2 );
            x1 = x2;
            y1 = y2;
            }
        G.drawLine( x1, y1, x0, y0 );
        }
    }

//***********************************************************************\
//*                                                                       *
//* This class generates problem instances for the Traveling Salesman     *
//* Problem.  Each call to "randomProb" or "staticProb" will return a     *
//* Vector of 2D points in the unit square [0,1]x[0,1] which are          *
//* guaranteed to be separated by some small distance (i.e. the points    *
//* will not be too clumped up).                                          *
//*                                                                       *
//* Problems can either be generated randomly, or from a static set of    *
//* data.  The latter is useful for comparing different algorithms on     *
//* the same data.                                                        *
//*                                                                       *
//***********************************************************************/
class TSP_Problem
    {
    private double  SepDist = 0.04;  // Minimum separation of points.
    private Random  rng;
    private Vector  points;
    private int     seed = 75825;

    // The following data is stored explicitly so that the same
    // test problems can be generated reliably, even if the
    // random number generators differ among platforms.

    private int data[][] = { 
        {335,  21}, {137,  55}, {343, 254}, {120, 150}, {530,  77}, 
        { 98,  77}, { 34,  83}, {215, 264}, { 55, 221}, {126, 210},
        {421,  50}, {269, 234}, {499, 202}, { 45,  66}, {374, 235},
        {212,  94}, {382, 201}, {244, 190}, {291,  49}, {241, 275},
        {355,  19}, {386, 116}, { 16, 224}, {466,  88}, {297, 157},
        {227,  48}, {229, 245}, {472, 115}, {383, 147}, {157, 214},
        { 67, 243}, {381, 296}, { 53,  32}, {462, 285}, {429, 178},
        {558, 116}, {337, 274}, {126, 156}, {551, 290}, {341, 291},
        {357, 282}, {150, 119}, { 71, 304}, {450, 193}, {210, 119},
        { 66, 105}, { 84,  32}, {554, 129}, {177, 264}, {110, 168},
        {293, 100}, {195, 137}, { 32, 229}, { 85,  55}, {224,  25},
        {252,  35}, { 71, 21},  {211, 302}, {296, 141}, {328,  94},
        {144, 235}, {550, 266}, {118,  42}, {548,  98}, { 19,  28},
        {501, 111}, {528, 165}, {427, 259}, {479, 303}, {169, 225},
        {123, 191}, {142,  17}, {448, 139}, {555, 180}, {188, 155},
        {388, 173}, {405, 279}, {493,  76}, {238, 229}, {162,  66},
        {539, 253}, {304,  34}, {380,  11}, { 47, 127}, {269, 148},
        {117,  12}, {529,  12}, {322,  46}, {260, 206}, {280, 258},
        {365, 158}, {167, 196}, {461,  18}, {425,  57}, {181,  39},
        {328, 232}, {499, 126}, {316, 181}, {536, 145}, {266, 284}
        };

    public TSP_Problem()
        {
        points = new Vector();        // This will hold new problem instances.
        rng    = new Random( seed );  // This is a random number generator.
        }

    // Pick n random points in [0,1]x[0,1] that are separated by
    // a small distance (so that we don't get clumps of points).
    public Vector randomProb( int n )
        {
        ga_Point Q = new ga_Point();   // This is a temporary variable.
        points.removeAllElements();  // Blow away the old vector.
        for( int npts = 0; npts < n; )
            {
            boolean too_close = false;
            Q.set( rng.nextFloat(), rng.nextFloat() );
            for( int j = 0; j < npts; j++ )
                {
                ga_Point P = (ga_Point)points.elementAt(j);
                if( P.distance( Q ) < SepDist ) { too_close = true; break; }
                }
            if( !too_close ) 
                {
                // Create a new 2D point containing the coordinates of Q,
                // and add it to the growing vector of points.  This, in
                // effect, adds a pointer to Q, so it's important to create
                // a new Point2D object to hold the coordinates.
                points.addElement( new ga_Point( Q.x, Q.y ) );
                npts++;
                }
            }
        return points;
        }

    public Vector staticProb( int size )
        {
        points.removeAllElements();  // Blow away the old vector.
        for( int i = 0; i < size; i++ )
            {
            float x = (float)( data[i][0] * 0.00175 );
            float y = (float)( data[i][1] * 0.00325 );
            points.addElement( new ga_Point( x, y ) );
            }
        return points;
        }

    }

//***********************************************************************\
//*                                                                       *
//* The Point2D class implements simple two-dimensional coordinates.      *
//* Coordinates can be set in numerous ways: via the constructor or the   *
//* "set" method, and using either two floats or another point.  The      *
//* "dist" method computes the Euclidean distance between the given       *
//* point and another point.                                              *
//*                                                                       *
/**********************************************************************
class Point2D
    {
    public float x, y;
    public Point2D () { x = 0; y = 0; }
    public Point2D ( float a, float b ) { x = a; y = b; }
    public void set( float a, float b ) { x = a; y = b; }
    public Point2D ( Point2D p ) { x = p.x; y = p.y; }
    public void set( Point2D p ) { x = p.x; y = p.y; }
    public double dist( Point2D p )
        {
        float dx = x - p.x;
        float dy = y - p.y;
        return Math.sqrt( dx*dx + dy*dy );
        }
    }*/

//***********************************************************************\
//*                                                                       *
//* This class encapsulates integer permutations of any size.  Each       *
//* permutation is of the form (0,1,2,...,n-1), where n is the "size" of  *
//* permutation.  The method "next" steps the permutation through all     *
//* distinct configurations, hitting each one exactly once.  This is a    *
//* convenient method for generating all possible permutations of n       *
//* integers.                                                             *
//*                                                                       *
//***********************************************************************/
class Permutation
    {
    private int p[];
    private int n;
    private StringBuffer buff;

    // This creates a permutation of n integers, 0, 1, ... , n-1.
    public Permutation( int size )
        {
        n = size;
        p = new int[ n ];           // This array is the actual permutation.
        buff = new StringBuffer();  // Used for creating string representations.
        reset();
        }

    // Returns the number of elements in the permutation.  A value of n means that
    // the permutation consists of the integers (0,1,...,n-1).
    public int size()  
        {
        return n;
        }

    // Returns the integer in the last position of the current permutation.
    public int lastIndex()  
        {
        return p[ n - 1 ];
        }

    // Returns the integer in the i'th position of the current permutation.
    public int index( int i )
        {
        return p[i];
        }

    // Creates the "first" permutation, which is (0,1,...,n-1).
    public void reset() 
        {
        for( int i = 0; i < n; i++ ) p[i] = i;
        }

    // Copy another permutation into this one, even if it is a different size.
    // If the size if different, reallocate space for the integer array. 
    public void set( Permutation Q )
        {
        if( n != Q.size() )
            {
            n = Q.size();
            p = new int[ n ];  // The old array will be garbage collected.
            }
        for( int i = 0; i < n; i++ ) p[i] = Q.index(i);  // Copy the permutation Q.
        }

    // This method creates a representation of the permutation as a string.
    // This is useful for demos and for debugging, but is not normally needed.
    public String string()
        {
        buff.setLength(0); // Build up the string by appending.
        buff.append( "( " );
        for( int i = 0; i < n; i++ )
            {
            buff.append( p[i] );
            buff.append(  ' ' );
            }
        buff.append( ")" );
        return buff.toString();
        }


	public void create(int z[],int size)

	{

		for(int h=0;h<size;h++)

			p[h]=z[h];

	}


    // The method steps the permutation to the "next" configuration according
    // to a simple canonical ordering, where (0,1,...,n-1) is first, and
    // (n-1,...,1,0) is last.  This will hit each distinct permutation exactly
    // once.  Returns "true" unless the permutation has not advanced because
    // the last one had already been reached.  Call "reset" to start over.
    public boolean next()
        {
        int i, m, k = 0;
        int N, M = 0;
   
        // Look for the first element of p that is larger than its successor.
        // If no such element exists, we are done.

        M = p[0];                 // M is always the "previous" value.
        for( i = 1; i < n; i++ )  // Now start with second element.
            {
            if( p[i] > M ) { k = i; break; }
            M = p[i];
            }
        if( k == 0 ) return false; // Already in descending order.
        m = k - 1;

        // Find the largest entry before k that is less than p[k].
        // One exists because p[k] is bigger than M, which is p[k-1].

        N = p[k];
        for( i = 0; i < k - 1; i++ )
            {
            if( p[i] < N && p[i] > M ) { M = p[i]; m = i; }
            }
        swap( m, k ); // Entries 0..k-1 are still decreasing.
        reverse( k ); // Make first k elements increasing.
        return true;
        }

    private void swap( int i, int j )
        {
        int t = p[i];
        p[i] = p[j];
        p[j] = t;
        }

    // Reverse the order of the first n elements.
    private void reverse( int n )
        {
        int k = n >> 1;
        int m = n - 1;
        for( int i = 0; i < k; i++ ) swap( i, m - i );
        }
    }

//***********************************************************************\
//*                                                                       *
//* This class implements two different strategies for "solving" the      *
//* Traveling Salesman Problem: both a brute-force "exhaustive search",   *
//* and a more efficient "heuristic search".  Both strategies can be      *
//* run incrementally, so that the solution can be gradually		d.    *
//*                                                                       *
//* The problem instance is encoded in the Vector "cities", which holds   *
//* a list of 2D coordinates for the cities.  This vector is set by the   *
//* class that extends this one.  Thus, it is the responsibility of       *
//* this class to solve the problem, but not to set it up.                *
//*                                                                       *
//* Solutions are to be represented as Permutations that indicate the     *
//* order in which to visit all but one of the cities in the original     *
//* list.  The last city is kept fixed.                                   *
//*                                                                       *
//***********************************************************************/
class Solver extends Panel
    {
        public Vector cities;			// The list of city coordinates
	public Permutation current;		// The current path from city to city
	public Permutation best;		// The best permutation of city order
        public double totaldistance;	        // The distance of the best tour
	public double currentdistance;	        // distance of the current tour
	public long totalconfigs=0;		// total number of tours taken
	public int optimalflag;			// flag to keep track if all permutations tried
		

  public Solver()  // Constructor (optional).
        {
		  //initializations of flags and tours

		  totalconfigs = 0;	    
		  optimalflag = 0;
		}

    // Create two permutations to encode "tours" of the cities.  That is, 
    // each will indicate an order in which the cities are to be visited.  
    // One is used to hold the best tour found so far, and the other is
    // used as a "temporary" permutation for trying out new tours.
    // The permutations are one shorter than the list of cities, since
    // we can keep one city fixed to simplify the search.
    public void startSolver() 
        {
	  current = new Permutation(permutationsize());	
	  best = new Permutation(permutationsize());
	  resetTour();  		                // resets all variables
	  totaldistance = distanceof(best);       // sets the initial distance
	}

    // Return the distance associated with the best tour so far.
    public double distance()
  {
    return totaldistance;
  }
      
      // Return the number of tours (or "configurations") tested so far.
  public long configs()
  {
    return totalconfigs;
  }
      
      // Go back to the initial random tour by resetting the permutations 
      // back to the identity.  Also reset things like the distance.
  public void resetTour( )
  {
    best.reset();	       //sets best to initial configuration
    current.reset();       //sets current to initial configuration
    totaldistance = distanceof(best);
    totalconfigs=1;
    optimalflag=0;
  }
      
      // Try out a number of new tours (i.e. the number specified by "steps").
      // This is done in such a way that we will eventually try every possible tour
      // with no duplication.  If a better tour is found, save it along with its 
      // length and return true.  Otherwise, return false.
  public boolean exhaustiveImprove( int steps )
  {
    int indexer;	       // used to count how many tours taken
    
    for(indexer=1 ;indexer <= steps; ++indexer)
      {
	// calculate the current tours total distance
	currentdistance = distanceof(current);	 
	
	// check to see if current tour better than best
	if(currentdistance < totaldistance)
	  {
	    best = current;
	    totaldistance = distanceof(best);
	    return true;
	  }
	
	// checks to see if all permutations have been tried
	if(!current.next())	  
	  {
	    optimalflag=1;
	    return false;
	  }	       
	totalconfigs=totalconfigs + 1; // indexes the totaltours
      }
    
    
    return false;
  }
      
      // Try to construct a better tour using a heuristic in which "steps" 
      // indicates how much effort is to expended in the search.  If a better tour 
      // is found, save it along with its length and return true.  
      // Otherwise, return false.
  public boolean heuristicImprove( int steps )
  {
    int x,y;                       // used to travel through all cities
    int heuristic[];               // array used to keep order of cities visited
    int inlist[];                  // array to keep a flag if city has been travelled to
    ga_Point p1,p2;                 // points used to calc. distance between cities
    int currentcity,nextcity = 0;  // holds number of city
    double distancebetween=0, bestdistance = 10000;
    
    currentcity = cities.size() - 1;
    heuristic = new int[cities.size()-1];
    inlist = new int[cities.size()];
    
    for(x=0; x < cities.size()-1;x++)     // sets all visited flags to not visited
      inlist[x] = 1;
    
    inlist[currentcity]=0;
    
    for( x = 0; x < cities.size() - 1; x ++)
      {
	for( y = 0; y < cities.size() - 1; y++) //checks from current city to all othercities
	  {
	    if(inlist[y] != 0)                  //if already visited, don't test distance
	      {

		// checking distances
		if(currentcity == (cities.size()-1))
		  {
		    p1 = (ga_Point)cities.elementAt(cities.size()-1);
		    p2 = (ga_Point)cities.elementAt(current.index(y));
		    distancebetween = p1.distance(p2);
		  }
		else
		  {
		    p1 = (ga_Point)cities.elementAt(current.index(currentcity));
		    p2 = (ga_Point)cities.elementAt(current.index(y));	
		    distancebetween = p1.distance(p2);
		  }

		// shortest distance checked and if better, then city is closest
		if(distancebetween < bestdistance)
		  {
		    nextcity = y;
		    bestdistance = distancebetween;
		  }
	      }
	  }
	bestdistance = 10000;
	heuristic[x]=nextcity;
	currentcity = nextcity;
	inlist[nextcity]=0;
      }
    
    
    best.create(heuristic,cities.size() - 1);       // creates a permutation based on array of
                                                    // visited cities
    
    totaldistance = distanceof(best);
    if(totalconfigs != (cities.size() * cities.size()))
      {
	totalconfigs = cities.size() * cities.size();
      }
    optimalflag=1;
    
    return true;  // algorithm always is going to calculate its shortest distance
    
  }
      
      // Return true if the current tour is known to be optimal (e.g. all possible
      // tours have been checked).
  public boolean isOptimal()
  {
    if(optimalflag==0)
      return false;
    return true;
  }
      
      // Return the permutation encoding the best tour discovered so far.
      // This permutation applies to all but the last city, which is fixed.
  public Permutation currentTour()
  {
    return best;
  }        

      // Permutationsize method calculates the size of the permutation based on the
      // number of cities.  It takes into account that one city is fixed.
  public int permutationsize()
  {
    int temp;
    temp = (cities.size() - 1);
    return temp;
  }
      
      // distanceof method finds the distance of a permutation that is passed to it.
      // The method returns a double value of the distance of the tour
  public double distanceof(Permutation one)
  {
    double distancebetween = 0.0;
    ga_Point p1,p2;
    int n=0;

    //calculates the distance around all the cities except the fixed one.
    for(n=0;n < cities.size() - 2; n++)	
      {
	p1 = (ga_Point)cities.elementAt(one.index(n));
	p2 = (ga_Point)cities.elementAt(one.index(n+1));		       
	distancebetween += p1.distance(p2);
      }
    
    
    //this adds the distance for the fixed city
    
    p1 = (ga_Point)cities.elementAt(one.index(0));
    p2 = (ga_Point)cities.elementAt(one.index(cities.size()-2));
    distancebetween += p1.distance((ga_Point)cities.elementAt(cities.size()-1));
    distancebetween += p2.distance((ga_Point)cities.elementAt(cities.size()-1));
   
    return distancebetween;
  }
}
