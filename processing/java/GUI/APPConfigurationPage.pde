/*
 GUI Element
 
 Classe parent de tous les élèments d'interfaces (Boutons, Texte, ..)
 
 */

class ConfigurationPage extends GUIPage {
  // Properties / Propriétés

  // Constructor / Constructeur
  // Premier constructeur raccourci
  ConfigurationPage () {
    super("Configuration");
  }


  // Methods / Méthodes
  void custom_draw() {
    draw_header();
    draw_content();
  }

  boolean custom_mouse_pressed() {
    return false;
    // Fonction qui sera appelée dans la boucle mousePressed() de Processing
  }
}
