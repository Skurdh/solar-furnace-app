// Librairies Ketai Bluetooth et Android
import android.content.Intent;
import android.os.Bundle;

import ketai.net.bluetooth.*;
import ketai.ui.*;
import ketai.net.*;

KetaiBluetooth bt


//********************************************************************
// The following code is required to enable bluetooth at startup.
//********************************************************************
void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    bt = new KetaiBluetooth(this);
    println("Creating KetaiBluetooth");
}

void onActivityResult(int requestCode, int resultCode, Intent data) {
    bt.onActivityResult(requestCode, resultCode, data);
}

//********************************************************************

enum ANCHOR { TOP, BOTTOM, LEFT, RIGHT, CENTER }



void setup(){
    fullscreen(); // Définit la taille de l'application en utilisant la résolution de l'appareil
    bt.start(); // Lance l'écoute des connections Bluetooth
}

void draw(){
  
}
