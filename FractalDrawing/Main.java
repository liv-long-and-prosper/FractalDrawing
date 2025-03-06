package src;

import javax.swing.*;

public class Main {
    public static void main(String[] args){
       try{
           FractalGui testGui = new FractalGui();} catch (Exception e) {
           System.out.println("Error: "+e);
           System.exit(0);
       }
    }
}