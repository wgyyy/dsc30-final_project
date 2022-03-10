import java.util.ArrayList;

public class Question extends Post {

    boolean answered;
    ArrayList<String> answers;

    /**
     * Constructor of Question
     * @param poster the poster of this question
     * @param header the summary of this question
     */
    public Question(User poster, String header, String UID) {
        super(poster, header, UID);
        answers = new ArrayList<String>();
    }

    /**
     * Overloaded constructor for Question. With more specifications
     *
     * @param poster The poster of this question
     * @param header the summary header of this question
     * @param question the question body
     * @param keyword the keywords of the question
     * @param PEID the unique identification ID of course ID
     */
    public Question(User poster, String header, String question, String keyword, String PEID, String UID){
        super(poster,header,question,keyword,PEID,UID);
        answers = new ArrayList<String>();
    }

    /**
     * getter method for text
     */
    public String getText(User u) throws OperationDeniedException {
        if (this.isPrivate == false) {
            return this.text;
        } else {
            if ((u != this.poster) && (u.getClass() != Tutor.class) && (u.getClass() != Instructor.class)) {
                throw new OperationDeniedException();
            } else {
                return this.text;
            }
        }
    }

    /**
     * Getting the status of this question
     *
     * @return the status of the question
     */
    public String getStatus(){
        if (this.answered == true) {
            return "Resolved";
        }
        return "Unresolved";
    }

    /**
     * To string method. Feel free to change it to anything
     * for debugging purposes
     */
    @Override
    public String toString() {
        return this.getKeyword();
    }

    /**
     * answer this post
     *
     * @param s the answer of this question
     * @return whether the action is successful
     */
    public boolean answerQuestion(String s) {
        this.answers.add(s);
        this.answered = true;
        return true;
    }
}
