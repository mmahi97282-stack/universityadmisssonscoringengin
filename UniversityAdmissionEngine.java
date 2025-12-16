import java.util.*;
import java.util.stream.Collectors;

// -------- ROUND 1 --------
abstract class Applicant {
    int id;
    String name;
    double marks;
    double score;

    Applicant(int id, String name, double marks) {
        this.id = id;
        this.name = name;
        this.marks = marks;
    }

    abstract void calculateScore();
}

class StudentApplicant extends Applicant {

    StudentApplicant(int id, String name, double marks) {
        super(id, name, marks);
    }

    @Override
    void calculateScore() {
        score = marks;
    }
}

interface ScoringPolicy {
    void apply(Applicant a);
}

class GeneralPolicy implements ScoringPolicy {
    @Override
    public void apply(Applicant a) {
        a.calculateScore();
    }
}

// -------- ROUND 2 --------
class QuotaExceededException extends Exception {
    QuotaExceededException(String msg) {
        super(msg);
    }
}

class QuotaPolicy {
    int limit;
    int count = 0;

    QuotaPolicy(int limit) {
        this.limit = limit;
    }

    void apply() throws QuotaExceededException {
        if (count >= limit) {
            throw new QuotaExceededException("Quota Full");
        }
        count++;
    }
}

// -------- MAIN CLASS --------
public class UniversityAdmissionEngine {

    public static void main(String[] args) {

        List<Applicant> list = new ArrayList<>();
        list.add(new StudentApplicant(1, "Ravi", 85));
        list.add(new StudentApplicant(2, "Anita", 92));
        list.add(new StudentApplicant(3, "Kiran", 78));

        // ROUND 1: Scoring
        ScoringPolicy policy = new GeneralPolicy();
        for (Applicant a : list) {
            policy.apply(a);
        }

        List<Applicant> merit = list.stream()
                .sorted((a, b) -> Double.compare(b.score, a.score))
                .collect(Collectors.toList());

        System.out.println("ROUND 1 : MERIT LIST");
        for (Applicant a : merit) {
            System.out.println(a.name + " : " + a.score);
        }

        // ROUND 2: Quota Selection
        QuotaPolicy quota = new QuotaPolicy(2);
        System.out.println("\nROUND 2 : FINAL SELECTION");

        for (Applicant a : merit) {
            try {
                quota.apply();
                System.out.println(a.name + " SELECTED");
            } catch (QuotaExceededException e) {
                System.out.println(a.name + " NOT SELECTED");
            }
        }
    }
}
