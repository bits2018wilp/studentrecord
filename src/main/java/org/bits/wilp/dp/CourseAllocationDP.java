package org.bits.wilp.dp;

// Java program to find number of ways to wear hats

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

class CourseAllocationDP
{
    static final int MOD = 1000000007;

    // for input
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    // capList[i]'th vector contains the list of persons having a cap with id i
    // id is between 1 to 100 so we declared an array of 101 vectors as indexing
    // starts from 0.
    static List<Integer> capList[] = new ArrayList[101];

    static Map<Integer, List<Integer>> courseToPersonsMap = new HashMap<>();

    // dp[2^10][101] .. in dp[i][j], i denotes the mask i.e., it tells that
    // how many and which persons are wearing cap. j denotes the first j caps
    // used. So, dp[i][j] tells the number ways we assign j caps to mask i
    // such that none of them wears the same cap
    static int dp[][] = new int[1025][101];

    // This is used for base case, it has all the N bits set
    // so, it tells whether all N persons are wearing a cap.
    static int allmask;

    // Mask is the set of persons, i is cap-id (OR the
    // number of caps processed starting from first cap).
    static long countWaysUtil(int student, int course)
    {
       // printDP();
        // If all persons are wearing a cap so we
        // are done and this is one way so return 1
        if (student == allmask) return 1;

        // If not everyone is wearing a cap and also there are no more
        // caps left to process, so there is no way, thus return 0;
        if (course > 100) return 0;

        // If we already have solved this subproblem, return the answer.
        if (dp[student][course] != -1){
          //  System.out.println("found");
            return dp[student][course];
        }

        // Ways, when we don't include this cap in our arrangement
        // or solution set.
        long ways = countWaysUtil(student, course+1);

        // size is the total number of persons having cap with id i.
        //int size = capList[i].size();
        int size = courseToPersonsMap.get(course).size();

        // So, assign one by one ith cap to all the possible persons
        // and recur for remaining caps.
        for (int j = 0; j < size; j++)
        {
            // if person capList[i][j] is already wearing a cap so continue as
            // we cannot assign him this cap
            //if ( (mask & (1 << capList[i].get(j))) != 0 ) continue;
            if ( student  == ( 1 << courseToPersonsMap.get(course).get(j)) )  { // same person..just ignore
                System.out.println("mask: "+student);
                continue;
            }
                // Else assign him this cap and recur for remaining caps with
                // new updated mask vector
            else {
                int nextStudent =  student | (1 << courseToPersonsMap.get(course).get(j));
               // int newMask =  courseToPersonsMap.get(i).get(j);
                System.out.println("nextStudent: "+ nextStudent);
                ways += countWaysUtil( nextStudent, course+1);
            }
           // ways %= MOD;
        }

        // Save the result and return it.
        return dp[student][course] = (int) ways;
    }

    private static void printDP() {

        for(int i=0;i<4;i++) {
            System.out.println();
            for(int j=0;j<9;j++) {
                System.out.print(dp[i][j] +" ");
            }
        }

        System.out.println();
        System.out.println();
    }

    // Reads n lines from standard input for current test case
    static void countWays(int n) throws Exception
    {
        //----------- READ INPUT --------------------------
        String str;
        String split[];
        int x;

        for (int i=0; i<n; i++)
        {
            str = br.readLine();
            split = str.split(" ");

            // while there are words in the split[]
            for (int j = 0; j < split.length; j++) {
                // add the ith person in the list of cap if with id x
                x = Integer.parseInt(split[j]);
//                capList[x].add(i);
                courseToPersonsMap.get(x).add(i);
            }

        }
        //----------------------------------------------------

        // All mask is used to check of all persons
        // are included or not, set all n bits as 1
        allmask = Double.valueOf(Math.pow(2, n)).intValue()-1; //1 << n) - 1;

        // Initialize all entries in dp as -1
        for (int[] is : dp) {
            for (int i = 0; i < is.length; i++) {
                is[i] = -1;
            }
        }

        // Call recursive function count ways
        System.out.println(countWaysUtil(0, 1));
    }

    // Driver method
    public static void main(String args[]) throws Exception
    {
        int n; // number of persons in every test case

        // initializing vector array
        for (int i = 0; i < capList.length; i++) {
            //capList[i] = new ArrayList<>();
            courseToPersonsMap.put(i, new ArrayList<>());
        }


        n = Integer.parseInt(br.readLine());
        countWays(n);
    }
}
