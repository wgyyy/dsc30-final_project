public class Tutor extends User{


    public Tutor(String PID, String username){
        super(PID, username);

    }

    public boolean answerQuestion(Post p, String response){
        if (p.getClass() != Question.class) {
            return false;
        } else {
            ((Question) p).answerQuestion(response);
            this.posts.add(p);
            this.numOfPostsAnswered++;
            return true;
        }
    }

    @Override
    public String displayName() {
        return "Tutor: "+this.username+", PID: "+this.PID;
    }

    @Override
    public boolean endorsePost(Post p){
        if (p.endorsedByCourseStaff) {
            return false;
        } else {
            p.endorsementCount++;
            p.poster.numOfEndorsement++;
            p.endorsedByCourseStaff = true;
            return true;
        }
    }

    @Override
    public boolean editPost(Post p, String newText) {
        p.text = newText;
        if (!this.posts.contains(p)) {
            this.numOfPostSubmitted++;
        }
        return true;
    }

    /**
     * get the top k urgent questions from a specific piazza
     *
     * @param pe the target Piazza
     * @param k the amount of urgent post that we want to get
     * @return the k urgent posts
     * @throws OperationDeniedException when the operation is denied
     */
    public Post[] getTopKUrgentQuestion(PiazzaExchange pe, int k) throws OperationDeniedException {
        if (k == 1){
            Post[] Question_array = new Post[]{pe.computeMostUrgentQuestion()};
            return Question_array;
        }
        return pe.computeTopKUrgentQuestion(k);
    }


}
