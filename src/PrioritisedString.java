import java.util.Objects;

public class PrioritisedString implements Comparable<PrioritisedString> {
    private int priority;
    private String string;

    public PrioritisedString(int priority, String string) {
        this.priority = priority;
        this.string = string;
    }

    public int getPriority() {
        return priority;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrioritisedString that = (PrioritisedString) o;
        return priority == that.priority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(priority);
    }

    @Override
    public int compareTo(PrioritisedString o) {
        return this.priority-o.priority;
    }
}
