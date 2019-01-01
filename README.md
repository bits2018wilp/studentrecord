# studentrecord
bits wilp 2018

At a university, there is a need to store 
all 
the details of graduating students. For this exercise, let us 
consider that the CGPA of the student is stored against the student id. 
The students ID has the following format
<YYYYAAADDDD> where
YYYY 
-
represents the year in which this student joined the
university
AAA 
-
a three letter (alphabet) representing degree program
DDDD
-
a four digit number representing the students roll number
For  instance,  an  ID  can  be  of  the  form  200
8
CSE1223  corresponding  to  a  student  joined  the 
university in the year 20
0
8
in the CSE department with the roll number 1223.
The university offers 
a 
4
year graduate degree program
in CSE (Computer Science and Eng
ineerin
g), MEC (
Mechanical
Eng
ineering
)
,  ECE
(Electronics  and  Communication  Eng
ineerin
g)
and  ARC
(Architecture)
. 
In  the 
year 
20
08
the first batch of 20 students were admitted to the university. Now in the year 2018, 200 
students were admitted
across all departments
. 
Create an input file 
input.txt
with a random list of 
students per year and their corresponding CGPA.
The university 
now 
wants to use the details of all its past students 
to 
1.
I
dentify  and  commemorate 
their  alumni  on  the  10
th
year  anniversary  of  the  University.
For 
this they will need to get a list of all students who scored over x CGPA.
2.
Extend a new cours
e offering to select
ed
students who have graduated in the past five years. 
For this they will need to get a list of all students who secured between CGPA x to CGPA y
in the past five years.
3.
Identify the maximum and average CGPA per department.

D
esign  a 
hash  table,
which  uses  student
I
d
as  the  key  to  hash  elements  into  the  hash 
table
.  Generate  necessary  hash  table  definitions  needed  and  provide  a  design  document  (2 
page) detailing clearly the design and the details of considerations while making this desi
gn. 

Design  a  hash  function 
HashId()
which  accepts
the
student
-
ID  as  a  parameter  and  returns 
the  hash  value.  You  are  only  allowed  to  use  basic  arithmetic  and  logic  operations  in 
implementing  this  hash  function.  Write  as  a  comment  to  this  function,  the  rea
sons  for  the 
specific choice of hash function.

Create  /  populate  the  hash  table  from  the  list  of  student  ID  and  the  corresponding  CGPA
given in the input file.
