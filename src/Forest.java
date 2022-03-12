import javax.swing.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Locale;

public class Forest {

    // Connects the InternalNode with their specific keywords in the hashmap
    private HashMap<String, InternalNode> forest;
    int treeCount;

    protected class InternalNode {

        String key;
        ArrayList<Post> posts;
        ArrayList<InternalNode> children;

        /**
         * A constructor that initializes the InternalNode instance variables.
         *
         * @param key      Node's key
         */
        public InternalNode(String key, Post post) {
            this.key = key;
            this.posts = new ArrayList<>();
            this.posts.add(post);
            this.children = new ArrayList<>();
        }

        /**
         * A constructor that initializes InternalNode variables. Note: This constructor is
         * used when you want to add a key with no related information yet. In this
         * case, you must create an empty ArrayList for the node.
         *
         * @param key Node's key
         */
        public InternalNode(String key) {
            this.key = key;
            this.posts = new ArrayList<>();
            this.children = new ArrayList<>();
        }

        /**
         * Return the key
         *
         * @return The key
         */
        public String getKey() {
            return this.key;
        }


        /**
         * Return the linked list of the node
         *
         * @return The linked list of the node
         */
        public ArrayList<Post> getPosts() {
            return this.posts;
        }

        public ArrayList<InternalNode> getChildren() {
            return this.children;
        }

        public void addChildren(InternalNode node) {
            this.children.add(node);
        }

        public void setChildren(ArrayList<InternalNode> children) {
            this.children = children;
        }

        public boolean removeChildren(InternalNode node) {
            this.children.remove(node);
            return true;
        }

        /**
         * Setter for the linked list of the node
         *
         * @param newPosts New linked list
         */
        public void setPostsList(ArrayList<Post> newPosts) {
            this.posts = newPosts;
        }

        /**
         * Append new data to the end of the existing linked list of the node
         *
         * @param data New data to be appended
         */
        public void addNewPost(Post data) {
            this.posts.add(data);
        }

        /**
         * Remove 'data' from the linked list of the node and return true. If the linked
         * list does not contain the value 'data', return false.
         *
         * @param data Info to be removed
         * @return True if data was found, false otherwise
         */
        public boolean removePost(Post data) {
            if (this.posts.contains(data)) {
                this.posts.remove(data);
                return true;
            }
            return false;
        }
    }

    /**
     * Constructor that initialize the instance variable of the forest
     */
    public Forest() {
        this.forest = new HashMap<>();
        this.treeCount = 0;
        String[] allKeywords = new String[]{"tree", "midterm", "heap", "heap sort", "in place",
                "bst", "linked list", "queue",
                "priority queue", "SLL", "DLL", "hash table", "collision", "element", "hash function", "bloom filters",
                "probing", "linear probing", "quadratic probing", "double hashing", "rehash", "chaining"};
        for (String allKeyword : allKeywords) {
            this.insert(allKeyword);
        }
    }


    /**
     * Insert the specific key into the forest with InternalNode with empty posts
     *
     * @param key the key of the internal node
     */
    public void insert(String key) {
        String adjusted_key = key.toLowerCase(Locale.ROOT);
        InternalNode to_be_insert = new InternalNode(adjusted_key);
        this.forest.put(adjusted_key,to_be_insert);
        this.treeCount++;
    }

    /**
     * Insert the specific key and value pairs into the forest
     *
     * @param post insert the post according to the post's key
     */
    public void insert(Post post) {
        if (post.getKeyword() != null) {
            String adjusted_key = post.getKeyword().toLowerCase(Locale.ROOT);
            if (this.forest.containsKey(adjusted_key)) {
                this.getPosts(adjusted_key).add(post);
            } else {
                InternalNode to_be_insert = new InternalNode(adjusted_key, post);
                this.forest.put(adjusted_key, to_be_insert);
                this.treeCount++;
            }
        }
    }

    /**
     * Helper method. Returns the node with the given key.
     * If the key doesn’t exist in the forest, return null.
     *
     * @param key querying the internal node with this specific key
     */
    public InternalNode nodeLookUp(String key) {
        String adjusted_key = key.toLowerCase(Locale.ROOT);
        if (!this.forest.containsKey(adjusted_key)) {
            throw new IllegalArgumentException();
        }
        return this.forest.get(adjusted_key);
    }

    /**
     * Get the posts that relate to a specific key. If the key does
     * not exist in the forest, throw IllegalArgumentException
     *
     * @param key the key
     * @return the Arraylist of posts
     */
    public ArrayList<Post> getPosts(String key) {
        return this.nodeLookUp(key).posts;
    }

    /**
     * add Connection of more than one internal nodes by their keys
     *
     * @param parent the parent node's key
     * @param children the array of children node's keys
     */
    public void addConnection(String parent, String[] children) {
        InternalNode parentNode = this.nodeLookUp(parent);
        for (int i = 0; i < children.length; i++) {
            parentNode.addChildren(this.nodeLookUp(children[i]));
        }
    }

    /**
     * add connection for one internal nodes by their keys
     *
     * @param parent the key of the parent key
     * @param child the key of the child key
     */
    public void addConnection(String parent, String child) {
        this.nodeLookUp(parent).addChildren(this.nodeLookUp(child));
    }

    /**
     * query the connection between the internal node by traversing the edge
     * of the forest
     *
     * @param key the initial start point of
     * @return the children of that specific node
     */
    public String[] queryConnection(String key) {
        if (!this.forest.containsKey(key.toLowerCase(Locale.ROOT))) {
            return null;
        }
        String[] return_list = new String[this.nodeLookUp(key).children.size()];
        for (int i = 0; i < return_list.length; i++) {
            return_list[i] = this.nodeLookUp(key).children.get(i).getKey();
        }
        return return_list;
    }
}
