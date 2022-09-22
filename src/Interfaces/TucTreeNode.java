package Interfaces;

public interface TucTreeNode<T  extends TucTreeNode,K> {

    /**
     * Getter for the left node
     * @return the left node
     */
    public T getLeftNode();

    /**
     * Getter for the right node
     * @return the right node
     */
    public T getRightNode();

    /**
     * Getter for the parent node
     * @return the parent node
     */
    public T getParentNode();

    /**
     * Getter for the key
     * @return the key
     */
    public K getKey();

    /**
     * Setter for the left node
     * @param node the left node
     */
    public void setLeftNode(T node);

    /**
     * Setter for the right node
     * @param node the right node
     */
    public void setRightNode(T node);

    /**
     * Setter for the parent node
     * @param node the parent node
     */
    public void setParentNode(T node);

    /**
     * Setter for the key
     * @param key the key
     */
    public void setKey(K key);

}
