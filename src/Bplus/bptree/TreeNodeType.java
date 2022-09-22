package Bplus.bptree;

import java.io.Serializable;

enum TreeNodeType implements Serializable {
    TREE_LEAF,
    TREE_INTERNAL_NODE,
    TREE_ROOT_INTERNAL,
    TREE_ROOT_LEAF,
    TREE_LEAF_OVERFLOW,
    TREE_LOOKUP_OVERFLOW
}
