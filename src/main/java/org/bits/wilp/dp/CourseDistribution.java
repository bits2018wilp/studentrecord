package org.bits.wilp.dp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CourseDistribution {

    public static void main(String[] args) {
        List<String> students = new ArrayList<>();

        students.add("sriraman");
        students.add("kasif");
        students.add("pp");
        students.add("pk");

        /*students.add("umang");
        students.add("mk");
        students.add("krishna");
        students.add("sachin");
        students.add("kannan");
        students.add("rangith");
        students.add("abhinav");
*/
        List<String> courses = new ArrayList<>();
        courses.add("nlp");
        courses.add("ai");
        courses.add("data mining");
        courses.add("big-data");

        Map<String, List<String>> choice = new HashMap<>();

        List<String> choice1 = new ArrayList<>();
        choice1.add("nlp"); choice1.add("big-data");
        choice.put("sriraman", choice1);

        List<String> choice2 = new ArrayList<>();
        choice2.add("ai"); choice2.add("data mining"); choice2.add("nlp");
        choice.put("kasif", choice2);

        List<String> choice3 = new ArrayList<>();
        choice3.add("data mining"); choice3.add("nlp");
        choice.put("pp", choice3);

        List<String> choice4 = new ArrayList<>();
        choice4.add("nlp"); choice4.add("ai");
        choice.put("pk", choice4);

/*
        List<String> choice1 = new ArrayList<>();
        choice1.add("nlp"); choice1.add("big-data"); choice1.add("data mining"); choice1.add("ai");
        choice.put("sriraman", choice1);

        List<String> choice2 = new ArrayList<>();
        choice2.add("nlp"); choice2.add("big-data"); choice2.add("data mining");
        choice.put("kasif", choice2);

        List<String> choice3 = new ArrayList<>();
        choice3.add("big-data"); choice3.add("data mining");
        choice.put("pp", choice3);

        List<String> choice4 = new ArrayList<>();
        choice4.add("nlp");
        choice.put("pk", choice4);

*/

        AtomicInteger counter = new AtomicInteger(0);

        CourseDistribution cd = new CourseDistribution();

        List<String> assignment  =  null;//new ArrayList<>();

        cd.assign(students, courses, choice, assignment, counter);

        System.out.println(counter.get());

    }

    public void assign(List<String> students, List<String> courses, Map<String, List<String>> choice, List<String> assignment, AtomicInteger counter ) {

        if(assignment != null && assignment.size() == 4)
            System.out.println(assignment);

        if(students == null || students.isEmpty() || courses == null || courses.isEmpty() )
            return;

        for(String st : students) {

           if (studentIsAssignedACourse(st, assignment)) {
               continue;
           }

            for (String c : choice.get(st)) {

                List<String> tmpAssignment = null;
                if(assignment == null)
                    tmpAssignment =  new ArrayList<>();
                else
                    tmpAssignment = new ArrayList<>(assignment);

                if (courses.contains(c) )  {

                    tmpAssignment.add(st + "#" + c);
                    counter.incrementAndGet();

                    ArrayList<String> studentLeft = new ArrayList<>();
                    studentLeft.addAll(students);
                    studentLeft.remove(st);

                    ArrayList<String> courseLeft = new ArrayList<>();
                    courseLeft.addAll(courses);
                    courseLeft.remove(c);

                    assign(studentLeft, courseLeft, choice, tmpAssignment, counter);
                }
            }
        }
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
}

