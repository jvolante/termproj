/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package termproj;

import java.util.Comparator;

/**
 * An implementation of a 2-4 tree
 * @version 1
 * @author Jackson
 */
public class TwoFourTree <Value>{
    private static final int INVALID_INDEX = -1;
    
    private int size = 0;
    private Comparator<Value> valueComparator;
    private TwoFourTreeNode root = null;
    
    /**
     * Constructs a new 2-4 tree with a comparator to compare keys.
     * 
     * @param comp the comparator used to sort the keys.
     */
    public TwoFourTree(Comparator<Value> comp){
        valueComparator = comp;
    }
    
    /**
     * returns the value of the first node with the specified value.
     * 
     * @param value the value to find the pair for.
     * @return the value of node found for value.
     */
    public Value find(Value value){
        TwoFourTreeNode node = findNode(value);
        int index = node.hasValue(value);
        
        if(index != INVALID_INDEX){
            return (Value)node.contents[index];
        } else{
            return null;
        }
    }
    
    /**
     * removes the first node with specified value and returns its value.
     * 
     * @param value the value to find the pair for.
     * @return the value of the first node with the specified value.
     */
    public Value remove(Value value){
        TwoFourTreeNode node = findNode(value);
        Value result;
        int index = node.hasValue(value);
        
        if(index != INVALID_INDEX){
            result = (Value)node.contents[index];
            size--;
        } else{
            result = null;
        }
        return result;
    }
    
    public void insert(Value value){
        //TODO
    }
    
    /**
     * finds the first node containing the value, or the node that the
     * value should be inserted into.
     * 
     * @param value value to search for.
     * @return first node containing value, or the node it belongs in.
     */
    private TwoFourTreeNode findNode(Value value){
        TwoFourTreeNode current = root;
        TwoFourTreeNode next = null;
        while(next != current){
            next = current.getCorrespondingChild(value);
            //if the current has the value we are done
            if(current.hasValue(value) != INVALID_INDEX){
                next = current;
            }
        }
        return current;
    }
    
    
    private class TwoFourTreeNode{
        public static final int MAX_CHILDREN = 4;

        //contains Values
        private Object[] contents;
        //contains TwoFourTreeNodes
        private Object[] children;
        private TwoFourTreeNode parent;

        /**
         * Default constructor
         */
        public TwoFourTreeNode(TwoFourTreeNode parent) {
            this.parent = parent;
            //make arrays one larger than necessary for when a node overflows
            children = new Object[MAX_CHILDREN + 1];
            contents = new Object[MAX_CHILDREN];
        }
        
        /**
        * checks if the node contains the specified key and returns the index
        * if found.
        * 
        * @param node the node to search in
        * @param value key to check for.
        * @return if key is found return its index in contents if not return INVALID_INDEX.
        */
       public int hasValue(Value value){
           //loop over each one of the contents and check if it has the key
           Value current;
           for(int i = 0; i < TwoFourTreeNode.MAX_CHILDREN; i++){
               current = (Value)contents[i];
               //if it is null we have reached the end of the valid contents
               if(current == null){
                   break;
               }
               if(valueComparator.compare(current, value) == 0){
                   return i;
               }
           }
           return INVALID_INDEX;
       }

       /**
        * Gets the child node that v should be placed in.
        * 
        * @param value value to find child node for
        * @return child node that value should belong in if the child is null
        *         the current node will be returned.
        */
        public TwoFourTreeNode getCorrespondingChild(Value value){
            Value current;
            TwoFourTreeNode result = null;
            //find the first value greater than or equal to
            for(int i = 0; i < TwoFourTreeNode.MAX_CHILDREN; i++){
                current = (Value)contents[i];
                //if the value is greater than all the elements return the last
                //child
                if(current == null){
                    result = (TwoFourTreeNode)children[i];
                }else if (valueComparator.compare(current, value) >= 0){
                    result = (TwoFourTreeNode)children[i];
                }
            }
            //if the child node does not exist return the current node
            if(result == null){
                return this;
            }else{
                return result;
            }
        }
    }
}
