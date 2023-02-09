#include <SoftwareSerial.h>
#include <Wire.h>
#include <Stepper.h>
#include <DHT.h>

SoftwareSerial bluetooth(10, 11); // RX, TX
Stepper myStepper = Stepper(2038, 3, 5, 4, 6);
DHT dht(7, DHT11);

const int stepsPerRevolution = 2038;
float value = 0;
float previous_value = 0;
unsigned long previousMillis = 0;
static uint32_t tmp;

void setup() {
  Serial.begin(38400);
  bluetooth.begin(38400);
  dht.begin();
}

String stringBuffer = "";

void loop() {
  unsigned long currentMillis = millis();
  if (currentMillis - previousMillis >= 10000) {
    bluetooth.print("T=" + String(dht.readTemperature()) + "\n");
    previousMillis = currentMillis;
  }

  
  if (bluetooth.available() > 0) {
    char c = bluetooth.read();
    Serial.println(c);
    if (c != '\n') {
      Serial.println("Added");
      stringBuffer += c;
    } else {
      Serial.println("Traitement" + stringBuffer);
      if (stringBuffer.equals("ASK_H")) {
          bluetooth.print("H=" + String(dht.readHumidity()) + "\n");
      } else if (stringBuffer.equals("R")) {
        myStepper.step(15);
      } else if (stringBuffer.equals("L")) {
        myStepper.step(-15);
      }
      stringBuffer = "";
      }   
  }
  
//  if (Serial.available() > 0) { 
//    Serial.read();
//    Serial.println("sended");
//    bluetooth.print("T=" + String(random(26, 32)) + "\n");
//  }
//
//  String s = "";
//  while (bluetooth.available() > 0) {
//      char c = bluetooth.read();
//      s += c;
//  }
//
//  if (!s.equals("")) {
//    Serial.println(s);
//  }
  


  
//  String serialOutput;
//
//  while (Serial.available() > 0){
//      
//  }
//  while (Serial.available()) { 
//    readString = Serial.readString();
//    Serial.println("Output_Data=" + readString);
//    }
//
//  if (readString.length() > 0) {
//    Serial.println(readString);
//  }
//
//  unsigned long currentMillis = millis();
//  if (currentMillis - previousMillis >= 1000) {
//    Serial.println("printed");
//    bluetooth.print("cmd15"); // print this to bluetooth module
//    previousMillis = currentMillis;
//  }
}
