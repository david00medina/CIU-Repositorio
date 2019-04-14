#include <Arduino.h>

#define A                       1.f
#define A_MIN                   -A
#define F                       1.f
#define W                       2.f * PI

#define carrier_wave(t)         ( A * sin(W * F * t) )
#define freq_factor(y)          ( 1.f + (y - A_MIN) )
#define new_freq(y)             ( F * freq_factor(y) )
#define generated_wave(y, t)    ( A * sin(W * new_freq(y) * t) )

float x = 0.f;

void setup() {
  Serial.begin(9600);
  pinMode(LED_BUILTIN, OUTPUT);
}

void loop() {
  float t = millis() / 1000.f;

  x = carrier_wave(t);
  Serial.println(new_freq(x));
  float y = generated_wave(x, t);

  if(y >= 0.f) {
      digitalWrite(LED_BUILTIN, HIGH);
  } else if (y < 0.f) {
      digitalWrite(LED_BUILTIN, LOW);
  }
}
