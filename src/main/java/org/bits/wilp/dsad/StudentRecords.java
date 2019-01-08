package org.bits.wilp.dsad;

import java.io.*;
import java.net.URL;
import java.util.*;

public class StudentRecords {

    public static final int curentYear = 2018;

    public StudentRecords() {

    }

    public void populateHashTable(StudentHash studentHashTable, String fileName) throws IOException {
        BufferedReader bfr = getInputFileReader(fileName); // get input file reader. it has about 36k lines. 9000 records per dept.

        String line = null;
        while ((line = bfr.readLine()) != null) { // read the file until EOF
            String[] split = line.split(","); // line contains studentId,cgpa. split it into 2 parts.
            if(split.length < 2) {
                System.out.println("ignoring record: "+ line);
            }
            insertStudentRec(studentHashTable, split[0], Float.valueOf(split[1]));
        }
        bfr.close();
    }

    public void initializeHash(StudentHash studentHash) {
    	//TODO: Shouldnt this N be a prime number to avoid collision ?
        //KASIF: yeah we can start with that to minimize collission.
        studentHash = new StudentHash(31);
    }

    public void insertStudentRec(StudentHash studentHashTable, String studentId, float value) {
        StudentHash.StudentRecord put = studentHashTable.put(studentId, value);
        if( put != null) {
            System.out.println("existing record with Id: "+ studentId +" updated with cgpa: "+ value);
        }
    }

    public List<StudentHash.StudentRecord> hallOfFame(StudentHash studentHashTable, float cgpa) throws FileNotFoundException {

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
        return hallOfFame;
    }

    public List<StudentHash.StudentRecord> newCourseList(StudentHash studentHashTable, float mincgpa, float maxcgpa) throws FileNotFoundException {

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
        return newCourse;
    }

    public List<DeptInfo> depAvg(StudentHash studentHashTable) throws FileNotFoundException {

        String departmentAverageFile = System.getProperty("java.io.tmpdir")+ "departmentAverage.txt";
        PrintWriter pw = new PrintWriter(new File(departmentAverageFile));

        DeptInfo cse = new DeptInfo("CSE");
        DeptInfo ece = new DeptInfo("ECE");
        DeptInfo mec = new DeptInfo("MEC");
        DeptInfo arc = new DeptInfo("ARC");

        Iterator<StudentHash.StudentRecord> elementsIterator = studentHashTable.getElementsIterator();

        while (elementsIterator.hasNext()) {
            StudentHash.StudentRecord studentRecord = elementsIterator.next();
            if (studentRecord != null) {
                String dept = getDeptCode(studentRecord.getStudentId());
                switch (dept) {
                    case "CSE" :
                        cse.setMaxCgpa(studentRecord.getCgpa());
                        cse.addToTotalCgpa(studentRecord.getCgpa());
                        cse.addToStudentCount(1);
                        break;
                    case "ECE" :
                        ece.setMaxCgpa(studentRecord.getCgpa());
                        ece.addToTotalCgpa(studentRecord.getCgpa());
                        ece.addToStudentCount(1);
                        break;
                    case "MEC" :
                        mec.setMaxCgpa(studentRecord.getCgpa());
                        mec.addToTotalCgpa(studentRecord.getCgpa());
                        mec.addToStudentCount(1);
                        break;
                    case "ARC" :
                        arc.setMaxCgpa(studentRecord.getCgpa());
                        arc.addToTotalCgpa(studentRecord.getCgpa());
                        arc.addToStudentCount(1);
                        break;
                }
            }
        }

        System.out.println("Dept: CSE  TotalStudents: " + cse.getStudentCount() + " Max: " + cse.getMaxCgpa() + " Avg: " + (cse.getTotalCgpa() / cse.getStudentCount()));
        System.out.println("Dept: ECE  TotalStudents: " + ece.getStudentCount() + " Max: " + ece.getMaxCgpa() + " Avg: " + (ece.getTotalCgpa() / ece.getStudentCount()));
        System.out.println("Dept: MEC  TotalStudents: " + mec.getStudentCount() + " Max: " + mec.getMaxCgpa() + " Avg: " + (mec.getTotalCgpa() / mec.getStudentCount()));
        System.out.println("Dept: ARC  TotalStudents: " + arc.getStudentCount() + " Max: " + arc.getMaxCgpa() + " Avg: " + (arc.getTotalCgpa() / arc.getStudentCount()));


        pw.println("CSE," + cse.getMaxCgpa() + "," + (cse.getTotalCgpa() / cse.getStudentCount()));
        pw.println("ECE," + ece.getMaxCgpa() + "," + (ece.getTotalCgpa() / ece.getStudentCount()));
        pw.println("MEC," + mec.getMaxCgpa() + "," + (mec.getTotalCgpa() / mec.getStudentCount()));
        pw.println("ARC," + arc.getMaxCgpa() + "," + (arc.getTotalCgpa() / arc.getStudentCount()));
        pw.close();

        System.out.println("Dept wise Max/Avg Output is available at: "+ departmentAverageFile);
        List<DeptInfo> deptInfos = new ArrayList<>();
        deptInfos.add(cse); deptInfos.add(ece); deptInfos.add(mec); deptInfos.add(arc);
        return deptInfos;
    }

    public String getDeptCode(String studentID) {
        return studentID.substring(4, 7);
    }

    private int getAdmissionYear(String studentId) {
        return Integer.valueOf(studentId.substring(0, 4));
    }
    
    private static BufferedReader getInputFileReader(String fileName) throws FileNotFoundException {
        URL resource = ClassLoader.getSystemClassLoader().getResource(fileName);
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


    public static void main(String[] args) throws IOException {

        //generateInput(); // utiltity to generate random input file

        StudentHash studentHashTable = new StudentHash(31);

        StudentRecords studentRecords = new StudentRecords();
        studentRecords.initializeHash(studentHashTable); // initialize StudentHash with size 1000 buckets

        studentRecords.populateHashTable(studentHashTable, "input/Input.txt");

        studentRecords.hallOfFame(studentHashTable, 6.0f);
        studentRecords.newCourseList(studentHashTable, 6.0f, 9.9f);
        studentRecords.depAvg(studentHashTable);

        studentRecords.destroyHash(studentHashTable);
    }

    public class DeptInfo {
        private String deptName;
        private float totalCgpa;
        private float maxCgpa = 0.0f;
        private int studentCount;

        public DeptInfo(String deptName) {
            this.deptName = deptName;
        }

        public float getTotalCgpa() {
            return totalCgpa;
        }

        public void addToTotalCgpa(float totalCgpa) {
            this.totalCgpa += totalCgpa;
        }

        public float getMaxCgpa() {
            return maxCgpa;
        }

        public void setMaxCgpa(float maxCgpa) {
            this.maxCgpa = Math.max( this.maxCgpa, maxCgpa);
        }

        public int getStudentCount() {
            return studentCount;
        }

        public void addToStudentCount(int studentCount) {
            this.studentCount += studentCount;
        }
    }
}
