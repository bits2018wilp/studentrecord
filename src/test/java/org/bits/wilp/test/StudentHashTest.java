package org.bits.wilp.test;

import org.bits.wilp.dsad.StudentHash;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StudentHashTest {

    @Test
    public void testInsert() {
        StudentHash studentHash = new StudentHash(5);

        studentHash.put("2018CSE1234", 2);
        studentHash.put("2018MEC1234", 3);
        studentHash.put("2018ECE1234", 4);
        studentHash.put("2018ARC1234", 5);

        studentHash.put("2018ARC1234", 5); // dupe insert

        Assert.assertEquals(4,studentHash.size());

        Assert.assertEquals(5f, studentHash.get("2018ARC1234").getCgpa(),0);
        studentHash.put("2018ARC1234", 6); // changing value of existing record
        Assert.assertEquals(6f, studentHash.get("2018ARC1234").getCgpa(),0);
    }

    @Test
    public void testRemove() {
        StudentHash studentHash = new StudentHash(5);

        studentHash.put("2018CSE1234", 2);
        studentHash.put("2018MEC1234", 3);
        studentHash.put("2018ECE1234", 4);
        studentHash.put("2018ARC1234", 5);

        Assert.assertEquals(4,studentHash.size());

        StudentHash.StudentRecord remove = studentHash.remove("2018MEC1234");

        //assert values of removed record
        Assert.assertEquals("2018MEC1234", remove.getStudentId());
        Assert.assertEquals(3, remove.getCgpa(),0);

        Assert.assertNull(studentHash.get("2018MEC1234"));

        //asert size after remove
        Assert.assertEquals(3, studentHash.size());
    }

    @Test
    public void testGet() {
        StudentHash studentHash = new StudentHash(5);

        studentHash.put("2018CSE1234", 2);
        studentHash.put("2018MEC1234", 3);
        studentHash.put("2018ECE1234", 4);
        studentHash.put("2018ARC1234", 5);

        //test get on existing record
        Assert.assertEquals(4, studentHash.get("2018ECE1234").getCgpa(),0);
        Assert.assertEquals(5, studentHash.get("2018ARC1234").getCgpa(),0);
        Assert.assertEquals(3, studentHash.get("2018MEC1234").getCgpa(),0);
        Assert.assertEquals(2, studentHash.get("2018CSE1234").getCgpa(),0);

        //test get on non existing record.
        Assert.assertNull(studentHash.get("2018CSE1239"));
    }

    @Test
    public void testIterator() {
        StudentHash studentHash = new StudentHash(5);

        studentHash.put("2018CSE1234", 2);
        studentHash.put("2018MEC1234", 3);
        studentHash.put("2018ECE1234", 4);
        studentHash.put("2018ARC1234", 5);
        studentHash.put("2018ARC1235", 5);
        studentHash.put("2018ARC1237", 5);

        List<StudentHash.StudentRecord> records = new ArrayList<>();
        Iterator<StudentHash.StudentRecord> elementsIterator = studentHash.getElementsIterator();

        while (elementsIterator.hasNext()) {
            records.add(elementsIterator.next());
        }
        Assert.assertEquals(6, records.size());
        System.out.println(records);
    }

    @Test
    public void testCompressionMap() {
        StudentHash studentHash = new StudentHash(7);

        studentHash.put("2018CSE1234", 2);
        studentHash.put("2018MEC1234", 3);
        studentHash.put("2018ECE1234", 4);
        studentHash.put("2018ARC1234", 5);

        Assert.assertEquals(0, studentHash.getRehashCount());
        Assert.assertEquals(3, studentHash.getBucketUsed(),0);
    }

    @Test
    public void testHashFunction() {
        StudentHash studentHash = new StudentHash(31);
        int h1 = studentHash.HashId("2018CSE1234");
        int h2 = studentHash.HashId("2018ARC1234");
        Assert.assertNotEquals(h1, h2);

        System.out.println(h1%11);
        System.out.println(h2%11);

        int h3 = studentHash.HashId("2018CSE1234");
        Assert.assertEquals(h1 , h3);

        int h4 = studentHash.HashId("2018MEC1234");
        int h5 = studentHash.HashId("2018ECE1234");
        int h6 = studentHash.HashId("2018ECE4234");

        Assert.assertNotEquals(h1, h2);
        Assert.assertNotEquals(h1, h4);
        Assert.assertNotEquals(h1, h5);
        Assert.assertNotEquals(h2, h4);
        Assert.assertNotEquals(h2, h5);
        Assert.assertNotEquals(h4, h5);
        Assert.assertNotEquals(h6, h2);
        Assert.assertNotEquals(h6, h4);
        Assert.assertNotEquals(h5, h6);
    }
}
