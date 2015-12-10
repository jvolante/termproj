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
        
        //if the value is found in the tree
        if(index != INVALID_INDEX){
            result = (Value)node.getElement(index);
            
            //get the in order successor
            TwoFourTreeNode current = node.getChild(index + 1);
            while(!current.isLeaf()){
                current = current.getChild(0);
            }
            
            //replace the removed element
            node.replaceElement(index, current.getElement(0));
            
            //remove the element we just used to replace
            current.removeElement(0);
            
            //if we emptied a leaf, fix it
            fixAfterRemoval(current);
            
            size--;
        } else{
            result = null;
        }
        return result;
    }
    
    private void fixAfterRemoval(TwoFourTreeNode current){
        if(current.getNumElements() == 0){
            //if the left or right sibiling is a 2 node or more
            //shift values from it to put a value into this node
            TwoFourTreeNode leftSibling = current.getLeftSibling();

            //check if we can fix the problem with the left sibling
            if(leftSibling != null && leftSibling.getNumElements() != 1){
                fixWithLeftSibling(current, leftSibling);
            }else{
                //if not try the right sibling
                TwoFourTreeNode rightSibling = current.getRightSibling();

                if(rightSibling != null && rightSibling.getNumElements() != 1){
                    fixWithRightSibling(current, rightSibling);
                }else{
                    //if not merge using elements from the parent
                    //continue merging until we reach the root or the
                    //underflow problem is solved
                    while(current != root && current.getNumElements() == 0){
                        mergeWithParentElements(current);
                        current = current.getParent();
                    }

                    //if we are the root and have underflowed
                    //combine our two children and set as the new root
                    if(current.getNumElements() == 0){
                        root = mergeWithChildren(current);
                    }
                }
            }
        }
    }
    
    private void fixWithLeftSibling(TwoFourTreeNode empty, TwoFourTreeNode sibling){
        TwoFourTreeNode parent = empty.getParent();
        int emptyIndex = parent.whatChildIsThis(empty);
        int leftIndex = emptyIndex - 1;
        
        empty.insertElement(0, parent.getElement(leftIndex));
        parent.replaceElement(leftIndex, sibling.getElement(sibling.getNumElements() - 1));
        sibling.removeElement(sibling.getNumElements() - 1);
    }
    
    private void fixWithRightSibling(TwoFourTreeNode empty, TwoFourTreeNode sibling){
        TwoFourTreeNode parent = empty.getParent();
        int emptyIndex = parent.whatChildIsThis(empty);
        int rightIndex = emptyIndex + 1;
        
        empty.insertElement(0, parent.getElement(rightIndex));
        parent.replaceElement(rightIndex, sibling.getElement(0));
        sibling.removeElement(0);
    }
    
    private void mergeWithParentElements(TwoFourTreeNode empty){
        TwoFourTreeNode parent = empty.getParent();
        int emptyIndex = parent.whatChildIsThis(empty);
        
        if(emptyIndex == 0){
            TwoFourTreeNode rightSibling = parent.getChild(emptyIndex + 1);
            rightSibling.insertSorted(parent.getElement(0));
            
            parent.removeChild(emptyIndex);
            parent.removeElement(0);
            
            mergeWithLeftSibling(rightSibling);
            
        }else{
            TwoFourTreeNode leftSibling = parent.getChild(emptyIndex - 1);
            leftSibling.insertSorted(parent.getElement(parent.getNumElements() - 1));
            
            parent.removeChild(emptyIndex);
            parent.removeElement(parent.getNumElements() - 1);
            
            mergeWithRightSibling(leftSibling);
        }
    }
    
    private TwoFourTreeNode mergeWithChildren(TwoFourTreeNode empty){
        TwoFourTreeNode leftChild = empty.getChild(0);
        TwoFourTreeNode rightChild = empty.getChild(1);
        
        for(int i = 0; i < rightChild.getNumElements(); i++){
            leftChild.replaceElement(leftChild.getNumElements() + i, rightChild.getElement(i));
            leftChild.replaceChild(leftChild.getNumElements() + 1 + i, rightChild.getChild(i + 1));
        }
        
        return leftChild;
    }
    
    private void mergeWithLeftSibling(TwoFourTreeNode rightSibling){
        TwoFourTreeNode parent = rightSibling.getParent();
        TwoFourTreeNode leftSibling = parent.getChild(parent.whatChildIsThis(rightSibling) - 1);
        
        //move children
        for(int i = leftSibling.getNumElements(); i >= 0; i--){
            rightSibling.insertChild(0, leftSibling.getChild(i));
        }
        
        //move elements
        for(int i = leftSibling.getNumElements() - 1; i >= 0; i--){
            rightSibling.insertElement(0, leftSibling.getElement(i));
        }
        
        parent.removeChild(parent.whatChildIsThis(leftSibling));
    }
    
    private void mergeWithRightSibling(TwoFourTreeNode leftSibling){
        TwoFourTreeNode parent = leftSibling.getParent();
        TwoFourTreeNode rightSibling = parent.getChild(parent.whatChildIsThis(leftSibling) + 1);
        
        //move children
        for(int i = 0; i < rightSibling.getNumElements() + 1; i++){
            leftSibling.insertChild(leftSibling.getNumElements() + i, rightSibling.getChild(i));
        }
        
        //move elements
        for(int i = 0; i < rightSibling.getNumElements(); i++){
            leftSibling.insertElement(leftSibling.getNumElements(), rightSibling.getElement(i));
        }
    }
    
    public void insert(Value value){
        if(isEmpty()){
            root = new TwoFourTreeNode(null);
            root.insertSorted(value);
        }else{
            TwoFourTreeNode current = findNode(root, value);

            //find the appropriate leaf and insert the value
            while(current.hasValue(value) != INVALID_INDEX && !current.isLeaf()){
                current = current.getCorrespondingChild(value);
                current = findNode(current, value);
            }
            current.insertSorted(value);

            //make sure the tree is still following the rules
            //if it is not fix it
            while(current != null && current.getNumElements() == 4){
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
        TwoFourTreeNode previous = null;
        while(previous != current){
            next = current.getCorrespondingChild(value);
            //if the current has the value we are done
            if(current.hasValue(value) == INVALID_INDEX){
                previous = current;
                current = next;
            }else{
                //if the value is found we are done
                previous = current;
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
        rightChild.insertElement(1, node.getElement(3));
        rightChild.insertChild(0, node.getChild(2));
        rightChild.insertChild(1, node.getChild(3));
        rightChild.insertChild(2, node.getChild(4));
        
        //fix the original node
        //remove all but middle node
        node.removeElement(3);
        node.removeElement(2);
        node.removeElement(0);
        node.clearChildren();
        node.replaceChild(0, leftChild);
        node.replaceChild(1, rightChild);
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
        
        public void setParent(TwoFourTreeNode parent){
            this.parent = parent;
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
            newChild.setParent(this);
            //shift up each of the elements until we get to the index of insertion
            for(int i = children.length - 2; i >= index; i--){
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
           for(int i = 0; i < numElements; i++){
               current = (Value)elements[i];
               
               if(valueComparator.compare(current, value) == 0){
                   return i;
               }
           }
           return INVALID_INDEX;
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
       
       public void replaceElement(int index, Value newElement){
           elements[index] = newElement;
       }
       
       /**
        * Sets the child node 
        * 
        * @param index index
        * @param node TwoFourTree node 
        */
       public void replaceChild(int index, TwoFourTreeNode newChild){
           newChild.setParent(this);
           children[index] = newChild;
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
                if(i == elements.length){
                    result = (TwoFourTreeNode)children[i];
                    break;
                }
                current = (Value)elements[i];
                //if the value is greater than all the elements return the last
                //child
                if(current == null){
                    result = (TwoFourTreeNode)children[i];
                    break;
                }else if (valueComparator.compare(current, value) >= 0){
                    result = (TwoFourTreeNode)children[i];
                    break;
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
        int i = 0;
        TwoFourTree<Integer> tfTree = new TwoFourTree<>(new Comparator<Integer>() {

            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        
        Random r = new Random();
        
        while(tfTree.size() < TEST_SIZE){
            tfTree.insert(i++);
        }
        
        tfTree.printTree(tfTree.root(), 0);
        
        for(int x = 0; x < tfTree.size(); x++){
            if(tfTree.remove(x) == null){
                System.out.println("fail " + Integer.toString(x));
            }
        }
    }
}
