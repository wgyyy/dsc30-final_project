public class Note extends Post{

    public Note(User poster, String header, String UID) {
        super(poster, header, UID);
    }

    public Note(User poster, String header, String text, String keyword, String PEID, String UID)  {
        super(poster, header, text, keyword, PEID, UID);
    }

    @Override
    public String getText(User u){
        return this.text;
    }

    @Override
    public String toString() {
        // TODO
        return null;
    }
}
