package cs2110;

import com.sun.source.tree.Tree;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.w3c.dom.Node;

public class TreeVarTable implements VarTable{
    Set<String> z;
    Node root;
    int size;
    public class Node {
        Node left;
        Node right;
        String name;
        double value;

        public Node(String name, double value) {
            this.name = name;
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }

    public TreeVarTable() {
        root = null;
        size = 0;
    }

    public void add(String na, double d) {
        Node t = new Node(na, d);
        if (root == null) {
            root = new Node(na, d);
        }
        else {
            boolean res = addHelper(root, t.name, t.value);
        }
        size++;
    }

    public boolean addHelper(Node f, String n, double g) {
        int cmp = n.compareTo(f.name);
        if(cmp < 0) {
            if(f.left == null) {
                f.left = new Node(f.name, f.value);
                return true;
            }
            else {
                return addHelper(f.left, n, g);
            }
        }
        else if(cmp > 0) {
            if(f.right == null) {
                f.right = new Node(f.name, f.value);
                return true;
            }
            else {
                return addHelper(f.right, n, g);
            }
        }
        return false;
    }

    @Override
    public double get(String name) throws UnboundVariableException {
        Node no = helperGet(root, name);
        if(no == null) {
            throw new UnboundVariableException(name);
        }
        return no.value;
    }
    public Node helperGet(Node t, String nam) {
        int cmp = nam.compareTo(t.name);
        if (cmp < 0) {
            return helperGet(t.left, nam);
        } else if (cmp > 0) {
            return helperGet(t.right, nam);
        }
        else {
            return t;
        }
    }

    @Override
    public void set(String name, double value) {
        if(root == null) {
            add(name, value);
        }
        else if (contains(name)) {
            setHelper(root, name, value);
        }
        else {
            add(name, value);
        }

    }
    public Node setHelper(Node t, String na, double va) {
        int cmp = na.compareTo(t.name);
        if(cmp < 0) {
            t.left = setHelper(t.left, t.left.name, t.left.value);
        }
        else if(cmp >0) {
            t.right = setHelper(t.right, t.right.name, t.right.value);
        }
        else {
            t.value = va;
        }
        return t;
    }

    @Override
    public void unset(String name) {
//
    }

//    public Node helperUnset(Node t, String nam) {
//        int cmp = nam.compareTo(t.name);
//        if (cmp < 0) {
//            return helperGet(t.left, nam);
//        } else if (cmp > 0) {
//            return helperGet(t.right, nam);
//        }
//        else {
//            return t;
//        }
//    }

    @Override
    public boolean contains(String name) {
        if (root == null){
            return false;
        }
        return containsHelperM(root, name);
    }

    public boolean containsHelperM(Node t, String na) {
        int cmp = na.compareTo(t.name);
        if (cmp == 0) {
            return true;
        }
        if (cmp < 0 && t.left != null) {
            if(containsHelperM(t.left, na)) {
                return true;
            }
        }
        if(cmp > 0 && t.right != null) {
            if(containsHelperM(t.right, na)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Set<String> names() {
        return z;
    }
}
