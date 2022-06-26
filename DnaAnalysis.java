import edu.duke.StorageResource;
import edu.duke.URLResource;

public class DnaAnalysis {

    // Find the index of stop codon. 
    public int findStopCodon(String dna, int startIndex, String stopCodon) {
        int stopIndex = dna.indexOf(stopCodon, startIndex + 3);
        if (stopIndex == -1) {
            return dna.length();
        }
        while (stopIndex != -1) {
            if ((stopIndex - startIndex) % 3 == 0) {
                return stopIndex;
            }
            stopIndex = dna.indexOf(stopCodon, stopIndex + 1);
        }
        return dna.length();
    }

    // Test findStopCodon method.
    public void testFindStopCodon() {
        // 01234567890123456789012345
        String dna = "xxxyxyyxxTAAxyTAAxzxTGAxzx";
        int result = findStopCodon(dna, 0, "TAA");
        if (result != 9)
            System.out.println("Error on 9");
        result = findStopCodon(dna, 2, "TAA");
        if (result != 14)
            System.out.println("Error on 14");
        result = findStopCodon(dna, 2, "TGA");
        if (result != 20)
            System.out.println("Error on 20");
        result = findStopCodon(dna, 0, "TGA");
        if (result != dna.length())
            System.out.println("Error on 26");
        System.out.println("Tests finished");
    }

    // Find a relevant gene in the dna sequence, with correct start and stop codons. 
    public String findGene(String dna, int where) {
        int startIndex = dna.indexOf("ATG", where), minIndex = 0;
        if (startIndex == -1) {
            return "";
        }
        int TAAindex = findStopCodon(dna, startIndex, "TAA"),
                TGAindex = findStopCodon(dna, startIndex, "TGA"),
                TAGindex = findStopCodon(dna, startIndex, "TAG");
        if (TAAindex == dna.length() || (TGAindex != dna.length() && TGAindex < TAAindex)) {
            minIndex = TGAindex;
        } else {
            minIndex = TAAindex;
        }
        if (minIndex == dna.length() || (TAGindex != dna.length() && TAGindex < minIndex)) {
            minIndex = TAGindex;
        }
        if (minIndex == dna.length()) {
            return "";
        }
        return dna.substring(startIndex, minIndex + 3);
    }

    // Test findGene method.
    public void testFindGene() {
        String testString, result;

        // 01234567890123456789012345
        testString = "zxyzyTAAxxyzyzyTGAxzyxzTAG"; // Dna with no ATG.
        result = findGene(testString, 0);
        if (!result.isEmpty()) {
            System.out.println("Problem at first test case");
        }

        // 01234567890123456789012345
        testString = "zxzxATGxxyzyTGAxyxyzxxTAGx"; // Dna with one valid stop codon.
        result = findGene(testString, 0);
        if (!result.equals("ATGxxyzyTGAxyxyzxxTAG")) {
            System.out.println("Problem at second test case");
        }

        // 01234567890123456789012345
        testString = "zxzxATGxxyzyxTGATAAyzxTAGx"; // Dna with multiple stop codons.
        result = findGene(testString, 0);
        if (!result.equals("ATGxxyzyxTGA")) {
            System.out.println("Problem at third test case");
        }

        // 01234567890123456789012345
        testString = "zxzxATGxxxyzyxTGATAAyzxTAG"; // Dna with ATG and no valid stop codons.
        result = findGene(testString, 0);
        if (!result.isEmpty()) {
            System.out.println("Problem at fourth test case");
        }

        System.out.println("Test is finished.");
    }

    // Print all genes in a dna sequence.
    public void printAllGenes(String dna) {
        int currIndex = dna.indexOf("ATG");
        String result;
        while (true) {
            result = findGene(dna, currIndex);
            if (result.isEmpty()) {
                break;
            }
            System.out.println(result);
            currIndex = dna.indexOf(result, currIndex) + result.length();
        }
    }

    // Find all genes in a dna sequence and add them to StorageResource object.
    public StorageResource getAllGenes(String dna) {
        StorageResource sr = new StorageResource();
        int currIndex = dna.indexOf("ATG");
        String result;
        while (true) {
            result = findGene(dna, currIndex);
            if (result.isEmpty()) {
                break;
            }
            sr.add(result);
            currIndex = dna.indexOf(result, currIndex) + result.length();
        }
        return sr;
    }

    // Return the ratio of C’s and G’s in dna as a fraction of the entire strand of dna.
    public double cgRatio(String dna) {
        double length = dna.length();
        int count = 0, startIndexC = dna.indexOf("C"), startIndexG = dna.indexOf("G");
        while(true) {
            if(startIndexC != -1) {startIndexC = dna.indexOf("C", startIndexC);}
            if(startIndexG != -1) {startIndexG = dna.indexOf("G", startIndexG);}
            if (startIndexC != -1 || startIndexG != -1){
                if(startIndexC != -1){
                    count++;
                }
                if (startIndexG != -1){
                    count++;
                }
            }
            else {
                break;
            }
            if(startIndexC != -1) {startIndexC++;}
            if(startIndexG != -1) {startIndexG++;}
        }
        return count / length;
    }

    // Test cgRatio method.
    public void testCgRatio() {
        String input;
        double result;
        
        input = "ATGCCATAG"; // 4
        result = cgRatio(input);
        System.out.println(result);

        input = "CCGAACGAAGCTTAG"; // 8
        result = cgRatio(input);
        System.out.println(result);

        input = "CCGGCG"; // 6
        result = cgRatio(input);
        System.out.println(result);

        input = "ATATCGCATTAGCATTA"; // 5
        result = cgRatio(input);
        System.out.println(result);

        input = "AATATAGATATATATTTTAATAAC"; // 2
        result = cgRatio(input);
        System.out.println(result);

        input = "CGCCGTAATTAT"; // 5 
        result = cgRatio(input);
        System.out.println(result);

        input = "CGCGCCGCGC"; // 10 
        result = cgRatio(input);
        System.out.println(result);
    }

    // Return the number of "CTG" occurences in a dna string.
    public int countCTG(String dna) {
        int startIndex = 0, count = 0;
        while(true) {
            startIndex = dna.indexOf("CTG", startIndex);
            if (startIndex != -1){
                count++;
            }
            else{
                break;
            }
            startIndex = dna.indexOf("CTG", startIndex) + 3;
        }
        return count;
    }

    // Test countCTG method.
    public void testCountCTG() {
        String input;
        int result;
        
        input = "AGATGCAGCTGATGCTG"; 
        result = countCTG(input);
        System.out.println(result); // 2

        input = "CTGCTGGATGACTGCGC";
        result = countCTG(input);
        System.out.println(result); // 3

        URLResource u = new URLResource("https://users.cs.duke.edu/~rodger/GRch38dnapart.fa");
        String dna = u.asString();
        System.out.println(countCTG(dna));
    }

    // Process the dna strings in the StorageResource object by using methods in this class.
    public void processGenes(StorageResource sr) {
        int nine = 0, cg = 0, max = 0, sixty = 0, total = 0;
        System.out.println("Strings that are longer than 9 characters");
        for(String item: sr.data()) {
            if(item.length() > 9 ) {
                System.out.println(item);
                nine++;
            }
        }
        System.out.println("Total: " + nine);
        System.out.println("Strings whose C-G-ratio is higher than 0.35");
        for(String item: sr.data()) {
            if(cgRatio(item) > 0.35) {
                System.out.println(item);
                cg++;
            }
        }
        System.out.println("Total: " + cg);
        for(String item: sr.data()) {
            if(item.length() > max) {
                max = item.length();
            }
        }
        System.out.println("Length of the longest gene: " + max);
        System.out.println("Strings that are longer than 60 characters");
        for(String item: sr.data()) {
            if(item.length() > 60) {
                System.out.println(item);
                sixty++;
            }
        }
        System.out.println("Total: " + sixty);

        for(String item: sr.data()) {
            total++;
        }
        System.out.println("Total number of genes: " + total);
    }

    // Test processGenes method.
    public void testProcessGenes() {
        String 
         ex1 = "xyxyxyxyxATGCGACGAGTCTGAxxyxATGCTACAAGTCTAA", // Genes that have more characters than nin9
         ex2 = "xyxyxyxyxATGCTCTGAxxyxATGTAA", // Genes that has no more characters than 9
         ex3 = "xyxyxxyxATGTCATATCAACATTAAxxxyxATGTAAxx", // Genes whose cg ratio is lower than 0.35
         ex4 = "xyxyxATGCCATAGxyxyxATGAACGCCGCTTAGxyxx"; // Genes whose cg ratio is higher than 0.35
        
         StorageResource sr = new StorageResource();
         sr = getAllGenes(ex1);
         processGenes(sr);
         System.out.println();

         sr = getAllGenes(ex2);
         processGenes(sr);
         System.out.println();

         sr = getAllGenes(ex3);
         processGenes(sr);
         System.out.println();

         sr = getAllGenes(ex4);
         processGenes(sr);
         System.out.println();

         URLResource fr = new URLResource("https://users.cs.duke.edu/~rodger/GRch38dnapart.fa");
         String dna = fr.asString(), dnaUpper = dna.toUpperCase();
         sr = getAllGenes(dnaUpper);
         processGenes(sr);
    }

    public static void main(String[] args) {
        DnaAnalysis test = new DnaAnalysis();
        //test.printAllGenes("ATGxxTAAxTAGxxATGxxxTGAxxTAAxxxATGxxTAAxTAG"); // output = ATGxxTAAxTAG ATGxxxTGA ATGxxTAAxTAG
        //test.testCgRatio();
        //test.testCountCTG();
        //test.testProcessGenes();

    }

}





















