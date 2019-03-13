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

    public static void main(String args[]) throws Exception
    {
        CourseAssignment courseAssignment = new CourseAssignment();
        courseAssignment.initAndReadInput();
    }

    void initAndReadInput() throws Exception
    {
        for(int i=0;i<TOTAL_COURSE+1;i++) {
            courseToStudentMap.put(i, new ArrayList<>());
        }
        assignments = new int[Double.valueOf(Math.pow(2, TOTAL_STUDENTS)).intValue()+1][TOTAL_COURSE+1];
        allCombinations = ( Double.valueOf(Math.pow(2, TOTAL_STUDENTS)).intValue()  - 1);

        initCourseNameToIdMap();
        readInput();

        // set all cell in matrix as -1
        for (int[] ass : assignments) {
            for (int i = 0; i < ass.length; i++) {
                ass[i] = -1;
            }
        }

        System.out.println("Total Possible Combinations: " + findPossibleAssignments(0, 1));
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

    private int readInput() throws Exception{
        BufferedReader bfr = new BufferedReader(new FileReader("D:\\intellijWS\\studentrecord\\src\\main\\resources\\input\\assignment2\\student-choice2"));
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
        return studentCounter;
    }

    private int findPossibleAssignments(int student, int course)
    {
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
            // this means the student has already being assigned this course
            if ((student == (1 << courseToStudentMap.get(course).get(j))) ) continue;

            // try assigning course to student.
            else assignment += findPossibleAssignments(student | (1 << courseToStudentMap.get(course).get(j)), course+1);
        }
        return assignments[student][course] = assignment;
    }
}