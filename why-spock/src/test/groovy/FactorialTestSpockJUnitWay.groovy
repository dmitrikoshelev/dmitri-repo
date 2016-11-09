import com.nexmo.spock.Factorial
import spock.lang.Specification

class FactorialTestSpockJUnitWay extends Specification {

    Factorial factorial

    def setup() {
        factorial = new Factorial()
    }
    def zero() {
        expect:
        factorial.getResult(0) == 1
    }
    def one() {
        expect:
        factorial.getResult(1) == 1
    }
    def seven() {
        expect:
        factorial.getResult(7) == 5040
    }
    def minusOne() {
        when:
        factorial.getResult(-1)
        then:
        thrown(IllegalArgumentException)
    }
    def cleanup() {
        factorial = null
    }
}