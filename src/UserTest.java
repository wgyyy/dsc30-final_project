import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Locale;

import static org.junit.Assert.*;

public class UserTest {

    Student me;
    Student Tom;
    Student Jack;
    Question test;
    Question test2;
    Instructor Dr_K;
    PiazzaExchange DSC;

    @Before
    public void set_up() {
        Student me = new Student("A16", "Gaoying Wang");
        Student Tom = new Student("A131313", "Tom");
        Student Jack = new Student("A132", "Jack");
        Question test = new Question(me, "Final", "1");
        Question test2 = new Question(me, "PA9", "What's the due date?", "PA9", "DSC30", "2");
        Instructor Dr_K = new Instructor("Dr.K");
        PiazzaExchange DSC = new PiazzaExchange(Dr_K, "DSC30", true);
    }

    @Test
    public void enrollClass() {
        Student me = new Student("A16", "Gaoying Wang");
        Student Tom = new Student("A131313", "Tom");
        Student Jack = new Student("A132", "Jack");
        Question test = new Question(me, "Final", "1");
        Question test2 = new Question(me, "PA9", "What's the due date?", "PA9", "DSC30", "2");
        Instructor Dr_K = new Instructor("Dr.K");
        PiazzaExchange DSC = new PiazzaExchange(Dr_K, "DSC30", true);
        me.enrollClass(DSC);
        assertEquals(false, me.courses.contains(DSC));
        assertEquals(false, DSC.users.contains(me));
        DSC.activatePiazza(Dr_K);
        me.enrollClass(DSC);
        assertEquals(true, me.courses.contains(DSC));
        assertEquals(true, DSC.users.contains(me));
    }

    @Test (expected = OperationDeniedException.class)
    public  void  editPostODE() throws OperationDeniedException {
        Student me = new Student("A16", "Gaoying Wang");
        Student Tom = new Student("A131313", "Tom");
        Student Jack = new Student("A132", "Jack");
        Question test = new Question(me, "Final", "1");
        Question test2 = new Question(me, "PA9", "What's the due date?", "PA9", "DSC30", "2");
        Instructor Dr_K = new Instructor("Dr.K");
        PiazzaExchange DSC = new PiazzaExchange(Dr_K, "DSC30", true);
        DSC.activatePiazza(Dr_K);
        me.enrollClass(DSC);
        Tom.addPost(DSC,test);
    }


    @Test
    public void editPost() throws OperationDeniedException {
        Student me = new Student("A16", "Gaoying Wang");
        Student Tom = new Student("A131313", "Tom");
        Student Jack = new Student("A132", "Jack");
        Question test = new Question(me, "Final", "1");
        Question test2 = new Question(me, "PA9", "What's the due date?", "PA9", "DSC30", "2");
        Instructor Dr_K = new Instructor("Dr.K");
        PiazzaExchange DSC = new PiazzaExchange(Dr_K, "DSC30", true);
        DSC.activatePiazza(Dr_K);
        me.enrollClass(DSC);
        me.addPost(DSC,test);
        me.editPost(test,"This is empty!");
        assertEquals("This is empty!", test.getText(me));
        Dr_K.editPost(test,"NoNoNo");
        assertEquals("NoNoNo", test.getText(Dr_K));
    }

    @Test
    public void addPost() throws OperationDeniedException {
        Student me = new Student("A16", "Gaoying Wang");
        Student Tom = new Student("A131313", "Tom");
        Student Jack = new Student("A132", "Jack");
        Question test = new Question(me, "Final", "1");
        Question test2 = new Question(me, "PA9", "What's the due date?", "PA9", "DSC30", "2");
        Instructor Dr_K = new Instructor("Dr.K");
        PiazzaExchange DSC = new PiazzaExchange(Dr_K, "DSC30", true);
        DSC.activatePiazza(Dr_K);
        me.enrollClass(DSC);
        me.addPost(DSC,test);
        assertEquals(true,DSC.posts.contains(test));
    }

    @Test
    public void requestStats() throws OperationDeniedException {
        Student me = new Student("A16", "Gaoying Wang");
        Student Tom = new Student("A131313", "Tom");
        Student Jack = new Student("A132", "Jack");
        Question test = new Question(me, "Final", "1");
        Question test2 = new Question(me, "PA9", "What's the due date?", "PA9", "DSC30", "2");
        Instructor Dr_K = new Instructor("Dr.K");
        PiazzaExchange DSC = new PiazzaExchange(Dr_K, "DSC30", true);
        DSC.activatePiazza(Dr_K);
        me.enrollClass(DSC);
        me.addPost(DSC,test);
        Tom.enrollClass(DSC);
        Jack.enrollClass(DSC);
        Tom.addPost(DSC,test2);
        LocalDate newtime = LocalDate.of(2022, 2,10);
        LocalDate current = LocalDate.of(2022, 3,9);
        test2.setDate(newtime);
        int[] result = me.requestStats(DSC,1);
        /*
        for (int i = 0; i < result.length; i++) {
            System.out.println(result[i]);
        }
         */
    }

    @Test
    public void searchKSimilarPosts() throws OperationDeniedException {
        Student me = new Student("A16", "Gaoying Wang");
        Student Tom = new Student("A131313", "Tom");
        Student Jack = new Student("A132", "Jack");
        Question test = new Question(me, "Final", "1");
        Question test2 = new Question(me, "PA9", "What's the due date?", "PA9", "DSC30", "2");
        Instructor Dr_K = new Instructor("Dr.K");
        PiazzaExchange DSC = new PiazzaExchange(Dr_K, "DSC30", true);
        DSC.activatePiazza(Dr_K);
        me.enrollClass(DSC);
        Tom.enrollClass(DSC);
        Jack.enrollClass(DSC);
        String placeHolder = ":-)";
        Tutor scott = new Tutor("A16145", "Scott");
        scott.enrollClass(DSC);
        Question p1 = new Question(scott, placeHolder, placeHolder, "linkedlist", "DSC30", "P1");
        Question p2 = new Question(scott, placeHolder, placeHolder, "SLL", "DSC30", "p2");
        Question p3 = new Question(scott, placeHolder, placeHolder, "DLL", "DSC30", "p3");
        Question p4 = new Question(scott, placeHolder, placeHolder, "midterm", "DSC30", "p4");
        Question p5 = new Question(scott, placeHolder, placeHolder, "BST", "DSC30", "p5");
        Question p6 = new Question(scott, placeHolder, placeHolder, "tree", "DSC30", "p6");
        Question p7 = new Question(scott, placeHolder, placeHolder, "heap", "DSC30", "p7");
        Question p8 = new Question(scott, placeHolder, placeHolder, "queue", "DSC30", "p8");
        Question p9 = new Question(scott, placeHolder, placeHolder, "priority queue", "DSC30", "p9");
        Question p10 = new Question(scott, placeHolder, placeHolder, "hash table", "DSC30", "p10");
        Question p11 = new Question(scott, placeHolder, placeHolder, "collision", "DSC30", "p11");
        Question p12 = new Question(scott, placeHolder, placeHolder, "element", "DSC30", "p12");
        Question p13 = new Question(scott, placeHolder, placeHolder, "hash function", "DSC30", "p13");
        Question p14 = new Question(scott, placeHolder, placeHolder, "Bloom filters", "DSC30", "p14");
        Question p15 = new Question(scott, placeHolder, placeHolder, "probing", "DSC30", "p15");
        Question p16 = new Question(scott, placeHolder, placeHolder, "double hashing", "DSC30", "p16");
        Question p17 = new Question(scott, placeHolder, placeHolder, "rehash", "DSC30", "p17");
        Question p18 = new Question(scott, placeHolder, placeHolder, "chaining", "DSC30", "p18");
        Question p19 = new Question(scott, placeHolder, placeHolder, "linear probing", "DSC30", "p19");
        Question p20 = new Question(scott, placeHolder, placeHolder, "quadratic probing", "DSC30", "p20");
        Question p21 = new Question(scott, placeHolder, "a", "quadratic probing", "DSC30", "p21");
        Question p22 = new Question(me, placeHolder, "b", "quadratic probing", "DSC30", "p22");
        Question[] list = new Question[]{p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p14,p15,p16,p17,p18,p19,p20,p21,p22};
        for (int i = 0; i < list.length; i ++) {
            scott.addPost(DSC, list[i]);
        }
        Post[] tes = DSC.retrievePost("quadratic probing");
        Post[] expected = new Post[] {p10,p11,p12,p13,p14,p15,p16,p17,p18,p19};
        Post[] result = me.searchKSimilarPosts(DSC,"hash table",10);
        Post[] expected2 = new Post[] {p7,p8,p9,null,null,null,null};
        Post[] result2 = me.searchKSimilarPosts(DSC,"heap",7);
        assertArrayEquals(expected,result);
        assertArrayEquals(expected2,result2);
    }

    @Test
    public void getPost() throws OperationDeniedException {
        Student me = new Student("A16", "Gaoying Wang");
        Student Tom = new Student("A131313", "Tom");
        Student Jack = new Student("A132", "Jack");
        Question test = new Question(me, "Final", "1");
        Question test2 = new Question(me, "PA9", "What's the due date?", "PA9", "DSC30", "q0");
        Question test3 = new Question(Tom, "PA9q1", "What's the number?", "PA9", "DSC30", "q1");
        Question test4 = new Question(me, "PA9q2", "What's the answer?", "PA9", "DSC30", "q2");
        Instructor Dr_K = new Instructor("Dr.K");
        PiazzaExchange DSC = new PiazzaExchange(Dr_K, "DSC30", true);
        DSC.activatePiazza(Dr_K);
        me.enrollClass(DSC);
        me.addPost(DSC,test);
        Tom.enrollClass(DSC);
        Jack.enrollClass(DSC);
        Tom.addPost(DSC,test2);
        me.addPost(DSC,test3);
        me.addPost(DSC,test4);
        System.out.println(Arrays.toString(me.getPost("PA9", 2, DSC)));
        System.out.println(Arrays.toString(me.getPost("PA9",1,DSC)));
        assertEquals(new Post[]{test,test2,test4}, me.getPost("PA9",1,DSC));
        assertEquals(new Post[]{test2,test3,test4}, me.getPost("PA9",3,DSC));

    }

    @Test
    public void getLog() throws OperationDeniedException {
        Student me = new Student("A16", "Gaoying Wang");
        Student Tom = new Student("A131313", "Tom");
        Student Jack = new Student("A132", "Jack");
        Question test = new Question(me, "Final", "1");
        Question test2 = new Question(me, "PA9", "What's the due date?", "PA9", "DSC30", "2");
        Question test3 = new Question(me, "PA9q1", "What's the number?", "PA9", "DSC30", "2");
        Question test4 = new Question(me, "PA9q2", "What's the answer?", "PA9", "DSC30", "2");
        Instructor Dr_K = new Instructor("Dr.K");
        PiazzaExchange DSC = new PiazzaExchange(Dr_K, "DSC30", true);
        DSC.activatePiazza(Dr_K);
        me.enrollClass(DSC);
        me.addPost(DSC,test);
        Tom.enrollClass(DSC);
        Jack.enrollClass(DSC);
        Tom.addPost(DSC,test2);
        me.addPost(DSC,test3);
        me.addPost(DSC,test4);
        LocalDate newtime1 = LocalDate.of(2022, 2,20);
        LocalDate newtime2 = LocalDate.of(2022, 2,10);
        LocalDate newtime3 = LocalDate.of(2022, 2,5);
        LocalDate current = LocalDate.of(2022, 3,9);
        test2.setDate(newtime1);
        test3.setDate(newtime2);
        test4.setDate(newtime3);
        Post[] list = me.getLog(10,1,DSC);
        assertEquals(new Post[]{test,test3,test4}, list);
        Post[] list2 = me.getLog(2,2,DSC);
        assertEquals(new Post[]{test,test3}, list2);

    }

    @Test
    public void answerQuestion() throws OperationDeniedException {
        Student me = new Student("A16", "Gaoying Wang");
        Student Tom = new Student("A131313", "Tom");
        Student Jack = new Student("A132", "Jack");
        Question test = new Question(me, "Final", "1");
        Question test2 = new Question(me, "PA9", "What's the due date?", "PA9", "DSC30", "2");
        Question test3 = new Question(me, "PA9q1", "What's the number?", "PA9", "DSC30", "2");
        Question test4 = new Question(me, "PA9q2", "What's the answer?", "PA9", "DSC30", "2");
        Instructor Dr_K = new Instructor("Dr.K");
        PiazzaExchange DSC = new PiazzaExchange(Dr_K, "DSC30", true);
        DSC.activatePiazza(Dr_K);
        me.enrollClass(DSC);
        me.addPost(DSC,test);
        /*
        Tom.enrollClass(DSC);
        Jack.enrollClass(DSC);
        Tom.addPost(DSC,test2);
        me.addPost(DSC,test3);
        me.addPost(DSC,test4);
        LocalDate newtime1 = LocalDate.of(2022, 2,20);
        LocalDate newtime2 = LocalDate.of(2022, 2,10);
        LocalDate newtime3 = LocalDate.of(2022, 2,5);
        LocalDate current = LocalDate.of(2022, 3,9);
        test2.setDate(newtime1);
        test3.setDate(newtime2);
        test4.setDate(newtime3);
        assertEquals(true,Tom.answerQuestion(test,"I don't know"));
        assertEquals(false, Tom.answerQuestion(test,"ddddddddddddddddddddddddd" +
                "dddddddddddddddddddddddddddddddddddddddddddddd" +
                "ddddddddddddddddddddddddd"));

         */
        assertEquals("1",Dr_K.deletePost(test,DSC));
    }

    @Test
    public void endorsePost() throws OperationDeniedException {
        Student me = new Student("A16", "Gaoying Wang");
        Student Tom = new Student("A131313", "Tom");
        Student Jack = new Student("A132", "Jack");
        Question test = new Question(me, "Final", "1");
        Question test2 = new Question(me, "PA9", "What's the due date?", "PA9", "DSC30", "2");
        Question test3 = new Question(me, "PA9q1", "What's the number?", "PA9", "DSC30", "2");
        Question test4 = new Question(me, "PA9q2", "What's the answer?", "PA9", "DSC30", "2");
        Instructor Dr_K = new Instructor("Dr.K");
        PiazzaExchange DSC = new PiazzaExchange(Dr_K, "DSC30", true);
        DSC.activatePiazza(Dr_K);
        me.enrollClass(DSC);
        me.addPost(DSC,test);
        Tom.enrollClass(DSC);
        Jack.enrollClass(DSC);
        Tom.addPost(DSC,test2);
        me.addPost(DSC,test3);
        me.addPost(DSC,test4);
        LocalDate newtime1 = LocalDate.of(2022, 2,20);
        LocalDate newtime2 = LocalDate.of(2022, 2,10);
        LocalDate newtime3 = LocalDate.of(2022, 2,5);
        LocalDate current = LocalDate.of(2022, 3,9);
        test2.setDate(newtime1);
        test3.setDate(newtime2);
        test4.setDate(newtime3);
        Tom.endorsePost(test);
        assertEquals(1,test.endorsementCount);
        assertEquals(0,me.numOfEndorsement);
        assertEquals(false, test.endorsedByCourseStaff);
        Dr_K.endorsePost(test);
        assertEquals(2,test.endorsementCount);
        assertEquals(1,me.numOfEndorsement);
        assertEquals(true, test.endorsedByCourseStaff);
    }

    @Test
    public void getTopTwoEndorsedPosts() throws OperationDeniedException {
        Student me = new Student("A16", "Gaoying Wang");
        Student Tom = new Student("A131313", "Tom");
        Student Jack = new Student("A132", "Jack");
        Question test = new Question(me, "Final", "1");
        Question test2 = new Question(me, "PA9", "What's the due date?", "PA9", "DSC30", "2");
        Question test3 = new Question(me, "PA9q1", "What's the number?", "PA9", "DSC30", "2");
        Question test4 = new Question(me, "PA9q2", "What's the answer?", "PA9", "DSC30", "2");
        Instructor Dr_K = new Instructor("Dr.K");
        PiazzaExchange DSC = new PiazzaExchange(Dr_K, "DSC30", true);
        DSC.activatePiazza(Dr_K);
        me.enrollClass(DSC);
        me.addPost(DSC,test);
        Tom.enrollClass(DSC);
        Jack.enrollClass(DSC);
        Tom.addPost(DSC,test2);
        me.addPost(DSC,test3);
        me.addPost(DSC,test4);
        LocalDate newtime1 = LocalDate.of(2022, 2,20);
        LocalDate newtime2 = LocalDate.of(2022, 2,10);
        LocalDate newtime3 = LocalDate.of(2022, 2,5);
        LocalDate current = LocalDate.of(2022, 3,9);
        assertEquals(new Post[]{test,test2}, me.getTopTwoEndorsedPosts(DSC));
        test2.setDate(newtime1);
        test3.setDate(newtime2);
        test4.setDate(newtime3);
        Tom.endorsePost(test3);
        Dr_K.endorsePost(test3);
        me.endorsePost(test2);
        assertEquals(new Post[]{test3,test2}, me.getTopTwoEndorsedPosts(DSC));
    }

    @Test
    public void displayName() throws OperationDeniedException {
        Student me = new Student("A16", "Gaoying Wang");
        Student Tom = new Student("A131313", "Tom");
        Student Jack = new Student("A132", "Jack");
        Question test = new Question(me, "Final", "1");
        Question test2 = new Question(me, "PA9", "What's the due date?", "PA9", "DSC30", "2");
        Question test3 = new Question(me, "PA9q1", "What's the number?", "PA9", "DSC30", "2");
        Question test4 = new Question(me, "PA9q2", "What's the answer?", "PA9", "DSC30", "2");
        Instructor Dr_K = new Instructor("Dr.K");
        Tutor Jason = new Tutor("A123","Jason");
        PiazzaExchange DSC = new PiazzaExchange(Dr_K, "DSC30", true);
        DSC.activatePiazza(Dr_K);
        me.enrollClass(DSC);
        me.addPost(DSC,test);
        Tom.enrollClass(DSC);
        Jack.enrollClass(DSC);
        Tom.addPost(DSC,test2);
        me.addPost(DSC,test3);
        me.addPost(DSC,test4);
        LocalDate newtime1 = LocalDate.of(2022, 2,20);
        LocalDate newtime2 = LocalDate.of(2022, 2,10);
        LocalDate newtime3 = LocalDate.of(2022, 2,5);
        LocalDate current = LocalDate.of(2022, 3,9);
        assertEquals("Student: Gaoying Wang, PID: A16",me.displayName());
        assertEquals("Tutor: Jason, PID: A123", Jason.displayName());
        assertEquals("Instructor: Dr.K, PID: A0000", Dr_K.displayName());
    }

    @Test
    public void otherTest() throws OperationDeniedException {
        Student me = new Student("A16", "Gaoying Wang");
        Student Tom = new Student("A131313", "Tom");
        Student Jack = new Student("A132", "Jack");
        Question test = new Question(me, "Final", "1");
        Question test2 = new Question(me, "PA9", "What's the due date?", "PA9", "DSC30", "2");
        Instructor Dr_K = new Instructor("Dr.K");
        PiazzaExchange DSC = new PiazzaExchange(Dr_K, "DSC30", true);
        DSC.activatePiazza(Dr_K);
        me.enrollClass(DSC);
        Tom.enrollClass(DSC);
        Jack.enrollClass(DSC);
        String placeHolder = ":-)";
        Tutor scott = new Tutor("A16145", "Scott");
        scott.enrollClass(DSC);
        Question p1 = new Question(scott, placeHolder, placeHolder, "linkedlist", "DSC30", "P1");
        Question p2 = new Question(scott, placeHolder, placeHolder, "SLL", "DSC30", "p2");
        Question p3 = new Question(scott, placeHolder, placeHolder, "DLL", "DSC30", "p3");
        Question p4 = new Question(scott, placeHolder, placeHolder, "midterm", "DSC30", "p4");
        Question p5 = new Question(scott, placeHolder, placeHolder, "BST", "DSC30", "p5");
        Question p6 = new Question(scott, placeHolder, placeHolder, "tree", "DSC30", "p6");
        Question p7 = new Question(scott, placeHolder, placeHolder, "heap", "DSC30", "p7");
        Question p8 = new Question(scott, placeHolder, placeHolder, "queue", "DSC30", "p8");
        Question p9 = new Question(scott, placeHolder, placeHolder, "priority queue", "DSC30", "p9");
        Question p10 = new Question(scott, placeHolder, placeHolder, "hash table", "DSC30", "p10");
        Question p11 = new Question(scott, placeHolder, placeHolder, "collision", "DSC30", "p11");
        Question p12 = new Question(scott, placeHolder, placeHolder, "element", "DSC30", "p12");
        Question p13 = new Question(scott, placeHolder, placeHolder, "hash function", "DSC30", "p13");
        Question p14 = new Question(scott, placeHolder, placeHolder, "Bloom filters", "DSC30", "p14");
        Question p15 = new Question(scott, placeHolder, placeHolder, "probing", "DSC30", "p15");
        Question p16 = new Question(scott, placeHolder, placeHolder, "double hashing", "DSC30", "p16");
        Question p17 = new Question(scott, placeHolder, placeHolder, "rehash", "DSC30", "p17");
        Question p18 = new Question(scott, placeHolder, placeHolder, "chaining", "DSC30", "p18");
        Question p19 = new Question(scott, placeHolder, placeHolder, "linear probing", "DSC30", "p19");
        Question p20 = new Question(scott, placeHolder, placeHolder, "quadratic probing", "DSC30", "p20");
        Question p21 = new Question(scott, placeHolder, "a", "quadratic probing", "DSC30", "p21");
        Question p22 = new Question(me, placeHolder, "b", "quadratic probing", "DSC30", "p22");
        Question p23 = new Question(me, placeHolder, "c", "quadratic probing", "DSC30", "p23");
        LocalDate newtime1 = LocalDate.of(2022, 2,20);
        LocalDate newtime2 = LocalDate.of(2022, 2,10);
        LocalDate newtime3 = LocalDate.of(2022, 2,5);
        LocalDate current = LocalDate.of(2022, 3,9);
        p1.setDate(newtime1);
        p2.setDate(newtime2);
        p3.setDate(newtime3);
        me.endorsePost(p2);
        Tom.endorsePost(p2);
        scott.endorsePost(p2);
        Jack.endorsePost(p2);
        Question[] list = new Question[]{p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p14,p15,p16,p17,p18,p19,p20,p21,p22};
        for (int i = 0; i < list.length; i ++) {
            scott.addPost(DSC, list[i]);
        }
        me.addPost(DSC,p23);
        Jack.endorsePost(p23);
        scott.endorsePost(p23);
        DSC.answerQuestion(me,p21,"No");
        System.out.println(DSC.viewStats(me));
    }
}