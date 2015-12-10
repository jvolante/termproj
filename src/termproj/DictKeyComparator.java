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
class DictKeyComparator implements Comparator{

    Comparator keyComp;
    
    public DictKeyComparator(Comparator keyComp){
        this.keyComp = keyComp;
    }
    
    @Override
    public boolean isLessThan(Object obj1, Object obj2) {
        DictionaryPair d1 = (DictionaryPair) obj1;
        DictionaryPair d2 = (DictionaryPair) obj2;
        
        return keyComp.isLessThan(d1.getKey(), d2.getKey());
    }

    @Override
    public boolean isLessThanOrEqualTo(Object obj1, Object obj2) {
        DictionaryPair d1 = (DictionaryPair) obj1;
        DictionaryPair d2 = (DictionaryPair) obj2;
        
        return keyComp.isLessThanOrEqualTo(d1.getKey(), d2.getKey());
    }

    @Override
    public boolean isGreaterThan(Object obj1, Object obj2) {
        DictionaryPair d1 = (DictionaryPair) obj1;
        DictionaryPair d2 = (DictionaryPair) obj2;
        
        return keyComp.isGreaterThan(d1.getKey(), d2.getKey());
    }

    @Override
    public boolean isGreaterThanOrEqualTo(Object obj1, Object obj2) {
        DictionaryPair d1 = (DictionaryPair) obj1;
        DictionaryPair d2 = (DictionaryPair) obj2;
        
        return keyComp.isGreaterThanOrEqualTo(d1.getKey(), d2.getKey());
    }

    @Override
    public boolean isEqual(Object obj1, Object obj2) {
        DictionaryPair d1 = (DictionaryPair) obj1;
        DictionaryPair d2 = (DictionaryPair) obj2;
        
        return keyComp.isEqual(d1.getKey(), d2.getKey());
    }

    @Override
    public boolean isComparable(Object obj) {
        DictionaryPair d1 = (DictionaryPair) obj;
        
        return keyComp.isComparable(d1.getKey());
    }
    
}
