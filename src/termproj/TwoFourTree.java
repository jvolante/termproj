/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package termproj;

import java.util.Random;
import java.util.Stack;

/**
 * An implementation of a 2-4 tree
 * @version 1
 * @author Jackson
 */
public class TwoFourTree <Value>{
    private static final int INVALID_INDEX = -1;
    
    private int size = 0;
    private Comparator valueComparator;
    private TwoFourTreeNode root = null;
    
    /**
     * Constructs a new 2-4 tree with a comparator to compare keys.
     * 
     * @param comp the comparator used to sort the keys.
     */
    public TwoFourTree(Comparator comp){
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
        TwoFourTreeNode current = null;
        Value result;
        int index = node.hasValue(value);
        
        //if the value is found in the tree
        if(index != INVALID_INDEX){
            result = (Value)node.getElement(index);
            
            //last element removal
            if(size == 1){
                root = null;
            //if not a leaf get the in order successor
            }else if(!node.isLeaf()){
                current = node.getChild(index + 1);
                while(!current.isLeaf()){
                    current = current.getChild(0);
                }
                
                //replace the removed element
                node.replaceElement(index, current.getElement(0));

                //remove the element we just used to replace
                current.removeElement(0);
            }else{
                node.removeElement(index);
                current = node;
            }
            
            //if we emptied a leaf, fix it
            if(current != null){
                fixAfterRemoval(current);
            }
            
            size--;
        } else{
            throw new ElementNotFoundException();
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
                leftTransfer(current, leftSibling);
            }else{
                //if not try the right sibling
                TwoFourTreeNode rightSibling = current.getRightSibling();

                if(rightSibling != null && rightSibling.getNumElements() != 1){
                    rightTransfer(current, rightSibling);
                }else{
                    //if not check left fusion
                    if(leftSibling != null){
                        leftFusion(current);
                    //if nothing else do right fusion
                    }else{
                        rightFusion(current);
                    }
                    
                    current = current.parent;

                    //if we are the root and have underflowed
                    //make our one child the new root
                    if(current == root && current.getNumElements() == 0){
                        TwoFourTreeNode leftChild = current.getChild(0);
                        leftChild.setParent(null);
                        root = leftChild;
                    }else{
                        fixAfterRemoval(current);
                    }
                }
            }
        }
    }
    
    private void leftTransfer(TwoFourTreeNode empty, TwoFourTreeNode sibling){
        TwoFourTreeNode parent = empty.getParent();
        int emptyIndex = parent.whatChildIsThis(empty);
        int leftIndex = emptyIndex - 1;
        
        empty.insertElement(0, parent.getElement(leftIndex));
        parent.replaceElement(leftIndex, sibling.getElement(
                sibling.getNumElements() - 1));
        empty.insertChild(0, sibling.getChild(sibling.getNumElements()));
        
        sibling.nullChild(sibling.getNumElements());
        sibling.removeElement(sibling.getNumElements() - 1);
    }
    
    private void rightTransfer(TwoFourTreeNode empty, TwoFourTreeNode sibling){
        TwoFourTreeNode parent = empty.getParent();
        int emptyIndex = parent.whatChildIsThis(empty);
        
        empty.insertElement(0, parent.getElement(emptyIndex));
        parent.replaceElement(emptyIndex, sibling.getElement(0));
        
        empty.insertChild(1, sibling.getChild(0));
        
        sibling.removeChild(0);
        
        sibling.removeElement(0);
    }
    
    private void leftFusion(TwoFourTreeNode empty){
        TwoFourTreeNode parent = empty.getParent();
        int emptyIndex = parent.whatChildIsThis(empty);
        
        empty.insertElement(0, parent.getElement(emptyIndex - 1));
        
        mergeWithLeftSibling(empty);
        
        parent.removeChild(emptyIndex - 1);
        parent.removeElement(emptyIndex - 1);
    }
    
    private void rightFusion(TwoFourTreeNode empty){
        TwoFourTreeNode parent = empty.getParent();
        int emptyIndex = parent.whatChildIsThis(empty);
        
        empty.insertElement(0, parent.getElement(emptyIndex));
        
        mergeWithRightSibling(empty);
        
        parent.removeChild(emptyIndex + 1);
        parent.removeElement(emptyIndex);
    }
    
    private void mergeWithLeftSibling(TwoFourTreeNode rightSibling){
        TwoFourTreeNode parent = rightSibling.getParent();
        TwoFourTreeNode leftSibling = parent.getChild(
                parent.whatChildIsThis(rightSibling) - 1);
        
        //move children
        for(int i = leftSibling.getNumElements(); i >= 0; i--){
            rightSibling.insertChild(0, leftSibling.getChild(i));
        }
        
        //move elements
        for(int i = leftSibling.getNumElements() - 1; i >= 0; i--){
            rightSibling.insertElement(0, leftSibling.getElement(i));
        }
    }
    
    private void mergeWithRightSibling(TwoFourTreeNode leftSibling){
        TwoFourTreeNode parent = leftSibling.getParent();
        TwoFourTreeNode rightSibling = parent.getChild(
                parent.whatChildIsThis(leftSibling) + 1);
        
        //move children
        for(int i = 0; i < rightSibling.getNumElements() + 1; i++){
            leftSibling.insertChild(leftSibling.getNumElements() + i, 
                    rightSibling.getChild(i));
        }
        
        //move elements
        for(int i = 0; i < rightSibling.getNumElements(); i++){
            leftSibling.insertElement(leftSibling.getNumElements(), 
                    rightSibling.getElement(i));
        }
    }
    
    public void insert(Value value){
        if(isEmpty()){
            root = new TwoFourTreeNode(null);
            root.insertSorted(value);
        }else{
            TwoFourTreeNode current = findNode(root, value);

            //find the appropriate leaf and insert the value
            while(current.hasValue(value) != INVALID_INDEX 
                    && !current.isLeaf()){
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
        node.nullChild(4);
        node.nullChild(3);
        
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
            //shift up each of the elements until we get to 
            //the index of insertion
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
            if(newChild != null){
                newChild.setParent(this);
            }
            //shift up each of the elements until we get to 
            //the index of insertion
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
        public void nullChild(int index){
            children[index] = null;
        }
        
        public void removeChild(int index){
            //shift over each element starting with the index
            for(int i = index; i < numElements; i++){
                children[i] = children[i+1];
            }
            children[numElements] = null;
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
        * @return if key is found return its index in contents 
        *         if not return INVALID_INDEX.
        */
       public int hasValue(Value value){
           //loop over each one of the contents and check if it has the key
           Value current;
           for(int i = 0; i < numElements; i++){
               current = (Value)elements[i];
               
               if(valueComparator.isEqual(current, value)){
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
           if(newChild != null){
                newChild.setParent(this);
           }
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
                //if the value is greater than all the elements
                //return the last child
                if(i == numElements){
                    result = (TwoFourTreeNode)children[i];
                    break;
                }
                current = (Value)elements[i];
                if (valueComparator.isGreaterThanOrEqualTo(current, value)){
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
         * gets the index of the specified node 
         * in this node's array of children.
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
            
            if(leftIndex < 0){
                return null;
            }else{
                return parent.getChild(leftIndex);
            }
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
                if(valueComparator.isGreaterThanOrEqualTo(
                        (Value)elements[i], value)){
                   return i;
                }
            }
            return numElements;
        }
    }
    
    public static void main(String[] args){
        final int TEST_SIZE = 1000;
        int i = 0;
        TwoFourTree<Integer> tfTree = new TwoFourTree<>(
                new IntegerComparator());
        
        Stack<Integer> s = new Stack();
        Random r = new Random();
        
        while(tfTree.size() < TEST_SIZE){
            i = r.nextInt(100);
            tfTree.insert(i);
            s.push(i);
        }
        
        tfTree.printTree(tfTree.root(), 0);
        while(s.size() > 0){
            int x = s.pop();
            if(tfTree.remove(x) == null){
                System.out.println("fail " + Integer.toString(x));
                tfTree.printTree(tfTree.root(), 0);
            }
            if (s.size() < 20){
                System.out.println("removing "+x);
                tfTree.printTree(tfTree.root(), 0);
            }
            System.out.flush();
        }
    }
}
