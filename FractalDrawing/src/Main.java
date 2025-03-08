package FractalDrawing.src;

public class Main {
    public static void main(String[] args){
        FractalGui testGui = null;
        FractalSubject testSubj = new FractalGenerator();
       try{
           testGui = new FractalGui(testSubj);} catch (Exception e) {
           System.out.println("Error: "+e);
           System.exit(0);
       }
       try {
           FractalDrawing testDrawing = new FractalDrawing(testSubj);
       }catch (Exception e){
           System.out.println("Error: "+e);
           System.exit(0);
       }
    }
}