package org.bits.wilp.test;

import org.bits.wilp.dsad.StudentHash;
import org.bits.wilp.dsad.StudentRecords;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class StudentRecordsTest {

    @Test
    public void testHashTablePopulation() throws IOException {

        StudentHash studentHashTable = new StudentHash(31);

        StudentRecords studentRecords = new StudentRecords();
        studentRecords.initializeHash(studentHashTable);

        studentRecords.populateHashTable(studentHashTable, "input/testInput.txt");

        Assert.assertEquals(9, studentHashTable.size());
    }

    @Test
    public void testHallOFFame() throws IOException {
        StudentHash studentHashTable = new StudentHash(31);

        StudentRecords studentRecords = new StudentRecords();
        studentRecords.initializeHash(studentHashTable);

        studentRecords.populateHashTable(studentHashTable, "input/testInput.txt");

        List<StudentHash.StudentRecord> records = studentRecords.hallOfFame(studentHashTable, 6.0f);

        Assert.assertEquals(3, records.size());
    }

    @Test
    public void testNewCourseList() throws IOException {
        StudentHash studentHashTable = new StudentHash(31);

        StudentRecords studentRecords = new StudentRecords();
        studentRecords.initializeHash(studentHashTable);

        studentRecords.populateHashTable(studentHashTable, "input/testInput.txt");

        List<StudentHash.StudentRecord> records = studentRecords.newCourseList(studentHashTable, 6.0f, 9.0f);

        Assert.assertEquals(2, records.size());
        System.out.println(records);
    }

    @Test
    public void testDeptAvg() throws IOException {
        StudentHash studentHashTable = new StudentHash(31);

        StudentRecords studentRecords = new StudentRecords();
        studentRecords.initializeHash(studentHashTable);

        studentRecords.populateHashTable(studentHashTable, "input/testInput.txt");

        List<StudentRecords.DeptInfo> deptInfos = studentRecords.depAvg(studentHashTable);

        StudentRecords.DeptInfo cse = deptInfos.get(0);
        Assert.assertEquals(5.5633345f, cse.getMaxCgpa(), 0);
        Assert.assertEquals(5.0633345f, cse.getTotalCgpa()/cse.getStudentCount(), 0);
        Assert.assertEquals(2, cse.getStudentCount());

        StudentRecords.DeptInfo ece = deptInfos.get(1);
        Assert.assertEquals(4.55112f, ece.getMaxCgpa(), 0);
        Assert.assertEquals(3.5511198f, ece.getTotalCgpa()/ece.getStudentCount(), 0);
        Assert.assertEquals(2, ece.getStudentCount());

        StudentRecords.DeptInfo mec = deptInfos.get(2);
        Assert.assertEquals(7.4326367f, mec.getMaxCgpa(), 0);
        Assert.assertEquals(5.4326367f, mec.getTotalCgpa()/mec.getStudentCount(), 0);
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

        Assert.assertEquals(9, studentHashTable.size());

        Assert.assertNotNull( studentHashTable.get("2011CSE9995"));

        studentRecords.destroyHash(studentHashTable);

        Assert.assertEquals(0, studentHashTable.size());

        boolean exceptionHappened = false;
        try {
            Assert.assertEquals(null, studentHashTable.get("2011CSE9995"));
        }catch (NullPointerException ex) {
            Assert.assertNotNull(ex);
            exceptionHappened = true;
        }
        Assert.assertTrue(exceptionHappened);
    }

}
