import com.nexmo.spock.Factorial;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class FactorialTestParameterisedException {

    private Factorial factorial;
    Double input;
    Throwable t;

    public FactorialTestParameterisedException(Double input, Throwable t) {
        this.input = input;
        this.t = t;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> negativeData() {
        List<Object[]> inputNumber = Arrays.asList(new Object[][]{
                {-1d, new IllegalArgumentException("wrong data")},
                {-2d, new IllegalArgumentException("wrong data")},
                {-100d, new IllegalArgumentException("wrong data")}});

        return inputNumber;
    }

    @Before
    public void setUp() {
        factorial = new Factorial();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFactorialCalculationExceptionThrown() {
        factorial.getResult(input);
    }
}
