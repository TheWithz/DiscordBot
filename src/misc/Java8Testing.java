package misc;

import java.util.Arrays;
import java.util.List;

/**
 * Created by TheWithz on 2/16/16.
 */
public class Java8Testing {

    public static void main(String[] args) {
        List<String> myList = Arrays.asList("a2", "a1", "b1", "c2", "c1", "c7", "c4", "c12", "c14");

        myList.stream().filter(s -> s.startsWith("c", 0)).map(String::toUpperCase).sorted().forEach(System.out::println);
    }
}
