#include <Arduino.h>

float signalScaling();

void setup() {
    Serial.begin(115200);
    delay(43);
}

void loop() {
    int val = (int) signalScaling();
    Serial.println(val);
    delay(100);
}

float signalScaling() {
    return analogRead(A0) * (1023. / 2.6) * (5. / 1023.) * (255. / 1023.);
}