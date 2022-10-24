package dbg;

public class TestReceiverClass {

    int x, y;

    public void testReceiver(int x, int y) {
        System.out.println("testReceiver()");
        this.x = x;
        this.y = y;
        JDISimpleDebuggee jdiSimpleDebuggee = new JDISimpleDebuggee();
        int result = jdiSimpleDebuggee.multiplication(x, y);
        System.out.println("Result: " + result);
        //double res = JDISimpleDebuggee.power(x, y);
        //System.out.println("res: " + res);
    }

}
