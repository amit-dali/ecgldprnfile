package nl.ebay.creditlimittracker.service;

        import nl.ebay.creditlimittracker.model.User;

        import java.util.Comparator;

public class SortByName implements Comparator<User> {
    @Override
    public int compare(User userOne, User userTwo) {
        return userOne.getName().compareTo(userTwo.getName());
    }
}
