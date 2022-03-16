public class Student extends User{

    public Student(String PID, String username) {
        super(PID, username);
    }

    public boolean answerQuestion(Post p, String response) {
        if ((response.length() > 50) || (p.getClass() != Question.class) ||
                ((p.isPrivate) && (p.getPoster() != this))) {
            return false;
        } else {
            ((Question) p).answerQuestion(response);
            this.posts.add(p);
            this.numOfPostsAnswered++;
            return true;
        }
    }

    @Override
    public boolean endorsePost(Post p) {
        if (p.isPrivate) {
            if (p.getPoster() ==  this) {
                p.endorsementCount++;
                return true;
            }else {
                return false;
            }
        }
        p.endorsementCount++;
        return true;
    }

    @Override
    public boolean editPost(Post p, String newText) {
        if (p.isPrivate) {
            if (p.poster == this) {
                p.text = newText;
                return true;
            } else {
                return false;
            }
        } else {
            p.text = newText;
            if (!this.posts.contains(p)) {
                this.numOfPostSubmitted++;
            }
            return true;
        }
    }

    @Override
    public String displayName() {
        return "Student: "+this.username+", PID: "+this.PID;
    }
}
