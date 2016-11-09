import com.nexmo.spock.Factorial;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FactorialTest {
    private Factorial factorial;

    @Before
    public void setUp() {
        factorial = new Factorial();
    }

    @Test
    public void zero() {
        assertEquals(1, factorial.getResult(0), 0);
    }

    @Test
    public void one() {
        assertEquals(1, factorial.getResult(1), 0);
    }

    @Test
    public void seven() {
        assertEquals(5040, factorial.getResult(7), 0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void minusOne() {
        factorial.getResult(-1); }

}
