/* 
GUI Button

Classe parent des boutons. (Hérite de GUIElement)
Gère la création, l'affichage et l'intéraction avec les boutons.
*/

//enum PIVOT_X { LEFT, CENTER, RIGHT };
//enum PIVOT_Y { TOP, BOTTOM, CENTER };

final IntDict PALETTE = new IntDict(new Object[][] {
  {"Blank", #FFFFFF},
  {"Background", #DEDEDE},
  {"Font.Normal", #272829},
  {"Element.Normal", #1B6195},
  {"Element.Hover", #2278B9},
  {"Element.Pressed",#216fa9}, 
  {"Element.Font.Normal", #F0F0F0}
});

int get_color(String name) {
  if (PALETTE.hasKey(name)) {
    return PALETTE.get(name);
  } else {
    return PALETTE.get("Blank");
  }
}
