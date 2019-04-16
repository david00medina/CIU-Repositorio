#include <Arduino.h>

#define AMPLITUDE               1.
#define MIN_FREQ                1.
#define sine_wave(f, t)         ( AMPLITUDE * sin(2 * PI * f * t) )

double clockTick = 0.f;

void setup() {
    Serial.begin(9600);
    pinMode(LED_BUILTIN, OUTPUT);
}

void loop() {
    double s = sine_wave(MIN_FREQ, clockTick);
    double f = MIN_FREQ + MIN_FREQ * (s / AMPLITUDE) + MIN_FREQ;

    double t = 1/f;

    delay(t/2 * 1000);
    digitalWrite(LED_BUILTIN, HIGH);
    delay(t/2 * 1000);
    digitalWrite(LED_BUILTIN, LOW);

    clockTick = clockTick + 1.f/16.f;
    if (clockTick >= 1000.f) clockTick = 0.f;
}