
//import java.util.Scanner;
import java.util.Random;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

// Timothy Lafontaine ID: 261066866
// Assignment 2 Mouse

public class Main{

    public static int cnt = 0;

    public static int miceLeft;

    public static int total;

    public static Timer t = new Timer();
    public static Timer t2 = new Timer();
    public static Timer t3 = new Timer();
    public static Timer t4 = new Timer();


    public static void main(String[] args){

        Random rndm = new Random();

        int miceAmt = rndm.nextInt(10) + 1;
        Mice[] mouse = new Mice[miceAmt];
        setMice(mouse, miceAmt);

        int trapsAmt = rndm.nextInt(miceAmt) + 1;
        Traps[] trap = new Traps[trapsAmt];
        setTraps(trap, trapsAmt);

        Queue<String> miceCollected = new LinkedList<>();

        miceLeft = miceAmt;

        setCheeseTimer(t); // 2 secs
        setOwnerTimer(t2, trap, trapsAmt, miceCollected);
        setMouseTimer(mouse, trap, rndm, trapsAmt, miceAmt, t3, miceCollected);
        setCheckMouseAmtTimer(t, t2, t3, t4);


    }
    public static <miceLeft> void setMouseTimer(Mice[] mouse, Traps[] trap, Random rndm, int trapsAmt, int miceAmt, Timer t3, Queue<String> miceCollected){
        t3.scheduleAtFixedRate(
                new TimerTask() {
                    public void run() {
                        int m = rndm.nextInt(miceAmt);
                        int trp = rndm.nextInt(trapsAmt);
                        int c = rndm.nextInt(3) + 1;
                        while (mouse[m].isCaught()) {
                            m = rndm.nextInt(miceAmt);
                        }
                        while (trap[trp].isOccupied()) {
                            trp = rndm.nextInt(trapsAmt);
                        }
                        int b = rndm.nextInt(2);
                        if (b == 1) {
                            //mouse[m].isCaught();
                            trap[trp].setOffTrap(mouse[m]);
                            //miceLeft -= 1;
                            subMouse();
                            miceCollected.add(mouse[m].name());
                        }
                        else {
                            if (cnt == 0) {

                                while (cnt == 0) {
                                    //mouse waits for cheese
                                }
                            } else if (cnt - c < 0) {
                                c = cnt;
                            }


                            cnt -= c;
                            //total += c;
                            newTotal(c);
                            System.out.println("[Action]\t" + mouse[m].name() + " ate " + c + " gram(s) of cheese.");
                            System.out.println("[Status]\tCheese left: " + cnt);
                        }
                    }
                },
                0, 1000);
    }
    public static void setCheckMouseAmtTimer(Timer t, Timer t2, Timer t3, Timer t4){
        t4.scheduleAtFixedRate(
                new TimerTask(){
                    public void run(){
                        if (miceLeft == 0) {
                            t.cancel();
                            t2.cancel();
                            t3.cancel();
                            t4.cancel();

                            System.out.println("\n\n[Status]\tTotal cheese lost: " + total + " grams");
                        }
                    }
                },
                0, 500);
    }
    public static void setCheeseTimer(Timer t) {
        t.scheduleAtFixedRate(
                new TimerTask(){
                    public void run(){
                        addCheese();
                        System.out.println("[Action]\tCheese machine adds 10 grams of cheese.");
                        System.out.println("[Status]\tCheese left: " + cnt);
                    }
                },
                0, 2000);
    }
    public static void setOwnerTimer(Timer t2, Traps[] trap, int trapsAmt, Queue<String> miceCollected){
        t2.scheduleAtFixedRate(
                new TimerTask(){
                    public void run(){
                        System.out.println("==================================================");
                        clearTraps(trap, trapsAmt);
                        miceCaught(miceCollected);
                        System.out.println("==================================================");
                    }
                },
                0, 5000);
    }
    public static <miceLeft> void subMouse() {
        miceLeft = miceLeft - 1;
    }

    public static void newTotal(int c) {
        total = total + c;
    }
    public static void setMice(Mice[] mouse, int total) {
        for (int i = 0; i < total; i++) {
            mouse[i] = new Mice(i);
        }
    }

    public static void setTraps(Traps[] trap, int total) {
        for (int i = 0; i < total; i++) {
            trap[i] = new Traps();
        }
    }
    public static void clearTraps(Traps[] trap, int total) {
        for (int i = 0; i < total; i++) {
            trap[i].resetTrap();
        }
    }
    public static void addCheese() {
        cnt += 10;
    }

    public static void miceCaught(Queue<String> miceCollected) {
        if (miceCollected.size() == 0) {
            System.out.println("[Status]\tNo more mice to catch");
            return;
        }
        String mouse = miceCollected.remove();
        System.out.println("[Action]\t" + mouse + " was collected.");
        miceCaught(miceCollected);

    }
}



class Mice{
    private String name;
    private boolean caught;

    public Mice(int num) {
        this.name = "mouse-" + (num + 1);
        this.caught = false;
    }

    public String name() {
        return this.name;
    }

    public boolean isCaught() {
        return this.caught;
    }

    public void caught() {
        this.caught = true;
    }

}

class Traps{

    private boolean occupied;

    private Mice mouse;

    public Traps() {
        this.occupied = false;
    }

    public void setOffTrap(Mice mouse) {
        this.occupied = true;
        this.mouse = mouse;
        System.out.println("[Action]\t" + mouse.name() + " got caught by a trap.");
        mouse.caught();
    }

    public void resetTrap() {
        this.occupied = false;
        this.mouse = null;
    }

    public boolean isOccupied() {
        return this.occupied;
    }
}
