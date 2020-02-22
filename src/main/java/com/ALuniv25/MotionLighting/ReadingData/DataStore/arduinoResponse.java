package com.ALuniv25.MotionLighting.ReadingData.DataStore;

public class arduinoResponse {
    private int motion;
    private int light;
    private int led;
    private String content;

    public arduinoResponse(){

    }

    public arduinoResponse(String content){
        this.content=content;
    }

    public arduinoResponse(int motion,int light,int led){
        this.motion=motion;
        this.light=light;
        this.led=led;
    }

    /**
     * @return int return the motion
     */
    public int getMotion() {
        return motion;
    }


    /**
     * @param motion the motion to set
     */
    public void setMotion(int motion) {
        this.motion = motion;
    }

    /**
     * @return int return the light
     */
    public int getLight() {
        return light;
    }

    /**
     * @param light the light to set
     */
    public void setLight(int light) {
        this.light = light;
    }

    /**
     * @return int return the led
     */
    public int getLed() {
        return led;
    }

    /**
     * @param led the led to set
     */
    public void setLed(int led) {
        this.led = led;
    }


    /**
     * @return String return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    public String toString(){
        return "Data : Led :"+ getLed() + " Light :" + getLight() + " Motion :" + getMotion() ;
    }

}
