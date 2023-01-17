// Lancer une cuisson

#include <SoftwareSerial.h>   // Librairie Bluetooth
#include <DHT.h> // Librairie DHT11 Adafruit Température et Humidité (https://learn.adafruit.com/dht)
#include <Stepper.h> // Librairie moteur pas à pas

// DHT11 PIN
#define DHT_PIN 7 // Data

// HC-05 Bluetooth
#define RX_PIN 10 // Brancher le PINOUT TXD du HC-05
#define TX_PIN 11 // Brancher le PINOUT RXD du HC-05

// Moteur pas à pas PIN
#define STEPS 2048 // Nombre de pas du moteur
#define MOTOR_IN1 2
#define MOTOR_IN2 3
#define MOTOR_IN3 4
#define MOTOR_IN4 5
#define MOTOR_SPEED 15

// Création des instances
SoftwareSerial BTSerial(RX_PIN, TX_PIN); // RX, TX 
DHT dht(DHT_PIN, DHT11);
// Stepper stepper(STEPS, MOTOR_IN1, MOTOR_IN2, MOTOR_IN3, MOTOR_IN4);
Stepper stepper(STEPS, MOTOR_IN1, MOTOR_IN3, MOTOR_IN2, MOTOR_IN4);
// Stepper stepper(STEPS, MOTOR_IN2, MOTOR_IN4, MOTOR_IN3, MOTOR_IN1);


void setup() {
    BTSerial.begin(38400); //HC-05 Baud rate
    pinMode(RX_PIN, INPUT); //Définir le PIN "Recevoir" en entrée
    pinMode(TX_PIN, OUTPUT); //Définir le PIN "Transmettre" en sortie

    dht.begin();
    stepper.setSpeed(MOTOR_SPEED);
}

void loop() {
    // On vérifie le nombre de caractère dans le buffer du Bluetooth
    if (BTSerial.available() > 0) { 
        // On lit le premier octet disponible dans le buffer
        char data = BTSerial.read(); // On utilise le type <char> pour que la communication soit plus rapide

        switch (data)
        {   
        case 'h': // Commande pour obtenir l'humidité
            BTSerial.println(dht.readHumidity());
            break;
        
        case 't': // Commande pour obtenir la temperature
            BTSerial.println(dht.readTemperature());
            break;
        
        case 'd':
            stepper.step(1024/2);
            break;

        case 'g':
            stepper.step(-1024);
            break;
        }
    }
}
