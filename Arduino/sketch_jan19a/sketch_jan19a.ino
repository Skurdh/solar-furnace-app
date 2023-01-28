#include <SoftwareSerial.h>
#include <Wire.h>
#include <DHT.h>

SoftwareSerial bluetooth(10, 11); // RX, TX
DHT dht(7, DHT11);

float value = 0;
float previous_value = 0;
unsigned long previousMillis = 0;
static uint32_t tmp;

void setup() {
  bluetooth.begin(38400); // Set the baudrate equal to HC06 setting
  Serial.begin(38400);
  //dht.begin();
}

void loop() {
  String serialOutput;

  while (Serial.available() > 0){
      
  }
  while (Serial.available()) { 
    readString = Serial.readString();
    Serial.println("Output_Data=" + readString);
    }
//
//  if (readString.length() > 0) {
//    Serial.println(readString);
//  }
//
//  unsigned long currentMillis = millis();
//  if (currentMillis - previousMillis >= 500) {
//    tmp++;
//    Serial.println(tmp);
//    previousMillis = currentMillis;
//    bluetooth.print(String(tmp)); // print this to bluetooth module
//  }
}
