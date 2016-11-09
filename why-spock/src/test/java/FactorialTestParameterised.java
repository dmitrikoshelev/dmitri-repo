import com.nexmo.spock.Factorial;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class FactorialTestParameterised {

    private Factorial factorial;
    Double input;
    Double result;

    public FactorialTestParameterised(Double input, Double result) {
        this.input = input;
        this.result = result;
    }

    @Before
    public void setUp() {
        factorial = new Factorial();
    }

    @Test
    public void testFactorialCalculation() {
        Double calculation = factorial.getResult(input);
        assertEquals(result, calculation);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> positiveData() {
        List<Object[]> inputNumber = Arrays.asList(new Object[][]{
                {0d, 1d},
                {1d, 1d},
                {7d, 5040d},
                {12d, 479001600d}});

        return inputNumber;
    }

}
