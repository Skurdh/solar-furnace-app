/* 
GUI Text

Classe parent des textes. (Hérite de GUIElement)
Gère la création, l'affichage des textes.
*/

class GUIText extends GUIElement{
    // Properties / Propriétés
    float size; // Taille de la police
    int font; // Police d'écriture
    int t_color; // Couleur du texte


    // Constructor / Constructeur
    // Premier constructeur raccourci
    GUIText (int x, int y, int w, int h, String l ) {
        super(x, y, w, h, l);
    }
    // Deuxième constructeur avec les paramètres de pivot
    GUIText (int x, int y, int w, int h, String l, PIVOT p_x, PIVOT p_y) {
        super(x, y, w, h, l, p_x, p_y);
    }

    // Methods / Méthodes
    void custom_draw() {
        text(label, get_global_pos_x(), get_global_pos_y());
    }
}
