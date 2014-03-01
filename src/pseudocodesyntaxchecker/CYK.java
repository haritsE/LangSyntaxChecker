/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pseudocodesyntaxchecker;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Harits
 */
public class CYK {

/* The CYK algorithm in pseudocode is as follows:
 
let the input be a string S consisting of n characters: a1 ... an.
let the grammar contain r nonterminal symbols R1 ... Rr.
This grammar contains the subset Rs which is the set of start symbols.
let P[n,n,r] be an array of booleans. Initialize all elements of P to false.
for each i = 1 to n
  for each unit production Rj -> ai
    set P[i,1,j] = true
for each i = 2 to n -- Length of span
  for each j = 1 to n-i+1 -- Start of span
    for each k = 1 to i-1 -- Partition of span
      for each production RA -> RB RC
        if P[j,k,B] and P[j+k,i-k,C] then set P[j,i,A] = true
if any of P[1,n,x] is true (x is iterated over the set s, where s are all the indices for Rs) then
  S is member of language
else
  S is not member of language
*/
    private String reserved[] = {"then", "if", "begin", "end", "repeat", "else", "repeat", "until","while","do","input","output"};
    private ArrayList<Character> symbols;
    private ArrayList<String> parsedText;
    private ArrayList<String> rules;
    private boolean dp[][][];
    public CYK(String t, String r){
        parsedText = new ArrayList<String>();
        rules = new ArrayList<String>();
        symbols = new ArrayList<Character>();
        dp = new boolean[100][100][50];
        
        parseInput(t);
        parseRules(r);
        
        //init DP val
        for(int i=0;i<100;i++)
            for(int j=0;j<100;j++)
                for(int k=0;k<50;k++)
                    dp[i][j][k] = false;
    }
    
    public int phraseCount(){
        int count = 0;
        int idx = 0;
        while(idx<parsedText.size()){
            //System.out.println(idx+" "+parsedText.size()+ "");
            if(parsedText.get(idx).charAt(0) >= 'a' && parsedText.get(idx).charAt(0) <= 'z' && parsedText.get(idx).length() == 1){
                while(idx<parsedText.size()){
                    if(parsedText.get(idx).charAt(0) >= 'a' && parsedText.get(idx).charAt(0) <= 'z' && parsedText.get(idx).length() == 1){
                        idx++;
                    } else
                        break;
                }
                count++;
            } else {
                count++;
                idx++;
            }
        }
        return count;
    }
    
    public String getLeftOperand(int index){
        //Misal rule: A->BC. Fungsi ini return string "A"
        int idx = rules.get(index).indexOf("->");
        //System.out.println(rules.get(index).indexOf("->"));
        idx--;
        while(rules.get(index).charAt(idx) == ' ')
            idx--;
        String p = rules.get(index).substring(0, idx+1);
        return p;
    }
    
    public String getRightOperand(int index){
        //Misal rule: A->BC. Fungsi ini return string "BC"
        int idx = rules.get(index).indexOf("->");
        idx += 2;
        while(rules.get(index).charAt(idx) == ' ')
            idx++;
        String p = rules.get(index).substring(idx);
        return p;
    }
    
    public boolean isTerminal(String t){
        for(int i=0;i<t.length();i++){
            if(t.charAt(i) >= 'A' && t.charAt(i) <= 'Z') //kalo kapital, berarti variabel
                return false;
        }
        return true;
    }
    
    public int getIdxOfSymbol(char c){
        for(int i=0;i<symbols.size();i++){
            if(symbols.get(i).charValue() == c){
                return i;
            }    
        }
        return -1;
    }
    
    public boolean process(){
        boolean result = true;
        for(int i=0;i<parsedText.size();i++){
            for(int j=0;j<rules.size();j++){
                String rightOperand = getRightOperand(j);
                if(isTerminal(rightOperand)){
                    /*
                    String text = "";
                    int idx = i;
                    while(text.length() < rightOperand.length() && idx < parsedText.size() && parsedText.get(idx).length() == 1){
                        text += parsedText.get(idx);
                        idx++;
                    }
                    */
                    
                   // System.out.println("text gabung: "+text);
                    //rightOperand.equals(parsedText.get(i))
                    /*
                    if(rightOperand.equals(text)){
                        dp[i][text.length()][getIdxOfSymbol(getLeftOperand(j).charAt(0))] = true;
                    } else 
                    */
                    if(rightOperand.equals(parsedText.get(i))){
                        dp[i][1][getIdxOfSymbol(getLeftOperand(j).charAt(0))] = true;
                    }
                }
            }
        }
        
        for(int i=1;i<=parsedText.size();i++){
            for(int j=0;j<parsedText.size()-i+1;j++){
                for(int k=1;k<i;k++){
                    for(int m=0;m<rules.size();m++){
                        if(getRightOperand(m).length() == 2){
                            char first = getRightOperand(m).charAt(0);
                            char second = getRightOperand(m).charAt(1);
                            char third = getLeftOperand(m).charAt(0);            
                            if(dp[j][k][getIdxOfSymbol(first)] && dp[j+k][i-k][getIdxOfSymbol(second)])
                                dp[j][i][getIdxOfSymbol(third)] = true;
                        }
                    }
                }
            }
        }
        
        /*
        for(int i=0;i<parsedText.size();i++){
            for(int j=1;j<=parsedText.size();j++){
                for(int k=0;k<symbols.size();k++){
                    if(dp[i][j][k])
                        System.out.println("true: " + i +" "+ j +" "+ symbols.get(k));
                    //System.out.print("" + i +" "+ j +" "+ k);
                }
            }
        }*/
        
        //if(dp[0][parsedText.size()][getIdxOfSymbol('S')]) <-- kalo misalnya mau diitung per karakter
        if(dp[0][parsedText.size()][getIdxOfSymbol('S')])
            return true;
        else
            return false;
        
    }
    
    public final void parseInput(String input){
        String tmp[] = input.split("\\s|\n");
        
        for(String s:tmp){
            boolean found = false;
            String resv = null;
            for(String t:reserved){
                if((s.indexOf(t) != -1 && s.indexOf('(') != -1) || s.equals(t)){
                    found = true;
                    resv = t;
                }
            }
            
            if(found){
                if(!s.equals(resv)){ //berarti, suatu reserved word yg ditempelin sesuatu yg ada simbol '('
                    parsedText.add(s.substring(0, resv.length()));
                    String tmp2[] = s.substring(resv.length()).split("");
                    for(String t:tmp2)
                        if(!t.equals(""))
                            parsedText.add(t);
                } else { //pure reserved word phraseW
                        parsedText.add(s);
                }
            } else { //frase ini bukan reserved word, potong kecil2 lalu masak
                String tmp2[] = s.split("");
                for(String t:tmp2)
                    if(!t.equals(""))
                        parsedText.add(t);
            }
        }
    }
    
    public final void parseRules(String r){
        String tmp[] = r.split("\n");
        for(String t:tmp){
            for(int i=0;i<t.length();i++)
                if(t.charAt(i) != ' ' && t.charAt(i) != '-' && t.charAt(i) != '>')
                    if(!symbols.contains(t.charAt(i))){
                        symbols.add(t.charAt(i));
                    }
        }
        
        rules.addAll(Arrays.asList(tmp));
    }
    
    public void debug(){
        for(String s:parsedText)
            System.out.println(s);
        for(String s:rules)
            System.out.println(s);
    }
    
}
