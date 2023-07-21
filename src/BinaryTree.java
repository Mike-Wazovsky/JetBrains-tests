class BinaryTree {

    int value;
    BinaryTree left;
    BinaryTree right;

    static boolean contentsSimilar(BinaryTree lhv, BinaryTree rhv) {
        if (lhv == null & rhv == null)
            return true;
        if (lhv == null)
            return false;
        if (rhv == null)
            return false;

        // Now then both lhv and rhv are not Null
        return ((lhv.value == rhv.value) &&
                (contentsSimilar(lhv.left, rhv.left) && contentsSimilar(lhv.right, rhv.right)) ||
                (contentsSimilar(lhv.left, rhv.right) && contentsSimilar(lhv.right, rhv.left))
                );
    }


    // You can consider that these methods are implemented // and you can use them if needed
//    boolean contains(int value);
//    boolean add(int value);
//    boolean remove(int value);
//    int size();
}