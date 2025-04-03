package tqs.sets;

import java.util.Iterator;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author ico0
 */
class BoundedSetOfNaturalsTest {
    private BoundedSetOfNaturals set;
    private BoundedSetOfNaturals setA;
    private BoundedSetOfNaturals setB;
    private BoundedSetOfNaturals setC;

    @BeforeEach
    public void setUp() {
        set = new BoundedSetOfNaturals(3);
        setA = new BoundedSetOfNaturals(1);
        setB = BoundedSetOfNaturals.fromArray(new int[]{10, 20, 30, 40, 50, 60});
        setC = BoundedSetOfNaturals.fromArray(new int[]{50, 60});
    }

    @AfterEach
    public void tearDown() {
        setA = setB = setC = null;
    }

    @Test

    public void testAddElement() {

        setA.add(9);
        assertTrue(setA.contains(9), "add: added element not found in set.");
        assertEquals(1, setA.size());
    }
    @Test
    void testAddValidElement() {
        set.add(10);
        assertTrue(set.contains(10), "add: added element should be present in the set");
        assertEquals(1, set.size(), "add: size should be updated after adding an element");
    }

    @Test
    void testAddDuplicateElementThrowsException() {
        set.add(5);
        assertThrows(IllegalArgumentException.class, () -> set.add(5),
                "add: should not allow duplicate elements");
    }

    @Test
    void testAddNegativeNumberThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> set.add(-3),
                "add: should not allow negative numbers");
    }

    @Test
    void testAddZeroThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> set.add(0),
                "add: should not allow zero");
    }

    @Test
    void testAddBeyondMaxSizeThrowsException() {
        set.add(1);
        set.add(2);
        set.add(3);
        assertThrows(IllegalArgumentException.class, () -> set.add(4),
                "add: should not allow adding elements beyond maxSize");
    }
    
    @Test
    public void testAddBeyondLimit() {
        setA.add(99);
        assertThrows(IllegalArgumentException.class, () -> setA.add(100),
            "Should throw exception when adding beyond maxSize.");
    }


    @Test
    public void testAddDuplicateElement() {
        assertThrows(IllegalArgumentException.class, () -> setB.add(10), "add: duplicate value should not be allowed.");
    }

    @Test
    public void testAddNegativeNumber() {
        assertThrows(IllegalArgumentException.class, () -> setA.add(-5), "add: negative values should not be allowed.");
    }

    @Test
    public void testAddZero() {
        assertThrows(IllegalArgumentException.class, () -> setA.add(0), "add: zero should not be allowed.");
    }

    @Test
    public void testBoundLimit() {
        setA.add(5);
        assertThrows(IllegalArgumentException.class, () -> setA.add(10), "add: should not allow adding more elements than maxSize.");
    }

    @Test
    public void testAddFromBadArray() {
        int[] elems = new int[]{10, -20, -30};
        assertThrows(IllegalArgumentException.class, () -> setA.add(elems));
    }

    @Test
    public void testFromArray() {
        int[] values = {5, 15, 25};
        BoundedSetOfNaturals setD = BoundedSetOfNaturals.fromArray(values);

        assertEquals(3, setD.size());
        assertTrue(setD.contains(5));
        assertTrue(setD.contains(15));
        assertTrue(setD.contains(25));
    }

    @Test

    public void testIntersects() {
        assertTrue(setB.intersects(setC), "intersects: should return true for overlapping sets.");
        BoundedSetOfNaturals setD = BoundedSetOfNaturals.fromArray(new int[]{100, 200});
        assertFalse(setB.intersects(setD), "intersects: should return false for non-overlapping sets.");
    }

    @Test
    public void testIterator() {
        Iterator<Integer> iterator = setB.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertEquals(10, iterator.next());
    }

    @Test
    public void testEquality() {
        BoundedSetOfNaturals setX = BoundedSetOfNaturals.fromArray(new int[]{10, 20, 30, 40, 50, 60});
        assertEquals(setB, setX, "equals: sets with the same elements should be equal.");
        
        BoundedSetOfNaturals setY = BoundedSetOfNaturals.fromArray(new int[]{10, 20, 30});
        assertNotEquals(setB, setY, "equals: sets with different elements should not be equal.");
    }
    @Test
    public void testEquals() {
        BoundedSetOfNaturals set1 = BoundedSetOfNaturals.fromArray(new int[]{10, 20, 30});
        BoundedSetOfNaturals set2 = BoundedSetOfNaturals.fromArray(new int[]{10, 20, 30});
        BoundedSetOfNaturals set3 = BoundedSetOfNaturals.fromArray(new int[]{10, 20});
        BoundedSetOfNaturals set4 = new BoundedSetOfNaturals(5);


        assertEquals(set1, set2, "equals: sets with same elements should be equal");

        assertNotEquals(set1, set3, "equals: sets with different elements should not be equal");
        assertNotEquals(set1, set4, "equals: non-empty set should not be equal to empty set");
        assertNotEquals(set1, null, "equals: should return false when comparing with null");
        assertNotEquals(set1, "SomeString", "equals: should return false when comparing with a different type");
}

    @Test
    public void testHashCode() {
        BoundedSetOfNaturals set1 = BoundedSetOfNaturals.fromArray(new int[]{10, 20, 30});
        BoundedSetOfNaturals set2 = BoundedSetOfNaturals.fromArray(new int[]{10, 20, 30});
        BoundedSetOfNaturals set3 = BoundedSetOfNaturals.fromArray(new int[]{10, 20});
        assertEquals(set1.hashCode(), set2.hashCode(), "hashCode: equal objects must have the same hashCode");
        assertNotEquals(set1.hashCode(), set3.hashCode(), "hashCode: different objects should have different hashCodes");
}

}
