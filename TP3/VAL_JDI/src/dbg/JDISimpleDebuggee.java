package dbg;

public class JDISimpleDebuggee {

    public static void main(String[] args) {
        String description = "Simple power printer";
        System.out.println(description + " -- starting");
        AClass aclass = new AClass();
        aclass.getInfo();
        int x = 40;
        int power = 2;
        printPower(x, power);
        loop();
    }

    public static double power(int x, int power) {
        double powerX = Math.pow(x, power);
        return powerX;
    }

    public static void printPower(int x, int power) {
        double powerX = power(x, power);
        System.out.println(powerX);
    }

    public static void loop() {
        for(int i = 0; i < 3; i++) {
            int ledouble = i * 2;
            System.out.println(ledouble);
        }
    }

}