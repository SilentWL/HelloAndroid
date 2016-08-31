package json;

/**
 * Created by Administrator on 2016/4/26 0026.
 */
public class User {
    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    private String name;
    private int age;


    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }
}
