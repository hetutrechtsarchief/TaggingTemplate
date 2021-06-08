String lines[];
PImage img;
Viewport view;
int CMD=157;
boolean shiftDown = false;
boolean cmdDown = false;
boolean selecting;
ArrayList<Area> areas = new ArrayList();
Area area;

void setup() {
  img = loadImage("NL-UtHUA_650_101_000182.jpg");
  size(1400, 700, P3D);
  view = new Viewport(this);
  view.setBounds(0, 0, width, height);
  view.setContentSize(img.width, img.height);

  Table table = loadTable("settings.csv", "header");
  for (TableRow row : table.rows()) {
    int x = row.getInt("x");
    int y = row.getInt("y");
    int w = row.getInt("width");
    int h = row.getInt("height");
    String label = row.getString("label");
    areas.add(new Area(x, y, w, h, label));
  }
}

void draw() {
  background(0);
  view.begin();
  image(img, 0, 0);

  fill(0, 255, 255, 50);
  stroke(0, 255, 255);
  for (Area r : areas) {
    rect(r.x, r.y, r.width, r.height);
  }

  view.end();
}

void mousePressed() {
  PVector p = view.fromScreenToView(mouseX, mouseY);

  if (shiftDown) {
    selecting = true;
    view.mouseEnabled = false;
    area = new Area(int(p.x), int(p.y), 0, 0, "");
    areas.add(area);
    println(p);
  }
}

void mouseDragged() {
  PVector p = view.fromScreenToView(mouseX, mouseY);
  if (selecting) {
    area.setSize(int(p.x-area.x), int(p.y-area.y));
  }
}

void mouseReleased() {
  if (selecting) {
    selecting = false;
    view.mouseEnabled = true;
  }
}

void keyPressed() {
  if (key==CODED && keyCode==CMD) {
    cmdDown = true;
  } else if (key==CODED && keyCode==SHIFT) {
    shiftDown = true;
  }
}

void keyReleased() {
  if (key==CODED && keyCode==SHIFT) {
    shiftDown = false;
  } else if (key==CODED && keyCode==CMD) {
    cmdDown = false;
  } else if (key=='s') {
    saveSettings();
  } else if (key=='c') {
    areas.clear();
  }
}

void saveSettings() {
  println("save", areas.size());

  Table table = new Table();
  table.addColumn("x");
  table.addColumn("y");
  table.addColumn("width");
  table.addColumn("height");
  table.addColumn("label");

  for (Area a : areas) {
    TableRow row = table.addRow();
    row.setInt("x", a.x);
    row.setInt("y", a.y);
    row.setInt("width", a.width);
    row.setInt("height", a.height);
    row.setString("label", a.label);
  }
  saveTable(table, "settings.csv");
}
