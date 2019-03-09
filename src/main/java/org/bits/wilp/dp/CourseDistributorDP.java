package org.bits.wilp.dp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CourseDistributorDP {
    public static void main(String[] args) throws IOException {

        CourseDistributorDP cd = new CourseDistributorDP();

        Map<String, List<String>> choiceMap = new HashMap<>();
        Set<String> courseSet = new HashSet<>();

        BufferedReader bfr = new BufferedReader(new FileReader(new File("D:\\intellijWS\\studentrecord\\src\\main\\resources\\input\\assignment2\\student-choice")));

        String line = null;
        while ((line = bfr.readLine()) != null) {
            cd.createChoiceMap(line, choiceMap, courseSet);
        }

        List<String> assignment  =  null;
        List<List<String>> possibleAssignments = new ArrayList<>();
        cd.dp(new ArrayList<>(choiceMap.keySet()), choiceMap, assignment, possibleAssignments, courseSet.size());

        for(List<String> list : possibleAssignments) {
            System.out.println(list);
        }
        System.out.println("total possible assignments: "+ possibleAssignments.size());

    }

    public void dp(List<String> students, Map<String, List<String>> choice, List<String> assignment, List<List<String>> possibleAssignments, int size ) {

        if(assignment != null && assignment.size() == size) {
            possibleAssignments.add(assignment);
        }

        if(students == null || students.isEmpty() )
            return;

        String st = students.get(0);

        if (studentIsAssignedACourse(st, assignment)) {
            System.out.println("student is already assigned");
            return;
        }

        for (String c : choice.get(st)) {

            List<String> tmpAssignment = null;

            if(assignment == null)
                tmpAssignment =  new ArrayList<>();
            else
                tmpAssignment = new ArrayList<>(assignment);

            if ( courseIsNotAssigned(c, tmpAssignment))  {

                tmpAssignment.add(st + "#" + c);

                ArrayList<String> studentLeft = new ArrayList<>();
                studentLeft.addAll(students);
                studentLeft.remove(st);

                dp(studentLeft, choice, tmpAssignment, possibleAssignments, size);
            }
            else {
                //System.out.println("course " + c + " cannot be assigned to " + st +". discarding computed result: "+ tmpAssignment);
            }
        }
    }

    private boolean courseIsNotAssigned(String c, List<String> tmpAssignment) {

        if (tmpAssignment == null)
            return false;

        for(String ass : tmpAssignment) {
            if(ass.contains(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean studentIsAssignedACourse(String st, List<String> localAssignment) {
        if (localAssignment == null)
            return false;

        for(String ass : localAssignment) {
            if(ass.contains(st)) {
                return true;
            }
        }
        return false;
    }

    public void createChoiceMap(String choice, Map<String, List<String>> choiceMap, Set<String> courseSet) {

        if( choice == null || choice.trim().equals("")) {
            return;
        }

        String c[] = choice.split("/");
        String student = c[0];

        List<String> choices = choiceMap.get(student);
        if(choices == null) {
            choices = new ArrayList<>();
            choiceMap.put(student, choices);
        }

        for(int i=1; i<c.length; i++) {
            choices.add(c[i]);
            courseSet.add(c[i]);
        }
    }

}

