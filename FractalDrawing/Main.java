package FractalDrawing;

import src.FractalGui;

public class Main {
    public static void main(String[] args){
       try{
           src.FractalGui testGui = new FractalGui();} catch (Exception e) {
           System.out.println("Error: "+e);
           System.exit(0);
       }
    }
}