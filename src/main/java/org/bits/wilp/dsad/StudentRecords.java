package org.bits.wilp.dsad;

import java.io.*;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class StudentRecords {

    private StudentHash studentHashTable;
    public static final int curentYear = 2018;

    public static void main(String[] args) throws IOException {

        //generateInput(); // utiltity to generate random input file

        StudentRecords studentRecords = new StudentRecords();
        studentRecords.initHashTable(); // initialize StudentHash with size 1000 buckets

        BufferedReader bfr = getInputFileReader(); // get input file reader. it has about 36k lines. 9000 records per dept.

        String line = null;
        while ((line = bfr.readLine()) != null) { // read the file until EOF
            String[] split = line.split(","); // line contains studentId,cgpa. split it into 2 parts.
            studentRecords.insertStudentRec(studentRecords.studentHashTable, split[0], Float.valueOf(split[1]));
        }
        bfr.close();

        studentRecords.hallOfFame(studentRecords.studentHashTable, 6.0f);
        studentRecords.newCourseList(studentRecords.studentHashTable, 6.0f, 9.9f);

        studentRecords.getDeptWiseMaxAvg(studentRecords.studentHashTable);

        studentRecords.destroyHash(studentRecords.studentHashTable);

    }

    private void initHashTable() {
    	//TODO: Shouldnt this N be a prime number to avoid collision ?
        //KASIF: yeah we can start with that to minimize collission.
        studentHashTable = new StudentHash(31);
    }


    public void insertStudentRec(StudentHash studentHashTable, String studentId, float value) {
        studentHashTable.put(studentId, value);
    }

    public void hallOfFame(StudentHash studentHashTable, float cgpa) throws FileNotFoundException {

        String halloffameFile = System.getProperty("java.io.tmpdir")+ "halloffame.txt";

        PrintWriter pw = new PrintWriter(new File(halloffameFile));

        List<StudentHash.StudentRecord> hallOfFame = new LinkedList<>();
        Iterator<StudentHash.StudentRecord> elementsIterator = studentHashTable.getElementsIterator();
        while (elementsIterator.hasNext()) {
            StudentHash.StudentRecord studentRecord = elementsIterator.next();
            if (studentRecord != null && studentRecord.getCgpa() > cgpa) {
                pw.println(studentRecord.getStudentId()+","+studentRecord.getCgpa());
                hallOfFame.add(studentRecord);
            }
        }
        pw.close();
        System.out.println("hall of Fame has " + hallOfFame.size() + " records. Output is available at: "+ halloffameFile);
    }

    public void newCourseList(StudentHash studentHashTable, float mincgpa, float maxcgpa) throws FileNotFoundException {

        List<StudentHash.StudentRecord> newCourse = new LinkedList<>();
        Iterator<StudentHash.StudentRecord> elementsIterator = studentHashTable.getElementsIterator();

        String courseOfferFile = System.getProperty("java.io.tmpdir")+ "courseOffer.txt";
        PrintWriter pw = new PrintWriter(new File(courseOfferFile));

        while (elementsIterator.hasNext()) {
            StudentHash.StudentRecord studentRecord = elementsIterator.next();
            if (studentRecord != null &&
                    curentYear - getAdmissionYear(studentRecord.getStudentId()) >= 5 &&
                    studentRecord.getCgpa() > mincgpa && studentRecord.getCgpa() < maxcgpa) {
                pw.println(studentRecord.getStudentId()+","+studentRecord.getCgpa());
                newCourse.add(studentRecord);
            }
        }
        pw.close();
        System.out.println("new course offering has " + newCourse.size() + " records. Output is available at: "+ courseOfferFile);
    }

    public void getDeptWiseMaxAvg(StudentHash studentHashTable) throws FileNotFoundException {

        String departmentAverageFile = System.getProperty("java.io.tmpdir")+ "departmentAverage.txt";
        PrintWriter pw = new PrintWriter(new File(departmentAverageFile));

        float csMax = 0.0f;
        float csTotalcgpa = 0.0f;
        int csTotalStudents = 0;

        float eceMax = 0.0f;
        float eceTotalcgpa = 0.0f;
        int eceTotalStudents = 0;

        float mecMax = 0.0f;
        float mecTotalcgpa = 0.0f;
        int mecTotalStudents = 0;

        float arcMax = 0.0f;
        float arcTotalcgpa = 0.0f;
        int arcTotalStudents = 0;

        Iterator<StudentHash.StudentRecord> elementsIterator = studentHashTable.getElementsIterator();

        while (elementsIterator.hasNext()) {
            StudentHash.StudentRecord studentRecord = elementsIterator.next();
            if (studentRecord != null) {
                String dept = getDeptCode(studentRecord.getStudentId());
                switch (dept) {
                    case "CSE" :
                        csMax = Math.max(csMax, studentRecord.getCgpa());
                        csTotalcgpa += studentRecord.getCgpa();
                        csTotalStudents++;
                        break;
                    case "ECE" :
                        eceMax = Math.max(eceMax, studentRecord.getCgpa());
                        eceTotalcgpa += studentRecord.getCgpa();
                        eceTotalStudents++;
                        break;
                    case "MEC" :
                        mecMax = Math.max(mecMax, studentRecord.getCgpa());
                        mecTotalcgpa += studentRecord.getCgpa();
                        mecTotalStudents++;
                        break;
                    case "ARC" :
                        arcMax = Math.max(arcMax, studentRecord.getCgpa());
                        arcTotalcgpa += studentRecord.getCgpa();
                        arcTotalStudents++;
                        break;
                }
            }
        }
        System.out.println("Dept: CSE  TotalStudents: " + csTotalStudents + " Max: " + csMax + " Avg: " + (csTotalcgpa / csTotalStudents));
        System.out.println("Dept: ECE  TotalStudents: " + eceTotalStudents + " Max: " + eceMax + " Avg: " + (eceTotalcgpa / eceTotalStudents));
        System.out.println("Dept: MEC  TotalStudents: " + mecTotalStudents + " Max: " + mecMax + " Avg: " + (mecTotalcgpa / mecTotalStudents));
        System.out.println("Dept: ARC  TotalStudents: " + arcTotalStudents + " Max: " + arcMax + " Avg: " + (arcTotalcgpa / arcTotalStudents));

        pw.println("CSE," + csMax + "," + (csTotalcgpa / csTotalStudents));
        pw.println("ECE," + eceMax + "," + (eceTotalcgpa / eceTotalStudents));
        pw.println("MEC," + mecMax + "," + (mecTotalcgpa / mecTotalStudents));
        pw.println("ARC," + arcMax + "," + (arcTotalcgpa / arcTotalStudents));
        pw.close();

        System.out.println("Dept wise Max/Avg Output is available at: "+ departmentAverageFile);
    }

    public String getDeptCode(String studentID) {
        return studentID.substring(4, 7);
    }

    private int getAdmissionYear(String studentId) {
        return Integer.valueOf(studentId.substring(0, 4));
    }
    
    private static BufferedReader getInputFileReader() throws FileNotFoundException {
        URL resource = ClassLoader.getSystemClassLoader().getResource("input/Input.txt");
        File file = new File(resource.getPath());
        BufferedReader bfr = new BufferedReader(new FileReader(file));
        return bfr;
    }

    private static void generateInput() throws FileNotFoundException {

        Random random = new Random();
        PrintWriter pw = new PrintWriter(new File("D:/Input.txt"));
        for (int i = 1; i <= 4; i++) {
            int r = 1000;
            for (int j = 1000; j <= 9999; j++) {
                String line = getRandomYear() + getRandomDept(i) + r++ + "," + getRandomCGPA(random);
                pw.println(line);
            }
        }
        pw.close();
    }

    public  void destroyHash(StudentHash studentHash) {
        studentHash.deleteAll();
    }

    public static String getRandomDept(int i) {
        //   int i = 1 + (int)(Math.random() * (4-0) + 1);

        switch (i) {
            case 1:
                return "CSE";
            case 2:
                return "ECE";
            case 3:
                return "MEC";
            case 4:
                return "ARC";
        }
        return "CSE";
    }

    public static float getRandomCGPA(Random random) {
        float result = random.nextFloat() * (10 - 1);
        return result;
    }

    private static int getRandomYear() {
        int i = 2000 + (int) (Math.random() * (2019 - 2000) + 1);
        return i;
    }
}
