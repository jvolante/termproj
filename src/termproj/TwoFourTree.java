/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package termproj;

import java.util.Comparator;
import java.util.Random;

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
        TwoFourTreeNode node = findNode(root, value);
        int index = node.hasValue(value);
        
        if(index != INVALID_INDEX){
            return (Value)node.elements[index];
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
        TwoFourTreeNode node = findNode(root, value);
        Value result;
        int index = node.hasValue(value);
        
        if(index != INVALID_INDEX){
            result = (Value)node.elements[index];
            size--;
        } else{
            result = null;
        }
        return result;
    }
    
    public void insert(Value value){
        if(isEmpty()){
            root = new TwoFourTreeNode(null);
            root.insertSorted(value);
        }else{
            TwoFourTreeNode current = findNode(root, value);

            //find the appropriate leaf and insert the value
            while(current.hasValue(value) != INVALID_INDEX){
                current = current.getCorrespondingChild(value);
                current = findNode(current, value);
            }
            current.insertSorted(value);

            //make sure the tree is still following the rules
            //if it is not fix it
            while(current.getNumElements() == 4){
                if(current == root){
                    splitNodeIntoThree(current);
                }else{
                    mergeWithParent(current);
                }
                current = current.parent;
            }
        }
        size++;
    }
    
    /**
     * returns the number of elements in the tree.
     * @return number of elements in the tree.
     */
    public int size(){
        return size;
    }
    
    public boolean isEmpty(){
        return size == 0;
    }
    
    /**
     * finds the first node containing the value, or the node that the
     * value should be inserted into.
     * 
     * @param value value to search for.
     * @return first node containing value, or the node it belongs in.
     */
    private TwoFourTreeNode findNode(TwoFourTreeNode current, Value value){
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
    
    private void splitNodeIntoThree(TwoFourTreeNode node){
        TwoFourTreeNode leftChild = new TwoFourTreeNode(node);
        TwoFourTreeNode rightChild = new TwoFourTreeNode(node);
        
        //set up left child
        leftChild.insertElement(0, node.getElement(0));
        leftChild.insertChild(0, node.getChild(0));
        leftChild.insertChild(1, node.getChild(1));
        
        //set up right child
        rightChild.insertElement(0, node.getElement(2));
        rightChild.insertChild(0, node.getChild(2));
        rightChild.insertChild(1, node.getChild(3));
        
        //fix the original node
        //remove all but middle node
        node.removeElement(1);
        node.removeElement(0);
        node.clearChildren();
        node.setChild(0, leftChild);
        node.setChild(1, rightChild);
    }
    
    private void mergeWithParent(TwoFourTreeNode node){
        TwoFourTreeNode parent = node.getParent();
        TwoFourTreeNode newChild = new TwoFourTreeNode(parent);
        
        parent.insertSorted(node.getElement(2));
        
        //set up the new child node
        newChild.insertElement(0, node.getElement(3));
        newChild.insertChild(0, node.getChild(3));
        newChild.insertChild(1, node.getChild(4));
        
        //clean up the old node
        node.removeElement(3);
        node.removeElement(2);
        node.removeChild(4);
        node.removeChild(3);
        
        int index = parent.whatChildIsThis(node) + 1;
        parent.insertChild(index, newChild);
        
    }
    
    public void printTree(TwoFourTreeNode start, int indent) {
        if (start == null) {
            return;
        }
        for (int i = 0; i < indent; i++) {
            System.out.print(" ");
        }
        printTFNode(start);
        indent += 4;
        int numChildren = start.getNumElements() + 1;
        for (int i = 0; i < numChildren; i++) {
            printTree(start.getChild(i), indent);
        }
    }

    public void printTFNode(TwoFourTreeNode node) {
        int numItems = node.getNumElements();
        for (int i = 0; i < numItems; i++) {
            System.out.print((Value) node.getElement(i) + " ");
        }
        System.out.println();
    }

    /**
     * returns the root of the tree.
     * @return root node in the tree.
     */
    public TwoFourTreeNode root() {
        return root;
    }
    
    private class TwoFourTreeNode{
        public static final int MAX_CHILDREN = 4;

        private int numElements = 0;
        
        //contains Values
        private Object[] elements;
        //contains TwoFourTreeNodes
        private Object[] children;
        private TwoFourTreeNode parent;

        /**
         * constructs a node with specified parent, pass null for root.
         * @param parent the node's parent.
         */
        public TwoFourTreeNode(TwoFourTreeNode parent) {
            this.parent = parent;
            //make arrays one larger than necessary for when a node overflows
            children = new Object[MAX_CHILDREN + 1];
            elements = new Object[MAX_CHILDREN];
        }
        
        public int getNumElements(){
            return numElements;
        }
        
        /**
         * inserts an element into the elements array without replacing
         * existing values.
         * 
         * @param index index of insertion
         * @param newValue value to insert
         */
        public void insertElement(int index, Value newValue){
            //shift up each of the elements until we get to the index of insertion
            for(int i = numElements - 1; i >= index; i--){
                elements[i + 1] = elements[i];
            }
            elements[index] = newValue;
            numElements++;
        }
        
        /**
         * inserts an element into the elements array without replacing
         * existing values.
         * 
         * @param index index of insertion
         * @param newChild child to insert
         */
        public void insertChild(int index, TwoFourTreeNode newChild){
            //shift up each of the elements until we get to the index of insertion
            for(int i = numElements; i >= index; i--){
                children[i + 1] = children[i];
            }
            children[index] = newChild;
        }
        
        /**
         * checks if the node is a leaf node.
         * 
         * @return true if this is a leaf false otherwise
         */
        public boolean isLeaf(){
            return children[0] == null;
        }
        
        /**
         * inserts the new value in a position that will keep the array sorted.
         * 
         * @param newValue value to insert
         * @return index of newValue
         */
        public int insertSorted(Value newValue){
            int index = getSortedIndex(newValue);
            insertElement(index, newValue);
            return index;
        }
            
         /**
         * removes an element from the elements array by overwriting it with
         * its neighbor
         * 
         * @param index index of element to be removed
         */
        public void removeElement(int index){
            //shift over each element starting with the index
            for(int i = index; i < numElements-1; i++){
                elements[i] = elements[i+1];
            }
            elements[numElements-1] = null;
            numElements--; 
        }
        
        /**
         * nulls a child pointer
         * 
         * @param index index of element to be removed
         */
        public void removeChild(int index){
            children[index] = null;
        }
        
        /**
         * returns element specified by the index
         * 
         * @param index index of the element
         */
        public Value getElement(int index){
            return((Value) elements[index]);
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
           int numChildren = numElements + 1;
           for(int i = 0; i < numChildren; i++){
               current = (Value)elements[i];
               
               if(valueComparator.compare(current, value) == 0){
                   return i;
               }
           }
           return INVALID_INDEX;
       }

       /**
        * Sets the child node 
        * 
        * @param index index
        * @param node TwoFourTree node 
        */
       public void setChild(int index, TwoFourTreeNode node){
           children[index] = node;
       }
       
       /**
        * Returns a reference to the child at the specified index.
        * 
        * @param index index of child to return.
        * @return 
        */
       public TwoFourTreeNode getChild(int index){
           return (TwoFourTreeNode)children[index];
       }
       
       /**
        * Sets all children to null
        * 
        */
       public void clearChildren(){
          for( int i = 0; i < children.length; i++){
              children[i] = null;
          }
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
            for(int i = 0; i < children.length; i++){
                current = (Value)elements[i];
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
        
        /**
         * returns the parent to the node.
         * @return parent
         */
        public TwoFourTreeNode getParent(){
            return parent;
        }
        
        /**
         * gets the index of the specified node in this node's array of children.
         * 
         * @param node node to look for
         * @return index of node in the children array. if it is not found
         *         returns INVALID_INDEX
         */
        public int whatChildIsThis(TwoFourTreeNode node){
            for(int i = 0; i < children.length; i++){
                if(node == children[i]){
                    return i;
                }
            }
            return INVALID_INDEX;
        }
        
        public TwoFourTreeNode getLeftSibling(){
            int leftIndex = parent.whatChildIsThis(this) - 1;
            return parent.getChild(leftIndex);
        }
        
        public TwoFourTreeNode getRightSibling(){
            int rightIndex = parent.whatChildIsThis(this) + 1;
            return parent.getChild(rightIndex);
        }
        
        /**
         * returns the position that value must be inserted to maintain sorted
         * order.
         * 
         * @param value value to check position
         * @return index value needs to be inserted into
         */
        private int getSortedIndex(Value value){
            for(int i = 0; i < numElements; i++){
                if(valueComparator.compare((Value)elements[i], value) >= 0){
                   return i;
                }
            }
            return numElements;
        }
    }
    
    public static void main(String[] args){
        final int TEST_SIZE = 1000;
        TwoFourTree<Integer> tfTree = new TwoFourTree<>(new Comparator<Integer>() {

            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        
        Random r = new Random();
        
        while(tfTree.size() < TEST_SIZE){
            tfTree.insert(r.nextInt(100));
        }
    }
}
