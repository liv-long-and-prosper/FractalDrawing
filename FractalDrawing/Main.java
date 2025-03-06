package FractalDrawing;

import src.FractalGui;

public class Main {
    public static void main(String[] args){
        FractalSubject subject = new FractalGenerator();
        new FractalGui(subject);
        new FractalDrawing(subject);
       try{
           src.FractalGui testGui = new FractalGui();} catch (Exception e) {
           System.out.println("Error: "+e);
           System.exit(0);
       }
    }
}
