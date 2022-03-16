import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class PostTest {

    @Test
    public void getKeyword() {
        Student test_st = new Student("A16", "Gaoying Wang");
        Question test = new Question(test_st, "Final", "1");
        assertNull(test.getKeyword());
        Question test2 = new Question(test_st, "PA9", "What's the due date?", "PA9", "DSC30", "2");
        assertEquals("PA9", test2.getKeyword());
    }

    @Test
    public void getText() throws OperationDeniedException {
        Student test_st = new Student("A16", "Gaoying Wang");
        Question test = new Question(test_st, "Final", "1");
        assertEquals("",test.getText(test_st));
        Question test2 = new Question(test_st, "PA9", "What's the due date?", "PA9", "DSC30", "2");
        assertEquals("What's the due date?", test2.getText(test_st));
    }

    @Test
    public void getDate() {
        Student test_st = new Student("A16", "Gaoying Wang");
        Question test = new Question(test_st, "Final", "1");
        Question test2 = new Question(test_st, "PA9", "What's the due date?", "PA9", "DSC30", "2");
        LocalDate newtime = LocalDate.of(2022,02,10);
        test.setDate(newtime);
        assertEquals("2022-02-10", test.getDate().toString());
    }

    @Test
    public void getPoster() {
        Student test_st = new Student("A16", "Gaoying Wang");
        Question test = new Question(test_st, "Final", "1");
        assertEquals(test_st,test.getPoster());
        Question test2 = new Question(test_st, "PA9", "What's the due date?", "PA9", "DSC30", "2");
        assertEquals(test_st,test2.getPoster());
    }

    @Test
    public void editText() throws OperationDeniedException {
        Student test_st = new Student("A16", "Gaoying Wang");
        Question test = new Question(test_st, "Final", "1");
        test.editText("This is an empty post!");
        assertEquals("This is an empty post!",test.getText(test_st));
        Question test2 = new Question(test_st, "PA9", "What's the due date?", "PA9", "DSC30", "2");
        test2.editText("");
        assertEquals("", test2.getText(test_st));
    }

    @Test
    public void testToString() {
    }

    @Test
    public void compareTo() {
        Student test_st = new Student("A16", "Gaoying Wang");
        Student Tom = new Student("A131313", "Tom");
        Student Jack = new Student("A132", "Jack");
        Question test = new Question(test_st, "Final", "1");
        Tom.endorsePost(test);
        Question test2 = new Question(test_st, "PA9", "What's the due date?", "PA9", "DSC30", "2");
        assertEquals(1, test.compareTo(test2));
        Jack.endorsePost(test2);
        Tom.endorsePost(test);
        Jack.endorsePost(test2);
        LocalDate newtime = LocalDate.of(2022, 2,10);
        LocalDate current = LocalDate.of(2022, 3,9);
        test.setDate(newtime);
        test2.setDate(current);
        System.out.println(test.calculatePriority());
        assertEquals(9,test.compareTo(test2));
    }

    @Test
    public void calculatePriority() {
        Student test_st = new Student("A16", "Gaoying Wang");
        Student Tom = new Student("A131313", "Tom");
        Student Jack = new Student("A132", "Jack");
        Question test = new Question(test_st, "Final", "1");
        Tom.endorsePost(test);
        Question test2 = new Question(test_st, "PA9", "What's the due date?", "PA9", "DSC30", "2");
        assertEquals(1, test.calculatePriority());
        LocalDate newtime = LocalDate.of(2022, 2,10);
        LocalDate current = LocalDate.of(2022, 3,9);
        test.setDate(newtime);
        test2.setDate(current);
        assertEquals(10,test.calculatePriority());
        assertEquals(0,test2.calculatePriority());
    }
}