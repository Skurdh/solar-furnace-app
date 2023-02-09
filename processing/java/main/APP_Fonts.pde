/* 
GUI Button

Classe parent des boutons. (Hérite de GUIElement)
Gère la création, l'affichage et l'intéraction avec les boutons.
*/

PFont[] fonts = new PFont[2];
IntDict FONT_STYLE = new IntDict(); 


void load_fonts() {
  // Regular font
  FONT_STYLE.add("regular",0);
  fonts[FONT_STYLE.get("regular")] = createFont("LibreFranklin-Regular.ttf", 36 * displayDensity, true);
  // Medium font
  FONT_STYLE.add("medium", 1);
  fonts[FONT_STYLE.get("medium")] = createFont("LibreFranklin-Medium.ttf", 36 * displayDensity, true);
}

PFont get_font(String style) {
  if (FONT_STYLE.hasKey(style)) {
    return fonts[FONT_STYLE.get(style)];
  } else {
    return fonts[0]; 
  }
}
