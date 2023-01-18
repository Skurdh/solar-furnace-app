GUIText t;

void setup() {
  size(640, 320);
  load_fonts();
  
  fill(0);
  t = new GUIText(0, 20, 0, 0, "Salut à tous les amis");
  textFont(font[FONT_STYLE.get("regular")]);
}

void draw() {
  background(0);
  t.custom_draw();
}
