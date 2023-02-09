/*
 GUI Element
 
 Classe parent de tous les élèments d'interfaces (Boutons, Texte, ..)
 
 */

class ConfigurationPage extends GUIPage {
  // Properties / Propriétés
  private int config_state = 0; // 0 = INFO, 1 = SEARCH, 2 = PAIRED, 
  private int[] state_el_idx = new int[3];
  
  
  // Constructor / Constructeur
  // Premier constructeur raccourci
  ConfigurationPage () {
    super("Configuration Bluetooth");
    // Élements pour la partie INFO
    content.add(new GUIText(CENTER, width/2.0, TOP, header_h + 24*displayDensity, "Appareillage"));
    content.add(new GUIButton(CENTER, width/2, BOTTOM, height - 64  * displayDensity, "Suivant"));
    
    state_el_idx[0] = 0;
    state_el_idx[1] = 2;
  }


  // Methods / Méthodes
  void custom_draw() {
    draw_header();
    int first_el = 0;
    int last_el = 0;
    switch (config_state) {
      case 0:
        first_el = state_el_idx[0];
        last_el = state_el_idx[1];
        break;
    }
    
    for (int i = first_el; i < last_el; i++) {
      content.get(i).custom_draw(); 
    }
  }

  boolean custom_mouse_pressed() {
    switch (config_state) {
      case 0:
        if (content.get(1).custom_mouse_pressed()) {
          config_state = 1;
        }
        break;
    }
    return false;
  }
  
  void set_state(int state) {
    switch(state) {
      case 0:
        break;
      case 1:
      
        break;
    }
  }
}
