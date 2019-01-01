package org.bits.wilp.dsad;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TestApp {

    private StudentHashTable studentHashTable;

    public static void main(String[] args) {
        TestApp testApp = new TestApp();

        testApp.initHashTable();

        Random random = new Random();

        for(int i= 1; i<= 50; i++) {
            testApp.insertStudentRec(testApp.studentHashTable,
                    "2018" + getRandomDept(random) + i,
                            getRandomCGPA(random));
        }
    }

    private void initHashTable() {
        studentHashTable = new StudentHashTable(10);
    }

    public static String getRandomDept(Random random) {
        int i = 1 + (int)(Math.random() * (3-0) + 1);

        switch (i) {
            case 1:
                return "CSE";
            case 2:
                return "ECE";
            case 3:
                return "MEC";
        }

        return "CSE";
    }

    public static float getRandomCGPA(Random random) {
        float result = random.nextFloat() * (99.9f - 0.0f) + 0.0f;
        return result;
    }

    public void insertStudentRec(StudentHashTable studentHashTable, String studentId, float value) {
        studentHashTable.put(studentId, value);
    }

    public List<StudentRecord> hallOfFame(StudentHashTable studentHashTable, float cgpa) {
        List<String> studentIds = studentHashTable.getAllKeys();

        List<StudentRecord> hallOfFame = new LinkedList<>();

        for(String id : studentIds) {
            StudentRecord studentRecord = studentHashTable.get(id);
            if( studentRecord != null && studentRecord.getCgpa() > cgpa) {
                hallOfFame.add(studentRecord);
            }
        }
        return hallOfFame;
    }

    public List<StudentRecord> newCourseList(StudentHashTable studentHashTable, float mincgpa, float maxcgpa) {
        List<String> studentIds = studentHashTable.getAllKeys();

        List<StudentRecord> newCourse = new LinkedList<>();

        for(String id : studentIds) {
            StudentRecord studentRecord = studentHashTable.get(id);
            if( studentRecord != null && studentRecord.getCgpa() > mincgpa && studentRecord.getCgpa() < maxcgpa) {
                newCourse.add(studentRecord);
            }
        }
        return newCourse;
    }

    public void depAvg(StudentHashTable studentHashTable) {
        List<String> studentIds = studentHashTable.getAllKeys();

        float csMax = 0.0f;
        float csMin = 99.9f;
        float csTotalcgpa = 0.0f;
        int csTotalStudents =0;


        for(String id : studentIds) {
            StudentRecord studentRecord = studentHashTable.get(id);
            if( studentRecord != null) {
                String dept  = getDeptCode(studentRecord.getStudentId());
                if("CSE".equals(dept)) {
                    csMax = Math.max(csMax, studentRecord.getCgpa());
                    csMin = Math.min(csMin, studentRecord.getCgpa());
                    csTotalcgpa += studentRecord.getCgpa();
                    csTotalStudents++;
                }
            }
        }

        System.out.println("Dept: CSE  TotalStudents: "+ csTotalStudents + " Max: "+csMax +" csMin: "+ csMin + " Avg: "+ (csTotalcgpa/csTotalStudents));
    }

    public String getDeptCode(String studentID) {
        return studentID.substring(4,7);
    }

}
