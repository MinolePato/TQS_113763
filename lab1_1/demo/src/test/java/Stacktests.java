import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.example.Stack;

public class Stacktests {
    Stack stack;

    
    @Test
    void Constructortest(){
        stack = new Stack();
        assertEquals(true, stack.isEmpty());
        assertEquals(0, stack.size());
        stack.push(1);
        assertEquals(false, stack.isEmpty());
        
    }
    @Test
    void pushtest(){
        stack = new Stack();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.size());
    }
    @Test
    void peektest(){
        stack = new Stack();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.peek());
    }
    @Test 
    void poptest(){
        stack =new Stack();
        stack.push(1);
        stack.push(2);
        assertEquals(2, stack.pop());
        assertEquals(1, stack.size());
        assertEquals(1, stack.peek());
    }
    @Test 
    void pop2test(){
        stack =new Stack();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.size());
        stack.pop();
        stack.pop();
        stack.pop();
        assertEquals(0, stack.size());

    }
    @Test
    void pop3test(){
        stack =new Stack();
        assertThrows(NoSuchElementException.class, stack::pop);
    }
    @Test
    void peek2test() {
        stack = new Stack();
        assertThrows(NoSuchElementException.class, stack::peek);
    }

    @Test
    void popTopNtest() {
        stack = new Stack();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(5);

        int result = stack.popTopN(2);
        assertEquals(4, result);
        assertEquals(3, stack.size());
    }
}
