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

    public int size() {
        return Float.valueOf(totalRecords).intValue();
    }

    public int getRehashCount() {
        return rehashCount;
    }

    public float getBucketUsed() {
        return bucketUsed;
    }

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

    public boolean put(String key, float value) {
        return put(new StudentRecord(key, value));
    }

    public boolean put(StudentRecord studentRecord) {
        boolean put = put(studentRecord, studentRecordTable);
        //TODO: should we use bucketSize instead of totalRecords ?

        //KASIF: loadfactor is n/N where n is total number of records across all buckets. we can take bucketSize but
        // KASIF: then we will need to add up all bucket size before calculating load factor. however i agree, rehashing is not helping here.
        //KASIF:  eventually we will end up creating lot of empty buckets after every rehashing.

        //given that we have hashed based on year and dept code, 
        //all the stud records of same year and dept are in the same bucket even after rehashing
        //this way we can initial table size (initHash) with lower prime number say 101 instead of 1000 
        // and segmentation fault may be avoided ?
        if(totalRecords/tableSize > 0.75) {
            reHash();
            rehashCount++;
        }
        return put;
    }

    public boolean put(StudentRecord studentRecord, List<StudentRecord>[] studentRecordTable) {
    	
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
                if(sr.getCgpa() == studentRecord.getCgpa()) {
                    return false;
                }
                else {
                    sr.setCgpa(studentRecord.getCgpa());
                    return true;
                }
            }
        }
        studentRecordList.add(studentRecord);
        totalRecords++;
        return true;
    }

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

    private int getTableIndex(int hashCode) {
    	//Compression Map
        return (Math.abs(hashCode)%tableSize);
    }

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

    public void deleteAll() {
        studentRecordTable = null;
    }

    public Iterator<StudentRecord> getElementsIterator() {
            return new RecordIterator(studentRecordTable);
    }

    public int HashId(String studentId) {
        if(studentId == null || studentId.length() < 8) {
            return -1;
        }
        int yearPart = Integer.parseInt(studentId.substring(0, 4));
        String deptCode = studentId.substring(4, 7);

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

        return hashCode;
    }

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
