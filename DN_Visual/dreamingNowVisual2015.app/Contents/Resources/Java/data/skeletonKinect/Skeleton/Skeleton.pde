import oscP5.*;
import netP5.*;
import SimpleOpenNI.*;

SimpleOpenNI  context;
OscP5 oscP5;
NetAddress myRemoteLocation;
boolean autoCalib = true;
boolean debug = false;
boolean sendOSC = true;

public PVector rightHandRaw = new PVector();
public PVector leftHandRaw  =  new PVector();
public PVector headRaw =  new PVector();
public PVector neckRaw =  new PVector();
public PVector leftShoulderRaw =  new PVector();
public PVector leftElbowRaw =  new PVector();
public PVector rightShoulderRaw =  new PVector();
public PVector rightElbowRaw =  new PVector();
public PVector torsoRaw =  new PVector();
public PVector leftHipRaw =  new PVector();  
public PVector leftKneeRaw =  new PVector();
public PVector rightHipRaw =  new PVector(); 
public PVector leftFootRaw =  new PVector();
public PVector rightKneeRaw =  new PVector(); 
public PVector rightFootRaw =  new PVector();

color[]       userClr = new color[]{ color(255,0,0),
                                     color(0,255,0),
                                     color(0,0,255),
                                     color(255,255,0),
                                     color(255,0,255),
                                     color(0,255,255)
                                   };

void setup() {
  // start OpenNI, load the library
  SimpleOpenNI.start();
  oscP5 = new OscP5(this, 12000);
  StrVector strList = new StrVector();
  SimpleOpenNI.deviceNames(strList);
  for(int i=0;i<strList.size();i++)
    println(i + ":" + strList.get(i));

  // check if there are enough cams  
  if(strList.size() < 2)
  {
    println("only works with 2 cams");
    exit();
    return;
  }  
  context = new SimpleOpenNI(1,this);

  // enable depthMap generation 
  if (context.enableDepth() == false) {
    println("Can't open the depthMap, maybe the camera is not connected!"); 
    exit();
    return;
  }

  // enable skeleton generation for all joints
  context.enableUser();

  background(0);

  stroke(0, 0, 255);
  strokeWeight(3);
  smooth();

  size(640, 480);
  myRemoteLocation = new NetAddress("127.0.0.1", 8002);
}

void draw() {
  
  // update the cam
  SimpleOpenNI.updateAll();

  // draw depthImageMap
 
   
  // draw depthImageMap
  image(context.depthImage(),0,0);
  // draw the skeleton if it's available
  int[] userList = context.getUsers();
  for(int i=0;i<userList.length;i++)
  {
    if(context.isTrackingSkeleton(userList[i]))
    {
      stroke(userClr[ (userList[i] - 1) % userClr.length ] );
      drawSkeleton(context,userList[i]);
      getSkeleton(userList[i]);
    }      
  }   

  if (sendOSC) {
  sendOsc();
  text("sending OSC", 0,0);
  }
}

void mousePressed() {
  debug =!debug;
  OscMessage myMessage2 = new OscMessage("/ping");
  myMessage2.add("skeleton");
  /* send the message */
  oscP5.send(myMessage2, myRemoteLocation);
}

void keyPressed(){
  
  if(key == '1'){
    debug=!debug;
  }

}

void sendOsc() {
  /* in the following different ways of creating osc messages are shown by example */
  OscMessage myMessage = new OscMessage("/leftHandRaw");
  myMessage.add(leftHandRaw.x);
  myMessage.add(leftHandRaw.y);
  myMessage.add(leftHandRaw.z); /* add an int to the osc message */
  /* send the message */
  oscP5.send(myMessage, myRemoteLocation);

  myMessage = new OscMessage("/rightHandRaw");
  myMessage.add(rightHandRaw.x);
  myMessage.add(rightHandRaw.y);
  myMessage.add(rightHandRaw.z); /* add an int to the osc message */
  /* send the message */
  oscP5.send(myMessage, myRemoteLocation);

  myMessage = new OscMessage("/headRaw");
  myMessage.add(headRaw.x);
  myMessage.add(headRaw.y);
  myMessage.add(headRaw.z); /* add an int to the osc message */
  /* send the message */
  oscP5.send(myMessage, myRemoteLocation);

  myMessage = new OscMessage("/rightShoulderRaw");
  myMessage.add(rightShoulderRaw.x);
  myMessage.add(rightShoulderRaw.y);
  myMessage.add(rightShoulderRaw.z); /* add an int to the osc message */
  /* send the message */
  oscP5.send(myMessage, myRemoteLocation);

  myMessage = new OscMessage("/leftElbowRaw");
  myMessage.add(leftElbowRaw.x);
  myMessage.add(leftElbowRaw.y);
  myMessage.add(leftElbowRaw.z); /* add an int to the osc message */
  /* send the message */
  oscP5.send(myMessage, myRemoteLocation);
  
    myMessage = new OscMessage("/rightElbowRaw");
  myMessage.add(rightElbowRaw.x);
  myMessage.add(rightElbowRaw.y);
  myMessage.add(rightElbowRaw.z); /* add an int to the osc message */
  /* send the message */
  oscP5.send(myMessage, myRemoteLocation);

  myMessage = new OscMessage("/leftShoulderRaw");
  myMessage.add(leftShoulderRaw.x);
  myMessage.add(leftShoulderRaw.y);
  myMessage.add(leftShoulderRaw.z); /* add an int to the osc message */
  /* send the message */
  oscP5.send(myMessage, myRemoteLocation);

  myMessage = new OscMessage("/torsoRaw");
  myMessage.add(torsoRaw.x);
  myMessage.add(torsoRaw.y);
  myMessage.add(torsoRaw.z); /* add an int to the osc message */
  /* send the message */
  oscP5.send(myMessage, myRemoteLocation);


  myMessage = new OscMessage("/leftHipRaw");
  myMessage.add(leftHipRaw.x);
  myMessage.add(leftHipRaw.y);
  myMessage.add(leftHipRaw.z); /* add an int to the osc message */
  /* send the message */
  oscP5.send(myMessage, myRemoteLocation);

  myMessage = new OscMessage("/leftKneeRaw");
  myMessage.add(leftKneeRaw.x);
  myMessage.add(leftKneeRaw.y);
  myMessage.add(leftKneeRaw.z); /* add an int to the osc message */
  /* send the message */
  oscP5.send(myMessage, myRemoteLocation);

  myMessage = new OscMessage("/rightHipRaw");
  myMessage.add(rightHipRaw.x);
  myMessage.add(rightHipRaw.y);
  myMessage.add(rightHipRaw.z); /* add an int to the osc message */
  /* send the message */
  oscP5.send(myMessage, myRemoteLocation);


  myMessage = new OscMessage("/rightKneeRaw");
  myMessage.add(rightKneeRaw.x);
  myMessage.add(rightKneeRaw.y);
  myMessage.add(rightKneeRaw.z); /* add an int to the osc message */
  /* send the message */
  oscP5.send(myMessage, myRemoteLocation);
}

// draw the skeleton with the selected joints
void getSkeleton(int userId) {
  context.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_LEFT_HAND, leftHandRaw);
  context.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_RIGHT_HAND, rightHandRaw);
  context.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_HEAD, headRaw);
  context.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_NECK, neckRaw);
  context.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER, leftShoulderRaw);
  context.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_LEFT_ELBOW, leftElbowRaw);
  context.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER, rightShoulderRaw);
  context.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_RIGHT_ELBOW, rightElbowRaw);
  context.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_TORSO, torsoRaw);
  context.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_LEFT_HIP, leftHipRaw);  
  context.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_LEFT_KNEE, leftKneeRaw);
  context.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_RIGHT_HIP, rightHipRaw);  
  context.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_LEFT_FOOT, leftFootRaw);
  context.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_RIGHT_KNEE, rightKneeRaw);
  context.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_RIGHT_FOOT, rightFootRaw);
}

void drawSkeleton(SimpleOpenNI curContext,int userId)
{
  // to get the 3d joint data
  /*
  PVector jointPos = new PVector();
  curContext.getJointPositionSkeleton(userId,SimpleOpenNI.SKEL_NECK,jointPos);
  println(jointPos);
  */
  
  curContext.drawLimb(userId, SimpleOpenNI.SKEL_HEAD, SimpleOpenNI.SKEL_NECK);

  curContext.drawLimb(userId, SimpleOpenNI.SKEL_NECK, SimpleOpenNI.SKEL_LEFT_SHOULDER);
  curContext.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER, SimpleOpenNI.SKEL_LEFT_ELBOW);
  curContext.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_ELBOW, SimpleOpenNI.SKEL_LEFT_HAND);

  curContext.drawLimb(userId, SimpleOpenNI.SKEL_NECK, SimpleOpenNI.SKEL_RIGHT_SHOULDER);
  curContext.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER, SimpleOpenNI.SKEL_RIGHT_ELBOW);
  curContext.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_ELBOW, SimpleOpenNI.SKEL_RIGHT_HAND);

  curContext.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER, SimpleOpenNI.SKEL_TORSO);
  curContext.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER, SimpleOpenNI.SKEL_TORSO);

  curContext.drawLimb(userId, SimpleOpenNI.SKEL_TORSO, SimpleOpenNI.SKEL_LEFT_HIP);
  curContext.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_HIP, SimpleOpenNI.SKEL_LEFT_KNEE);
  curContext.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_KNEE, SimpleOpenNI.SKEL_LEFT_FOOT);

  curContext.drawLimb(userId, SimpleOpenNI.SKEL_TORSO, SimpleOpenNI.SKEL_RIGHT_HIP);
  curContext.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_HIP, SimpleOpenNI.SKEL_RIGHT_KNEE);
  curContext.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_KNEE, SimpleOpenNI.SKEL_RIGHT_FOOT);  
}
// -----------------------------------------------------------------
// SimpleOpenNI events

void onNewUser(SimpleOpenNI curContext, int userId)
{
  println("onNewUser - deviceIndex: " + curContext.deviceIndex() + " , " + "userId: " + userId);
  println("\tstart tracking skeleton");
  curContext.startTrackingSkeleton(userId);
}

void onLostUser(SimpleOpenNI curContext, int userId)
{
  println("onLostUser - userId: " + userId);
}

void onVisibleUser(SimpleOpenNI curContext, int userId)
{
  //println("onVisibleUser - userId: " + userId);
}

void onOutOfSceneUser(SimpleOpenNI curContext, int userId)
{
  println("onOutOfSceneUserUser - userId: " + userId);
}
