/*********************************************************************
**  Description:                                                    **
**  This file is a sample code for your reference.                  **
**                                                                  **
**  Copyright (C) 2011 ElecFreaks Corp.                           **
**  Created by ElecFreaks Robi.W /10 June 2011                      **
**                                                                  **
**  http://www.elecfreaks.com                                       **
*********************************************************************/
#include <SoftwareSerial.h>

#define rxPin 2
#define txPin 3

int tmpPin = 0;
int ledTmpPin = 9; 
int ledSigPin = 11;
SoftwareSerial mySerial =  SoftwareSerial(rxPin, txPin);

void setup() {
  // set the pinModes for the bluetooth communication
  pinMode(rxPin, INPUT);
  pinMode(txPin, OUTPUT);

  // begin bluetooth communication
  mySerial.begin(9600);

  // begin serial communication
  Serial.begin(9600);

  // set the pinModes for the LEDs
  pinMode(ledTmpPin, OUTPUT);
  pinMode(ledSigPin, OUTPUT);
}

void loop() {
  // If bluetooth communication is available
  if(mySerial.available()) {
    int i = 0;
    char measureValue[32] = {};
    // read every incoming char (value)
    do {
       measureValue[i++] = mySerial.read();
    } while(mySerial.available());
    // transformate the incoming chars to int    
    String measureValueAsString = measureValue;
    int measureTimes = measureValueAsString.toInt();
    
    // print out the incoming bluetooth value
    Serial.print("Measure times = ");
    Serial.println(measureTimes);

    /**** Temperature Measure begins ****/
    // Temperature Measure LED is ON
    digitalWrite(ledTmpPin, HIGH);

    // Measure temperature with the given times (incoming with bluetooth communication)
    float sumTemp = 0;
    for(int i = 0; i < measureTimes; i++) {
      // Signal LED is ON
      digitalWrite(ledSigPin, HIGH);

      // Measure the temperature
      float temp = analogRead(tmpPin);
      temp = temp * 0.48828;
      sumTemp = sumTemp + temp;
      
      // Signal LED is OFF (but wait 250ms to see the blink)
      delay(250);
      digitalWrite(ledSigPin, LOW);
    
      // Wait for next measurement
      delay(500);
    }
  
    // Calculate the average temperature
    sumTemp = sumTemp/measureTimes;

    // Print out the calculated temperature
    Serial.print("Temp = ");
    Serial.println(sumTemp);

    // Send the measured temperature to the device
    mySerial.print(sumTemp);
    mySerial.print("s");
    
    // Temperature LED is OFF
    digitalWrite(ledTmpPin, LOW);
  }
}
