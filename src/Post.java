import java.time.LocalDate;
import java.util.ArrayList;

public abstract class Post implements Comparable<Post> {

    String UID;
    String parentPEID;
    int endorsementCount;
    boolean endorsedByCourseStaff;
    private String header;
    protected String text;
    boolean isPrivate;
    User poster;
    LocalDate date;
    int priority;
    private String keyword;

    /**
     * Constructor for Post
     *
     * @param poster the poster of the post
     * @param header the header of the post
     */
    public Post(User poster, String header, String UID) {
        this.poster = poster;
        this.header = header;
        this.UID = UID;
        this.text = "";
        this.keyword = null;
        this.parentPEID = null;
        this.priority = 0;
        this.date = LocalDate.now();
    }

    /**
     * Overloaded constructor for Post
     */
    public Post(User poster, String header, String text, String keyword, String PEID, String UID) {
        this.poster = poster;
        this.header = header;
        this.UID = UID;
        this.text = text;
        this.keyword = keyword;
        this.parentPEID = PEID;
        this.priority = 0;
        this.date = LocalDate.now();
    }

    /**
     * Getter method of the keyword of the post
     * @return the keyword of the post
     */
    public String getKeyword() {
        return this.keyword;
    }

    /**
     * Getter method of the text of the post
     * @return the text of the post
     */
    public abstract String getText(User u) throws OperationDeniedException;

    public LocalDate getDate() {
        return  this.date;
    }

    /**
     * Set the date of the post to the provided new date
     *
     * @param newDate the new date we are setting the post to
     */
    public void setDate(LocalDate newDate) {
        this.date = newDate;
    }

    public User getPoster() {
        return  this.poster;
    }

    public void editText(String text) {
        this.text = text;
    }

    public String toString() {
        String rs = ""+this.getDate();
        return rs;
    }

    /**
     * Compare two posts by their priority
     *
     * @param other the other post that we are comparing
     * @return whether this post is larger than the other post
     */
    public int compareTo(Post other){
        int this_priority = this.calculatePriority();
        int other_priority = other.calculatePriority();
        return this_priority - other_priority;
    }

    public int calculatePriority() {
        int p = this.endorsementCount+(int)(this.date.until(LocalDate.now()).getDays()/3.0);
        this.priority = p;
        return p;
    }

}
