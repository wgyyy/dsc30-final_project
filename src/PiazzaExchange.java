import sun.rmi.runtime.Log;

import java.time.LocalDate;
import java.util.*;

public class PiazzaExchange {

    String courseID;
    Instructor instructor;
    ArrayList<User> users;
    ArrayList<Post> posts;
    ArrayList<Post> unanswered;
    String status;
    boolean selfEnroll;
    private Forest keywordForest;

    private static final String STATS_STRING = "%s submitted %d posts, answered %d posts, received %d endorsements\n";

    /**
     * Constructor of the PiazzaExchange.
     *
     * @param instructor the instructor of this class
     * @param courseID The course ID
     * @param selfEnroll whether the class allow self enrolling or not.
     */
    public PiazzaExchange(Instructor instructor, String courseID, boolean selfEnroll) {
        this.instructor = instructor;
        this.courseID = courseID;
        this.selfEnroll = selfEnroll;
        this.status = "inactive";
        this.users = new ArrayList<>();
        this.posts = new ArrayList<>();
        this.unanswered = new ArrayList<>();
        this.keywordForest = new Forest();
        this.initializeForest();
    }

    //is there a reason why we don't combine these two constructors?
    //by default, selfEnroll is false, and we're setting DSC30 as default courseID

    /**
     * Constructor of the PiazzaExchange with a roster of user.
     *
     * @param instructor the instructor who create this piazza
     * @param roster the list of Users that will be included in this piazza
     */
    public PiazzaExchange(Instructor instructor, ArrayList<User> roster) {
        this.instructor = instructor;
        this.selfEnroll = false;
        this.users = roster;
        this.courseID = "DSC30";
        this.status = "inactive";
        this.posts = new ArrayList<>();
        this.unanswered = new ArrayList<>();
        this.keywordForest = new Forest();
        this.initializeForest();
    }

    public Forest getKeywordForest() {
        return this.keywordForest;
    }

    /**
     * Query for the top two endorsed posts
     *
     * @return two posts that has the highest endorsed
     */
    public Post[] computeTopTwoEndorsedPosts() {
        Post[] return_list = new Post[2];
        if (this.posts.size() == 0) {
            return return_list;
        } if (this.posts.size() == 1) {
            return_list[0] = this.posts.get(0);
            return  return_list;
        }
        return_list[0] = this.posts.get(0);
        if (this.posts.get(1).endorsementCount > return_list[0].endorsementCount) {
            return_list[0] = this.posts.get(1);
            return_list[1] = this.posts.get(0);
        } else {
            return_list[1] = this.posts.get(1);
        }
        for (int i = 2; i < this.posts.size(); i++) {
            if (this.posts.get(i).endorsementCount > return_list[0].endorsementCount) {
                return_list[1] = return_list[0];
                return_list[0] = this.posts.get(i);
            } else {
                if (this.posts.get(i).endorsementCount > return_list[1].endorsementCount) {
                    return_list[1] = this.posts.get(i);
                }
            }
        }
        return return_list;
    }

    
    /* helper method for getTopStudentContributions() */

    private int getStudentContributions(User u) {
        return u.numOfPostSubmitted + u.numOfPostsAnswered + u.numOfEndorsement;
    }

    /**
     * get recent-30 day stats(including today), where index i corresponds to ith day away from current day
     *
     * @return integer array with the daily post status
     */
    public int[] computeDailyPostStats() {
        int[] return_list = new int[30];
        int count;
        LocalDate now = LocalDate.now();
        for (int i = 0; i < 30; i++) {
            count = 0;
            for (int n = 0; n < this.posts.size(); n++) {
                if (this.posts.get(n).getDate().until(LocalDate.now()).getDays() == i) {
                    count++;
                }
            }
            return_list[i] = count;
        }
        return return_list;
    }

    /**
     * get the month post stats for the last 12 month
     *
     * @return integer array that indicates the monthly status.
     */
    public int[] computeMonthlyPostStats(){
        int[] return_list = new int[12];
        int count;
        for (int i = 0; i < 12; i++) {
            count =0;
            for (int n = 0; n < this.posts.size(); n++) {
                if (this.posts.get(n).getDate().getMonthValue() == (i + 1)) {
                    count++;
                }
            }
            return_list[i] = count;
        }
        return return_list;
    }

    /**
     * Activate the pizza. This action should be done by instructor only.
     *
     * @param u the instructor who wish to activate the class
     * @return successfulness of the action call
     */
    public boolean activatePiazza(User u){
        if ((u.getClass() == Instructor.class) && this.status.equals("inactive")) {
            this.status = "active";
            return true;
        }
        return false;
    }

    /**
     * Activate the pizza. This action should be done by instructor only.
     *
     * @param u the instructor who wish to activate the class
     * @return successfulness of the action call
     */
    public boolean deactivatePiazza(User u){
        if ((u.getClass() == Instructor.class) && this.status.equals("active")) {
            this.status = "inactive";
            this.selfEnroll = false;
            return true;
        }
        return false;
    }

    /**
     * Enroll the user to this PiazzaExchange. If the self enroll is disabled, only
     * instructor and tutor can request a new enrollment action.
     *
     * @param requester the requester of enrollment
     * @param u the user to enroll
     * @return successfulness of the action call
     */
    public boolean enrollUserToDatabase(User requester, User u){
        if (this.users.contains(u) || Objects.equals(this.status, "inactive")){
            return false;
        } else {
            if (requester == u) {
                if (!this.selfEnroll) {
                    return false;
                }
                this.users.add(u);
                u.courses.add(this);
                return true;
            }
            if ((requester.getClass() == Instructor.class) || (requester.getClass() == Tutor.class)) {
                this.users.add(u);
                u.courses.add(this);
                return true;
            }
            return false;
        }
    }

    /**
     * Enroll this user to PiazzaExchange
     *
     * @param u the user to enroll
     * @return successfulness of the action call
     */
    public boolean enrollUserToDatabase(User u){
        return this.enrollUserToDatabase(u,u);
    }

    ////////////// BEGIN BENCHMARKED METHOD /////////////

    /**
     * Given a specific posts, add this post to the database
     *
     * @param u The user that initiate this add-post action
     * @param p the post that we are going to add to the database
     * @throws OperationDeniedException when the action is not allowed
     */
    public void addPostToDatabase(User u, Post p) throws OperationDeniedException {
        if ((!this.users.contains(u)) || (this.status=="inactive")) {
            throw new OperationDeniedException();
        }
        this.posts.add(p);
        u.posts.add(p);
        u.numOfPostSubmitted++;
        try{
            this.keywordForest.nodeLookUp(p.getKeyword()).addNewPost(p);
        }
        catch (IllegalArgumentException | NullPointerException ignored) {
        }
    }

    /**
     * Get the post posted by this user that has the specific keyword
     *
     * @param u the poster of the post
     * @param keyword the keywords that we are going to query on
     * @return the post array that contains every single post that has the keyword
     */
    public Post[] retrievePost(User u, String keyword){
        ArrayList<Post> record = new ArrayList<>();
        for (int i = 0; i<this.posts.size(); i++) {
            if ((u.posts.contains(this.posts.get(i))) && (Objects.equals(this.posts.get(i).getKeyword(), keyword))) {
                record.add(this.posts.get(i));
            }
        }
        Post[] return_list = new Post[record.size()];
        int count = 0;
        while (!record.isEmpty()) {
            return_list[count] = record.get(0);
            record.remove(0);
            count++;
        }
        return return_list;
    }

    /**
     * Get the post that has the specific keyword
     *
     * @param keyword the keyword that we are searching on
     * @return the post array that contains every single post that has the keyword
     */
    public Post[] retrievePost(String keyword){
        ArrayList<Post> record = new ArrayList<>();
        for (int i = 0; i<this.posts.size(); i++) {
            if (Objects.equals(this.posts.get(i).getKeyword(), keyword)) {
                record.add(this.posts.get(i));
            }
        }
        Post[] return_list = new Post[record.size()];
        int count = 0;
        while (!record.isEmpty()) {
            return_list[count] = record.get(0);
            record.remove(0);
            count++;
        }
        return return_list;
    }

    /**
     * Get the post with specific poster
     *
     * @param u the poster of posts
     * @return the post array that contains every single post that has specified poster u
     */
    public Post[] retrievePost(User u) {
        ArrayList<Post> record = new ArrayList<>();
        for (int i = 0; i<this.posts.size(); i++) {
            if (u.posts.contains(this.posts.get(i))) {
                record.add(this.posts.get(i));
            }
        }
        Post[] return_list = new Post[record.size()];
        int count = 0;
        while (!record.isEmpty()) {
            return_list[count] = record.get(0);
            record.remove(0);
            count++;
        }
        return return_list;
    }

    /**
     * delete the post from the PE. User should be either the creator of the post or a course staff
     * return whether the post got successfully deleted or not
     *
     * @param u the user who initiate this action
     * @param p the post to delete
     * @return whether the action is successful
     * @throws OperationDeniedException when the action is denied
     */
    public boolean deletePostFromDatabase(User u, Post p) throws OperationDeniedException {
        if (!(u.getClass() == Instructor.class)) {
            throw new OperationDeniedException();
        }
        if (!this.posts.contains(p)) {
            return false;
        }
        this.posts.remove(p);
        return true;
    }

    /**
     * Compute the most urgent question in the unanswered post DS
     * for future answering.
     *
     * @return the Post with the highest urgency rating
     */
    public Post computeMostUrgentQuestion() {
        int most_urgent = 0;
        for (int i = 0; i < this.unanswered.size(); i++) {
            if(this.unanswered.get(most_urgent).compareTo(this.unanswered.get(i)) > 0) {
                most_urgent = i;
            }
        }
        return this.unanswered.get(most_urgent);
    }

    /**
     * Compute the top K urgent question from the unanswered post DS
     *
     * @param k the number of unanswered post that we want to have
     * @return the post array that is sorted by the urgency of the post
     * @throws OperationDeniedException when the operation is denied
     */
    public Post[] computeTopKUrgentQuestion(int k) throws OperationDeniedException{
        if (k > this.posts.size()) {
            throw new OperationDeniedException();
        }
        Post[] return_list = new Post[k];
        return_list[0] = this.posts.get(0);
        int count = 1;
        for (int i = 1; i < this.posts.size(); i++) {
            /*
            loop through the posts list
             */
            int count2 = count;
            while (return_list[count2].compareTo(this.posts.get(i)) < 0) {
                count2--;
                if (count2 < 0) {
                    break;
                }
            }
            for (int m = count; m > count2; m--) {
                return_list[m] = return_list[m-1];
            }
            return_list[count2] = this.posts.get(i);
            if (count < k) {
                count++;
            }
        }
        return  return_list;
    }

    /**
     * answer the post. Removed from the unanswered post DataStructure
     *
     * @param u the answerer of the post
     * @param p the post that u is trying to answer
     * @param response the response to be appended to the post as an answer
     * @return the Post instance of the answered post
     * @throws OperationDeniedException when the operation is denied
     */
    public Post answerQuestion(User u, Post p, String response) throws OperationDeniedException{
        if (!this.posts.contains(p)) {
            throw new OperationDeniedException();
        }
        this.unanswered.remove(p);
        u.answerQuestion(p,response);
        return p;
    }

    ////////////// END BENCHMARKED METHOD /////////////

    /**
     *
     * @param u
     * @return
     */
    public String viewStats(User u){
        if (u.getClass() == Student.class) {
            return u.username + " submitted " + u.numOfPostSubmitted
                    + " posts, answered " + u.numOfPostsAnswered
                    + " posts, received " + u.numOfEndorsement + " endorsements.";
        }
        else {
            String return_statement = "";
            for (int i = 0; i < this.users.size(); i++) {
                if (this.users.get(i).getClass() == Student.class) {
                    return_statement = return_statement + this.users.get(i).username
                            + " submitted "
                            + this.users.get(i).numOfPostSubmitted + " posts, answered "
                            + this.users.get(i).numOfPostsAnswered
                            + " posts, received " + this.users.get(i).numOfEndorsement +
                            " endorsements.\n";
                }
            }
            return return_statement;
        }
    }

    /**
     * Retrieve all the posts from this piazza
     *
     * @param u the user who initiate this action
     * @return the posts array that contains every single post
     *      in this piazza
     */
    public Post[] retrieveLog(User u){
        ArrayList<Post> return_l = new ArrayList<>();
        for (int i = 0; i < u.posts.size(); i++) {
            if (this.posts.contains(u.posts.get(i))) {
                return_l.add(u.posts.get(i));
            }
        }
        ArrayList<Post> return_list = new ArrayList<>();
        while (!return_l.isEmpty()) {
            Post earliest = (Post) return_l.get(0);
            for (int i = 0; i < return_l.size(); i ++) {
                Post current  = (Post) return_l.get(i);
                if (current.getDate().isAfter(earliest.getDate())) {
                    earliest = current;
                }
            }
            return_list.add(earliest);
            return_l.remove(earliest);
        }
        return return_list.toArray(new Post[0]);
    }

    //If the length > 10, students only be able to access the first 10 posts right?

    /**
     * Retrieve posts log with specified length
     *
     * @param u the user who initiate this action
     * @param length of the posts that is allowed to fetch
     * @return the posts array that satisfy the conditions
     */
    public Post[] retrieveLog(User u, int length){
        ArrayList<Post> return_l = new ArrayList<>();
        for (int i = 0; i < u.posts.size(); i++) {
            if (this.posts.contains(u.posts.get(i))) {
                return_l.add(u.posts.get(i));
            }
        }
        ArrayList<Post> return_list = new ArrayList<>();
        int count = 0;
        while ((!return_l.isEmpty()) && (count < length)) {
            Post earliest = (Post) return_l.get(0);
            for (int i = 0; i < return_l.size(); i ++) {
                Post current  = (Post) return_l.get(i);
                if (current.getDate().isAfter(earliest.getDate())) {
                    earliest = current;
                }
            }
            return_list.add(earliest);
            return_l.remove(earliest);
            count++;
        }
        return return_list.toArray(new Post[0]);
    }

    private String[] getEleMultipleIndex(String[] arr, int[] indexes) {
        String[] output = new String[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            output[i] = arr[indexes[i]];
        }
        return output;
    }

    /**
     * Helper method that initialize the semantic connection
     * of the keywordForest.
     */
    private void initializeForest() {
        String[] allKeywords = new String[]{"tree", "midterm", "heap", "heap sort", "in place",
                "bst", "linked list", "queue",
                "priority queue", "SLL", "DLL", "hash table", "collision", "element", "hash function", "bloom filters",
                 "probing", "linear probing", "quadratic probing", "double hashing", "rehash", "chaining"};
        int[][] childrenIndex = {
                {2, 5}, // i = 0
                {5, 6},
                {7, 3},
                {4},
                {},
                {}, // i = 5
                {9, 10},
                {8},
                {},
                {},
                {}, // i = 10
                {12, 13, 14, 15},
                {16},
                {},
                {19, 20, 21},
                {}, // i = 15
                {17, 18},
                {},
                {},
                {},
                {}, // i = 20
                {}
        };
        for (int i = 0; i < allKeywords.length; i++) {
            keywordForest.addConnection(allKeywords[i], getEleMultipleIndex(allKeywords, childrenIndex[i]));
        }
    }

    /**
     * Sort by the title, return the first k occurrences of the posts with the keyword
     * Forest of tree and storing key using HashMap.
     *
     * @param keyword The keyword that we initiate the starting point of the search
     * @param k the number of similar post that we are querying
     */
    public Post[] computeKSimilarPosts(String keyword, int k) {
        ArrayList<Post> record = new ArrayList<>();
        ArrayList<Forest.InternalNode> list = new ArrayList<>();
        list.add(keywordForest.nodeLookUp(keyword));
        while (!list.isEmpty()) {
            for (int i = 0; i < list.get(0).posts.size(); i++) {
                if (list.get(0).posts.get(i) != null) {
                    record.add(list.get(0).posts.get(0));
                }
            }
            list.addAll(list.get(0).children);
            list.remove(0);
        }
        Post[] return_list = new Post[k];
        if (record.size() > k) {
            for (int i = 0; i < return_list.length; i++) {
                return_list[i] = record.get(i);
            }
            return return_list;
        }
        return record.toArray(new Post[k]);
    }

    /**
     * Sort by the title, return the first k occurrences of the posts with the keyword
     * Forest of tree of BST and store key using HashMap.
     */
    public Post[] computeKSimilarPosts(String keyword, int k, int level) {
        ArrayList<Post> record = new ArrayList<>();
        ArrayList<Forest.InternalNode> list = new ArrayList<>();
        list.add(keywordForest.nodeLookUp(keyword));
        while (!list.isEmpty()) {
            record.addAll(list.get(0).posts);
            list.addAll(list.get(0).children);
            list.remove(0);
        }
        return record.toArray(new Post[k]);
    }

    /**
     * describes basic course info, user and post status
     * NOT GRADED, for your own debugging purposes
     */
    public String toString(){
        // TODO
        return null;
    }

}
