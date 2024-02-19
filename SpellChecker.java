import java.io.*;
import java.util.*;

public class SpellChecker{
    Set<String> dictionary;

    //Constructor
    //load dictionary from file
    public SpellChecker(String dictionaryFilePath){
        dictionary = new HashSet<>();
        loadDictionary(dictionaryFilePath);
    }


    //load words from  englishDictionary file and store in dictionary set
    public void loadDictionary(String dictionaryFilePath){
        try(BufferedReader reader = new BufferedReader(new FileReader(dictionaryFilePath))){
            String word;
            while((word = reader.readLine()) != null){
                dictionary.add(word);
            }
        }
        catch(IOException e){
            //it prints the details of exception
            e.printStackTrace();
        }
    }

    public List<String> checkAndSuggest(String inputWord){
        int possibleWordLength = inputWord.length() + 2;
        List<String> suggestions = new ArrayList<>();

        if(dictionary.contains(inputWord.toLowerCase())){
            System.out.println(inputWord + " spelled correctly");
        }
        else{
            //iterate to dictionary, to find possible correct words
            for(String word : dictionary){
                int wordLength = word.length();
                //we taking only those word which are in range of possible word which we suggesting
                if(possibleWordLength >= wordLength && isCorrectWord(inputWord.toLowerCase(), word)){
                    suggestions.add(word);
                }
            }

            if(!suggestions.isEmpty()){
                System.out.println(inputWord + " not spelled correctly");
                //String.join used to concatenate multiple strings with a specified delimiter between them
                System.out.println("Suggestions: " + String.join(", ", suggestions));
            }
            else{
                System.out.println(inputWord + " not spelled correctly, and no suggestions found");
            }
        }
        
        return suggestions;
            
    }
    
    public boolean isCorrectWord(String inputWord, String word){
        int inputLength = inputWord.length();
        int wordLength = word.length();
    
        //if the input word is partial
        if(word.startsWith(inputWord)){
            return true;
        }

        //check if both words are anagram
        //if they are anagram then we find one suggestion word
        if(inputLength == wordLength && isAnagram(inputWord, word)){
            return true;
        }
    
        //if input word has extra letter
        if(wordLength == inputLength-1){
            for(int i = 0; i < inputLength; i++){
                //substring method start index is inclusive end index is exclusive
                //it checks if we remove one letter from input word then can the word form
                //we did this just becoz to check if we accidently add extra single character in input word
                //so this should suggest proper word, if we remove single character from input word
                String possibleCompletion = inputWord.substring(0, i) + inputWord.substring(i + 1);
                if(word.equals(possibleCompletion)){
                    return true;
                }
            }
        }


        //if the input word is incomplete we suggesting the words, in which if we add two letter in the input word then it could complete
        if(inputLength == wordLength-1){
            for(int i = 0; i < wordLength; i++){
                //substring method start index is inclusive end index is exclusive
                //it checks if we remove one letter then can the input word be formed
                //we did this just becoz to check if we forgot one character in input word
                //so this should suggest proper word, if we add one letter what word makes
                String possibleCompletion = word.substring(0, i) + word.substring(i + 1);
                if(inputWord.equals(possibleCompletion)){
                    return true;
                }
                  
            }
        }
         

        return false;
    }

    //check if two words are anagram or not
    //creatiing array with size 26 (english letter)
    //for word1 we increase the letter count for its position
    //for word2 we subtract letter count from array but if array at perticular index is already zero and we are trying to delete
    //then we return false
    // eg: index:
    // a-> 0, b->1 and so on
    public boolean isAnagram(String word1, String word2){
        if((word1.length() != word2.length()) || word1.charAt(0) != word2.charAt(0)){
        //if(word1.length() != word2.length()){
            return false;
        }
        
        //convert both strings to lowercase
        //we are taking word in lower case
        //word1 = word1.toLowerCase();
        //word2 = word2.toLowerCase();
        int[] charCount = new int[26];

        for(char c : word1.toCharArray()){
            if(c >= 'a' && c <= 'z'){
                charCount[c - 'a']++;
            } 
        }

        for(char c : word2.toCharArray()){
            if(c >= 'a' && c <= 'z' && charCount[c - 'a'] > 0){
                charCount[c - 'a']--;
            }
            else{
                return false;
            }
        }

        return true;
    }
    







    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter word: ");
        String wordToCheck = sc.nextLine();
	
        long startTime = System.currentTimeMillis();	

        String dictionaryFilePath = "englishDictionary.txt";
        SpellChecker checker = new SpellChecker(dictionaryFilePath);
        checker.checkAndSuggest(wordToCheck);   

        long endTime = System.currentTimeMillis();

        double totalTime = (endTime - startTime) / 1000.0;
        System.out.println("Time: "+ totalTime);
    }
}
