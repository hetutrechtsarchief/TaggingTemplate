//package viewport;
import processing.core.*;
import processing.event.*;
import java.awt.Rectangle;

public class Viewport {
  PApplet app;
  float toX, toY, toScale = 1; //(to value)
  float x,y,scale = 1; //tween (current value)
  int mouseX, mouseY, pmouseX, pmouseY; //mouse values in this viewport
  Rectangle bounds;
  boolean dragging, mouseEnabled = true;
  float contentWidth, contentHeight;
  float minScale = 0.5f, maxScale = 40.0f;
  boolean enableDragging = true;
  PVector contentTopLeftInScreenCoords = new PVector();
  PVector contentBottomRightInScreenCoords = new PVector();
  float toScreenScale;
  float zoomFactor = 1;

  Viewport(PApplet p) {
    this.app = p;
    app.registerMethod("mouseEvent", this);
  }

  void setBounds(int x, int y, int w, int h) {
    this.bounds = new Rectangle(x, y, w, h);
  }

  void setContentSize(int w, int h) {
    this.contentWidth = w;
    this.contentHeight = h;
  }

  void begin() {
  assert bounds!=null :
    "forgot to call setBounds()?" ;
    
    float smoothing = .2f;
    x = app.lerp(x, toX, smoothing);
    y = app.lerp(y, toY, smoothing);
    scale = app.lerp(scale, toScale, smoothing);
    
    app.clip(bounds.x, bounds.y, bounds.width, bounds.height);
    app.pushMatrix();
    app.translate(bounds.x, bounds.y);
    app.scale(scale);
    
    app.translate(-x, -y);

    //content
    app.translate(bounds.width/2, bounds.height/2); //center in viewport
    app.scale(PApplet.min((float)(bounds.width)/contentWidth, (float)(bounds.height)/contentHeight)); //scale to fit content
    app.translate(-contentWidth/2, -contentHeight/2); //imageMode center

    contentTopLeftInScreenCoords.set(new PVector(app.screenX(0, 0), app.screenY(0, 0)));
    contentBottomRightInScreenCoords.set(new PVector(app.screenX(contentWidth, contentHeight), app.screenY(contentWidth, contentHeight)));
    toScreenScale = 1 / (scale * PApplet.min(bounds.width/contentWidth, bounds.height/contentHeight));
  }

  PVector fromScreenToView(float x, float y) {
    x = PApplet.map(x, contentTopLeftInScreenCoords.x, contentBottomRightInScreenCoords.x, 0, contentWidth);
    y = PApplet.map(y, contentTopLeftInScreenCoords.y, contentBottomRightInScreenCoords.y, 0, contentHeight);
    return new PVector(x, y);
  }

  PVector fromViewToScreen(float x, float y) {
    x = PApplet.map(x, 0, contentWidth, contentTopLeftInScreenCoords.x, contentBottomRightInScreenCoords.x);
    y = PApplet.map(y, 0, contentHeight, contentTopLeftInScreenCoords.y, contentBottomRightInScreenCoords.y);
    return new PVector(x, y);
  }

  void end() {
    app.popMatrix();
    app.noClip();
  }

  void mouseDragged() {
    if (!enableDragging) return;
    moveByScreen(mouseX-pmouseX, mouseY-pmouseY);
  }

  void moveByScreen(float screenX, float screenY) {
    toX -= screenX/toScale;
    toY -= screenY/toScale;
  }

  void moveToScreen(float screenX, float screenY) {
    toX = screenX/toScale;
    toY = screenY/toScale;
  }

  void moveToView(float x, float y) {
    PVector toScreen = fromViewToScreen(x, y);
    moveByScreen(-toScreen.x, -toScreen.y);
  }

  void mouseWheel(MouseEvent event) {
    //zoom factor needs to be between about 0.99 and 1.01 to be able to multiply so add 1
    zoomFactor = -event.getCount()*.01f + 1.0f; 
    float newScale = PApplet.constrain(scale * zoomFactor, minScale, maxScale);

    //next two lines are the most important lines of the code.
    //subtract mouse in 'old' scale from mouse in 'new' scale and apply that to position.
    toX -= (mouseX/newScale - mouseX/toScale);
    toY -= (mouseY/newScale - mouseY/toScale);

    //apply the new scale
    toScale = newScale;
  }

  public void mouseEvent(MouseEvent event) {
    if (!mouseEnabled) return;
    boolean mouseOver = bounds.contains(event.getX(), event.getY());
    pmouseX = mouseX;
    pmouseY = mouseY;
    mouseX = event.getX() - bounds.x;
    mouseY = event.getY() - bounds.y;

    switch (event.getAction()) {
    case MouseEvent.PRESS:
      if (mouseOver) dragging = true;
      break;
    case MouseEvent.RELEASE:
      dragging = false;
      break;
    case MouseEvent.CLICK:
      break;
    case MouseEvent.DRAG:
      if (dragging) this.mouseDragged();
      break;
    case MouseEvent.MOVE:
      break;
    case MouseEvent.WHEEL:
      if (mouseOver) this.mouseWheel(event);
      break;
    }
  }

  void reset() {
    toScale = scale = 1;
    toX = toY = x = y = 0;
  }
}
