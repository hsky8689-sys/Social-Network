package domain;

import java.util.Objects;

public class Friendship {
    private Long first;
    private Long second;
    public Friendship(Long one,Long another) {
        this.first=one;this.second=another;
    }
    public Long getFirst() {
        return first;
    }
    public Long getSecond() {
        return second;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return (Objects.equals(first,that.second) && Objects.equals(second,that.first))||(Objects.equals(first, that.first) && Objects.equals(second, that.second));
    }
    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
    @Override
    public String toString() {
        return first.toString() +"|"+second.toString();
    }
}
/*
package domain;

import java.util.Objects;

public class Friendship {
    private User first;
    private User second;
    public Friendship(User one,User another) {
        this.first=one;this.second=another;
    }
    public User getFirst() {
        return first;
    }
    public User getSecond() {
        return second;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return (Objects.equals(first,that.second) && Objects.equals(second,that.first))||(Objects.equals(first, that.first) && Objects.equals(second, that.second));
    }
    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
    @Override
    public String toString() {
        return first.toString() +"|"+second.toString();
    }
}
 */
