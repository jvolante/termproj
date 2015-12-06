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
public class DictionaryNode <Key, Value>{
    private Key k;
    Value v;
    
    public DictionaryNode(Key newKey, Value newValue){
        setKey(newKey);
        setValue(newValue);
    }
    
    public Key getKey(){
        return k;
    }
    
    public Value getValue(){
        return v;
    }
    
    public void setKey(Key newKey){
        k = newKey;
    }
    
    public void setValue(Value newValue){
        v = newValue;
    }
}
