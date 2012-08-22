package reuze;import java.io.PrintWriter;import java.text.DecimalFormat;public class gb_HeightMapFractal{  private static final int WORLD_LEN = 64;  /* WORLD_LEN should be a power of 2 since it will be     halved repeatedly until a single mesh cell as a     width of 1 unit. */  // a height range of 10 units  private final static double MIN_HEIGHT = -2.0;  private final static double MAX_HEIGHT = 8.0;  public gb_Vector3 mesh[][];    // stores the mesh's (x,y,z) points  private DecimalFormat df;    // for output of mesh  private double flatness;  /* Amount used to reduce dHeight on each recursive     call to divideMesh(). A larger value makes     landscape smoother; smaller is more chaotic  */   public gb_HeightMapFractal(double flat)  {    flatness = flat;    mesh = new gb_Vector3[WORLD_LEN+1][WORLD_LEN+1];    df = new DecimalFormat("0.##");  // 2 dp    makeMesh();  } // end of FractalMesh()  private void makeMesh()  /* Initialise the 4 corners of the mesh with random heights     within the MIN and MAX range. Then start recursively     generating midoints by calling divideMesh(). */  {    System.out.println("Building the landscape...please wait");    mesh[0][0] =    // back left      new gb_Vector3( -WORLD_LEN/2, (float)randomHeight(), -WORLD_LEN/2 );    mesh[0][WORLD_LEN] =    // back right      new gb_Vector3( WORLD_LEN/2, (float)randomHeight(), -WORLD_LEN/2 );    mesh[WORLD_LEN][0] =    // front left      new gb_Vector3( -WORLD_LEN/2, (float)randomHeight(), WORLD_LEN/2 );    mesh[WORLD_LEN][WORLD_LEN] =    // front right      new gb_Vector3( WORLD_LEN/2, (float)randomHeight(), WORLD_LEN/2 );    divideMesh( (MAX_HEIGHT-MIN_HEIGHT)/flatness, WORLD_LEN/2);  } // end of makeMesh()  private double randomHeight()  // between MIN_HEIGHT and MAX_HEIGHT  {  return (Math.random()*(MAX_HEIGHT-MIN_HEIGHT) + MIN_HEIGHT);       // return (rnd.nextDouble()*(MAX_HEIGHT-MIN_HEIGHT) + MIN_HEIGHT);              // to fix the randomness  }  private void divideMesh(double dHeight, int stepSize)  /* At each step, we must perform the diamond step for     the entire mesh before doing the square step. */  {    int xPt, zPt;    if (stepSize >= 1) {   // stop recursing once stepSize is < 1      // diamond step for all mid points at this level      zPt = stepSize;      while (zPt < WORLD_LEN+1) {        xPt = stepSize;        while (xPt < WORLD_LEN+1) {           mesh[zPt][xPt] = getDiamond(zPt, xPt, dHeight, stepSize);           xPt += (stepSize*2);        }        zPt += (stepSize*2);      }      // square step for all points surrounding diamonds      zPt = stepSize;      while (zPt < WORLD_LEN+1) {        mesh[zPt][0] = getSquare(zPt, 0, dHeight, stepSize);  // left column        xPt = stepSize;        while (xPt < WORLD_LEN+1) {           getSquares(zPt, xPt, dHeight, stepSize);  // back & right cells           xPt += (stepSize*2);        }        zPt += (stepSize*2);      }      xPt = stepSize;      while (xPt < WORLD_LEN+1) {        mesh[WORLD_LEN][xPt] =           getSquare(WORLD_LEN, xPt, dHeight, stepSize);  // front row        xPt += (stepSize*2);      }      divideMesh(dHeight/flatness, stepSize/2);    }  }  // end of divideMesh()  private void getSquares(int z, int x, double dHeight, int stepSize)  {    mesh[cCoord(z-stepSize)][x] =    // back           getSquare(cCoord(z-stepSize), x, dHeight, stepSize);    mesh[z][cCoord(x+stepSize)] =    // right           getSquare(z, cCoord(x+stepSize), dHeight, stepSize);  } // end of getSquares()    private gb_Vector3 getDiamond(int z, int x, double dHeight, int stepSize)  {    // System.out.println("getDiamond: " + z + ", " + x + " /" + stepSize);    gb_Vector3 leftBack = mesh[cCoord(z-stepSize)][cCoord(x-stepSize)];    gb_Vector3 rightBack = mesh[cCoord(z-stepSize)][cCoord(x+stepSize)];    gb_Vector3 leftFront = mesh[cCoord(z+stepSize)][cCoord(x-stepSize)];    gb_Vector3 rightFront = mesh[cCoord(z+stepSize)][cCoord(x+stepSize)];    double height =         calcHeight(leftBack, rightBack, leftFront, rightFront, dHeight);    double xWorld = x - (WORLD_LEN/2);    double zWorld = z - (WORLD_LEN/2);    return new gb_Vector3((float)xWorld, (float)height, (float)zWorld);  } // end of getDiamond()  private gb_Vector3 getSquare(int z, int x, double dHeight, int stepSize)  {    // System.out.println("getSquare: " + z + ", " + x + " /" + stepSize);    gb_Vector3 back = mesh[cCoord(z-stepSize)][x];    gb_Vector3 front = mesh[cCoord(z+stepSize)][x];    gb_Vector3 left = mesh[z][cCoord(x-stepSize)];    gb_Vector3 right = mesh[z][cCoord(x+stepSize)];    double height = calcHeight(back, front, left, right, dHeight);    double xWorld = x - (WORLD_LEN/2);    double zWorld = z - (WORLD_LEN/2);    return new gb_Vector3((float)xWorld, (float)height, (float)zWorld);  } // end of getSquare()    private int cCoord(int coordIdx)  /* If the coord index is less then o, greater then WORLD_LEN     then use the coord on the opposite edge of the mesh.  */  {    if (coordIdx < 0)      return WORLD_LEN + coordIdx;    else if (coordIdx > WORLD_LEN)      return coordIdx - WORLD_LEN;    else      return coordIdx;  } // end of cCoord()  private double calcHeight(gb_Vector3 back, gb_Vector3 front,                       gb_Vector3 left, gb_Vector3 right, double dHeight)  /* If the calculated height is < MIN_HEIGHT, set it to MIN_HEIGHT.     If the calculated height is > MAX_HEIGHT, then take modulo     MAX_HEIGHT.  */  {    double height = (back.y + front.y + left.y + right.y)/4.0f +											   randomRange(dHeight);    if (height < MIN_HEIGHT)      height = MIN_HEIGHT;    else if (height > MAX_HEIGHT)      height = height%MAX_HEIGHT;    return height;  }  // end of calcHeight()  private double randomRange(double h)  // between -h and h  {  return ((Math.random() * 2 * h) - h);       // return ((rnd.nextDouble() * 2 * h) - h);   // to fix the randomness  }  public gb_Vector3[] getVertices()  /* Return the mesh as an array of vertices. Each group of 4 points     will be used to create a TexturedPlane object.      We order the points so a plane is specified in anti-clockwise order,     which will be used when a texture is placed on it.  */  { int numVerts = WORLD_LEN*WORLD_LEN*4;    gb_Vector3 vertices[] = new gb_Vector3[numVerts];        int vPos = 0;    for(int z=0; z<WORLD_LEN; z++) {      for(int x=0; x<WORLD_LEN; x++) {        vertices[vPos++] = mesh[z+1][x];     // anti-clockwise creation        vertices[vPos++] = mesh[z+1][x+1];   // from bottom-left        vertices[vPos++] = mesh[z][x+1];        vertices[vPos++] = mesh[z][x];      }    }    return vertices;  }  // end of getVertices  // ---------------------- debugging ------------------  public void printMesh(int axis)  // axis values: x=0, y=1, z=2  {    //File f = new File("mesh.txt");    PrintWriter pw = null;    try {      pw = new PrintWriter(System.out, true); //new FileWriter(f),true);      if (axis == 0)        pw.println("---------- World X Coords ------------");      else if (axis == 1)        pw.println("---------- World Y Coords ------------");      else         pw.println("---------- World Z Coords ------------");      for(int z=0; z<WORLD_LEN+1; z++) {        for(int x=0; x<WORLD_LEN+1; x++)          if (axis == 0)            pw.print( df.format(mesh[z][x].x) + " ");          else if (axis == 1)            pw.print( df.format(mesh[z][x].y) + " ");          else           pw.print( df.format(mesh[z][x].z) + " ");        pw.println();      }      pw.println("--------------------------------------");      System.out.println("Mesh written to mesh.txt");    }    catch(Exception e)    {  System.out.println("Could not write mesh to mesh.txt");  }  }  // end of printMesh()  public static void main(String args[]) {	  gb_HeightMapFractal m=new gb_HeightMapFractal(2.0);	  m.printMesh(1);  }}  // end of FractalMesh class