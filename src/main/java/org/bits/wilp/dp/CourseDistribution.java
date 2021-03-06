package org.bits.wilp.dp;

import java.io.*;
import java.sql.SQLOutput;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CourseDistribution {

    public static void main(String[] args) throws IOException {
        List<String> students = new ArrayList<>();

        students.add("sriraman");
        students.add("kasif");
        students.add("pp");
        //students.add("pk");
        //students.add("mk");
     //   students.add("umang");

/*
        students.add("krishna");
        students.add("sachin");
        students.add("kannan");
        students.add("rengith");
        students.add("abhinav");
*/

        List<String> courses = new ArrayList<>();
        courses.add("nlp");
        courses.add("ai");
        courses.add("data mining");
        //courses.add("big-data");
       // courses.add("spatial data analysis");
       // courses.add("image processing");

/*
        courses.add("machine learning");
        courses.add("graph mining");
        courses.add("e commerce");
        courses.add("wireless mobile comm");
        courses.add("cloud computing");
*/

        Map<String, List<String>> choice = new HashMap<>();

        List<String> choice1 = new ArrayList<>();
        choice1.add("nlp"); choice1.add("ai"); choice1.add("data mining");
        //choice1.add("data mining"); choice1.add("ai"); choice1.add("image processing");
        choice.put("sriraman", choice1);

        List<String> choice2 = new ArrayList<>();
        choice2.add("ai"); choice2.add("data mining"); choice2.add("nlp");
        //choice2.add("big-data"); choice2.add("image processing");
        choice.put("kasif", choice2);

        List<String> choice3 = new ArrayList<>();
        choice3.add("data mining"); choice3.add("nlp"); choice3.add("ai");
        //  choice3.add("big-data"); choice3.add("image processing");
        choice.put("pp", choice3);

        /*List<String> choice4 = new ArrayList<>();
        choice4.add("nlp"); choice4.add("ai");
        //choice4.add("data mining"); choice4.add("big-data");choice1.add("image processing");
        choice.put("pk", choice4);
*/
        /*List<String> choice5 = new ArrayList<>();
        //choice5.add("spatial data analysis");
        choice5.add("image processing");  choice5.add("ai");
        //choice5.add("data mining"); choice5.add("big-data"); choice5.add("nlp");
        choice.put("mk", choice5);
*/
        /*List<String> choice6 = new ArrayList<>();
        choice6.add("data mining"); choice6.add("image processing");
        choice.put("umang", choice6);*/

/*


        List<String> choice7 = new ArrayList<>();
        choice7.add("wireless mobile comm"); choice7.add("e commerce");
        choice.put("krishna", choice7);

        List<String> choice8 = new ArrayList<>();
        choice8.add("cloud computing"); choice8.add("nlp");
        choice.put("rengith", choice8);

        List<String> choice9 = new ArrayList<>();
        choice9.add("machine learning"); choice9.add("image processing");
        choice.put("abhinav", choice9);

        List<String> choice10 = new ArrayList<>();
        choice10.add("big-data"); choice10.add("cloud computing");
        choice.put("sachin", choice10);

        List<String> choice11 = new ArrayList<>();
        choice11.add("spatial data analysis"); choice11.add("wireless mobile comm");
        choice.put("kannan", choice11);
*/

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
        CourseDistribution cd = new CourseDistribution();

        Map<String, List<String>> choiceMap = new HashMap<>();
        Set<String> courseSet = new HashSet<>();

        BufferedReader bfr = new BufferedReader(new FileReader(new File("D:\\intellijWS\\studentrecord\\src\\main\\resources\\input\\assignment2\\student-choice")));

        String line = null;
        while ((line = bfr.readLine()) != null) {
            cd.createChoiceMap(line, choiceMap, courseSet);
        }
        System.out.println(choiceMap.keySet());
        System.out.println(courseSet);

        AtomicInteger counter = new AtomicInteger(0);

        List<String> assignment  =  null;
        cd.assign( new ArrayList<>(choiceMap.keySet()), new ArrayList<>(courseSet), choiceMap, assignment, counter);
        //cd.assign2(new ArrayList<>(choiceMap.keySet()), choiceMap, assignment, counter);

       /* cd.assign( students, courses, choice, assignment, counter);
        System.out.println(counter);

        System.out.println();
*/

        /*counter = new AtomicInteger(0);
        assignment = null;

        cd.assign2(students, choice, assignment, counter);
        System.out.println(counter.get());
*/
    }

    public void createChoiceMap(String choice, Map<String, List<String>> choiceMap, Set<String> courseSet) {

        if( choice == null || choice.trim().equals("")) {
            return;
        }

        System.out.println(choice);

        String c[] = choice.split("/");
        String student = c[0];

        List<String> choices = choiceMap.get(student);
        if(choices == null) {
            choices = new ArrayList<>();
            choiceMap.put(student, choices);
        }

        for(int i=1; i<c.length; i++) {
            //if( c[i] != null && c[i].trim() != "") {
                choices.add(c[i]);
                courseSet.add(c[i]);
            //}
        }
    }

    public void assign2(List<String> students, Map<String, List<String>> choice, List<String> assignment, AtomicInteger counter ) {

        if(assignment != null && assignment.size() == 3)
            System.out.println(assignment);

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
                counter.incrementAndGet();

                ArrayList<String> studentLeft = new ArrayList<>();
                studentLeft.addAll(students);
                studentLeft.remove(st);

                assign2(studentLeft, choice, tmpAssignment, counter);
            }
            else {
                //counter.incrementAndGet();
              //  System.out.println("course " + c + " cannot be assigned to " + st +". discarding computed result: "+ tmpAssignment);
            }
        }
    }

    public void assign(List<String> students, List<String> courses, Map<String, List<String>> choice, List<String> assignment, AtomicInteger counter ) {

        if(assignment != null && assignment.size() == 3)
            System.out.println(assignment);

        if(students == null || students.isEmpty() || courses == null || courses.isEmpty() )
            return;

            String st = students.get(0);
            if (studentIsAssignedACourse(st, assignment)) {
                return;
            }

            for (String c : choice.get(st)) {
                System.out.println("in");

                List<String> tmpAssignment = null;
                if(assignment == null)
                    tmpAssignment =  new ArrayList<>();
                else
                    tmpAssignment = new ArrayList<>(assignment);

                if (courses.contains(c))  { //&& courseIsNotAssigned(c, tmpAssignment))  {


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
                else {
//                    System.out.println("course " + c + " cannot be assigned to " + st +". discarding computed result: "+ tmpAssignment);
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
}

