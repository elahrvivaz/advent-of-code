package day01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day01 implements Runnable {

    public static void main(String[] args) {
        new Day01().run();
    }

    @Override
    public void run() {
        var left = new ArrayList<Integer>();
        var right = new ArrayList<Integer>();
        try(var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("input"), StandardCharsets.UTF_8))) {
            reader.lines().forEach((line) -> {
                if (!line.isEmpty()) {
                    var array = line.split(" +");
                    left.add(Integer.parseInt(array[0].trim()));
                    right.add(Integer.parseInt(array[1].trim()));
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        part1(left, right);
        part2(left, right);
    }

    public void part1(List<Integer> left, List<Integer> right) {
        Collections.sort(left);
        Collections.sort(right);
        var sum = 0;
        for (var i = 0; i < left.size(); i++) {
            sum += Math.abs(left.get(i) - right.get(i));
        }
        System.out.println(sum);
    }

    public void part2(List<Integer> left, List<Integer> right) {
        var sum = 0L;
        for (Integer id : left) {
            var mult = right.stream().filter(r -> r.equals(id)).count();
            sum += (id * mult);
        }
        System.out.println(sum);
    }
}
