/* 
GUI Element

Classe parent de tous les élèments d'interfaces (Boutons, Texte, ..)

*/

class GUIElement {
    // Properties / Propriétés
    public int pos_x, pos_y, size_w, size_h; // Position x, y, largeur, hauteur de l'élément
    public String label; // Nom de l'élement
    public int pivot_x;
    public int pivot_y; // Position du pivot de l'élément

    // Constructor / Constructeur
    // Premier constructeur raccourci
    GUIElement (int x, int y, int w, int h, String l ) {
        pos_x = x; pos_y = y;
        size_w = w; size_h = h;
        label = l;
        pivot_x = LEFT;
        pivot_y = TOP;
    }
    // Deuxième constructeur avec les paramètres de pivot
    GUIElement (int p_x, int x, int p_y, int y, int w, int h, String l) {
        pos_x = x; pos_y = y;
        size_w = w; size_h = h;
        label = l;
        pivot_x = p_x;
        pivot_y = p_y;
    }

    // Methods / Méthodes
    void custom_draw() {
        // Fonction qui sera appelée dans la boucle draw() de Processing (même fonction que loop() dans Arduino) 
    }

    void custom_mouse_moved() {
        // Fonction qui sera appelée dans la boucle mouseMoved() de Processing
    }

    boolean custom_mouse_pressed() {
      return false;
        // Fonction qui sera appelée dans la boucle mousePressed() de Processing
    }

    void custom_mouse_released() {
        // Fonction qui sera appelée dans la boucle mouseReleased() de Processing
    }

    void custom_mouse_dragged() {
        // Fonction qui sera appelée dans la boucle mouseDragged() de Processing
    }

    int get_global_pos_x() {
        return pos_x;
    }

    int get_global_pos_y() {
        return pos_y;
    }
    
    boolean is_mouse_hover() {
      return mouseX >= get_global_pos_x() && mouseX <= get_global_pos_x() + size_w && mouseY >= get_global_pos_y() && mouseY <= get_global_pos_y() + size_h;
    }
}
