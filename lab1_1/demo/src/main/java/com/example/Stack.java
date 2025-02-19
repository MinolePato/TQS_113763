package com.example;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Stack {
    private ArrayList<Integer> stack;

    public Stack() {
        stack = new ArrayList<>();
    }

    public void push(int x) {
        stack.add(x);
    }

    public int pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack is empty!");
        }
        return stack.remove(stack.size() - 1);
    }

    public int peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack is empty!");
        }
        return stack.get(stack.size() - 1);
    }

    public int size() {
        return stack.size();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }
    public int popTopN(int n) {
        if (n > size() || n <= 0) {
            throw new IllegalArgumentException("Invalid n: Stack does not have " + n + " elements");
        }
        for (int i = 0; i < n - 1; i++) {
            pop();
        }
        return pop();
    }
}
