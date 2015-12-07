/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package termproj;

/**
 *
 * @author Jackson
 */
public class TwoFourTreeNode <Key, Value>{
    public static final int MAX_CHILDREN = 4;
    
    /*package*/ DictionaryNode<Key, Value>[] contents;
    private TwoFourTreeNode<Key, Value>[] children;
    private TwoFourTreeNode<Key, Value> parent;

    /**
     * Default constructor
     */
    public TwoFourTreeNode() {
        //make arrays one larger than necessary for when a node overflows
        children = new TwoFourTreeNode[MAX_CHILDREN + 1];
        contents = new DictionaryNode[MAX_CHILDREN];
    }
    
    
}
