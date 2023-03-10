/*
 GUI Element
 
 Classe parent de tous les élèments d'interfaces (Boutons, Texte, ..)
 
 */

class GUIPage extends GUIElement {
  // Properties / Propriétés
  boolean disable_header = false;
  float header_h = 54 * displayDensity;
  //private ArrayList<GUIElement> header = new ArrayList<GUIElement>();
  ArrayList<GUIElement> content = new ArrayList<GUIElement>();

  // Constructor / Constructeur
  // Premier constructeur raccourci
  GUIPage (String l ) {
    super(0, 0, displayWidth, displayHeight, l);
  }


  // Methods / Méthodes
  void custom_draw() {
    draw_header();
    draw_content();
  }

  void custom_mouse_moved() {
    if (!content.isEmpty()) {
      for (int i = 0; i < content.size(); i++) {
         content.get(i).custom_mouse_moved();
      }
    }
  }

  boolean custom_mouse_pressed() {
    return false;
    // Fonction qui sera appelée dans la boucle mousePressed() de Processing
  }

  void custom_mouse_released() {
    if (!content.isEmpty()) {
      for (int i = 0; i < content.size(); i++) {
         content.get(i).custom_mouse_released();
      }
    }
  }

  void custom_mouse_dragged() {
    if (!content.isEmpty()) {
      for (int i = 0; i < content.size(); i++) {
         content.get(i).custom_mouse_dragged();
      }
    }
  }

  void draw_header() {
    if (!disable_header) {
      fill(get_color("Element.Normal"));
      rect(pos_x, pos_y , size_w, header_h);
      fill(get_color("Element.Font.Normal"));
      textFont(get_font("medium"));
      textAlign(CENTER, CENTER);
      textSize(17 * displayDensity);
      text(label, get_global_pos_x() + size_w/2 - 2, get_global_pos_y() + header_h/2 - 2);
    }
  }
  
  void draw_content() {
    if (!content.isEmpty()) {
      for (int i = 0; i < content.size(); i++) {
         content.get(i).custom_draw();
      }
    }
  }
}
