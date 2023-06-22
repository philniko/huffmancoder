import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

class Huffman {


    // recursive function to record the characters in an array and the ca.concordia.datastructures.huffman.Huffman codes in a string array
    public static void printCode(HuffmanNode root, String s, ArrayList<Character> charList, ArrayList<String> codeList)
    {
        if (root.left == null && root.right == null) {
            charList.add(root.c);
            codeList.add(s);
            return;
        }

        printCode(root.left, s + "0", charList, codeList);
        printCode(root.right, s + "1", charList, codeList);
    }

    // main function
    public static void main(String[] args)
    {
        // Check if the number of arguments is correct
        String fileName = "";
        if (args.length != 2) {
            System.err.println("Expected 2 arguments, got " + args.length);
            System.exit(0);
        } else {
            fileName = args[0];
            String mode = args[1];

            // Check if the mode is valid
            if (!(mode.equals("encode") || mode.equals("decode"))) {
                System.err.println("Invalid mode: " + mode);
                System.exit(0);
            }
        }
        FileInputStream fis = null;
        int[] frequency = new int[256];
        try {
            fis = new FileInputStream(fileName);
            int currentByte;
            while ((currentByte = fis.read()) != -1) {
                frequency[currentByte]++;
            }
        } catch(FileNotFoundException e) {
            System.err.println("File not found");
            System.exit(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        // Initialize the array of characters
        char[] chars = new char[256];
        // Fill the array with the characters
        for (int i = 0; i < 256; i++) {
            chars[i] = (char) i;
        }
        // Create Arraylists for the characters and their frequencies
        ArrayList<Character> charList = new ArrayList<>();
        ArrayList<Integer> frequencyList = new ArrayList<>();
        for (int i = 0; i < 256; i++) {
            if (frequency[i] != 0) {
                charList.add(chars[i]);
                frequencyList.add(frequency[i]);
            }
        }

        int n = charList.size();
        // creating a priority queue q.
        // makes a min-priority queue(min-heap).
        PriorityQueue<HuffmanNode> q
                = new PriorityQueue<HuffmanNode>(
                n, new MyComparator());

        for (int i = 0; i < n; i++) {

            // creating a ca.concordia.datastructures.huffman.Huffman node object
            // and add it to the priority queue.
            HuffmanNode hn = new HuffmanNode();

            hn.c = charList.get(i);
            hn.data = frequencyList.get(i);

            hn.left = null;
            hn.right = null;

            // add huffman node to queue
            q.add(hn);
        }

        // create a root node
        HuffmanNode root = null;

        // extract 2 minimum values from heap
        // until size reduces to 1
        while (q.size() > 1) {


            HuffmanNode x = q.poll();
            HuffmanNode y = q.poll();

            // new node f which is the sum of the frequency of the two nodes
            HuffmanNode f = new HuffmanNode();

            f.data = x.data + y.data;
            f.c = '-';

            // first extracted node as left child.
            f.left = x;

            // second extracted node as the right child.
            f.right = y;

            // marking the f node as the root node.
            root = f;

            // add this node to the priority-queue.
            q.add(f);
        }

        // initiate the encode or decode process
        ArrayList<Character> charListNew = new ArrayList<>();
        ArrayList<String> codeList = new ArrayList<>();
        printCode(root, "", charListNew, codeList);
        if (args[1].equals("encode")) {
            encode(charListNew, codeList);
        } else {
            decode(charListNew, codeList);
        }

    }

    public static void encode(ArrayList<Character> charList, ArrayList<String> codeList) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (!charList.contains(c)) {
                System.err.println("\nHuffman code not found for this character: " + c);
                System.exit(0);
            } else {
                int index = charList.indexOf(c);
                System.out.print(codeList.get(index));
            }
        }
        System.out.println();
    }
    public static void decode(ArrayList<Character> charList, ArrayList<String> codeList)  {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        String temp = "";
        for (int i = 0; i < input.length(); i++) {
            temp += input.charAt(i);
            int index = codeList.indexOf(temp);
            if (index != -1) {
                System.out.print(charList.get(index));
                temp = "";
            }
        }
        System.out.println();
    }
}

class HuffmanNode {

    int data; // frequency of the character
    char c; // character

    HuffmanNode left;
    HuffmanNode right;
}

// comparator class to compare the nodes
// based on value of data (frequency)
class MyComparator implements Comparator<HuffmanNode> {
    public int compare(HuffmanNode x, HuffmanNode y)
    {

        return x.data - y.data;
    }
}