/* 
GUI Button

Classe parent des boutons. (Hérite de GUIElement)
Gère la création, l'affichage et l'intéraction avec les boutons.
*/

class GUIButton extends GUIElement{
   // Properties / Propriétés


   // Constructor / Constructeur
   // Premier constructeur raccourci
   GUIButton (int x, int y, int w, int h, String l) {
       super(x, y, w, h, l);
   }
   // Deuxième constructeur avec les paramètres de pivot
   GUIButton (int x, int y, int w, int h, String l, PIVOT p_x, PIVOT p_y) {
       super(x, y, w, h, l, p_x, p_y);
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
