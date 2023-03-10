/*
 GUI Button
 
 Classe parent des boutons. (Hérite de GUIElement)
 Gère la création, l'affichage et l'intéraction avec les boutons.
 */

class GUIButton extends GUIElement {
  // Properties / Propriétés
  private boolean mouse_entered = false;
  private boolean is_pressed = false;

  // Constructor / Constructeur
  // Premier constructeur raccourci
  GUIButton (float x, float y, String l ) {
    super(x, y, (int(textWidth(l) * 1.5) + 22) * displayDensity, 32  * displayDensity, l);
  }
  // Deuxième constructeur avec les paramètres de pivot
  GUIButton (int p_x, float x, int p_y, float y, String l) {
    super(p_x, x, p_y, y, (textWidth(l) * 1.5 + 22) * displayDensity, 32  * displayDensity, l);
  }

  // Methods / Méthodes
  void custom_draw() {
    int element_color = (is_pressed) ? get_color("Element.Pressed") : (mouse_entered) ? get_color("Element.Hover") : get_color("Element.Normal");
    int text_color = get_color("Element.Font.Normal");
    
    fill(element_color);
    rect(get_global_pos_x(), get_global_pos_y(), size_w, size_h);
    fill(text_color);
    textFont(get_font("medium"));
    textAlign(CENTER, CENTER);
    textSize(16 * displayDensity);
    text(label, get_global_pos_x() + size_w/2, get_global_pos_y() + size_h/2 - 2);
  }


  void custom_mouse_moved() { 
    if (is_mouse_hover()) {
      mouse_entered = true;
      //cursor(HAND);
    } else {
      //cursor(ARROW);
      mouse_entered = false;
    }
  }

  boolean custom_mouse_pressed() {
    if (is_mouse_hover()) {
       is_pressed = true;
       return true;
    }
    return false;
  }

  void custom_mouse_released() {
    if (is_pressed) {
      print("disable");
      is_pressed = false;
    }
  }

  void custom_mouse_dragged() {
    // Fonction qui sera appelée dans la boucle mouseDragged() de Processing
  }
}
