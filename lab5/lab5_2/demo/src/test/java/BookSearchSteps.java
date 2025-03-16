import static java.lang.invoke.MethodHandles.lookup;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

import com.example.Book;
import com.example.Library;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class BookSearchSteps {
    static final Logger log = getLogger(lookup().lookupClass());
    Library library = new Library();
    List<Book> booksFound = new ArrayList<>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Given("the following books are available in the store:")
    public void setup(DataTable books) throws ParseException {
        List<Map<String, String>> rows = books.asMaps(String.class, String.class);
        for (Map<String, String> columns : rows) {
            library.addBook(new Book(columns.get("Title"), columns.get("Author"), dateFormat.parse(columns.get("Published"))));
        }
    }

    @When("the customer searches for books published between {string} and {string}")
    public void searchBooksByYear(String from, String to) throws ParseException {
        Date fromDate = dateFormat.parse(from);
        Date toDate = dateFormat.parse(to);
        log.info("Searching for books published between {} and {}", fromDate, toDate);
        booksFound = library.findBooks(fromDate, toDate);
    }

    @When("the customer searches for a book titled {string}")
    public void searchBooksByTitle(String title) {
        log.info("Searching for a book titled {}", title);
        Book book = library.findBookByTitle(title);
        booksFound = book != null ? Collections.singletonList(book) : Collections.emptyList();
    }

    @When("the customer searches for books written by {string}")
    public void searchBooksByAuthor(String author) {
        log.info("Searching for books written by {}", author);
        booksFound = library.findBooksByAuthor(author);
    }

    @Then("{int} books should be found")
    public void verifyResultSize(int expectedCount) {
        assertEquals(expectedCount, booksFound.size());
    }

    @Then("no books should be found")
    public void verifyNoBooksFound() {
        assertTrue(booksFound.isEmpty());
    }
}
