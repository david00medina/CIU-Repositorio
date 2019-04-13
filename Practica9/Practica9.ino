const int PIN_LED = 13;
const byte interruptPin = 2;
volatile byte state = LOW;
float sinVal = 0.f;
const float frequency  = 0.1f;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  pinMode(PIN_LED, OUTPUT);
  pinMode ( interruptPin , INPUT_PULLUP) ;
  

}

void loop() {
  float t = millis() / 1000.f;
  float f = frequency * sinVal / 255;
  
  float w = 2.f * PI * frequency;
 
  sinVal = 128.f + 128.f * sin(w * t);
  //analogWrite(PIN_LED, sinVal);
  digitalWrite(PIN_LED,HIGH);
  delay(sinVal);
  digitalWrite(PIN_LED,LOW);
  delay(sinVal);
  Serial.println(sinVal);
}
