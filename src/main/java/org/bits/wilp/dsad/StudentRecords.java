package org.bits.wilp.dsad;

import java.io.*;
import java.net.URL;
import java.util.*;


/**
 * StudentRecords class uses StudentHash to store the records of studentID as key & cgpa as value.
 *
 * it has various function which university need based on the input like HallOFFame, offering new course and Dept wise Max/Avg cgpa.
 *
 * main() populates the hashTable with 36000 records spread across 4 dept & year from 2008 to 2018. *
 * it then calls functions to get HallOfFame, newCourseOffering & Dept Avg/Max cgpa . in the end HashTable is destroyed and program terminates.
 */
public class StudentRecords {

    public static final int curentYear = 2018;

    /**
     * constructor
     */
    public StudentRecords() {
    }

    /**
     * @param studentHashTable populate this hashtable from input file records
     * @param fileName read this file to get input records and insert it into hashTable in the form of studentId as Key & cgpa as value
     * @throws IOException
     */
    public void populateHashTable(StudentHash studentHashTable, String fileName) throws IOException {
        BufferedReader bfr = getInputFileReader(fileName); // get input file reader. it has about 36k lines. 9000 records per dept.

        String line = null;
        while ((line = bfr.readLine()) != null) { // read the file until EOF
            String[] split = line.split(","); // line contains studentId,cgpa. split it into 2 parts.
            if(split.length < 2) {
                System.out.println("ignoring record: "+ line + " as both studentid and cgpa should be present and separated by comma ");
                continue;
            }
            if(isValidRecord(split[0], split[1])) {
                insertStudentRec(studentHashTable, split[0], Float.valueOf(split[1]));
            } 
        }
        bfr.close();
    }
    
    /*
     * Method that validates the student id and the cgpa in the input record
     * returns true of both student id and cgpa are of correct format
     * if either of them are not of correct format then it returns false
     * student id should be of YYYYAAADDDD format where YYYY should be from 2008 to 2018
     * AAA should be one of CSC / MEC / ECE / ARC and DDDD should be a integer
     * cgpa should be a valid float number
     */
    private boolean isValidRecord(String studentId, String cgpa) {
    	//Validate if it is of length 11 - YYYYAAADDDD
    	if(studentId.length() < 11 || studentId.length() > 11) {
    		System.out.println(String.format("ignoring the studentid %s as it is not of length 11  - YYYYAAADDDD format ", studentId));
    		return false;
    	}
    	int i = 0;
    	//Validate YYYY part of student Id is all digits
    	for (; i < 4; i++) {
            if (!Character.isDigit(studentId.charAt(i))) {
            	System.out.println(String.format("ignoring the studentid %s as the year part of the id are not digits ", studentId));
                return false;
            }
        }
    	//Validate AAA part of student Id is alphabet
    	for(; i < 7; i++) {
    		if (!Character.isAlphabetic(studentId.charAt(i))) {
    			System.out.println(String.format("ignoring the studentid %s as the dept part of the id are not alphabets ", studentId));
                return false;
            }
    	}
    	
    	//Validate DDDD part of student Id is all digits
    	for(; i < 11; i++) {
    		if (!Character.isDigit(studentId.charAt(i))) {
    			System.out.println(String.format("ignoring the studentid %s as the roll number part of the id are not digits ", studentId));
                return false;
            }
    	}
    	
    	//Validate if YYYY is from 2008 to 2018
    	int year = Integer.parseInt(studentId.substring(0, 4));
    	if(year < 2008 || year > 2018) {
    		System.out.println(String.format("ignoring the studentid %s as the year is not within 2008 and 2018 ", studentId));
    		return false;
    	}
    	
    	//Validate if AAA is one of CSE, MEC, ECE, ARC
    	String dept = studentId.substring(4, 7);
    	if(!(dept.equalsIgnoreCase("CSE") 
    			|| dept.equalsIgnoreCase("MEC") 
    			|| dept.equalsIgnoreCase("ECE") 
    			|| dept.equalsIgnoreCase("ARC"))) {
    		System.out.println(String.format("ignoring the studentid %s as the dept part is not CSE / MEC / ECE / ARC ", studentId));
    		return false;
    	}
    	
    	//Validate cgpa to be a valid float
    	//variable to track if there is only one dot in the float number
    	boolean foundDot = false;
    	for (i = 0; i < cgpa.length(); i++) {
            if (!Character.isDigit(cgpa.charAt(i))) {
            	if('.' == cgpa.charAt(i) && !foundDot) {
            		foundDot = true;
            	} else {
            		System.out.println(String.format("ignoring the studentid %s as the cgpa %s is not a valid float number ", studentId, cgpa));
                    return false;
            	}
            }
        }
    	return true;
    }

    /**
     * @param studentHash
     * initialize hashTable with size of 31
     */
    public void initializeHash(StudentHash studentHash) {
    	studentHash = new StudentHash(31);
    }

    /**
     * @param studentHashTable
     * @param studentId
     * @param value
     *
     * inserts studentId as Key & cgpa as value in hashTable
     */
    public void insertStudentRec(StudentHash studentHashTable, String studentId, float value) {
        StudentHash.StudentRecord put = studentHashTable.put(studentId, value);
        if( put != null) {
            System.out.println("existing record with Id: "+ studentId +" updated with cgpa: "+ value);
        }
    }

    /**
     * @param studentHashTable
     * @param cgpa
     * @return
     * @throws FileNotFoundException
     *
     * get list of students for Hall of Fame who has secured above the cgpa passed in function.
     * write the list in output file.
     */
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

    /**
     * @param studentHashTable
     * @param mincgpa
     * @param maxcgpa
     * @return
     * @throws FileNotFoundException
     *
     * offer new courses to student who has secured between min & Max cgpa in last 5 years of passout.
     * output is saved in file.
     */
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

    /**
     * @param studentHashTable
     * @return
     * @throws FileNotFoundException
     *
     * calculate dept wise avg & max cgpa across all students and save theputput in file.
     */
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


    /**
     * @param studentHash
     * destroys the hashTable. after this any attempt to access hashTable will result in NullPointerException
     */
    public  void destroyHash(StudentHash studentHash) {
        studentHash.deleteAll();
    }

    /**
     * @param studentID
     * @return dept code from studentId
     */
    public String getDeptCode(String studentID) {
        return studentID.substring(4, 7);
    }

    /**
     * @param studentId
     * @return addmission year from studentId
     */
    private int getAdmissionYear(String studentId) {
        return Integer.valueOf(studentId.substring(0, 4));
    }

    /**
     * @param fileName
     * @return  file reader on the input file. used for population of hashtable at the start of program.
     * @throws FileNotFoundException
     */
    private static BufferedReader getInputFileReader(String fileName) throws FileNotFoundException {
        URL resource = ClassLoader.getSystemClassLoader().getResource(fileName);
        File file = new File(resource.getPath());
        BufferedReader bfr = new BufferedReader(new FileReader(file));
        return bfr;
    }

    /**
     * generates random input for testing.
     * @throws FileNotFoundException
     */
    private static void generateInput() throws FileNotFoundException {

        String inputFile = System.getProperty("java.io.tmpdir")+ "input.txt";
        System.out.println("input will be genearted at: " + inputFile);
        Random random = new Random();
        PrintWriter pw = new PrintWriter(new File(inputFile));
        for (int i = 1; i <= 4; i++) {
            int r = 1000;
            for (int j = 1000; j <= 9999; j++) {
                String line = getRandomYear() + getRandomDept(i) + r++ + "," + getRandomCGPA(random);
                pw.println(line);
            }
        }
        pw.close();
    }

    /**
     * @param i
     * @return  one of the dept like CSE, MEC, ECE or ARC
     */
    public static String getRandomDept(int i) {

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

    /**
     * @param random
     * @return  cgpa in the range of o to 9.9
     */
    public static float getRandomCGPA(Random random) {
        float result = random.nextFloat() * (10 - 1);
        return result;
    }

    /**
     * @return random year between 2008 & 2018
     */
    private static int getRandomYear() {
        int i = 2007 + (int) (Math.random() * (2019 - 2007) + 1);
        return i;
    }


    /**
     * inner class to hold dept wise total cgpa, total students and max cgpa.
     */
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

    /**
     * main function to run the program.
     * it does following :
     *      creates/initialize the hashTable
     *      populate with records.
     *      generates hallOFFame studentlist
     *      generates list of student to offer new course
     *      calculate dept avg & max cgpa
     *      destroys the hashtable.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        //generateInput(); // utiltity to generate random input file

        StudentHash studentHashTable = new StudentHash(31);

        StudentRecords studentRecords = new StudentRecords();
        studentRecords.initializeHash(studentHashTable); // initialize StudentHash with size 31 buckets

        studentRecords.populateHashTable(studentHashTable, "input/input.txt");

        studentRecords.hallOfFame(studentHashTable, 6.0f);
        studentRecords.newCourseList(studentHashTable, 6.0f, 9.9f);
        studentRecords.depAvg(studentHashTable);

        studentRecords.destroyHash(studentHashTable);
    }

}
