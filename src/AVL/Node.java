package AVL;

import Interfaces.Statistics.StatisticsInterface;
import Interfaces.TucTreeNode;

// Java program for insertion in AVL Tree
class Node implements TucTreeNode<Node, Integer>, StatisticsInterface {
    int key, height;
    Node left, right;

    Node(int d) {
        Increment();
        key = d;
        height = 1;
    }

    /**
     * Getter for the left node
     *
     * @return the left node
     */
    @Override
    public Node getLeftNode() {
        return left;
    }

    /**
     * Getter for the right node
     *
     * @return the right node
     */
    @Override
    public Node getRightNode() {
        return right;
    }

    /**
     * Getter for the parent node
     *
     * @implNote This method is not implemented
     * @throws UnsupportedOperationException Unsupported operation
     * @return the parent node
     */
    @Override
    public Node getParentNode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Getter for the key
     *
     * @return the key
     */
    @Override
    public Integer getKey() {
        return key;
    }

    /**
     * Setter for the left node
     *
     * @param node the left node
     */
    @Override
    public void setLeftNode(Node node) {
        left = node;
    }

    /**
     * Setter for the right node
     *
     * @param node the right node
     */
    @Override
    public void setRightNode(Node node) {
        right = node;
    }

    /**
     * Setter for the parent node
     *
     * @implNote This method is not implemented
     * @throws UnsupportedOperationException Unsupported operation
     * @param node the parent node
     */
    @Override
    public void setParentNode(Node node) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Setter for the key
     *
     * @param key the key
     */
    @Override
    public void setKey(Integer key) {
        Increment();
        this.key = key;
    }
}