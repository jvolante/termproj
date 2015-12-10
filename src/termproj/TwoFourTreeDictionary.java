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
public class TwoFourTreeDictionary implements Dictionary {
    
    private TwoFourTree<DictionaryPair> tfTree;
    
    public TwoFourTreeDictionary(Comparator keyComp){
        DictKeyComparator comp = new DictKeyComparator(keyComp);
    }
    
    @Override
    public int size() {
        return tfTree.size();
    }

    @Override
    public boolean isEmpty() {
        return tfTree.isEmpty();
    }

    @Override
    public Object findElement(Object key) {
        DictionaryPair value = new DictionaryPair(key, null);
        return tfTree.find(value);
    }

    @Override
    public void insertElement(Object key, Object element) {
        DictionaryPair value = new DictionaryPair(key, element);
        tfTree.insert(value);
    }

    @Override
    public Object removeElement(Object key) throws ElementNotFoundException {
        DictionaryPair value = new DictionaryPair(key, null);
        return tfTree.remove(value);
    }
    
    public void printTree(){
        tfTree.printTree(tfTree.root(), 0);
    }
}
