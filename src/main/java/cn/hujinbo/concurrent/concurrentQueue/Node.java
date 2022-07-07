package cn.hujinbo.concurrent.concurrentQueue;

/**
 * @Auther: jinbo.hu
 * @Date: 2022/6/29 15:42
 * @Description:
 */
public class Node {
    public String name;

    public String getName() {
        return name;
    }

    public static void main(String[] args) {

        Node node1 = new Node();
        node1.name = "zhangsan";
        Node node2 = node1;
        node2.getName();
        System.out.println(node2.getName());
        node2.name = "lisi";
        System.out.println(node1.getName());

        System.out.println(node1);
        System.out.println(node2);
    }
}
