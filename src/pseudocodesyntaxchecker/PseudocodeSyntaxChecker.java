/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pseudocodesyntaxchecker;

import javax.swing.JFileChooser;
import javax.swing.UIManager;

/**
 *
 * @author Harits
 */

public class PseudocodeSyntaxChecker {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        
        // TODO code application logic here
        //String input = "a * b";
        //String rules = "S->AB\nA->CD\nC->a\nD->*\nB->b\n";
        //CYK cyk = new CYK(input, rules);
        //cyk.debug();
        //boolean res = cyk.process();
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        MainFrame frame = new MainFrame();
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        
    }
}
