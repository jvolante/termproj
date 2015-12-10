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
class DictionaryPair {
    Object key;
    Object value;
    
    public DictionaryPair(Object newKey, Object newValue){
        key = newKey;
        value = newValue;
    }
    
    public Object getKey(){
        return key;
    }
    
    public Object getValue(){
        return value;
    }
}
