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
public class TwoFourTree <Key, Value>{
    private static final int INVALID_INDEX = -1;
    
    private int size = 0;
    private Comparator<Key> keyComparator;
    private TwoFourTreeNode<Key, Value> root = null;
    
    /**
     * Constructs a new 2-4 tree with a comparator to compare keys.
     * 
     * @param comp the comparator used to sort the keys.
     */
    public TwoFourTree(Comparator<Key> comp){
        keyComparator = comp;
    }
    
    /**
     * returns the value of the first node with the specified key.
     * 
     * @param key the key to find the pair for.
     * @return the value of node found for key.
     */
    public Value find(Key key){
        TwoFourTreeNode<Key, Value> node = findNode(key);
        int index = hasKey(node, key);
        
        if(index != INVALID_INDEX){
            return node.contents[index].getValue();
        } else{
            return null;
        }
    }
    
    /**
     * removes the first node with specified key and returns its value.
     * 
     * @param key the key to find the pair for.
     * @return the value of the first node with the specified key.
     */
    public Value remove(Key key){
        TwoFourTreeNode<Key, Value> node = findNode(key);
        int index = hasKey(node, key);
        
        if(index != INVALID_INDEX){
            Value result = node.contents[index].getValue();
            size--;
        } else{
            return null;
        }
    }
    
    public void insert(Key key, Value value){
        //TODO
    }
    
    /**
     * finds the first node containing the key, or the node that
     * the key should be inserted into.
     * 
     * @param key key to search for.
     * @return first node containing key, or the node it belongs in.
     */
    private TwoFourTreeNode<Key, Value> findNode(Key key){
        //TODO
    }
    
    /**
     * finds the first node containing the key in the DictionaryNode, or 
     * the node that the key should be inserted into.
     * 
     * @param node node containing the key to search for.
     * @return 
     */
    private TwoFourTreeNode<Key, Value> findNode(DictionaryNode<Key, Value> node){
        return findNode(node.getKey());
    }
    
    /**
     * checks if the node contains the specified key and returns the index
     * if found.
     * 
     * @param node the node to search in
     * @param key key to check for.
     * @return if key is found return its index in contents if not return INVALID_INDEX.
     */
    public int hasKey(TwoFourTreeNode<Key, Value> node, Key key){
        //loop over each one of the contents and check if it has the key
        DictionaryNode<Key, Value> current;
        for(int i = 0; i < TwoFourTreeNode.MAX_CHILDREN; i++){
            current = node.contents[i];
            //if it is null we have reached the end of the valid contents
            if(current == null){
                break;
            }
            if(keyComparator.compare(current.getKey(), key) == 0){
                return i;
            }
        }
        return INVALID_INDEX;
    }
}
