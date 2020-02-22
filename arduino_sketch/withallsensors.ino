
int led = 13;    //pin for the LED 
int motion = 2; // pin for motion sensor 
int motion_value = 0; // motion sensor values
int led_value = 0;
int pirState = LOW; 
byte prestate;
byte state = 1;
void setup() {
    Serial.begin(9600);
    pinMode(LED_BUILTIN, OUTPUT);
    pinMode(motion, INPUT);
    pinMode(led, OUTPUT);
}

void loop() {
  int light_value = analogRead(A0);
  motion_value = digitalRead(motion);
  led_value = digitalRead(led);
            Serial.print(motion_value);
            Serial.print("-");
            Serial.print(light_value);
            Serial.print("-");
            Serial.print(led_value);
            Serial.println();
            if(light_value < 500){
                digitalWrite(led, LOW);
                digitalWrite(led, LOW);
            }
            if(light_value > 500){
                if(motion_value == 1){
                    digitalWrite(led, HIGH);
                    digitalWrite(led, HIGH);
                }
                if(motion_value == 0) {
                    digitalWrite(led, LOW);
                }  
            }
        }
