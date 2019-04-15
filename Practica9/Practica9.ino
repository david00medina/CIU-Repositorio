#include <Arduino.h>

float sinVal = 0.f;
const float frequency  = 0.1f;

void setup() {
    Serial.begin(9600);
    pinMode(LED_BUILTIN, OUTPUT);
}

void loop() {
    float t = millis() / 1000.f;
    float f = frequency * sinVal / 255;

    float w = 2.f * PI * frequency;
    sinVal = 128.f + 128.f * sin(w * t);

    digitalWrite(LED_BUILTIN,HIGH);
    delay(sinVal);
    digitalWrite(LED_BUILTIN,LOW);
    delay(sinVal);
}