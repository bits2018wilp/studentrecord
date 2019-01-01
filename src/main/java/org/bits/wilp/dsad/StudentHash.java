package org.bits.wilp.dsad;

import java.util.*;

public class StudentHash {

    private List<StudentRecord> studentRecordTable[];
    private float totalRecords;
    private int tableSize;
    private int rehashCount;
    private float bucketUsed;

    public StudentHash(int tableSize) {
        this.tableSize = tableSize;
        studentRecordTable = new LinkedList[tableSize];
    }

    public StudentRecord get(String studentId) {
        int tableIndex = getTableIndex(HashId(studentId));
        List<StudentRecord> studentRecordList = studentRecordTable[tableIndex];

        StudentRecord studentRecord = null;

        if(studentRecordList != null && !studentRecordList.isEmpty()) {
            for(StudentRecord str : studentRecordList) {
                if(str.getStudentId().equals(studentId)) {
                    studentRecord = str;
                }
            }
        }
        if(studentRecord == null) {
            throw  new NoSuchElementException("record not found for "+ studentId);
        }
        return studentRecord;
    }

    public void put(String key, float value) {
        put(new StudentRecord(key, value));
    }

    public void put(StudentRecord studentRecord) {
        put(studentRecord, studentRecordTable);
      //  System.out.println("TotalRecords: "+ totalRecords + " LoadFactor: "+ (totalRecords/tableSize) +" wasteFactor: "+ ( (tableSize-bucketUsed)/tableSize ));
        if(totalRecords/tableSize > 0.75) {
            reHash();
        }
    }

    public void put(StudentRecord studentRecord, List<StudentRecord>[] studentRecordTable) {

       // System.out.println("inserting: "+ studentRecord);
        int index = getTableIndex(studentRecord.hashCode());
        List<StudentRecord> studentRecordList = null;

        studentRecordList = studentRecordTable[index];
        if(studentRecordList == null) {
            studentRecordList = new LinkedList<>();
            studentRecordTable[index] = studentRecordList;
           // System.out.println("bucket "+ index +" initialized..");
            bucketUsed++;
        }
        studentRecordList.add(studentRecord);
        totalRecords++;
    }

    public List<String> getAllKeys() {
        List<String> keys = new LinkedList<>();

        for(List<StudentRecord> list : studentRecordTable) {
            if(list != null) {
                for(StudentRecord str : list) {
                    keys.add(str.getStudentId());
                }
            }
        }
        return keys;
    }

    private int getTableIndex(int hashCode) {
        return (Math.abs(hashCode)%tableSize);
    }

    private void reHash() {

        tableSize = tableSize * 2;
        totalRecords =0;
        bucketUsed = 0.0f;
        List<StudentRecord> tmp[] = new LinkedList[tableSize];

       // System.out.println("rehashing " + (rehashCount++) + " with Size=  " + studentRecordTable.length);

        for(List<StudentRecord> list : studentRecordTable) {
            if(list != null) {
                for(StudentRecord studentRecord : list) {
                    put(studentRecord, tmp);
                }
            }
        }
        studentRecordTable = tmp;
    }

    public void deleteAll() {
        studentRecordTable = null;
    }


    public int HashId(String studentId) {

        return Objects.hash(studentId); // hashing only on year & dept code
    }

    public Iterator<StudentRecord> getElementsIterator() {
            return new RecordIterator(studentRecordTable);
    }

    private class RecordIterator implements Iterator<StudentRecord> {

        int tableIndex = 0;
        int currentListIndex=0;

        List<StudentRecord> currentList = null;
        List<StudentRecord> studentRecordTable[];

        public RecordIterator( List<StudentRecord> studentRecordTable[] ) {
            this.studentRecordTable = studentRecordTable;
        }

        @Override
        public boolean hasNext() {

            if(tableIndex < studentRecordTable.length) {

                if( currentList !=  null && currentListIndex < currentList.size()) {
                    return true;
                }

                currentListIndex = 0;
                while( (currentList = studentRecordTable[tableIndex++]) == null && tableIndex < tableSize ) {
                    //keep iterating untill it find non null list or exhaust the table
                }
                return currentList == null? false : true;
            }
            else
                return false;
        }

        @Override
        public StudentRecord next() {
             return currentList.get(currentListIndex++);
        }
    }

    public class StudentRecord {
        private String studentId;
        private float cgpa;

        public StudentRecord(String studentId, float cgpa) {
            this.studentId = studentId;
            this.cgpa = cgpa;
        }

        @Override
        public int hashCode() {
            return HashId(studentId);
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
