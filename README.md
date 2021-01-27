# Using Huffman tree to compress and decompress a text file

## Overview
This program get a text file and produce another file which has less size using Huffman algorithm.

## Features
- You can have a text file and get another file with less size, just by giving the address of text file.
- The output file in a .cmp file which include the object of tree and the encoded data of your text.(Note: this binary code is your final output and the tree is just for decoding.)
- This program use Gson class to store the Huffman tree in file using Json.Gson is in Google library. If you are interested in knowing how to add Gson to your IDM, just check out this video on YouTube. [Watch Now](https://www.youtube.com/watch?v=HSuVtkdej8Q&t=245s)

## Some explanation about methods and classes
1. This class implements nodes of tree and it Overrides compareTo method of comparable class that help us compare nodes.
>static class Node implements Comparable{}

2. This method uses priority queue to arrange nodes in a increasing order which is helpful for Huffman algorithm
>static Node buildTree(int[] freq) {}

3. This is for aski code and for characters
>static int ALPHA_SIZE = 256;

4. This will compress text and produce binary code instead of that.
>public EncodedResult compress(String data) {}

5. This will decompress binary code according to object of tree which has stored.
>public String decompress(EncodedResult result) {}

6. This method counts the number of each character in the text. 
>static int[] buildFrequencyTable(String data) {}

7. This will match each character with its binary code.
> static Map<Character, String> buildLookup(Node root) {}



 
