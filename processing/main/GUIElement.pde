/* 
GUI Element

Classe parent de tous les élèments d'interfaces (Boutons, Texte, ..)

*/

class GUIElement {
    // Properties / Propriétés
    private int pos_x, pos_y, width, height; // Position x, y, largeur, hauteur de l'élément
    private String label; // Nom de l'élement
    private int pivot_x, pivot_y; // Position du pivot de l'élément

    // Constructor / Constructeur
    // Premier constructeur raccourci
    GUIElement (int x, int y, int w, int h, String l ) {
        pos_x = x; pos_y = y;
        width = w; height = h;
        label = l;
        pivot_x = ANCHOR.LEFT;
        pivot_y = ANCHOR.TOP;
    }
    // Deuxième constructeur avec les paramètres de pivot
    GUIElement (int x, int y, int w, int h, String l, int p_x, int p_y) {
        pos_x = x; pos_y = y;
        width = w; height = h;
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

    void custom_mouse_pressed() {
        // Fonction qui sera appelée dans la boucle mousePressed() de Processing
    }

    void custom_mouse_released() {
        // Fonction qui sera appelée dans la boucle mouseReleased() de Processing
    }

    void custom_mouse_dragged() {
        // Fonction qui sera appelée dans la boucle mouseDragged() de Processing
    }
}