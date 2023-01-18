/* autogenerated by Processing revision 1289 on 2023-01-18 */
import processing.core.*;
import processing.data.*;
import processing.event.*;
import processing.opengl.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class GUI extends PApplet {

GUIText t;

public void setup() {
  /* size commented out by preprocessor */;
  load_fonts();
  
  fill(0);
  t = new GUIText(0, 20, 0, 0, "Salut à tous les amis");
  textFont(font.get("regular"));
}

public void draw() {
  background(0);
  t.custom_draw();
}
HashMap<String.PFont> font = new HashMap<String.PFont>();

public void load_fonts() {
    font.add("regular", createFont("fonts/LibreFranklin-Regular.ttf", 36));
}
/* 
GUI Button

Classe parent des boutons. (Hérite de GUIElement)
Gère la création, l'affichage et l'intéraction avec les boutons.
*/

class GUIButton extends GUIElement{
   // Properties / Propriétés


   // Constructor / Constructeur
   // Premier constructeur raccourci
   GUIButton (int x, int y, int w, int h, String l ) {
       super(x, y, w, h, l);
   }
   // Deuxième constructeur avec les paramètres de pivot
   GUIButton (int x, int y, int w, int h, String l, PIVOT p_x, PIVOT p_y) {
       super(x, y, w, h, l, p_x, p_y);
   }

   // Methods / Méthodes
   public void custom_draw() {
       // Fonction qui sera appelée dans la boucle draw() de Processing (même fonction que loop() dans Arduino) 
   }

   public void custom_mouse_moved() {
       // Fonction qui sera appelée dans la boucle mouseMoved() de Processing
   }

   public void custom_mouse_pressed() {
       // Fonction qui sera appelée dans la boucle mousePressed() de Processing
   }

   public void custom_mouse_released() {
       // Fonction qui sera appelée dans la boucle mouseReleased() de Processing
   }

   public void custom_mouse_dragged() {
       // Fonction qui sera appelée dans la boucle mouseDragged() de Processing
   }
}
/* 
GUI Element

Classe parent de tous les élèments d'interfaces (Boutons, Texte, ..)

*/

class GUIElement {
    // Properties / Propriétés
    public int pos_x, pos_y, width, height; // Position x, y, largeur, hauteur de l'élément
    public String label; // Nom de l'élement
    public PIVOT pivot_x, pivot_y; // Position du pivot de l'élément

    // Constructor / Constructeur
    // Premier constructeur raccourci
    GUIElement (int x, int y, int w, int h, String l ) {
        pos_x = x; pos_y = y;
        width = w; height = h;
        label = l;
        pivot_x = PIVOT.LEFT;
        pivot_y = PIVOT.TOP;
    }
    // Deuxième constructeur avec les paramètres de pivot
    GUIElement (int x, int y, int w, int h, String l, PIVOT p_x, PIVOT p_y) {
        pos_x = x; pos_y = y;
        width = w; height = h;
        label = l;
        pivot_x = p_x;
        pivot_y = p_y;
    }

    // Methods / Méthodes
    public void custom_draw() {
        // Fonction qui sera appelée dans la boucle draw() de Processing (même fonction que loop() dans Arduino) 
    }

    public void custom_mouse_moved() {
        // Fonction qui sera appelée dans la boucle mouseMoved() de Processing
    }

    public void custom_mouse_pressed() {
        // Fonction qui sera appelée dans la boucle mousePressed() de Processing
    }

    public void custom_mouse_released() {
        // Fonction qui sera appelée dans la boucle mouseReleased() de Processing
    }

    public void custom_mouse_dragged() {
        // Fonction qui sera appelée dans la boucle mouseDragged() de Processing
    }

    public int get_global_pos_x() {
        return pos_x;
    }

    public int get_global_pos_y() {
        return pos_y;
    }
}
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
    public void custom_draw() {
        text(label, pos_x, pos_y);
        // Fonction qui sera appelée dans la boucle draw() de Processing (même fonction que loop() dans Arduino) 
    }

}
/* 
GUI Tree



*/
enum PIVOT { TOP, BOTTOM, LEFT, RIGHT, CENTER };


  public void settings() { size(640, 320); }

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "GUI" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
