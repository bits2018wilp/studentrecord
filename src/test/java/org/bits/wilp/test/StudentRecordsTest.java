package org.bits.wilp.test;

import org.bits.wilp.dsad.StudentHash;
import org.bits.wilp.dsad.StudentRecords;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StudentRecordsTest {

    @Test
    public void testValidation() throws IOException {
        StudentHash studentHashTable = new StudentHash(31);

        StudentRecords studentRecords = new StudentRecords();
        studentRecords.initializeHash(studentHashTable);

        studentRecords.populateHashTable(studentHashTable, "input/validation-Input.txt");

        Assert.assertEquals(4, studentHashTable.size());
        Iterator<StudentHash.StudentRecord> itr = studentHashTable.getElementsIterator();
        while(itr.hasNext()) {
            System.out.println(itr.next());
        }

    }

    @Test
    public void testHashTablePopulation() throws IOException {

        StudentHash studentHashTable = new StudentHash(31);

        StudentRecords studentRecords = new StudentRecords();
        studentRecords.initializeHash(studentHashTable);

        studentRecords.populateHashTable(studentHashTable, "input/testInput.txt");

        Assert.assertEquals(14, studentHashTable.size());
    }

    @Test
    public void testHallOFFame() throws IOException {
        StudentHash studentHashTable = new StudentHash(31);

        StudentRecords studentRecords = new StudentRecords();
        studentRecords.initializeHash(studentHashTable);

        studentRecords.populateHashTable(studentHashTable, "input/testInput.txt");

        List<StudentHash.StudentRecord> records = studentRecords.hallOfFame(studentHashTable, 6.0f);

        Assert.assertEquals(9, records.size());
    }

    @Test
    public void testNewCourseList() throws IOException {
        StudentHash studentHashTable = new StudentHash(31);

        StudentRecords studentRecords = new StudentRecords();
        studentRecords.initializeHash(studentHashTable);

        studentRecords.populateHashTable(studentHashTable, "input/testInput.txt");

        List<StudentHash.StudentRecord> records = studentRecords.newCourseList(studentHashTable, 6.0f, 9.0f);
        System.out.println(records);
        Assert.assertEquals(5, records.size());

        List<String> collect = records.stream().map(new Function<StudentHash.StudentRecord, String>() {
            @Override
            public String apply(StudentHash.StudentRecord studentRecord) {
                return studentRecord.getStudentId();
            }
        }).collect(Collectors.toList());

        Assert.assertTrue(collect.contains("2011MEC9997"));
        Assert.assertTrue(collect.contains("2012ARC9994"));
        Assert.assertTrue(collect.contains("2014MEC9997"));
        Assert.assertTrue(collect.contains("2013CSE9997"));
        Assert.assertTrue(collect.contains("2010ECE9997"));

        Assert.assertFalse(collect.contains("2008CSE9995"));
        Assert.assertFalse(collect.contains("2009ECE9995"));
        Assert.assertFalse(collect.contains("2016ECE9987"));

    }

    @Test
    public void testDeptAvg() throws IOException {
        StudentHash studentHashTable = new StudentHash(31);

        StudentRecords studentRecords = new StudentRecords();
        studentRecords.initializeHash(studentHashTable);

        studentRecords.populateHashTable(studentHashTable, "input/testInput.txt");

        List<StudentRecords.DeptInfo> deptInfos = studentRecords.depAvg(studentHashTable);

        StudentRecords.DeptInfo cse = deptInfos.get(0);
        Assert.assertEquals(7.5633345f, cse.getMaxCgpa(), 0);
        Assert.assertEquals(6.2806597f, cse.getTotalCgpa()/cse.getStudentCount(), 0);
        Assert.assertEquals(4, cse.getStudentCount());

        StudentRecords.DeptInfo ece = deptInfos.get(1);
        Assert.assertEquals(7.5633345f, ece.getMaxCgpa(), 0);
        Assert.assertEquals(5.90617f, ece.getTotalCgpa()/ece.getStudentCount(), 0);
        Assert.assertEquals(5, ece.getStudentCount());

        StudentRecords.DeptInfo mec = deptInfos.get(2);
        Assert.assertEquals(7.4326367f, mec.getMaxCgpa(), 0);
        Assert.assertEquals(7.4326367f, mec.getTotalCgpa()/mec.getStudentCount(), 0);
        Assert.assertEquals(2, mec.getStudentCount());

        StudentRecords.DeptInfo arc = deptInfos.get(3);
        Assert.assertEquals(8.325594f, arc.getMaxCgpa(), 0);
        Assert.assertEquals(5.669334f, arc.getTotalCgpa()/arc.getStudentCount(), 0);
        Assert.assertEquals(3, arc.getStudentCount());
    }

    @Test
    public void testHashTableDestroy() throws IOException {

        StudentHash studentHashTable = new StudentHash(31);

        StudentRecords studentRecords = new StudentRecords();
        studentRecords.initializeHash(studentHashTable);

        studentRecords.populateHashTable(studentHashTable, "input/testInput.txt");

        Assert.assertEquals(14, studentHashTable.size());

        Assert.assertNotNull( studentHashTable.get("2011CSE9995"));

        studentRecords.destroyHash(studentHashTable); // hash table destroyed

        Assert.assertEquals(0, studentHashTable.size());

        boolean exceptionHappened = false;
        try {
            StudentHash.StudentRecord studentRecord = studentHashTable.get("2011CSE9995");// it should throw NPE because hashtable is null.
        }catch (NullPointerException ex) {
            Assert.assertNotNull(ex);
            exceptionHappened = true;
        }
        Assert.assertTrue(exceptionHappened);
    }

}
