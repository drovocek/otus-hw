package ru.otus;

import ru.otus.tester.*;

public class TestExample {

    void ignore(){
        System.out.println("- ignore call by " + this.hashCode());
    }

    @Before
    void before_1(){
        System.out.println("- before_1 call by " + this.hashCode());
    }

    @Before
    void before_2(){
        System.out.println("- before_2 call by " + this.hashCode());
    }

    @After
    void after_1(){
        System.out.println("- after_1 call by " + this.hashCode());
    }

    @After
    void after_2(){
        System.out.println("- after_2 call by " + this.hashCode());
    }

    @Test
    private void test_1() {
        System.out.println("- test_1 call by " + this.hashCode());
    }

    @Test
    private void test_2() {
        System.out.println("- test_2 call by " + this.hashCode());
        throw new RuntimeException("test_2 exception message call by " + this.hashCode());
    }

    @Test
    private void test_3() {
        System.out.println("- test_3 call by " + this.hashCode());
    }

    @Test
    private void test_4() {
        System.out.println("- test_4 call by " + this.hashCode());
        throw new RuntimeException("test_4 exception message call by " + this.hashCode());
    }

    @Test
    private void test_5() {
        System.out.println("- test_5 call by " + this.hashCode());
    }
}
