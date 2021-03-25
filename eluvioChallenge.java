import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class eluvioChallenge {
//    args is expected to be an array of filenames to examine
    public static void main(String[] args) {
        Group2<Set<String>,Integer,Integer> returned = determineStrandFromGroup(args);
        System.out.print("The files that possessed the longest byte strings are: ");
        System.out.println(returned.longest);
        System.out.print("The length of the byte string is: ");
        System.out.println(returned.length);
        System.out.print("The offset of this string is: ");
        System.out.println(returned.offset);

    }

//    Takes in a series of filenames where the binary files are stored, and returns a set of the filenames that possess longest string
//    for example String[] array = {"samples/sample.1","samples/sample.2"} etc
//    also returns offset and length of the byte string

//    NOTE: I assume that the shared String is the same offset in, ie if two files share a string but one has an offset of 10 and the other 20, then this won't find it.
//    not the most efficient solution since for an array of n files each file is examined n times
    static Group2<Set<String>,Integer,Integer> determineStrandFromGroup(String[] array) {
        Set<String> longestSamples = new HashSet<>();
        String longestWord = "";
        int longestLength = 0;
        int offset = 0;
        for (int i =0; i < array.length; i++) {
            String insideWord = "";
            Set<String> insideSet = new HashSet<>();
            int insideLength = 0;
            int insideOffset = 0;
            for (int j = i+1; j < array.length; j++) {
                Group<String,Integer,Integer> pairStrand = longestByteStrand(array[i],array[j]);
                if (pairStrand.length > insideLength) {
                    insideSet.clear();
                    insideSet.add(array[i]);
                    insideSet.add(array[j]);
                    insideLength = pairStrand.length;
                    insideOffset = pairStrand.offset;
                    insideWord = pairStrand.longest;
                } else if (pairStrand.longest.equals(insideWord)) {
                    insideSet.add(array[i]);
                    insideSet.add(array[j]);
                } else {

                }
            }
            if (insideLength > longestLength) {
                longestSamples.clear();
                Iterator<String> it = insideSet.iterator();
                while(it.hasNext()){
                    longestSamples.add(it.next());
                }
                longestLength = insideLength;
                offset = insideOffset;
                longestWord = insideWord;
            } else if (longestWord.equals(insideWord)) {
                Iterator<String> it = insideSet.iterator();
                while(it.hasNext()){
                    longestSamples.add(it.next());
                }
            } else {

            }
        }
        return new Group2(longestSamples,offset,longestLength);
    }

//    does a pairwise comparison of two files to find longest byte string, offset, and length
    static Group<String, Integer, Integer> longestByteStrand(String file1, String file2) {
        try {
            // create a reader
            FileInputStream reader1 = new FileInputStream(new File(file1));
            FileInputStream reader2 = new FileInputStream(new File(file2));

            int currentBeginning = 0;
            int longestOffset = 0;
            int current = 0;
            int currentLength = 0;
            int longestLength = 0;

            String longestString = "";
            String currentString = "";

            int ch1_1;
            int ch1_2;
            int ch2_1;
            int ch2_2;
            while ((ch1_1 = reader1.read()) != -1) {
                ch1_2 = reader1.read();
                 String b1 = Integer.toBinaryString(ch1_1) + Integer.toBinaryString(ch1_2);
                 ch2_1 = reader2.read();
                 ch2_2 = reader2.read();
                 String b2 = Integer.toBinaryString(ch2_1) + Integer.toBinaryString(ch2_2);
                 current += 1;
                 if (b1.equals(b2)) {
                     currentString += b1;
                     currentLength += 1;
                 } else {
                     if (currentLength > longestLength) {
                         longestOffset = currentBeginning;
                         longestLength = currentLength;
                         longestString = currentString;
                     }
                     currentBeginning = current;
                     currentLength = 0;
                 }

            }
            if (currentLength > longestLength) {
                longestOffset = currentBeginning;
                longestLength = currentLength;
                longestString = currentString;
            }

            // close the reader
            reader1.close();
            reader2.close();
            return new Group(longestString,longestOffset,longestLength);

        } catch (IOException ex) {
            ex.printStackTrace();
        }


        return null;
    }

//  class used to returned multiple different objects
    public static class Group<X, Y, Z> {

        private String longest;
        private Integer offset;
        private Integer length;

        public Group(String first, Integer second, Integer third){
            this.longest = first;
            this.offset = second;
            this.length = third;
        }

        public String getLongest() {
            return longest;
        }

        public Integer getOffset() {
            return offset;
        }

        public Integer getLength() {
            return length;
        }

        public void setLongest(String s) {
            this.longest = s;
        }

        public void setOffset(Integer i) {
            this.offset = i;
        }

        public void setLength(Integer i) {
            this.length = i;
        }
    }
//  class used to return multiple different objects
    public static class Group2<X, Y, Z> {

        private Set<String> longest;
        private Integer offset;
        private Integer length;

        public Group2(Set<String> first, Integer second, Integer third){
            this.longest = first;
            this.offset = second;
            this.length = third;
        }

        public Set<String> getLongest() {
            return longest;
        }

        public Integer getOffset() {
            return offset;
        }

        public Integer getLength() {
            return length;
        }

        public void setLongest(Set<String> s) {
            this.longest = s;
        }

        public void setOffset(Integer i) {
            this.offset = i;
        }

        public void setLength(Integer i) {
            this.length = i;
        }
    }




    }
