import java.util.*;

public class BadSmellExample {

    public void process(int t, String n, double p, int q, String type) {
        double total = 0;

        if (type.equals("VIP")) {
            total = p * q * 0.8;
            if (q > 10) {
                total = total - 50;
                if (total > 500) {
                    total = total - 20;
                    if (total > 1000) {
                        total = total - 30;
                    }
                }
            }
            System.out.println("VIP customer: " + n + ", total: " + total);
        } else if (type.equals("NORMAL")) {
            total = p * q;
            if (q > 10) {
                total = total - 20;
                if (total > 500) {
                    total = total - 10;
                    if (total > 1000) {
                        total = total - 15;
                    }
                }
            }
            System.out.println("Normal customer: " + n + ", total: " + total);
        } else {
            total = p * q;
            System.out.println("Unknown customer: " + n + ", total: " + total);
        }

        if (t == 1) {
            System.out.println("Type 1 order");
        } else if (t == 2) {
            System.out.println("Type 2 order");
        } else if (t == 3) {
            System.out.println("Type 3 order");
        } else if (t == 4) {
            System.out.println("Type 4 order");
        }

        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");

        for (String s : list) {
            if (s.equals("A")) {
                System.out.println("Found A");
            }
            if (s.equals("B")) {
                System.out.println("Found B");
            }
            if (s.equals("C")) {
                System.out.println("Found C");
            }
        }

        for (String s : list) {
            if (s.equals("A")) {
                System.out.println("Found A");
            }
            if (s.equals("B")) {
                System.out.println("Found B");
            }
            if (s.equals("C")) {
                System.out.println("Found C");
            }
        }
    }

    public void a() {
        System.out.println("a");
    }

    public void b() {
        System.out.println("b");
    }

    public void c() {
        System.out.println("c");
    }

    public void d() {
        System.out.println("d");
    }
}