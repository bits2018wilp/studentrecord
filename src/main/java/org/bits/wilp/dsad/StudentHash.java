package org.bits.wilp.dsad;

import java.util.*;

/*
    StudentHash is a basic implementation of hash table using an array of java.util.List datastructure
    it uses sequence chaining method to store elements which get hashed to same array index.

    input is studentId & CGPA. using studentId, a hashcode is generated, hashcode is passed through
    compression map to get an array index. if the list at that index is not null, it iterates through
    the elements of list comparing the studentId. if no existing record is found with given studentId,
    then, the new studentId and CGPA is inserted by creating a object of StudentRecord which is holder of
    key (studentID) and value (cgpa).
    if an existing record is found with same studentId then it is updated with the cgpa value passed.

    if the list at index is null, a new list is initialized for the array index reference and element is added
    in the form of StudentRecord which is holder of key (studentID) and value (cgpa).

 */
public class StudentHash {

    private List<StudentRecord> studentRecordTable[];
    private float totalRecords;
    private int tableSize;
    private int rehashCount;
    private float bucketUsed;

    /*
     *   initialize hash table with size passed.
     */
    public StudentHash(int tableSize) {
        this.tableSize = tableSize;
        studentRecordTable = new LinkedList[tableSize];
    }

    /**
     * @return size of hashtable in terms of total records.
     */
    public int size() {
        return Float.valueOf(totalRecords).intValue();
    }

    /**
     * @return how may times the hashtable was reHashed
     */
    public int getRehashCount() {
        return rehashCount;
    }

    /**
     * @return how many buckets have being used so far.
     */
    public float getBucketUsed() {
        return bucketUsed;
    }


    /**
     * @param studentId
     * @return corresponding StudentRecord containing studentId & CGPA if the Id exist otherwise null
     */
    public StudentRecord get(String studentId) {
        int tableIndex = getTableIndex(new StudentRecord(studentId, 0).hashCode());
        List<StudentRecord> studentRecordList = studentRecordTable[tableIndex];

        StudentRecord studentRecord = null;

        if(studentRecordList != null && !studentRecordList.isEmpty()) {
            for(StudentRecord str : studentRecordList) {
                if(str.getStudentId().equals(studentId)) {
                    studentRecord = str;
                }
            }
        }
        return studentRecord;
    }


    /**
     * @param key  studentId in  the form of YYYY<DEPTCODE>RollNumber
     * @param value  cgpa
     * @return exisiting record if record was updated. null if the  record didnt exist.
     */
    public StudentRecord put(String key, float value) {
        return put(new StudentRecord(key, value));
    }


    /**
     * @param studentRecord containing studentId & cgpa
     * @return exisiting record if record was updated. null if the  record didnt exist.
     * it also reHashes if loadFactor goes above 0.75
     */
    public StudentRecord put(StudentRecord studentRecord) {
        StudentRecord put = put(studentRecord, studentRecordTable);
        if(totalRecords/tableSize > 0.75) {
            reHash();
            rehashCount++;
        }
        return put;
    }


    /**
     * @param studentRecord containing studentId & CGPA
     * @param studentRecordTable  the array of list which is hashtable where the student record will be inserted
     * @return exisiting record if record was updated. null if the  record didnt exist.
     */
    public StudentRecord put(StudentRecord studentRecord, List<StudentRecord>[] studentRecordTable) {

        int index = getTableIndex(studentRecord.hashCode());
        List<StudentRecord> studentRecordList = null;

        studentRecordList = studentRecordTable[index];
        if(studentRecordList == null) {
            studentRecordList = new LinkedList<>();
            studentRecordTable[index] = studentRecordList;
            bucketUsed++;
        }
        for(StudentRecord sr : studentRecordList) {
            if(sr.studentId.equals(studentRecord.studentId)) {
                sr.setCgpa(studentRecord.getCgpa());
                return sr;
            }
        }

        studentRecordList.add(studentRecord);
        totalRecords++;
        return null;
    }

    /**
     * @param studentId . this is the key corresponding to which record need to eb removed.
     * @return if the correspondng record exist with studentId same as passed one then it is removed and
     * returned.  else return null.
     */
    public StudentRecord remove(String studentId) {

        int index = getTableIndex(HashId(studentId));
        List<StudentRecord> studentRecordList = null;

        studentRecordList = studentRecordTable[index];
        if(studentRecordList == null) {
            studentRecordList = new LinkedList<>();
            studentRecordTable[index] = studentRecordList;
            bucketUsed++;
        }
        StudentRecord sr = null;
        int rindex =-1;
        for( StudentRecord tmp : studentRecordList) {
            rindex++;
            if(tmp.studentId.equals(studentId)) {
                sr = tmp;
                break;
            }
        }
        if(sr != null) {
            studentRecordList.remove(rindex);
        }
        totalRecords--;
        return sr;
    }

    /**
     * @return all the keys of inserted records
     */
    public List<String> getAllKeys() {
        List<String> keys = new LinkedList<>();

        for(List<StudentRecord> set : studentRecordTable) {
            if(set != null) {
                for(StudentRecord str : set) {
                    keys.add(str.getStudentId());
                }
            }
        }
        return keys;
    }

    /**
     * @param hashCode to calculate array index where the element should be put.
     * @return  index by applying modulo function on absolute value of hashcode
     */
    private int getTableIndex(int hashCode) {
        //Compression Map
        return (Math.abs(hashCode)%tableSize);
    }


    /**
     * increase the size of hashtable by double and put the elements by rehashing the key.
     */
    private void reHash() {

        tableSize = tableSize * 2;
        totalRecords =0;
        bucketUsed = 0.0f;
        List<StudentRecord> tmp[] = new LinkedList[tableSize];

        for(List<StudentRecord> set : studentRecordTable) {
            if(set != null) {
                for(StudentRecord studentRecord : set) {
                    put(studentRecord, tmp);
                }
            }
        }
        studentRecordTable = tmp;
    }

    /**
     * destroy the hash table. all the elements are deleted.
     */
    public void deleteAll() {
        studentRecordTable = null;
        totalRecords =0;
        bucketUsed =0;
        //tableSize =0;
        rehashCount =0;
    }


    /**
     * @return an iterator on the underlying elements in hashtable
     */
    public Iterator<StudentRecord> getElementsIterator() {
        return new RecordIterator(studentRecordTable);
    }


    /**
     * @param studentId
     * @return hashcode as integer value which is calculated by aplying polynomial hashfunction.
     * only YYYY & DeptCode is used to calculate hashcode.
     */
    public int HashId(String studentId) {
        if(studentId == null || studentId.length() < 8) {
            return -1;
        }
        int yearPart = Integer.parseInt(studentId.substring(0, 4));
        String deptCode = studentId.substring(4, 7);
        int rollNumber = Integer.parseInt(studentId.substring(7,11));

        //Hash Code section
        int polynomialConst = 33;
        int hashCode = 0;

        // using the polynomial only for the department part because polynomial is done with finite bit representation
        // and the value might overflow for higher powers of polynomialConst
        for(int i =0; i < deptCode.length() -1; i++) {
            hashCode += ((int)deptCode.charAt(i) * Math.pow(polynomialConst, deptCode.length() - (i+1)));
        }
        //finally adding the year part so that the dept of subsequent years has different hashCode values
        hashCode += yearPart;
        hashCode += rollNumber;

        return hashCode;
    }

    /**
     * inner class implementing iterator for hastable elements.
     */
    private class RecordIterator implements Iterator<StudentRecord> {

        int tableIndex = 0;
        Iterator<StudentRecord> iterator;

        List<StudentRecord> studentRecordTable[];

        public RecordIterator( List<StudentRecord> studentRecordTable[] ) {
            this.studentRecordTable = studentRecordTable;
            List<StudentRecord> studentRecords = studentRecordTable[tableIndex++];
            while(studentRecords == null && tableIndex < studentRecordTable.length) {
                studentRecords = studentRecordTable[tableIndex++];
            }
            if(studentRecords != null) {
                iterator = studentRecords.iterator();
            }
        }

        @Override
        public boolean hasNext() {
            boolean hasNext = iterator.hasNext();
            if( !hasNext && tableIndex < studentRecordTable.length ) {
                List<StudentRecord> studentRecords = studentRecordTable[tableIndex++];
                while (studentRecords == null && tableIndex < studentRecordTable.length) {
                    studentRecords = studentRecordTable[tableIndex++];
                }
                if(studentRecords == null) {
                    return false;
                }
                iterator = studentRecords.iterator();
                return true;
            }
            if(hasNext) {
                return true;
            }
            else {
                return false;
            }
        }

        @Override
        public StudentRecord next() {
            return iterator.next();
        }
    }

    /**
     * this is similar to Entry Object in java.util.Map
     * StudentRecord has 2 fields.  studentId which holds key and cgpa which holds value.
     */
    public class StudentRecord {
        private String studentId;
        private float cgpa;

        public StudentRecord(String studentId, float cgpa) {
            this.studentId = studentId;
            this.cgpa = cgpa;
        }

        public void setCgpa(float cgpa) {
            this.cgpa = cgpa;
        }

        // as per assignment doc, we need a function HashId(). can we have that function to do hash calculation and hashcode() can call HashId()
        @Override
        public int hashCode() {
            return HashId(studentId);
        }

        @Override
        public boolean equals(Object inRecord) {

            if(inRecord == null || !(inRecord instanceof StudentRecord)) {
                return false;
            } else {
                StudentRecord inStudRecord = (StudentRecord) inRecord;
                if(inStudRecord.getStudentId() == null) {
                    return false;
                } else {
                    return inStudRecord.getStudentId().equals(this.studentId);
                }
            }
        }

        public String getStudentId() {
            return studentId;
        }

        public float getCgpa() {
            return cgpa;
        }

        @Override
        public String toString() {
            return "StudentRecord{" +
                    "studentId='" + studentId + '\'' +
                    ", cgpa=" + cgpa +
                    '}';
        }
    }
}
