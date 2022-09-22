package Interfaces;

public interface TucTree {

    public void insert(int key);

    public boolean search(int key);

    public int[] delete(int key);

    public <T extends TucTreeNode<?,Integer>> boolean isValidTree();

    Object ResetTree();
}
