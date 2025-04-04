package tqs.bookstore;

import java.util.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.slf4j.LoggerFactory.getLogger;

import static java.lang.invoke.MethodHandles.lookup;

import org.slf4j.Logger;

import com.example.Book;
import com.example.Library;




public class bookTest {
    Library library = new Library();
	List<Book> result = new ArrayList<>();
    static final Logger log = getLogger(lookup().lookupClass());

    @ParameterType("([0-9]{4})")
	public Date iso8601Date(String year){
        return java.sql.Date.valueOf(LocalDate.of(Integer.parseInt(year), 1, 1));
	}
	

    
    @Given("a book with the title {string}, written by {string}, published in {int}")
    public void addBook(String title, String author, int year) {
        log.debug("Adding book with title {}, author {} and published in {}", title, author, year);
        library.addBook(new Book(title, author, year));
    }

    @When("search for books published between {iso8601Date} and {iso8601Date}")
    public void setSearchParameters(Date from, Date to) {
        result = library.findBooks(from, to);
        log.info("Search for books published between {} and {}", from, to);
        log.info("Result: {}", result);
    }

    @Then("the search result should contain the book {string} by {string}")
    public void checkResult(String title, String author) {
        assert(result.stream().anyMatch(book -> book.getTitle().equals(title) && book.getAuthor().equals(author)));
    }

    @Then("the search result should not contain the book {string} by {string}")
    public void checkNegativeResult(String title, String author) {
        assert(result.stream().noneMatch(book -> book.getTitle().equals(title) && book.getAuthor().equals(author)));
    }
    
}
