package dbg;

public class JDISimpleDebuggee {

    public static void main(String[] args) {
        String description = "Simple power printer";
        System.out.println(description + " -- starting");
        int x = 40;
        int power = 2;
        printPower(x, power);
        TestReceiverClass c = new TestReceiverClass();
        c.testReceiver(10, 20);
    }

    public static double power(int x, int power) {
        double powerX = Math.pow(x, power);
        return powerX;
    }

    public static void printPower(int x, int power) {
        double powerX = power(x, power);
        System.out.println(powerX);
    }

    public int multiplication(int x, int y) {
        int result = x * y;
        return result;
    }


}