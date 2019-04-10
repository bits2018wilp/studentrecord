package org.bits.wilp.dp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

class CourseAssignment {

    static int TOTAL_COURSE = 11;
    static int TOTAL_STUDENTS = 11;

    int assignments[][] ;

    int allCombinations;
    Map<Integer, List<Integer>> courseToStudentMap = new HashMap<>();
    Map<String, Integer> courseNameToIdMap = new HashMap<>();
    private int rows;
    private int cols;

    public static void main(String args[]) throws Exception
    {
        String file = "D:\\intellijWS\\studentrecord\\src\\main\\resources\\input\\assignment2\\student-choice";
        CourseAssignment courseAssignment = new CourseAssignment();
        courseAssignment.initAndReadInput(file);
        int possibleAssignments = courseAssignment.findPossibleAssignments(0, 1);
        System.out.println("Total Possible Combinations: " + possibleAssignments);
    }

    void initAndReadInput(String filePath) throws Exception
    {
        for(int i=0; i<TOTAL_COURSE+1; i++) {
            courseToStudentMap.put(i, new ArrayList<>());
        }
        rows = Double.valueOf(Math.pow(2, TOTAL_STUDENTS)).intValue()+1;
        cols = TOTAL_COURSE+1;
        assignments = new int[rows][cols];
        allCombinations = ( Double.valueOf(Math.pow(2, TOTAL_STUDENTS)).intValue()  - 1);

        initCourseNameToIdMap();
        readInput(filePath);

        // set all cell in matrix as -1
        for (int[] ass : assignments) {
            for (int i = 0; i < ass.length; i++) {
                ass[i] = -1;
            }
        }
    }

    private void initCourseNameToIdMap() {
        int id =0;
        courseNameToIdMap.put("DM",++id);
        courseNameToIdMap.put("SDA",++id);
        courseNameToIdMap.put("WMC",++id);
        courseNameToIdMap.put("CC",++id);
        courseNameToIdMap.put("NLP",++id);
        courseNameToIdMap.put("AI",++id);
        courseNameToIdMap.put("IP",++id);
        courseNameToIdMap.put("GM",++id);
        courseNameToIdMap.put("EC",++id);
        courseNameToIdMap.put("ML",++id);
        courseNameToIdMap.put("BD",++id);
    }

    private int readInput(String filePath) throws Exception{

        BufferedReader bfr = new BufferedReader(new FileReader(filePath));
        int studentCounter=-1;
        String line;
        Set<String> uniqueStudents = new HashSet<>();

        while( (line = bfr.readLine()) != null)
        {
            studentCounter++;

            String courses[] = line.split("/");

            if(uniqueStudents.contains(courses[0])) {
                throw new IllegalArgumentException("student " + courses[0] +" has already given his choice once.");
            }
            else {
                uniqueStudents.add(courses[0]);
            }

            if(courses.length <= 1)
                throw new IllegalArgumentException("each student must provide atleast 1 choice");

            for (int c = 1; c < courses.length; c++) {

                Integer courseId  = courseNameToIdMap.get(courses[c].trim());

                if(courseId == null) {
                    throw new IllegalArgumentException("illegal course: " + courses[c] +" valid courses are: "+ courseNameToIdMap.keySet());
                }

                List<Integer> students = courseToStudentMap.get(courseId);
                if(students == null) {
                    students = new ArrayList<>();
                    courseToStudentMap.put(courseId, students);
                }
                students.add(studentCounter);
            }
        }
        if(studentCounter+1 < TOTAL_STUDENTS || studentCounter+1>TOTAL_STUDENTS) {
            throw new IllegalArgumentException("not all students are given in input. students/TotalStudents  "+  (studentCounter+1) +"/"+ TOTAL_STUDENTS);
        }
        return studentCounter;
    }

    private int findPossibleAssignments(int student, int course)
    {
        //debugPrint(assignments);
        // if all students have been assigned the course then return
        if (student == allCombinations) return 1;

        // if courses have exhausted but students are still left then this is not a possible assignment.
        if (course > TOTAL_COURSE) return 0;

        // use previous solution if it exist
        if (assignments[student][course] != -1) return assignments[student][course];

        // case when the course is not icnluded in our assignment
        int assignment = findPossibleAssignments(student, course+1);

        //students having course
        int students = courseToStudentMap.get(course).size();

        // try to assign course the course to students which are not assigned.
        for (int j = 0; j < students; j++)
        {
            Integer studentCourseChoice = courseToStudentMap.get(course).get(j);
            // this means the student has already being assigned a course and cant be assinged this one
            if ( (student & (1 << studentCourseChoice)) !=0 ) continue;

            // set student mask bit and count the number of ways courses can be assigned
            else assignment += findPossibleAssignments(student | (1 << studentCourseChoice), course+1);
        }

        assignments[student][course] = assignment;
        //debugPrint(assignments);
        return assignment;
    }

    private void debugPrint(int[][] assignments) {
        System.out.println("assignments: ");

        for (int i = 0; i < rows; i++) {
            System.out.println();
            for (int j = 0; j < cols; j++) {
                System.out.print(assignments[i][j] + " ");
            }
        }
        System.out.println();
    }
}