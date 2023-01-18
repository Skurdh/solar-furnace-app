GUIText t;

void setup() {
  fullScreen();
  fill(0);
  t = new GUIText(0, 20, 0, 0, "Salut à tous les amis");
  
}

void draw() {
  t.custom_draw();
}
