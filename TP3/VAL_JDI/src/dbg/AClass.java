package dbg;

public class AClass {
    String name = "SenderTest";
    int age = 10;

    public String getInfo() {
        System.out.println("======> SenderTest.getInfo()" + this);
        this.age += 1;
        return name;
    }



}
