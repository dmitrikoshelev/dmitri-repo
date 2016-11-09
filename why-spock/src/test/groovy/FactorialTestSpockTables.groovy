import com.nexmo.spock.Factorial
import spock.lang.Specification
import spock.lang.Title
import spock.lang.Unroll

@Title("Testing calculation of a factorial function")
@Unroll
class FactorialTestSpockTables extends Specification {

    def "Calculation should pass for any positive number '#input' using data tables"() {
        given:"a factorial"
        Factorial factorial = new Factorial()

        when:"number is passed to function for calculation"
        double calculation = factorial.getResult(input)

        then:"#calculation should be equal to expected result #result"
        calculation == result

        where: "input is #input"
        input  | result
        0      | 1d
        1      | 1d
        7      | 5040d
        12     | 479001600d
        20     | 2432902008176640000d
        40     | 815915283247897734345611269596115894272000000000d
    }

    def "Calculation should pass for any positive number '#input' using data pipes"() {
        given:"a factorial"
        Factorial factorial = new Factorial()

        when:"number is passed to function for calculation"
        double calculation = factorial.getResult(input)

        then:"#calculation should be equal to expected result #result"
        calculation == result

        where: "input is #input"
        input << [0, 1, 7, 12, 20, 40]
        result << [1d, 1d, 5040d, 479001600d, 2432902008176640000d, 815915283247897734345611269596115894272000000000d ]
    }

    def "Calculation should pass for any positive number '#input' using multivalued assignments"() {
        given:"a factorial"
        Factorial factorial = new Factorial()

        when:"number is passed to function for calculation"
        double calculation = factorial.getResult(input)

        then:"#calculation should be equal to expected result #result"
        calculation == result

        where: "input is #input"
        [input, result] << [[0, 1d], [1, 1d], [7, 5040d],  [12, 479001600d]]
    }

    def "Calculation should fail for any negative number '#input'"() {
        given:"a factorial"
        Factorial factorial = new Factorial()

        when:"number passed to function for calculation"
        factorial.getResult(input)

        then:"IllegalArgumentException is thrown"
        thrown(IllegalArgumentException)

        where:"input is '#input'"
        input << [-1d, -2d, -5d, -10d, -20d, -100d]
    }

}
