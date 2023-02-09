/*
 GUI Text
 
 Classe parent des textes. (Hérite de GUIElement)
 Gère la création, l'affichage des textes.
 */

class GUIText extends GUIElement {
  // Properties / Propriétés
  float size = 14 * displayDensity; // Taille de la police
  String font = "regular"; // Police d'écriture
  String color_name = "Font.Normal"; // Couleur du texte


  // Constructor / Constructeur
  // Premier constructeur raccourci
  GUIText (float x, float y, String l ) {
    super(x, y, 0, 0, l);
  }
  // Deuxième constructeur avec les paramètres de pivot
  GUIText (int p_x, float x, int p_y, float y, String l) {
    super(p_x, x, p_y, y, 0, 0, l);
  }

  // Methods / Méthodes
  void custom_draw() {
    fill(get_color(color_name));
    textFont(get_font(font));
    textAlign(pivot_x, pivot_y);
    textSize(size);
    text(label, pos_x, pos_y);
    // Fonction qui sera appelée dans la boucle draw() de Processing (même fonction que loop() dans Arduino)
  }
}
