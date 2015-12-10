/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package termproj;

/**
 * adapts the twofourtree to implement the dictionary interface
 * @author Jackson
 * @version 1
 */
public class TwoFourTreeDictionary implements Dictionary {
    
    private TwoFourTree<DictionaryPair> tfTree;
    
    /**
     * constructor which takes comparator for keys
     * @param keyComp comparator used to compare keys
     */
    public TwoFourTreeDictionary(Comparator keyComp){
        DictKeyComparator comp = new DictKeyComparator(keyComp);
    }
    
    /**
     * number of elements in the tree
     * @return number of elements in the tree
     */
    @Override
    public int size() {
        return tfTree.size();
    }

    /**
     * if the tree is empty
     * @return returns true if the tree is empty
     */
    @Override
    public boolean isEmpty() {
        return tfTree.isEmpty();
    }

    /**
     * finds an element in the tree
     * @param key key for element to find
     * @return first element corresponding to key.
     */
    @Override
    public Object findElement(Object key) {
        DictionaryPair value = new DictionaryPair(key, null);
        return tfTree.find(value);
    }

    /**
     * inserts a key value pair into the tree
     * @param key key for the pair
     * @param element value for the pair
     */
    @Override
    public void insertElement(Object key, Object element) {
        DictionaryPair value = new DictionaryPair(key, element);
        tfTree.insert(value);
    }

    /**
     * removes an element in the tree
     * @param key key for element to remove
     * @return first element corresponding to key.
     */
    @Override
    public Object removeElement(Object key) throws ElementNotFoundException {
        DictionaryPair value = new DictionaryPair(key, null);
        return tfTree.remove(value);
    }
    
    /**
     * pretty prints the tree.
     */
    public void printTree(){
        tfTree.printTree(tfTree.root(), 0);
    }
}
