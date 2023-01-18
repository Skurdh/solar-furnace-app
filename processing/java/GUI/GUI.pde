GUIText t;
GUIButton b;
GUIPage p;


void setup() {
  size(640, 320);
  load_fonts();
  noStroke();
  t = new GUIText(width/2, height/2, "Salut à tous les amis", CENTER, CENTER);
  b = new GUIButton(50, 50, "BUTTON");
  p = new GUIPage("Configuration");
  textFont(get_font("regular"));
}

void draw() {
  background(get_color("Background"));
  p.custom_draw();
  t.custom_draw();
}

void mouseMoved() {
  b.custom_mouse_moved();
}

void mousePressed() {
 b.custom_mouse_pressed(); 
}

void mouseReleased() {
 b.custom_mouse_released(); 
}
