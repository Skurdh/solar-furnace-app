/*
 GUI Text
 
 Classe parent des textes. (Hérite de GUIElement)
 Gère la création, l'affichage des textes.
 */

class GUIText extends GUIElement {
  // Properties / Propriétés
  float size; // Taille de la police
  int font; // Police d'écriture
  int t_color; // Couleur du texte


  // Constructor / Constructeur
  // Premier constructeur raccourci
  GUIText (int x, int y, String l ) {
    super(x, y, 0, 0, l);
  }
  // Deuxième constructeur avec les paramètres de pivot
  GUIText (int x, int y, String l, int p_x, int p_y) {
    super(p_x, x, p_y, y, 0, 0, l);
  }

  // Methods / Méthodes
  void custom_draw() {
    fill(get_color("Font.Normal"));
    textFont(get_font("regular"));
    textAlign(pivot_x, pivot_y);
    textSize(14);
    text(label, pos_x, pos_y);
    // Fonction qui sera appelée dans la boucle draw() de Processing (même fonction que loop() dans Arduino)
  }
}
